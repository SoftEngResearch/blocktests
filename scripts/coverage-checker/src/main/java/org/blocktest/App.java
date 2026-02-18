package org.blocktest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        String url = args[0];
        String commit = args[1];
        String relpathToSrc = args[2];
        int startLine = Integer.parseInt(args[3]);
        int endLine = Integer.parseInt(args[4]);
        String outputDir = new File(args[5]).getAbsolutePath();
        String resourcesDir = new File(args[6]).getAbsolutePath(); // The place where all the jars are located

        String jacocoExtJar = new File(resourcesDir, Constants.JACOCO_EXT_JAR).getAbsolutePath();
        String evosuiteJar = new File(resourcesDir, Constants.EVOSUITE_JAR).getAbsolutePath();
        String randoopJar = new File(resourcesDir, Constants.RANDOOP_JAR).getAbsolutePath();
        String junitStandaloneJar = new File(resourcesDir, Constants.JUNIT_STANDALONE_JAR).getAbsolutePath();
        String jacocoAgentJar = new File(resourcesDir, Constants.JACOCO_AGENT_JAR).getAbsolutePath();
        String jacocoCliJar = new File(resourcesDir, Constants.JACOCO_CLI_JAR).getAbsolutePath();

        if (!new File(outputDir).exists()) {
            new File(outputDir).mkdirs();
        }
        String projectDir = cloneAndCheckout(url, commit);
        String projectBuildDir = projectDir + File.separator + "target" + File.separator + "classes";

        getTestCoverage(projectDir, outputDir, jacocoExtJar);
        CoverageChecker devTestCoverageChecker = new CoverageChecker(relpathToSrc, startLine, endLine,
                new File(outputDir, "dev-tests-jacoco.exec"),
                new File(projectBuildDir));
        devTestCoverageChecker.check(outputDir + File.separator + "dev-tests-cov.csv");

        if (!Utils.buildDeps(projectDir, outputDir)) {
            System.out.println("Failed to build dependencies, exiting early.");
            return;
        }

        try {
            EvoSuiteComponent evosuiteComponent = new EvoSuiteComponent(projectDir, relpathToSrc, outputDir,
                evosuiteJar, junitStandaloneJar, jacocoAgentJar);
            evosuiteComponent.getCoverage();
            CoverageChecker evosuiteCoverageChecker = new CoverageChecker(relpathToSrc, startLine, endLine,
                    new File(outputDir, "evosuite-jacoco.exec"),
                    new File(projectBuildDir));
            evosuiteCoverageChecker.check(outputDir + File.separator + "evosuite-cov.csv");
        } catch (Exception ex) {
            System.out.println("Failed to collect coverage for EvoSuite-generated tests.");
            ex.printStackTrace();
        }
        try {
            RandoopComponent randoopComponent = new RandoopComponent(projectDir, outputDir, jacocoAgentJar,
                randoopJar, junitStandaloneJar);
            randoopComponent.getCoverage();
            if (new File(outputDir, "randoop-reg-jacoco.exec").exists()) {
                CoverageChecker randoopCoverageChecker = new CoverageChecker(relpathToSrc, startLine, endLine,
                        new File(outputDir, "randoop-reg-jacoco.exec"), new File(projectBuildDir));
                randoopCoverageChecker.check(outputDir + File.separator + "randoop-reg-cov.csv");
            }
            if (new File(outputDir, "randoop-err-jacoco.exec").exists()) {
                CoverageChecker randoopCoverageChecker = new CoverageChecker(relpathToSrc, startLine, endLine,
                        new File(outputDir, "randoop-err-jacoco.exec"), new File(projectBuildDir));
                randoopCoverageChecker.check(outputDir + File.separator + "randoop-err-cov.csv");
            }
        } catch (Exception ex) {
            System.out.println("Failed to collect coverage for Randoop-generated tests.");
            ex.printStackTrace();
        }
        // Merge all jacoco.exec files into one.
        try {
            // Find all files in outputDir ending with -jacoco.exec and merge them
            File[] jacocoExecFiles = new File(outputDir).listFiles((dir, name) -> name.endsWith("-jacoco.exec"));
            List<String> command = new ArrayList<>();
            command.add("java");
            command.add("-jar");
            command.add(jacocoCliJar);
            command.add("merge");
            if (jacocoExecFiles != null) {
                for (File f : jacocoExecFiles) {
                    command.add(f.getAbsolutePath());
                }
            }
            command.add("--destfile");
            command.add(outputDir + File.separator + "combined-jacoco.exec");
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(new File(outputDir));
            pb.redirectOutput(new File(outputDir, "merged-jacoco.log"));
            pb.redirectErrorStream(true); // Merge stderr into stdout
            pb.start().waitFor();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        CoverageChecker combinedTestsCoverageChecker = new CoverageChecker(relpathToSrc, startLine, endLine, new File(outputDir, "combined-jacoco.exec"), new File(projectBuildDir));
        combinedTestsCoverageChecker.check(outputDir + File.separator + "combined-cov.csv");
        // Copy the project directory to the output directory
        try {
            Utils.copyRecursively(Paths.get(projectDir), Paths.get(outputDir + File.separator + "project"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static String cloneAndCheckout(String url, String commit) {
        try {
            Path tmpDir = Files.createTempDirectory("tmp");
            System.out.println("Temporarily cloning project to: " + tmpDir.toAbsolutePath());
            ProcessBuilder pb = new ProcessBuilder("git", "clone", url, tmpDir.toAbsolutePath().toString());
            pb.start().waitFor(); // Must wait for the clone to finish.
            ProcessBuilder pb2 = new ProcessBuilder("git", "checkout", commit);
            pb2.directory(tmpDir.toFile());
            pb2.start().waitFor(); // Must wait for the checkout to finish.
            return tmpDir.toAbsolutePath().toString();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Print the block of code at the given path, start line, and end line.
     * @param projectDir The directory of the project.
     * @param pathStr The path of the file.
     * @param startLine The start line of the block.
     * @param endLine The end line of the block.
     */
    private static void printBlock(String projectDir, String pathStr, int startLine, int endLine) {
        String filePath = projectDir + File.separator + pathStr;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return;
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            int currentLine = 1;
            System.out.println("Printing block from " + startLine + " to " + endLine + " in " + filePath);
            while ((line = reader.readLine()) != null) {
                if (currentLine >= startLine && currentLine <= endLine) {
                    System.out.println(line);
                }
                if (currentLine > endLine) {
                    break;
                }
                currentLine++;
            }
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void getTestCoverage(String projectDir, String outputDir, String jacocoExtensionPath) {
        System.out.println("===== Collecting test coverage =====");
        File logFile = new File(outputDir, "dev-tests.log");
        ProcessBuilder testProcess = new ProcessBuilder("mvn", "test", "-Dmaven.ext.class.path=" + jacocoExtensionPath, "--no-transfer-progress");
        testProcess.directory(new File(projectDir));
        testProcess.redirectOutput(logFile);
        testProcess.redirectErrorStream(true); // Merge stderr into stdout
        try {
            testProcess.start().waitFor(); // Must wait for the test to finish.
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        
        ProcessBuilder cpProcess = new ProcessBuilder("cp", projectDir + File.separator + "target" + File.separator + "jacoco.exec", outputDir + File.separator + "dev-tests-jacoco.exec");
        try {
            cpProcess.start().waitFor(); // Must wait for the copy to finish.
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        // Remove jacoco.exec file after copying it
        File jacocoExecFile = new File(projectDir + File.separator + "target" + File.separator + "jacoco.exec");
        if (jacocoExecFile.exists()) {
            if (!jacocoExecFile.delete()) {
                System.err.println("Warning: Failed to delete jacoco.exec");
            }
        }
    }
}
