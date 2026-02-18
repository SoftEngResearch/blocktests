package org.blocktest;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Stream;
import java.util.stream.Collectors;

public class EvoSuiteComponent {
    private String projectDir;
    private String relpathToSrc;
    private String outputDir;
    private String evosuiteJar;
    private String junitStandaloneJar;
    private String jacocoAgentJar;

    public EvoSuiteComponent(String projectDir, String relpathToSrc, String outputDir, String evosuiteJar, String junitStandaloneJar, String jacocoAgentJar) {
        this.projectDir = projectDir;
        this.relpathToSrc = relpathToSrc;
        this.outputDir = outputDir;
        this.evosuiteJar = evosuiteJar;
        this.junitStandaloneJar = junitStandaloneJar;
        this.jacocoAgentJar = jacocoAgentJar;
    }

    public void getCoverage() {
        generate();
        saveGenerated();
        Set<String> classes = compile();
        execute(classes);
    }

    private void generate() {
        System.out.println("===== Generating evosuite tests =====");

        long genStartTime = System.currentTimeMillis();
        try {
            File logFile = new File(outputDir, "evosuite-generation.log");
            List<String> command = Arrays.asList(
                "timeout", Constants.EVOSUITE_TIMEOUT_S + "", "java", "-jar", evosuiteJar, "-class",
                Utils.getClassNameFromSrc(relpathToSrc), "-projectCP", projectDir + File.separator + "target" + File.separator + "classes" + File.pathSeparator + Utils.getDepsContent(projectDir, outputDir), "-seed", "" + Constants.DEFAULT_SEED, "-Dsearch_budget=120",
                "-Duse_separate_classloader=false", "-Dminimize=false", "-Dassertion_strategy=all",
                "-Dfilter_assertions=true", "-Dvirtual_fs=false", "-Dvirtual_net=false",
                "-Dsandbox_mode=OFF", "-Dfilter_sandbox_tests=true", "-Dmax_loop_iterations=-1"
            );
            System.out.println("Running command: " + String.join(" ", command));
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(new File(projectDir));
            pb.redirectOutput(logFile);
            pb.redirectErrorStream(true); // Merge stderr into stdout
            pb.start().waitFor();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        long genEndTime = System.currentTimeMillis();
        File genTimeFile = new File(outputDir, "evosuite-generation-time-ms.log");
        try (PrintWriter writer = new PrintWriter(genTimeFile)) {
            writer.println(genEndTime - genStartTime);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private Set<String> compile() {
        System.out.println("===== Compiling evosuite tests =====");
        
        Set<String> srcSet = new HashSet<>();
        try (Stream<Path> stream = Files.walk(Paths.get(new File(projectDir, "evosuite-tests").getAbsolutePath()))) {
            srcSet = stream.filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".java"))
                    .map(Path::toString)
                    .collect(Collectors.toSet());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            File logFile = new File(outputDir, "evosuite-compilation.log");
            List<String> command = new ArrayList<>();
            command.addAll(Arrays.asList("javac", "-cp", evosuiteJar + File.pathSeparator + junitStandaloneJar
                        + File.pathSeparator + Utils.getDepsContent(projectDir, outputDir)));
            command.addAll(srcSet);
            System.out.println("Running command: " + String.join(" ", command));
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(new File(projectDir));
            pb.redirectOutput(logFile);
            pb.redirectErrorStream(true); // Merge stderr into stdout
            pb.start().waitFor();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }

        Set<String> classes = null;
        try (Stream<Path> stream = Files.walk(Paths.get(new File(projectDir, "evosuite-tests").getAbsolutePath()))) {
            classes = stream.filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith("Test.java"))
                    .map(path -> path.toString().replace(new File(projectDir, "evosuite-tests").getAbsolutePath() + File.separator, "").replace(".java", "")
                            .replace('/', '.'))
                    .collect(Collectors.toSet());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return classes;
    }

    private void execute(Set<String> classes) {
        System.out.println("===== Executing evosuite tests =====");

        File logFile = new File(outputDir, "evosuite-execution.log");
        try {
            List<String> command = new ArrayList<>();
            command.addAll(Arrays.asList("java", "-javaagent:" + jacocoAgentJar, "-cp",
                    jacocoAgentJar + File.pathSeparator
                    + new File(projectDir, "evosuite-tests").getAbsolutePath() + File.pathSeparator
                    + evosuiteJar + File.pathSeparator + junitStandaloneJar + File.pathSeparator
                    + Utils.getDepsContent(projectDir, outputDir),
                    "org.junit.runner.JUnitCore"));
            command.addAll(classes);
            System.out.println("Running command: " + String.join(" ", command));
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(new File(projectDir));
            pb.redirectOutput(logFile);
            pb.redirectErrorStream(true); // Merge stderr into stdout
            pb.start().waitFor();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        // Copy jacoco.exec file out of the project directory to the output directory for later analysis
        try {
            File jacocoFile = new File(projectDir, "jacoco.exec");
            if (jacocoFile.exists()) {
                File destFile = new File(outputDir, "evosuite-jacoco.exec");
                Files.copy(jacocoFile.toPath(), destFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Copied jacoco.exec to output directory: " + destFile.getAbsolutePath());
            } else {
                System.out.println("jacoco.exec file not found at: " + jacocoFile.getAbsolutePath());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        // Remove jacoco.exec
        try {
            File jacocoFile = new File(projectDir, "jacoco.exec");
            if (jacocoFile.exists()) {
                jacocoFile.delete();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void saveGenerated() {
        try {
            Utils.copyRecursively(Paths.get(new File(projectDir, "evosuite-tests").getAbsolutePath()),
                    Paths.get(new File(outputDir, "evosuite-tests").getAbsolutePath()));
            Utils.copyRecursively(Paths.get(new File(projectDir, "evosuite-report").getAbsolutePath()),
                    Paths.get(new File(outputDir, "evosuite-report").getAbsolutePath()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
