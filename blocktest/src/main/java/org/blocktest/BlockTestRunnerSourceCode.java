package org.blocktest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import org.blocktest.utils.TypeResolver;
import org.blocktest.utils.Util;

public class BlockTestRunnerSourceCode {

    public static void main(String[] args) {
        HashMap<String, String> params = convertToKeyValuePair(args);
        String inputFile = params.get("input_file");

        if (inputFile == null || inputFile.isEmpty()) {
            throw new RuntimeException("input_file parameter is required");
        }

        Path inputFilePath = Paths.get(inputFile).toAbsolutePath();
        if (!Files.exists(inputFilePath)) {
            throw new RuntimeException("input file does not exist");
        }

        if (params.containsKey("dep_file_path")) {
            String depFilePath = params.get("dep_file_path");
            if (!Files.exists(Paths.get(depFilePath))) {
                throw new RuntimeException("dep file does not exist: " + depFilePath);
            }
            try {
                Util.depClassPaths = new String(Files.readAllBytes(Paths.get(depFilePath)));
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }

        if (params.containsKey("app_src_path")) {
            Util.appSrcPath = params.get("app_src_path");
        }

        if (params.containsKey("junit_version")) {
            Util.junitVersion = params.get("junit_version").toLowerCase().replace(" ", "");
        }

        String publicClassName = inputFilePath.getFileName().toString().split(".java")[0];
        String inputFolder = inputFilePath.getParent().toFile().toString();

        // Notify java parser about all the types, based on jar (class path) or app_src_path
        TypeResolver.setup();

        String testClassName = publicClassName + "BlockTest";
        String testOutputFile;
        if (params.containsKey("output_file")) {
            testOutputFile = params.get("output_file");
        } else {
            if (params.containsKey("output_dir")) {
                testOutputFile = params.get("output_dir") + "/" + testClassName + ".java";
            } else {
                testOutputFile = null;
            }
        }
        
        int duplicatedTestCount = 0;
        if (params.containsKey("duplicated_test_count")) {
            duplicatedTestCount = Integer.parseInt(params.get("duplicated_test_count"));
        }
        
        boolean coverage = false;
        if (params.containsKey("coverage")) {
            coverage = Boolean.parseBoolean(params.get("coverage"));
        }
        
        boolean rewrite = false;
        if (params.containsKey("rewrite")) {
            rewrite = Boolean.parseBoolean(params.get("rewrite"));
        }

        boolean compile = true;
        if (params.containsKey("compile")) {
            compile = Boolean.parseBoolean(params.get("compile"));
        }

        if (params.containsKey("predict")) {
            Util.predictValue = Boolean.parseBoolean(params.get("predict"));
        }

        TestExtraction.extractTest(inputFilePath.toAbsolutePath().toString(), testOutputFile, publicClassName,
                testClassName, duplicatedTestCount, coverage, rewrite, compile);
    }

    private static HashMap<String, String> convertToKeyValuePair(String[] args) {
        HashMap<String, String> params = new HashMap<>();

        for (String arg : args) {
            String[] splitFromEqual = arg.split("=");
            // key is expected to start with --
            String key = splitFromEqual[0].substring(2);
            String value = splitFromEqual[1];
            params.put(key, value);
        }

        return params;
    }
}
