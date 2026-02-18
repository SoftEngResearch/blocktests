package org.blocktest;

import java.io.File;
import java.io.IOException;
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
    private String outputDir;
    private String evosuiteJar;
    private String junitStandaloneJar;
    private String jacocoAgentJar;
    private String generatedTestsDir;
    private Set<String> classes;

    public EvoSuiteComponent(String projectDir, String outputDir, String evosuiteJar, String junitStandaloneJar, String jacocoAgentJar, String generatedTestsDir) {
        this.projectDir = projectDir;
        this.outputDir = outputDir;
        this.evosuiteJar = evosuiteJar;
        this.junitStandaloneJar = junitStandaloneJar;
        this.jacocoAgentJar = jacocoAgentJar;
        this.generatedTestsDir = generatedTestsDir;
    }

    /** Load the already-generated tests to the project directory. */
    public void load() throws IOException {
        this.generatedTestsDir = new File(generatedTestsDir, "evosuite-tests").getAbsolutePath();
        Utils.copyRecursively(Paths.get(this.generatedTestsDir), Paths.get(new File(projectDir, "evosuite-tests").getAbsolutePath()));
    }

    public int compile() {
        System.out.println("===== Compiling evosuite tests =====");
        
        int result = 0;
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
            result = pb.start().waitFor();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }

        this.classes = new HashSet<>();
        try (Stream<Path> stream = Files.walk(Paths.get(new File(projectDir, "evosuite-tests").getAbsolutePath()))) {
            this.classes = stream.filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith("Test.java"))
                    .map(path -> path.toString().replace(new File(projectDir, "evosuite-tests").getAbsolutePath() + File.separator, "").replace(".java", "")
                            .replace('/', '.'))
                    .collect(Collectors.toSet());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public int execute() {
        System.out.println("===== Executing evosuite tests =====");

        File logFile = new File(outputDir, "evosuite-execution.log");
        int result = 0;
        try {
            List<String> command = new ArrayList<>();
            command.addAll(Arrays.asList("java", "-javaagent:" + jacocoAgentJar, "-cp",
                    jacocoAgentJar + File.pathSeparator
                    + new File(projectDir, "evosuite-tests").getAbsolutePath() + File.pathSeparator
                    + evosuiteJar + File.pathSeparator + junitStandaloneJar + File.pathSeparator
                    + Utils.getDepsContent(projectDir, outputDir),
                    "org.junit.runner.JUnitCore"));
            command.addAll(this.classes);
            System.out.println("Running command: " + String.join(" ", command));
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(new File(projectDir));
            pb.redirectOutput(logFile);
            pb.redirectErrorStream(true); // Merge stderr into stdout
            Process ps = pb.start();
            boolean finished = ps.waitFor(10, java.util.concurrent.TimeUnit.MINUTES);
            if (!finished) {
                ps.destroyForcibly();
                System.out.println("EvoSuite tests timed out after 10 minutes");
                return -1;
            }
            result = ps.exitValue();
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
        return result;
    }
}
