package org.blocktest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.stream.Stream;
import java.nio.file.Paths;
import java.io.File;

public class Utils {
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

    public static String getClassNameFromSrc(String relpathToSrc) {
        return relpathToSrc.replace("src/main/java/", "").replace(".java", "").replace(File.separatorChar, '.');
    }
}

