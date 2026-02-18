package org.blocktest.utils;

import com.github.javaparser.*;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.expr.LambdaExpr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class LambdaChecker {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: java LambdaChecker <source-file>");
            System.exit(1);
        }

        String sourceFile = args[0];

        if (sourceFile.endsWith(".java")) {
            try {
                ParserConfiguration config = new ParserConfiguration();
                JavaParser parser = new JavaParser(config);
                CompilationUnit cu = parser.parse(new File(sourceFile)).getResult().get();

                cu.findAll(LambdaExpr.class).forEach(lambda -> System.out.println(sourceFile + ":" + lambda.getRange().map(r -> r.begin.line + "-" + r.end.line).orElse("unknown")));
            } catch (Exception ignored) {
            }
        } else {
            try (BufferedReader reader = new BufferedReader(new FileReader(sourceFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    try {
                        ParserConfiguration config = new ParserConfiguration();
                        JavaParser parser = new JavaParser(config);
                        CompilationUnit cu = parser.parse(new File(line)).getResult().get();

                        String lineTmp = line;
                        cu.findAll(LambdaExpr.class).forEach(lambda -> System.out.println(lineTmp + ":" + lambda.getRange().map(r -> r.begin.line + "-" + r.end.line).orElse("unknown")));
                    } catch (Exception ignored) {
                    }

                }
            } catch (Exception ignored) {}
        }
    }
}
