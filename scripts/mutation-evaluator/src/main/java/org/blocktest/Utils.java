package org.blocktest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.stream.Stream;
import java.io.BufferedReader;
import java.io.FileReader;

public class Utils {
    public static String SKIPS = "-Drat.skip";

    /**
     * Copy a directory recursively.
     * @param src The source directory.
     * @param dest The destination directory.
     * @throws IOException
     */
    public static void copyRecursively(Path src, Path dest) throws IOException {
        Files.walk(src).forEach(srcPath -> {
                try {
                    Path targetPath = dest.resolve(src.relativize(srcPath));
                    Files.copy(srcPath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
        });
    }

    /**
     * Write the classpath list to a file.
     * @param projectDir The project directory.
     * @param outputPathStr The output path string.
     */
    public static void writeClasspathList(String projectDir, String outputPathStr) {
        try (PrintWriter writer = new PrintWriter(outputPathStr)) {
            try (Stream<Path> stream = Files.walk(Paths.get(projectDir + File.separator + "target" + File.separator + "classes"))) {
                stream.filter(Files::isRegularFile)
                        .filter(path -> path.getFileName().toString().endsWith(".class"))
                        // package-info.class needs to be filtered out.
                        .filter(path -> !path.getFileName().toString().contains("package-info"))
                        .forEach(path -> // This lambda function turns a path into Java fqn.
                                writer.println(path.toString()
                                        .split("target" + File.separator + "classes" + File.separator)[1]
                                        .replace(".class", "")
                                        .replace(File.separatorChar, '.')
                                )
                        );
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }   

    /** This method builds the classpath list file for Randoop by removing all inner classes. */
    public static void writeRandoopClasspathList(String classpathList, String randoopClasspathList) {
        try {
            PrintWriter writer = new PrintWriter(randoopClasspathList);
            BufferedReader reader;
            try {
                reader = new BufferedReader(new FileReader(classpathList));
                String classpath = reader.readLine();
                while (classpath != null) {
                    // Exclude all inner classes and anonymous inner classes.
                    if (!classpath.contains("$")) {
                        writer.println(classpath);
                    }
                    classpath = reader.readLine();
                }
                reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static boolean buildDeps(String projectDir, String outputDir) {
        ProcessBuilder pb = new ProcessBuilder("mvn", "dependency:build-classpath", "-Dmdep.outputFile=" + projectDir + File.separator + "deps.txt");
        pb.directory(new File(projectDir));
        try {
            pb.redirectOutput(new File(outputDir, "deps-build.log"));
            pb.redirectErrorStream(true);
            pb.start().waitFor();
            try {
                File depsFile = new File(projectDir + File.separator + "deps.txt");
                if (depsFile.exists()) {
                    FileWriter fw = new FileWriter(depsFile, true);
                    fw.write(File.pathSeparator + projectDir + File.separator + "target" + File.separator + "classes");
                    fw.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return true;
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    public static String getDepsContent(String projectDir, String outputDir) {
        String cpString = null;
        if (!new File(projectDir + File.separator + "deps.txt").exists()) {
            buildDeps(projectDir, outputDir);
        }
        try {
            cpString = Files.readAllLines(Paths.get(projectDir + File.separator + "deps.txt")).get(0);
        } catch (IOException | ArrayIndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
        return cpString;
    }

    public static int compileSingleSource(String projectDir, String outputDir, String resourcesDir, String source, int mutantId) {
        List<String> command = new ArrayList<>(Arrays.asList("javac", "-cp",
                getDepsContent(projectDir, outputDir) + File.pathSeparator
                + projectDir + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.pathSeparator
                + resourcesDir + File.separator + "blocktest-1.0.jar",
                "-d", projectDir + File.separator + "target" + File.separator + "classes", source));
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(new File(projectDir));
        if (!new File(outputDir + File.separator + "logs").exists()) {
            new File(outputDir + File.separator + "logs").mkdirs();
        }
        pb.redirectOutput(new File(outputDir + File.separator + "logs" + File.separator + "compile-mutant-" + mutantId + ".log"));
        pb.redirectErrorStream(true);
        try {
            return pb.start().waitFor();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public static int testWithOriginal(String projectDir, String outputDir) {
        System.out.println("===== Testing with original code =====");
        
        ProcessBuilder pb = new ProcessBuilder("mvn", "clean", "test", SKIPS);
        pb.directory(new File(projectDir));
        pb.redirectOutput(new File(outputDir + File.separator + "logs" + File.separator + "test-with-original.log"));
        pb.redirectErrorStream(true);
        try {
            Process ps = pb.start();
            boolean finished = ps.waitFor(10, java.util.concurrent.TimeUnit.MINUTES);
            if (!finished) {
                ps.destroyForcibly();
                return -1;
            }
            return ps.exitValue();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public static int testWithExtension(String projectDir, String outputDir, String resourcesDir, String log) {
        System.out.println("===== Testing with extension =====");

        ProcessBuilder pb = new ProcessBuilder("mvn", "clean", "test", "-Dmaven.ext.class.path=" + resourcesDir + File.separator + "blocktest-extension-1.0.jar", SKIPS);
        pb.directory(new File(projectDir));
        pb.redirectOutput(new File(outputDir + File.separator + "logs" + File.separator + log));
        pb.redirectErrorStream(true);
        try {
            Process ps = pb.start();
            boolean finished = ps.waitFor(10, java.util.concurrent.TimeUnit.MINUTES);
            if (!finished) {
                ps.destroyForcibly();
                System.out.println("Developer tests timed out after 10 minutes");
                return -1;
            }
            return ps.exitValue();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    private static void extractBlockTests(String projectDir, String outputDir, String resourcesDir, String src) {
        String extractedTestsDir = outputDir + File.separator + "extracted-tests";
        if (!new File(extractedTestsDir).exists()) {
            new File(extractedTestsDir).mkdirs();
        }
        String testDir = null;
        String framework = "";
        String workingDir = null;
        // Read the first 3 lines of src for additional information
        try (BufferedReader reader = new BufferedReader(new FileReader(src))) {
            for (int i = 0; i < 3; i++) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                if (line.contains("FRAMEWORK")) {
                    framework = " --junit_version=" + line.split(":")[1].trim();
                }
                if (line.contains("TEST_DIR")) {
                    testDir = line.split(":")[1].trim();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        ProcessBuilder pb = new ProcessBuilder(
            "mvn",
            "exec:java",
            "-Dexec.mainClass=org.blocktest.BlockTestRunnerSourceCode",
            "-Dexec.args=--input_file=" + src + " --output_dir=" + extractedTestsDir + framework,
            "-Dmaven.ext.class.path=" + resourcesDir + File.separator + "blocktest-extension-1.0.jar"
        );
        pb.directory(new File(projectDir));
        // TODO: This will get overwritten many times.
        pb.redirectOutput(new File(outputDir + File.separator + "logs" + File.separator + "extract-blocktests.log"));
        pb.redirectErrorStream(true);
        try {
            pb.start().waitFor();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        String testParentForBTest = new File(src).getParentFile().getAbsolutePath().replace("src" + File.separator + "main" + File.separator + "java" + File.separator, "src" + File.separator + "test" + File.separator + "java" + File.separator);
        if (testDir != null) {
            testParentForBTest = projectDir + File.separator + testDir;
        }
        if (!new File(testParentForBTest).exists()) {
            new File(testParentForBTest).mkdirs();
        }
        try {
            Files.copy(Paths.get(extractedTestsDir + File.separator + new File(src).getName().replace(".java", "BlockTest.java")), Paths.get(testParentForBTest + File.separator + new File(src).getName().replace(".java", "BlockTest.java")), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static int runBlockTest(String projectDir, String outputDir, String resourcesDir, String log, String src) {
        System.out.println("===== Running BlockTest =====");
        extractBlockTests(projectDir, outputDir, resourcesDir, src);

        ProcessBuilder pb = new ProcessBuilder("mvn", "clean", "test", "-Dtest=*BlockTest", "-Dmaven.ext.class.path=" + resourcesDir + File.separator + "blocktest-extension-1.0.jar", SKIPS);
        pb.directory(new File(projectDir));
        pb.redirectOutput(new File(outputDir + File.separator + "logs" + File.separator + log));
        pb.redirectErrorStream(true);
        try {
            Process ps = pb.start();
            boolean finished = ps.waitFor(10, java.util.concurrent.TimeUnit.MINUTES);
            if (!finished) {
                ps.destroyForcibly();
                System.out.println("BlockTest timed out after 10 minutes");
                return -1;
            }
            return ps.exitValue();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    /**
     * Compile the project with the original code.
     * @param projectDir The project directory.
     * @param outputDir The output directory.
     * @param resourcesDir The resources directory.
     * @return The result of the compilation. 0 if successful, otherwise -1.
     */
    public static int compileProjectWithOriginal(String projectDir, String outputDir, String resourcesDir) {
        ProcessBuilder pb = new ProcessBuilder("mvn", "clean", "test-compile", SKIPS);
        pb.directory(new File(projectDir));
        pb.redirectOutput(new File(outputDir + File.separator + "logs" + File.separator + "compile-project-with-original.log"));
        pb.redirectErrorStream(true);
        try {
            return pb.start().waitFor();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    /**
     * Compile the project with the BTest extension.
     * @param projectDir The project directory.
     * @param outputDir The output directory.
     * @param resourcesDir The resources directory.
     * @return The result of the compilation. 0 if successful, otherwise -1.
     */
    public static int compileProjectWithBTest(String projectDir, String outputDir, String resourcesDir) {
        ProcessBuilder pb = new ProcessBuilder("mvn", "clean", "test-compile", "-Dmaven.ext.class.path=" + resourcesDir + File.separator + "blocktest-extension-1.0.jar", SKIPS);
        pb.directory(new File(projectDir));
        pb.redirectOutput(new File(outputDir + File.separator + "logs" + File.separator + "compile-project-with-btest.log"));
        pb.redirectErrorStream(true);
        try {
            return pb.start().waitFor();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    /**
     * Replace the original source code with the injected source code.
     * @param projectDir The project directory.
     * @param absPathToSrc The absolute path to the original source code.
     * @param injectedSrc The absolute path to the source code with block tests injected.
     */
    public static void replaceWithInjectedSource(String projectDir, String absPathToSrc, String injectedSrc) {
        try {
            Files.copy(Paths.get(injectedSrc), Paths.get(absPathToSrc), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void earlyExitWithLog(String outputDir, String message) {
        try (PrintWriter writer = new PrintWriter(new File(outputDir, "early-exit.log"))) {
            writer.println(message);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        System.exit(1);
    }

    public static String getClassNameFromSrc(String relpathToSrc) {
        return relpathToSrc.replace("src/main/java/", "").replace(".java", "").replace(File.separatorChar, '.');
    }
}
