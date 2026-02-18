// From inlinetest
package org.blocktest.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.EmptyStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.IntersectionType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.UnionType;
import com.github.javaparser.ast.type.WildcardType;

public class Util {
    public static String depClassPaths;
    public static String appSrcPath;
    public static String junitVersion = "junit4";

    public static boolean predictValue = false;

    public static boolean loadXml = true;

    public static Type getTypeFromStr(String input) {
        input = input.trim();

        // Handle primitive types
        switch (input) {
            case "int":
                return new PrimitiveType(PrimitiveType.Primitive.INT);
            case "boolean":
                return new PrimitiveType(PrimitiveType.Primitive.BOOLEAN);
            case "char":
                return new PrimitiveType(PrimitiveType.Primitive.CHAR);
            case "byte":
                return new PrimitiveType(PrimitiveType.Primitive.BYTE);
            case "short":
                return new PrimitiveType(PrimitiveType.Primitive.SHORT);
            case "long":
                return new PrimitiveType(PrimitiveType.Primitive.LONG);
            case "float":
                return new PrimitiveType(PrimitiveType.Primitive.FLOAT);
            case "double":
                return new PrimitiveType(PrimitiveType.Primitive.DOUBLE);
            // Add cases for other primitives as needed
        }

        if (input.equals("T") || input.equals("? extends T") || input.equals("? super T"))
            return new ClassOrInterfaceType().setName("Object");

        // Handle array types
        if (input.endsWith("[]")) {
            // Handle wildcard types
            if (input.startsWith("?")) {
                if (input.startsWith("? extends ")) {
                    input = input.substring(10);
                } else if (input.startsWith("? super ")) {
                    input = input.substring(8);
                }
            }

            return new ArrayType(getTypeFromStr(input.substring(0, input.length() - 2)));
        }

        // Handle wildcard types
        if (input.startsWith("?")) {
            WildcardType wildcardType = new WildcardType();
            if (input.startsWith("? extends ")) {
                wildcardType.setExtendedType(new ClassOrInterfaceType().setName(input.substring(10)));
            } else if (input.startsWith("? super ")) {
                wildcardType.setSuperType(new ClassOrInterfaceType().setName(input.substring(8)));
            }
            return wildcardType;
        }

        // Handle union types (for multi-catch)
        if (input.contains("|")) {
            return new UnionType((NodeList<ReferenceType>) Arrays.stream(input.split("\\|"))
                    .map(String::trim)
                    .map(str -> (new ClassOrInterfaceType().setName(str)).asReferenceType())
                    .collect(Collectors.toList()));
        }

        // Handle intersection types (for generics)
        if (input.contains("&")) {
            return new IntersectionType((NodeList<ReferenceType>) Arrays.stream(input.split("&"))
                    .map(String::trim)
                    .map(str -> (new ClassOrInterfaceType().setName(str)).asReferenceType())
                    .collect(Collectors.toList()));
        }

        // Handle generic types (simplified example)
        if (input.contains("<") && input.contains(">")) {
            String baseType = input.substring(0, input.indexOf('<'));
            String typeParam = input.substring(input.indexOf('<') + 1, input.lastIndexOf('>'));
            return new ClassOrInterfaceType()
                    .setName(baseType)
                    .setTypeArguments(getTypeFromStr(typeParam));
        }

        // Handle simple class or interface types
        return new ClassOrInterfaceType().setName(input);
    }

    public static boolean isConstant(String name) {
        return name.toUpperCase().equals(name);
    }

    public static boolean isTestStatement(Node node) {
        return isTestStatement(node, false);
    }

