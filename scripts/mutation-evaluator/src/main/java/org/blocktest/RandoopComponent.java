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
import java.util.stream.Stream;

public class RandoopComponent {
    private String projectDir;
    private String outputDir;
    private String jacocoAgentJar;
    private String junitStandaloneJar;
    private String sourcePath;
    private String generatedTestsDir;

    public RandoopComponent(String projectDir, String outputDir, String jacocoAgentJar, String randoopJar, String junitStandaloneJar, String generatedTestsDir) {
        this.projectDir = projectDir;
        this.outputDir = outputDir;
        this.jacocoAgentJar = jacocoAgentJar;
        this.junitStandaloneJar = junitStandaloneJar;
        this.generatedTestsDir = generatedTestsDir;
        this.sourcePath = projectDir + File.separator + "src" + File.separator + "main" + File.separator + "java";
    }

    public void load() throws IOException {
        this.generatedTestsDir = new File(generatedTestsDir, "randoop-tests").getAbsolutePath();
        Utils.copyRecursively(Paths.get(this.generatedTestsDir), Paths.get(new File(projectDir, "randoop-tests").getAbsolutePath()));
    }

    public int compile() {
        System.out.println("===== Compiling randoop tests =====");

        int result = 0;
        String randoopSources = outputDir + File.separator + "randoop-sources.txt";
        String randoopCompilationLog = outputDir + File.separator + "randoop-compilation-log.txt";
        // Caution! It is VERY important to compile the project again before executing generated unit tests.
        // Otherwise, the bytecode for the extracted class may be the version that is un-instrumented!
        try (PrintWriter writer = new PrintWriter(randoopSources)) {
            try (Stream<Path> sources = Files.walk(Paths.get(projectDir + File.separator + "randoop-tests"))) {
                sources.filter(Files::isRegularFile).filter(path -> path.getFileName().toString().endsWith(".java"))
                        .map(Path::toString).forEach(writer::println);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        List<String> compilationCommand = new ArrayList<>(Arrays.asList("javac", "-classpath", Utils.getDepsContent(projectDir, outputDir),
                "@" + randoopSources, "-sourcepath", sourcePath));
        try {
            ProcessBuilder pb = new ProcessBuilder(compilationCommand);
            pb.directory(new File(projectDir));
            pb.redirectOutput(new File(randoopCompilationLog));
            pb.redirectErrorStream(true); // Merge stderr into stdout
            result = pb.start().waitFor();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
            result = -1;
        }
        return result;
    }

    public int executeRegressionTest(String randoopExecutionLog) {
        System.out.println("===== Executing randoop regression tests =====");

        // Need to check if RegressionTest exist at all.
        int result = 0;
        File randoopRegressionTestFile = new File(projectDir + File.separator + "randoop-tests" + File.separator + "RegressionTest.class");
        boolean hasRegressionTest = randoopRegressionTestFile.exists() && !randoopRegressionTestFile.isDirectory();
        if (hasRegressionTest) {
            List<String> executionCommand = new ArrayList<>(Arrays.asList("java", "-javaagent:" + jacocoAgentJar,
                    "-classpath",
                    jacocoAgentJar + File.pathSeparator + sourcePath + File.pathSeparator
                            + Utils.getDepsContent(projectDir, outputDir) + File.pathSeparator
                            + projectDir + File.separator + "randoop-tests" + File.pathSeparator
                            + junitStandaloneJar,
                    "org.junit.runner.JUnitCore", "RegressionTest"));
            try {
                ProcessBuilder regressionTestPb = new ProcessBuilder(executionCommand);
                regressionTestPb.directory(new File(projectDir));
                regressionTestPb.redirectOutput(new File(randoopExecutionLog));
                regressionTestPb.redirectErrorStream(true); // Merge stderr into stdout
                Process ps = regressionTestPb.start();
                boolean finished = ps.waitFor(10, java.util.concurrent.TimeUnit.MINUTES);
                if (!finished) {
                    ps.destroyForcibly();
                    System.out.println("Randoop regression tests timed out after 10 minutes");
                    return -1;
                }
                result = ps.exitValue();
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        // Copy jacoco.exec under projectDir to outputDir, rename to randoop-reg-jacoco.exec.
        File jacocoExecFile = new File(projectDir + File.separator + "jacoco.exec");
        File randoopRegJacocoExec = new File(outputDir, "randoop-reg-jacoco.exec");
        if (jacocoExecFile.exists()) {
            try {
                Files.copy(jacocoExecFile.toPath(), randoopRegJacocoExec.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public int executeErrorTest(String randoopExecutionLog) {
        System.out.println("===== Executing randoop error tests =====");
        
        // Need to check if ErrorTest exist at all.
        int result = 0;
        File randoopErrorTestFile = new File(projectDir + File.separator + "randoop-tests" + File.separator + "ErrorTest.class");
        boolean hasErrorTest = randoopErrorTestFile.exists() && !randoopErrorTestFile.isDirectory();
        if (hasErrorTest) {
            List<String> executionCommand = new ArrayList<>(Arrays.asList("java", "-javaagent:" + jacocoAgentJar,
                    "-classpath",
                    jacocoAgentJar + File.pathSeparator + sourcePath + File.pathSeparator
                            + Utils.getDepsContent(projectDir, outputDir) + File.pathSeparator
                            + projectDir + File.separator + "randoop-tests" + File.pathSeparator
                            + junitStandaloneJar,
                    "org.junit.runner.JUnitCore", "ErrorTest"));
            try {
                ProcessBuilder errorTestPb = new ProcessBuilder(executionCommand);
                errorTestPb.directory(new File(projectDir));
                errorTestPb.redirectOutput(new File(randoopExecutionLog));
                errorTestPb.redirectErrorStream(true); // Merge stderr into stdout
                Process ps = errorTestPb.start();
                boolean finished = ps.waitFor(10, java.util.concurrent.TimeUnit.MINUTES);
                if (!finished) {
                    ps.destroyForcibly();
                    System.out.println("Randoop error tests timed out after 10 minutes");
                    return -1;
                }
                result = ps.exitValue();
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        // Copy jacoco.exec under projectDir to outputDir, rename to randoop-reg-jacoco.exec.
        File jacocoExecFile = new File(projectDir + File.separator + "jacoco.exec");
        File randoopErrJacocoExec = new File(outputDir, "randoop-err-jacoco.exec");
        if (jacocoExecFile.exists()) {
            try {
                Files.copy(jacocoExecFile.toPath(), randoopErrJacocoExec.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
}
