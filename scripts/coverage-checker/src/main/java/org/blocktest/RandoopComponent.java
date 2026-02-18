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
import java.nio.charset.StandardCharsets;

public class RandoopComponent {
    private String projectDir;
    private String outputDir;
    private String jacocoAgentJar;
    private String randoopJar;
    private String junitStandaloneJar;
    private String sourcePath;

    public RandoopComponent(String projectDir, String outputDir, String jacocoAgentJar, String randoopJar, String junitStandaloneJar) {
        this.projectDir = projectDir;
        this.outputDir = outputDir;
        this.jacocoAgentJar = jacocoAgentJar;
        this.randoopJar = randoopJar;
        this.junitStandaloneJar = junitStandaloneJar;
        this.sourcePath = projectDir + File.separator + "src" + File.separator + "main" + File.separator + "java";
    }

    public void getCoverage() {
        generate();
        saveGenerated();
        compile();
        execute();
    }

    private void generate() {
        System.out.println("===== Generating randoop tests =====");

        long genStartTime = System.currentTimeMillis();
        String classpathList = projectDir + File.separator + "classpath-list.txt";
        Utils.writeClasspathList(projectDir, classpathList);
        String randoopClasspathList = projectDir + File.separator + "randoop-classpath-list.txt";
        Utils.writeRandoopClasspathList(classpathList, randoopClasspathList);
        long classpathCount = 0;
        try (Stream<String> stream = Files.lines(Paths.get(classpathList), StandardCharsets.UTF_8)) {
            classpathCount = stream.count();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        long timeout = Constants.RANDOOP_TIMEOUT_S;
        if (classpathCount * Constants.RANDOOP_SINGLE_CLASS_TIMEOUT_S < Constants.RANDOOP_TIMEOUT_S) {
            timeout = classpathCount * Constants.RANDOOP_SINGLE_CLASS_TIMEOUT_S;
        }

        try {
            List<String> command = new ArrayList<>();
            command.addAll(Arrays.asList("timeout", timeout + 180 + "", "java", "-Xmx512G", "-classpath",
                    randoopJar + File.pathSeparator + Utils.getDepsContent(projectDir, outputDir) + File.pathSeparator
                            + junitStandaloneJar,
                    "randoop.main.Main", "gentests", "--time-limit=" + timeout, "--usethreads=true",
                        "--randomseed=" + Constants.DEFAULT_SEED, "--classlist=" + randoopClasspathList));
            ProcessBuilder pb = new ProcessBuilder(command);
            File randoopTestsDir = new File(projectDir, "randoop-tests");
            if (!randoopTestsDir.exists()) {
                randoopTestsDir.mkdirs();
            }
            pb.directory(randoopTestsDir);
            pb.redirectOutput(new File(outputDir, "randoop-generation.log"));
            pb.redirectErrorStream(true); // Merge stderr into stdout
            pb.start().waitFor();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        long genEndTime = System.currentTimeMillis();
        File genTimeFile = new File(outputDir, "randoop-generation-time-ms.log");
        try (PrintWriter writer = new PrintWriter(genTimeFile)) {
            writer.println(genEndTime - genStartTime);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private void compile() {
        System.out.println("===== Compiling randoop tests =====");

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
            pb.start().waitFor();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private void execute() {
        System.out.println("===== Executing randoop tests =====");

        String randoopExecutionLog = outputDir + File.separator + "randoop-execution-log.txt";
        executeRegressionTest(randoopExecutionLog);
        executeErrorTest(randoopExecutionLog);
    }

    private void executeRegressionTest(String randoopExecutionLog) {
        // Need to check if RegressionTest exist at all.
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
                regressionTestPb.start().waitFor();
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
    }

    private void executeErrorTest(String randoopExecutionLog) {
        // Need to check if ErrorTest exist at all.
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
                errorTestPb.start().waitFor();
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
    }

    private void saveGenerated() {
        try {
            Utils.copyRecursively(Paths.get(projectDir + File.separator + "randoop-tests"),
                    Paths.get(outputDir + File.separator + "randoop-tests"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
