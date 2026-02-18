package org.blocktest;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

public class App {
    public static void main(String[] args) {
        String execFile = args[0];
        String outputFile = args[1];
        String url = args[2];
        String commit = args[3];
        String projectDir = cloneAndCheckout(url, commit);
        CoverageChecker coverageChecker = new CoverageChecker(new File(execFile), new File(projectDir + File.separator + "target" + File.separator + "classes"));
        coverageChecker.check(outputFile);
    }

    private static String cloneAndCheckout(String url, String commit) {
        try {
            Path tmpDir = Files.createTempDirectory("tmp");
            System.out.println("Temporarily cloning project to: " + tmpDir.toAbsolutePath());
            ProcessBuilder pb = new ProcessBuilder("git", "clone", url, tmpDir.toAbsolutePath().toString());
            pb.start().waitFor(); // Must wait for the clone to finish.
            ProcessBuilder pb2 = new ProcessBuilder("git", "checkout", commit);
            pb2.directory(tmpDir.toFile());
            pb2.start().waitFor();
            ProcessBuilder pb3 = new ProcessBuilder("mvn", "clean", "compile");
            pb3.directory(tmpDir.toFile());
            pb3.start().waitFor();
            return tmpDir.toAbsolutePath().toString();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