    public static boolean isTestStatement(Node node, boolean matched) {
        if (node instanceof ExpressionStmt) {
            return isTestStatement(((ExpressionStmt) node).getExpression());
        } else if (node instanceof MethodCallExpr) {
            if (((MethodCallExpr) node).getNameAsString().equals(Constant.DECLARE_NAME)
                    || ((MethodCallExpr) node).getNameAsString().equals(Constant.DECLARE_NAME_LAMBDA)) {
                return matched;
            }

            if (((MethodCallExpr) node).getNameAsString().equals(Constant.GIVEN) || ((MethodCallExpr) node).getNameAsString().equals(Constant.ARGS)
                    || ((MethodCallExpr) node).getNameAsString().equals(Constant.MOCK)) {
                matched = true;
            }

            if (((MethodCallExpr) node).getScope().isPresent()) {
                return isTestStatement(((MethodCallExpr) node).getScope().get(), matched);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /*
     * Given testName, return true is line is blocktest().end(testName)
     */
    public static String isEndStatement(Node node) {
        return isEndStatement(node, false);
    }

    public static boolean isBlockTest(Node node) {
        if (node instanceof MethodCallExpr) {
            return isBlocktestMethodCall((MethodCallExpr) node);
        }

        // Case 2: Node is an ExpressionStmt containing a MethodCallExpr
        if (node instanceof ExpressionStmt) {
            ExpressionStmt exprStmt = (ExpressionStmt) node;
            Expression expr = exprStmt.getExpression();
            if (expr instanceof MethodCallExpr) {
                return isBlocktestMethodCall((MethodCallExpr) expr);
            }
        }

        return false;
    }

    private static boolean isBlocktestMethodCall(MethodCallExpr methodCall) {
        // Find the root of the method call chain
        MethodCallExpr current = methodCall;
        while (current.getScope().isPresent() && current.getScope().get() instanceof MethodCallExpr) {
            current = (MethodCallExpr) current.getScope().get();
        }
        return "blocktest".equals(current.getNameAsString());
    }

    public static String isEndStatement(Node node, boolean matched) {
        if (node instanceof ExpressionStmt) {
            return isEndStatement(((ExpressionStmt) node).getExpression(), matched);
        } else if (node instanceof MethodCallExpr) {
            MethodCallExpr methodCall = ((MethodCallExpr) node).asMethodCallExpr();
            if (methodCall.getNameAsString().equals(Constant.END) ||
                    methodCall.getNameAsString().equals(Constant.START)) {
                // Match end()
                if (!((MethodCallExpr) node).getScope().isPresent()) {
                    return null;
                }

                return isEndStatement(((MethodCallExpr) node).getScope().get(), true);

            } else if (matched && (methodCall.getNameAsString().equals(Constant.DECLARE_NAME)
                    || methodCall.getNameAsString().equals(Constant.DECLARE_NAME_LAMBDA))) {
                // Match blocktest().end()
                if (methodCall.getArguments().size() == 1) {
                    // Match blocktest(?).end()
                    Expression arg = methodCall.getArgument(0);
                    if (arg instanceof StringLiteralExpr) {
                        // Match blocktest(testName).end()
                        return ((StringLiteralExpr) arg).getValue();
                    }
                }
            }
        }

        return null;
    }

    public static String renameGiven(String input) {
        return input.replace("*", "time").replace("+", "plus").replace("-", "minus").replace("/", "divide")
                .replace("=", "equal").replace("!", "not").replace(">", "greater").replace("<", "less")
                .replace("&", "and").replace("|", "or").replace("^", "xor").replace("%", "mod").replace("?", "question")
                .replace("(", "_").replace(")", "_").replace(" ", "").replace("[", "__").replace("]", "")
                .replace(".", "__").replace("\"", "").replace(",", "_").replace("'", "_");
    }

    public static void replaceExpressionsWithVariables(Node blockStmt, Set<String> replacements) {
        if (replacements.isEmpty())
            return;

        blockStmt.findAll(Expression.class).forEach(expr -> {
            String exprString = expr.toString();
            if (replacements.contains(exprString)) {
                String replacementVar = renameGiven(exprString);
                NameExpr replacement = new NameExpr(replacementVar);
                expr.replace(replacement);
            }
        });
    }

    public static BlockStmt substituteMockedValues(BlockStmt blockStmt, Map<String, List<Expression>> mockMethods) {
        if (mockMethods == null || mockMethods.isEmpty()) {
            return blockStmt;
        }

        Map<String, Integer> mockedCounts = mockMethods.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> 0));

        System.out.println("MOCKING... with methods: " + mockMethods.keySet());

        blockStmt.findAll(MethodCallExpr.class).stream()
                .filter(methodCall -> {
                    // Check if this method call is NOT the scope of another method call
                    Optional<Node> parent = methodCall.getParentNode();
                    if (parent.isPresent() && parent.get() instanceof MethodCallExpr) {
                        MethodCallExpr parentMethodCall = (MethodCallExpr) parent.get();
                        return !parentMethodCall.getScope().map(scope -> scope.equals(methodCall)).orElse(false);
                    }
                    return true; // Keep it if it's not the scope of another method call
                })
                .forEach(methodCall -> {
                    String methodName = methodCall.toString();
                    if (!mockMethods.containsKey(methodName)) {
                        // This part enables a more powerful parameter matching for method calls
                        // for example, if the method call is a.b(123).c(456).d();
                        // we can match this using mock method a.b(..).c(..).d(), a.b(..).c(456).d(), etc.
                        MethodCallPatternGenerator generator = new MethodCallPatternGenerator();
                        System.out.println(methodName + " alternative contains " + generator.generateAllPatterns(methodCall));
                        for (String potentialName : generator.generateAllPatterns(methodCall)) {
                            if (mockMethods.containsKey(potentialName)) {
                                System.out.println("Although " + methodName + " is not found in mock method, the alternative " +
                                        potentialName + " is found in mock methods");
                                methodName = potentialName;
                                break;
                            }
                        }
                    }

                    if (mockMethods.containsKey(methodName)) {
                        List<Expression> mockValues = mockMethods.get(methodName);
                        if (mockValues != null && !mockValues.isEmpty()) {
                            if (mockedCounts.get(methodName) >= mockValues.size()) {
                                // running out of mocked values, just use the last one
                                System.out.println("Replacing method call " + methodName + " with " + mockValues.get(mockValues.size() - 1));
                                methodCall.replace(mockValues.get(mockValues.size() - 1));
                            } else {
                                System.out.println("Replacing method call " + methodName + " with " + mockValues.get(mockedCounts.get(methodName)));
                                methodCall.replace(mockValues.get(mockedCounts.get(methodName)));
                                mockedCounts.put(methodName, mockedCounts.get(methodName) + 1);
                            }
                        } else {
                            System.out.println("Replacing method call " + methodName + " with empty statement");
                            methodCall.findAncestor(ExpressionStmt.class).ifPresent(Node::remove);
                        }
                    }
        });

        blockStmt.findAll(NameExpr.class).forEach(nameExpr -> {
            String variableName = nameExpr.getNameAsString();
            if (mockMethods.containsKey(variableName)) {
                List<Expression> mockValues = mockMethods.get(variableName);
                if (mockValues != null && !mockValues.isEmpty()) {
                    if (mockedCounts.get(variableName) >= mockValues.size()) {
                        // running out of mocked values, just use the last one
                        System.out.println("Replacing variable name " + variableName + " with " + mockValues.get(mockValues.size() - 1));
                        nameExpr.replace(mockValues.get(mockValues.size() - 1));
                    } else {
                        System.out.println("Replacing variable name " + variableName + " with " + mockValues.get(mockedCounts.get(variableName)));
                        nameExpr.replace(mockValues.get(mockedCounts.get(variableName)));
                        mockedCounts.put(variableName, mockedCounts.get(variableName) + 1);
                    }
                } else {
                    System.out.println("Replacing variable name " + variableName + " with empty statement");
                    nameExpr.findAncestor(ExpressionStmt.class).ifPresent(Node::remove);
                }
            }
        });

        blockStmt.findAll(FieldAccessExpr.class).forEach(fieldAccessExpr -> {
            String fieldName = fieldAccessExpr.toString();
            if (mockMethods.containsKey(fieldName)) {
                List<Expression> mockValues = mockMethods.get(fieldName);
                if (mockValues != null && !mockValues.isEmpty()) {
                    if (mockedCounts.get(fieldName) >= mockValues.size()) {
                        // running out of mocked values, just use the last one
                        System.out.println("Replacing field access " + fieldName + " with " + mockValues.get(mockValues.size() - 1));
                        fieldAccessExpr.replace(mockValues.get(mockValues.size() - 1));
                    } else {
                        System.out.println("Replacing field access " + fieldName + " with " + mockValues.get(mockedCounts.get(fieldName)));
                        fieldAccessExpr.replace(mockValues.get(mockedCounts.get(fieldName)));
                        mockedCounts.put(fieldName, mockedCounts.get(fieldName) + 1);
                    }
                } else {
                    System.out.println("Replacing field access " + fieldName + " with empty statement");
                    fieldAccessExpr.findAncestor(ExpressionStmt.class).ifPresent(Node::remove);
                }
            }
        });

        blockStmt.findAll(Expression.class).forEach(expression -> {
            String expressionString = expression.toString();
            if (mockMethods.containsKey(expressionString)) {
                List<Expression> mockValues = mockMethods.get(expressionString);
                if (mockValues != null && !mockValues.isEmpty()) {
                    if (mockedCounts.get(expressionString) >= mockValues.size()) {
                        // running out of mocked values, just use the last one
                        System.out.println("Replacing expression " + expressionString + " with " + mockValues.get(mockValues.size() - 1));
                        expression.replace(mockValues.get(mockValues.size() - 1));
                    } else {
                        System.out.println("Replacing expression " + expressionString + " with " + mockValues.get(mockedCounts.get(expressionString)));
                        expression.replace(mockValues.get(mockedCounts.get(expressionString)));
                        mockedCounts.put(expressionString, mockedCounts.get(expressionString) + 1);
                    }
                } else {
                    System.out.println("Replacing expression " + expressionString + " with empty statement");
                    expression.findAncestor(ExpressionStmt.class).ifPresent(Node::remove);
                }
            }
        });

        return blockStmt;
    }
}
