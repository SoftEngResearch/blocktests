package org.blocktest.utils;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ComplexityChecker {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: java ComplexityChecker <source-file> <line-number>");
            System.exit(1);
        }
        String sourceFile = args[0];
        if (sourceFile.endsWith(".java") && args.length < 2) {
            System.err.println("Usage: java ComplexityChecker <source-file> <line-number>");
            System.exit(1);
        }

        if (sourceFile.endsWith(".java")) {
            int lineNumber = args[1].contains("C") ? Integer.parseInt(args[1].split("C")[0]) : Integer.parseInt(args[1]);
            Result r = analyzeStatement(lineNumber, sourceFile);
            System.out.println(sourceFile + ":" + lineNumber + "," + r.error + "," + r.guards + "," + r.mType + "," + r.mArgs);
        } else {
            try (BufferedReader reader = new BufferedReader(new FileReader(sourceFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    try {
                        sourceFile = line.split(",")[0];
                        int lineNumber = line.split(",")[1].contains("C") ? Integer.parseInt(line.split(",")[1].split("C")[0]) : Integer.parseInt(line.split(",")[1]);
                        Result r = analyzeStatement(lineNumber, sourceFile);
                        System.out.println(sourceFile + ":" + lineNumber + "@#$#@" + r.error + "@#$#@" + r.guards + "@#$#@" + r.mType + "@#$#@" + r.mArgs + "@#$#@" + r.lineCount + "@#$#@" + r.sign);
                    } catch (Exception e) {
                        System.out.println(sourceFile + ",failed");
                    }
                }
            } catch (Exception ignored) {
            }
        }
    }

    public static Result analyzeStatement(int lineNumber, String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            return new Result(-1, "", "",-1, 0,-1);
        }

        JavaParser parser = new JavaParser();
        CompilationUnit cu;
        try {
            cu = parser.parse(Files.newInputStream(file.toPath())).getResult().get();
        } catch (Exception e) {
            return new Result(-1, "", "", -1, 0, -2);
        }

        Expression finestStatement = findFinestStatementAtLine(cu, lineNumber);

        Optional<MethodDeclaration> methodOpt = cu.findFirst(MethodDeclaration.class, m -> {
            return m.getRange()
                    .map(r -> r.begin.line <= lineNumber && r.end.line >= lineNumber)
                    .orElse(false);
        });

        int mArgs = -1;
        String mType = "";
        String sign = "";
        int lineCount = 0;
        if (methodOpt.isPresent()) {
            MethodDeclaration method = methodOpt.get();
            if (method.isPublic()) {
                mType = "public";
            } else if (method.isPrivate()) {
                mType = "private";
            } else if (method.isProtected()) {
                mType = "protected";
            } else {
                mType = "package-private";
            }

            sign = getPrettySignature(method);
            mArgs = method.getParameters().size();

            lineCount = method.getRange()
                    .map(r -> r.end.line - r.begin.line + 1) // +1 because both start and end are inclusive
                    .orElse(-1); // if range is not available
        }

        if (finestStatement != null) {
            int guard = countGuardBlocks(finestStatement);
            return new Result(guard, mType, sign, mArgs,lineCount, 0);
        } else {
            return new Result(-1, "", "",-1, 0, -2);
        }
    }

    private static Expression findFinestStatementAtLine(CompilationUnit cu, int lineNumber) {
        // Find all statements that contain the target line
        List<Expression> candidateStatements = new ArrayList<Expression>();

        // Use findAll to get all Statement nodes
        // findAll(Class<T> clazz) returns List<T> - finds all descendants of the given type
        List<Expression> allStatements = cu.findAll(Expression.class);

        for (Expression stmt : allStatements) {
            if (stmt.getRange().isPresent()) {
                int startLine = stmt.getRange().get().begin.line;
                int endLine = stmt.getRange().get().end.line;

                // Check if the target line is within this statement
                if (lineNumber >= startLine && lineNumber <= endLine) {
                    candidateStatements.add(stmt);
                }
            }
        }

        // Find the finest (smallest) statement
        Expression finestStatement = null;
        int smallestSize = Integer.MAX_VALUE;

        for (Expression stmt : candidateStatements) {
            if (stmt.getRange().isPresent()) {
                int startLine = stmt.getRange().get().begin.line;
                int endLine = stmt.getRange().get().end.line;
                int size = endLine - startLine + 1;

                if (size < smallestSize) {
                    smallestSize = size;
                    finestStatement = stmt;
                }
            }
        }

        return finestStatement;
    }

    private static int countGuardBlocks(Node node) {
        int count = 0;
        Optional<Node> parentOpt = node.getParentNode();

        while (parentOpt.isPresent()) {
            Node parent = parentOpt.get();
            if (isGuardBlock(parent)) {
                count++;
            }
            parentOpt = parent.getParentNode();
        }

        return count;
    }

    private static boolean isGuardBlock(Node node) {
        return node instanceof IfStmt ||
                node instanceof WhileStmt ||
                node instanceof ForStmt ||
                node instanceof ForEachStmt ||
                node instanceof DoStmt ||
                node instanceof SwitchStmt ||
                node instanceof CatchClause;
        // Note: TryStmt and SynchronizedStmt are NOT guard blocks because they don't have conditions to enter them
    }

    public static String getPrettySignature(MethodDeclaration m) {
        String methodName = m.getNameAsString();

        String params = m.getParameters().stream()
                .map(p -> {
                    String typeStr = p.getType().asString();

                    // erase generics: "Set<InetAddress>" => "Set"
                    int genericIdx = typeStr.indexOf('<');
                    if (genericIdx != -1) {
                        typeStr = typeStr.substring(0, genericIdx);
                    }

                    // strip array brackets: "byte[]" => "byte"
                    typeStr = typeStr.replaceAll("\\[\\]", "");

                    // handle varargs: String[] → String...
                    if (p.isVarArgs()) {
                        typeStr = typeStr.replace("[]", "") + "...";
                    }

                    return typeStr;
                })
                .reduce((a, b) -> a + ", " + b)
                .orElse("");

        return methodName + "(" + params + ")";
    }

    public static class Result {
        public int guards = -1;
        public String mType = "";
        public String sign = "";
        public int mArgs = -1;
        public int lineCount = 0;
        public int error = 0;

        public Result(int guard, String mType, String sign, int mArgs, int lineCount, int error) {
            this.guards = guard;
            this.mType = mType;
            this.sign = sign;
            this.mArgs = mArgs;
            this.lineCount = lineCount;
            this.error = error;
        }
    }
}
