package org.blocktest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class App {
    private static boolean evoSuiteCompiled = false;
    private static boolean randoopCompiled = false;
    private static EvoSuiteComponent evosuiteComponent;
    private static RandoopComponent randoopComponent;
    public static void main(String[] args) {
        String url = args[0];
        String commit = args[1];
        String relpathToSrc = args[2];
        int startLine = Integer.parseInt(args[3]);
        int endLine = Integer.parseInt(args[4]);
        String outputDir = new File(args[5]).getAbsolutePath();
        String resourcesDir = new File(args[6]).getAbsolutePath(); // The place where all the jars are located
        String injectedSrc = new File(args[7]).getAbsolutePath();
        String generatedTestsDir = new File(args[8]).getAbsolutePath(); // The directory that contains generated tests
        
        if (!new File(outputDir).exists()) {
            new File(outputDir).mkdirs();
        }
        String projectName = url.split("/")[3] + "-" + url.split("/")[4];
        String projectDir = cloneAndCheckout(url, commit, resourcesDir, projectName);
        String projectBuildDir = projectDir + File.separator + "target" + File.separator + "classes";
        String absPathToSrc = projectDir + File.separator + relpathToSrc;
        String logsDir = outputDir + File.separator + "logs";
        if (!new File(logsDir).exists()) {
            new File(logsDir).mkdirs();
        }

        Map<Integer, Integer> lineNumberMap = getLineMap(absPathToSrc, injectedSrc);
        
        int result = Utils.testWithOriginal(projectDir, outputDir);
        if (result != 0) {
            System.out.println("Failed to test with original. Exiting.");
            Utils.earlyExitWithLog(outputDir, "Failed to test with original. Exiting.");
        }
        Utils.replaceWithInjectedSource(projectDir, absPathToSrc, injectedSrc);
        int mutantCountRaw = generateMutants(projectDir, relpathToSrc, outputDir, startLine, endLine, lineNumberMap);
        int mutantCountPassedCompilation = compileMutants(projectDir, absPathToSrc, injectedSrc, outputDir + File.separator + "mutants", resourcesDir, outputDir);
        runMutantsAgainstTests("dev", projectDir, outputDir, absPathToSrc, resourcesDir, generatedTestsDir);
        runMutantsAgainstTests("block", projectDir, outputDir, absPathToSrc, resourcesDir, generatedTestsDir);
        evosuiteComponent = new EvoSuiteComponent(projectDir, outputDir, resourcesDir + File.separator + Constants.EVOSUITE_JAR, resourcesDir + File.separator + Constants.JUNIT_STANDALONE_JAR, resourcesDir + File.separator + Constants.JACOCO_AGENT_JAR, generatedTestsDir);
        runMutantsAgainstTests("evosuite", projectDir, outputDir, absPathToSrc, resourcesDir, generatedTestsDir);
        randoopComponent = new RandoopComponent(projectDir, outputDir, resourcesDir + File.separator + Constants.JACOCO_AGENT_JAR, resourcesDir + File.separator + Constants.RANDOOP_JAR, resourcesDir + File.separator + Constants.JUNIT_STANDALONE_JAR, generatedTestsDir);
        runMutantsAgainstTests("randoop", projectDir, outputDir, absPathToSrc, resourcesDir, generatedTestsDir);
    }

    private static String cloneAndCheckout(String url, String commit, String resourcesDir, String projectName) {
        try {
            Path tmpDir = Files.createTempDirectory("tmp");
            System.out.println("Temporarily cloning project to: " + tmpDir.toAbsolutePath());
            ProcessBuilder pb = new ProcessBuilder("git", "clone", url, tmpDir.toAbsolutePath().toString());
            pb.start().waitFor(); // Must wait for the clone to finish.
            ProcessBuilder pb2 = new ProcessBuilder("git", "checkout", commit);
            pb2.directory(tmpDir.toFile());
            pb2.start().waitFor(); // Must wait for the checkout to finish.
            ProcessBuilder pb3 = new ProcessBuilder("bash", resourcesDir + File.separator + "treat_special.sh", projectName, commit);
            pb3.directory(tmpDir.toFile());
            pb3.start().waitFor();
            return tmpDir.toAbsolutePath().toString();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    private static Map<Integer, Integer> getLineMap(String absPathToSrc, String injectedSrc) {
        Map<Integer, Integer> lineNumberMap = new HashMap<>();
        List<String> originalLines = new ArrayList<>();
        List<String> injectedLines = new ArrayList<>();
        try {
            originalLines = java.nio.file.Files.readAllLines(java.nio.file.Paths.get(absPathToSrc));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            injectedLines = java.nio.file.Files.readAllLines(java.nio.file.Paths.get(injectedSrc));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Map<String, List<Integer>> injectedLineMap = new HashMap<>();
        for (int injectedLineNumber = 1; injectedLineNumber <= injectedLines.size(); injectedLineNumber++) {
            String injectedLine = injectedLines.get(injectedLineNumber - 1).replaceAll("\\s+", "");
            injectedLineMap.computeIfAbsent(injectedLine, key -> new ArrayList<>()).add(injectedLineNumber);
        }

        for (int originalLineNumber = 1; originalLineNumber <= originalLines.size(); originalLineNumber++) {
            String originalLine = originalLines.get(originalLineNumber - 1).replaceAll("\\s+", "");
            List<Integer> matches = injectedLineMap.get(originalLine);
            if (matches != null && !matches.isEmpty()) {
                // Use the first match and remove it to avoid duplicate mapping
                lineNumberMap.put(originalLineNumber, matches.remove(0));
            } else {
                lineNumberMap.put(originalLineNumber, -1);
            }
        }
        // System.out.println("Line number map: " + lineNumberMap);
        return lineNumberMap;
    }

    private static int generateMutants(String projectDir, String relpathToSrc, String outputDir, int startLine, int endLine, Map<Integer, Integer> lineNumberMap) {
        String mutantsDir = outputDir + File.separator + "mutants";
        if (!new File(mutantsDir).exists()) {
            new File(mutantsDir).mkdirs();
        }
        prepareLineNumbersFile(outputDir, startLine, endLine, lineNumberMap);
        int mutantCount = mutate(projectDir + File.separator + relpathToSrc, outputDir, mutantsDir);
        return mutantCount;
    }

    private static void prepareLineNumbersFile(String outputDir, int startLine, int endLine, Map<Integer, Integer> lineNumberMap) {
        System.out.println("===== Preparing line numbers file =====");
        
        String lineNumbersFile = outputDir + File.separator + "line-numbers.txt";
        try (PrintWriter writer = new PrintWriter(new File(lineNumbersFile))) {
            for (int i = startLine; i <= endLine; i++) {
                int lineNumber = lineNumberMap.getOrDefault(i, -1);
                if (lineNumber == -1) {
                    continue;
                }
                writer.println(lineNumber);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Modified from Genie
    private static int mutate(String absPathToSrc, String outputDir, String mutantsDir) {
        System.out.println("===== Generating mutants with universalmutator =====");

        String lineNumbersList = outputDir + File.separator + "line-numbers.txt";
        String umLog = outputDir + File.separator + "universalmutator-log.txt";
        List<String> command = new ArrayList<>(Arrays.asList("mutate", absPathToSrc, "--noCheck",
                "--mutantDir", mutantsDir, "--lines", lineNumbersList));
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(new File(outputDir));
        pb.redirectOutput(new File(umLog));
        pb.redirectErrorStream(true);
        try {
            pb.start().waitFor();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        File mutantsDirectory = new File(mutantsDir);
        File[] mutantFiles = mutantsDirectory.listFiles();
        int mutantCount = (mutantFiles == null) ? 0 : mutantFiles.length;
        if (mutantCount == 0) {
            System.out.println("No mutants are generated.");
            Utils.earlyExitWithLog(outputDir, "No mutants generated. Exiting.");
        }
        System.out.println("Generated a total of " + mutantCount + " mutants.");
        return mutantCount;
    }

    /**
     * @return The number of mutants that compiled successfully.
     */
    private static int compileMutants(String projectDir, String originalFilePath, String injectedSrc, String mutantsDir, String resourcesDir, String outputDir) {
        System.out.println("===== Compiling mutants =====");

        Set<Integer> compilableMutants = new HashSet<>();
        Utils.compileProjectWithBTest(projectDir, outputDir, resourcesDir);
        File[] mutantFiles = new File(mutantsDir).listFiles();
        int beforeCount = mutantFiles.length;
        for (File mutant : mutantFiles) {
            try {
                Files.copy(Paths.get(mutant.getAbsolutePath()), Paths.get(originalFilePath),
                        StandardCopyOption.REPLACE_EXISTING);
                // Files.move(Paths.get(Paths.get(originalFilePath).getParent() + File.separator + mutant.getName()), Paths.get(originalFilePath));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            int mutantId = Integer.parseInt(mutant.getName().split("\\.mutant\\.")[1].split("\\.")[0]);
            System.out.println("Compiling mutant #" + mutantId);
            if (Utils.compileSingleSource(projectDir, outputDir, resourcesDir, originalFilePath, mutantId) != 0) {
                System.out.println("Failed to compile mutant: " + mutant.getName()
                        + ", will be removed from mutant set.");
                try {
                    Files.delete(Paths.get(mutant.getAbsolutePath()));
                } catch (IOException ex) {
                    System.out.println("Failed to remove mutant file " + mutant.getAbsolutePath() + ".");
                    ex.printStackTrace();
                }
                Utils.replaceWithInjectedSource(projectDir, originalFilePath, injectedSrc);
                continue;
            } else {
                compilableMutants.add(mutantId);
            }
            Utils.replaceWithInjectedSource(projectDir, originalFilePath, injectedSrc);
        }
        mutantFiles = new File(mutantsDir).listFiles();
        int afterCount = mutantFiles.length;
        System.out.println(afterCount + " out of " + beforeCount + " mutants compiled successfully.");
        String compilableMutantsFile = outputDir + File.separator + "compilable-mutants.txt";
        try (PrintWriter writer = new PrintWriter(new File(compilableMutantsFile))) {
            for (Integer mutantId : compilableMutants) {
                writer.println(mutantId);
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return afterCount;
    }

    private static void runMutantsAgainstTests(String type, String projectDir, String outputDir, String originalFilePath, String resourcesDir, String generatedTestsDir) {
        System.out.println("===== Running mutants against " + type + " tests =====");

        List<Integer> survivedMutants = new ArrayList<>();
        List<Integer> killedMutants = new ArrayList<>();
        
        for (File mutant : new File(outputDir + File.separator + "mutants").listFiles()) {
            try {
                Files.copy(Paths.get(mutant.getAbsolutePath()), Paths.get(originalFilePath),
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            int mutantId = Integer.parseInt(mutant.getName().split("\\.mutant\\.")[1].split("\\.")[0]);
            System.out.println("Running mutant #" + mutantId);
            int result = -1;
            switch (type) {
                case "dev":
                    result = Utils.testWithExtension(projectDir, outputDir, resourcesDir, type + "-test-with-mutant-" + mutantId + ".log");
                    break;
                case "block":
                    result = Utils.runBlockTest(projectDir, outputDir, resourcesDir, type + "-test-with-mutant-" + mutantId + ".log", originalFilePath);
                    break;
                case "evosuite":
                    if (!evoSuiteCompiled) {
                        try {
                            evosuiteComponent.load();
                        } catch (IOException ex) {
                            System.out.println("Failed to load evosuite tests from " + generatedTestsDir + File.separator + "evosuite-tests. Skipping evosuite tests.");
                            return;
                        }
                        if (evosuiteComponent.compile() != 0) {
                            System.out.println("Failed to compile evosuite tests. Skipping evosuite tests.");
                            return;
                        }
                        evoSuiteCompiled = true;
                    }
                    result = evosuiteComponent.execute();
                    break;
                case "randoop":
                    if (!randoopCompiled) {
                        try {
                            randoopComponent.load();
                        } catch (IOException ex) {
                            System.out.println("Failed to load randoop tests from " + generatedTestsDir + File.separator + "randoop-tests. Skipping randoop tests.");
                            return;
                        }
                        if (randoopComponent.compile() != 0) {
                            System.out.println("Failed to compile randoop tests. Skipping randoop tests.");
                            return;
                        }
                        randoopCompiled = true;
                    }
                    result = randoopComponent.executeRegressionTest(type + "-test-with-mutant-" + mutantId + ".log");
                    break;
                default:
                    break;
            }
            if (result != 0) {
                killedMutants.add(mutantId);
            } else {
                survivedMutants.add(mutantId);
            }
        }
        File survivedMutantsFile = new File(outputDir + File.separator + type + "-tests-survived-mutants.txt");
        File killedMutantsFile = new File(outputDir + File.separator + type + "-tests-killed-mutants.txt");
        File mutationScoreFile = new File(outputDir + File.separator + type + "-tests-mutation-score.txt");
        try (PrintWriter writer = new PrintWriter(survivedMutantsFile)) {
            for (Integer mutantId : survivedMutants) {
                writer.println(mutantId);
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        try (PrintWriter writer = new PrintWriter(killedMutantsFile)) {
            for (Integer mutantId : killedMutants) {
                writer.println(mutantId);
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        try (PrintWriter writer = new PrintWriter(mutationScoreFile)) {
            writer.println(killedMutants.size() / (double) (survivedMutants.size() + killedMutants.size()));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        System.out.println("Survived mutants: " + survivedMutants.size());
        System.out.println("Killed mutants: " + killedMutants.size());
        System.out.println("Mutation score: " + killedMutants.size() / (double) (survivedMutants.size() + killedMutants.size()));
    }
}
