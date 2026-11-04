package org.blocktest.utils;

import java.util.*;
import java.util.stream.Collectors;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;

public class MockUtil {
    public static boolean newMock = false;

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

    /*
     * Unlike substituteMockedValues, this method will not simply replace mocked method calls with the provided values.
     * Instead, it will create a dummy method that returns the mocked value, and replace the method call with a call to this dummy method.
     * This is useful when we have foo().length(), and we want to set foo() to null. Because null.length() will not compile.
     * This method can also handle parameters matching. So mock(foo(1), 1) will only replace foo(j) with 1 if j == 1, null otherwise.
     * This is useful when we have foo(j) and we do not want to replace all foo(j) with the same value.
     */
    public static BlockStmt substituteMockedValuesAdvanced(BlockStmt blockStmt, Map<String, List<Expression>> mockMethods) {
        if (mockMethods == null || mockMethods.isEmpty()) return blockStmt;
        MethodDeclaration enclosingMethod = blockStmt.findAncestor(MethodDeclaration.class)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Detached blocks need substituteMockedValuesAdvanced(block, mocks, testName)"));
        ClassOrInterfaceDeclaration enclosingClass = blockStmt.findAncestor(ClassOrInterfaceDeclaration.class)
                .orElseThrow(() -> new IllegalArgumentException("Mock helpers require a containing class")); // generated unit test class
        List<FieldDeclaration> countFields = new ArrayList<>();
        List<MethodDeclaration> helpers = substituteMockedValuesAdvanced(
                blockStmt, mockMethods, enclosingMethod.getNameAsString(), countFields);
        for (FieldDeclaration field : countFields) {
            enclosingClass.addMember(field);
            // System.out.println(field);
            // count how many time the method is called
        }
        for (MethodDeclaration helper : helpers) {
            enclosingClass.addMember(helper);
            // System.out.println(helper);
            // mocked method
        }
        return blockStmt;
    }

    public static List<MethodDeclaration> substituteMockedValuesAdvanced(
            BlockStmt blockStmt, Map<String, List<Expression>> mockMethods, String testName) {
        Optional<ClassOrInterfaceDeclaration> enclosingClass =
                blockStmt.findAncestor(ClassOrInterfaceDeclaration.class);
        if (!enclosingClass.isPresent()) {
            return substituteMockedValuesAdvanced(blockStmt, mockMethods, testName, null);
        }
        List<FieldDeclaration> countFields = new ArrayList<>();
        List<MethodDeclaration> helpers = substituteMockedValuesAdvanced(
                blockStmt, mockMethods, testName, countFields);
        for (FieldDeclaration field : countFields) {
            enclosingClass.get().addMember(field);
        }
        return helpers;
    }

    // Mocked method
    public static List<MethodDeclaration> substituteMockedValuesAdvanced(
            BlockStmt blockStmt, Map<String, List<Expression>> mockMethods, String testName,
            List<FieldDeclaration> countFields) {
        if (mockMethods == null || mockMethods.isEmpty()) {
            return Collections.emptyList();
        }

        List<AdvancedMockRule> methodRules = new ArrayList<>();
        Map<String, List<Expression>> otherMocks = new HashMap<>();
        for (Map.Entry<String, List<Expression>> entry : mockMethods.entrySet()) {
            try {
                Expression pattern = StaticJavaParser.parseExpression(
                        entry.getKey().replace("(..)", "(BLOCKTEST_ANYTHING)"));
                if (pattern.isMethodCallExpr()) {
                    methodRules.add(new AdvancedMockRule(pattern.asMethodCallExpr(), entry.getValue()));
                } else {
                    otherMocks.put(entry.getKey(), entry.getValue());
                }
            } catch (RuntimeException e) {
                otherMocks.put(entry.getKey(), entry.getValue());
            }
        }
                
        System.out.println(">>otherMocks");
        System.out.println(otherMocks);

        if (methodRules.isEmpty()) {
            substituteMockedValues(blockStmt, otherMocks);
            return Collections.emptyList();
        }

        Map<String, AdvancedMockHelper> helpers = new LinkedHashMap<>();
        Set<String> usedCounterNames = new HashSet<>();
        boolean replaced;
        do {
            replaced = false;
            for (MethodCallExpr call : blockStmt.findAll(MethodCallExpr.class)) {
                if (!isWithin(call, blockStmt)) {
                    continue;
                }
                String shape = mockCallShape(call);
                List<AdvancedMockRule> rules = matchingAdvancedMockRules(call, methodRules);
                if (rules.isEmpty()) {
                    continue;
                }
                
                System.out.println("Shape is " + shape);
                
                // idea: a mock for a whole call chain takes precedence over a mock for its scope
                if (call.getParentNode().filter(MethodCallExpr.class::isInstance)
                        .map(MethodCallExpr.class::cast)
                        .filter(parent -> parent.getScope().orElse(null) == call)
                        .filter(parent -> !matchingAdvancedMockRules(parent, methodRules).isEmpty())
                        .isPresent()) {
                    continue;
                }

                String returnType = advancedMockReturnType(call, rules); // get the return type of the mocked method
                System.out.println("Return type is " + returnType);
                String helperKey = shape + "\u0000" + returnType; // a unique key
                AdvancedMockHelper helper = helpers.get(helperKey);
                if (helper == null) {
                    int index = helpers.size();
                    String localCounterName = null;
                    if (countFields == null) {
                        localCounterName = "BLOCKTEST_MOCK_COUNTS_" + index;
                        while (blockStmt.toString().contains(localCounterName)
                                || usedCounterNames.contains(localCounterName)) {
                            localCounterName += "_";
                        }
                        usedCounterNames.add(localCounterName);
                    }
                    helper = new AdvancedMockHelper(testName + "_mock" + index,
                            returnType, localCounterName, rules);
                    helpers.put(helperKey, helper);
                }
                NodeList<Expression> arguments = new NodeList<>();
                if (helper.localCounterName != null) {
                    arguments.add(new NameExpr(helper.localCounterName));
                }
                for (Expression argument : mockCallArguments(call)) {
                    arguments.add(argument.clone());
                }
                call.replace(new MethodCallExpr(null, helper.name, arguments));
                replaced = true;
            }
        } while (replaced);

        if (helpers.isEmpty()) {
            substituteMockedValues(blockStmt, otherMocks);
            return Collections.emptyList();
        }
        substituteMockedValues(blockStmt, otherMocks);
        List<MethodDeclaration> methods = new ArrayList<>();
        int localIndex = 0;
        for (AdvancedMockHelper helper : helpers.values()) {
            if (helper.localCounterName == null) {
                countFields.add(StaticJavaParser.parseBodyDeclaration("private static int[] "
                        + helper.countFieldName + " = new int[" + helper.rules.size() + "];"
                        ).asFieldDeclaration());
            } else {
                blockStmt.getStatements().add(localIndex++, StaticJavaParser.parseStatement(
                        "int[] " + helper.localCounterName + " = new int["
                                + helper.rules.size() + "];"));
            }
            methods.add(StaticJavaParser.parseBodyDeclaration(helper.source()).asMethodDeclaration());
        }
        return methods;
    }

    private static final class AdvancedMockRule {
        final MethodCallExpr pattern;
        final List<Expression> arguments;
        final List<Expression> values;
        final int specificity;

        AdvancedMockRule(MethodCallExpr call, List<Expression> values) {
            this(call, null, values);
        }

        AdvancedMockRule(MethodCallExpr pattern, List<Expression> arguments,
                         List<Expression> values) {
            this.pattern = pattern;
            this.arguments = arguments;
            this.values = values;
            this.specificity = arguments == null ? 0 : (int) arguments.stream()
                    .filter(Objects::nonNull).count();
        }
    }

    private static final class AdvancedMockHelper {
        final String name;
        final String countFieldName;
        final String localCounterName;
        final String returnType;
        final List<AdvancedMockRule> rules;

        AdvancedMockHelper(String name, String returnType, String localCounterName,
                           List<AdvancedMockRule> rules) {
            this.name = name;
            this.countFieldName = name + "_count";
            this.localCounterName = localCounterName;
            this.returnType = returnType;
            this.rules = new ArrayList<>(rules);
            this.rules.sort(Comparator.comparingInt((AdvancedMockRule rule) -> rule.specificity)
                    .reversed().thenComparing(rule -> rule.pattern.toString()));
        }

        String source() {
            // this is the generated mocked method
            /*  Example:
                private static java.lang.Boolean testLine148_mock0(Object... args) {
                    if (java.util.Objects.deepEquals(args[0], <arg1>)) {
                        testLine148_mock0_count[0]++;
                    }
                    if (java.util.Objects.deepEquals(args[0], <arg2>)) { // if multiple values are provided: e.g., mock("mockMethod(arg2)", true, false)
                        int index = testLine148_mock0_count[1]++;
                        if (index == 0)
                            return true;
                        if (index == 1)
                            return false;
                        return true;
                    }
                    if (true) { // if default mock is provided: e.g., mock("mockMethod(..)", true)
                        testLine148_mock0_count[1]++;
                        return true;
                    }
                }
            */
            StringBuilder source = new StringBuilder("private static ")
                    .append(returnType).append(' ').append(name).append("(Object... args) {");
            if (localCounterName != null) {
                source.append("int[] counts = (int[]) args[0];");
            }
            for (int ruleIndex = 0; ruleIndex < rules.size(); ruleIndex++) {
                AdvancedMockRule rule = rules.get(ruleIndex);
                source.append("if (");
                boolean hasCondition = false;
                for (int i = 0; i < rule.arguments.size(); i++) {
                    Expression expected = rule.arguments.get(i);
                    if (expected == null) continue;
                    if (hasCondition) source.append(" && ");
                    source.append("java.util.Objects.deepEquals(args[")
                            .append(i + (localCounterName == null ? 0 : 1)).append(']')
                            .append(", ").append(expected).append(')');
                    hasCondition = true;
                }
                if (!hasCondition) source.append("true");
                source.append(") {");
                String counter = localCounterName == null ? countFieldName : "counts";
                if (rule.values != null && rule.values.size() > 1) {
                    source.append("int index = ").append(counter).append('[')
                            .append(ruleIndex).append("]++;");
                } else {
                    source.append(counter).append('[').append(ruleIndex)
                            .append("]++;");
                }
                if (rule.values == null || rule.values.isEmpty()) {
                    source.append("return null;");
                } else {
                    if (rule.values.size() > 1) {
                        for (int i = 0; i < rule.values.size() - 1; i++) {
                            source.append("if (index == ").append(i).append(") return ")
                                    .append(rule.values.get(i)).append(';');
                        }
                    }
                    source.append("return ").append(rule.values.get(rule.values.size() - 1))
                            .append(';');
                }
                source.append('}');
            }
            source.append("return null; }");
            return source.toString();
        }
    }

    private static boolean isAdvancedMockWildcard(Expression expression) {
        return expression.isNameExpr()
                && expression.asNameExpr().getNameAsString().equals("BLOCKTEST_ANYTHING");
    }

    private static List<AdvancedMockRule> matchingAdvancedMockRules(
            MethodCallExpr call, List<AdvancedMockRule> methodRules) {
        List<AdvancedMockRule> matches = new ArrayList<>();
        for (AdvancedMockRule rule : methodRules) {
            Optional<List<Expression>> arguments = advancedMockExpectedArguments(rule.pattern, call);
            arguments.ifPresent(expressions -> matches.add(
                    new AdvancedMockRule(rule.pattern, expressions, rule.values)));
        }
        return matches;
    }

    private static Optional<List<Expression>> advancedMockExpectedArguments(
            MethodCallExpr pattern, MethodCallExpr call) {
        if (!pattern.getNameAsString().equals(call.getNameAsString())) {
            return Optional.empty();
        }

        List<Expression> expected = new ArrayList<>();
        Optional<Expression> patternScope = pattern.getScope();
        Optional<Expression> callScope = call.getScope();
        if (patternScope.isPresent() != callScope.isPresent()) {
            return Optional.empty();
        }
        if (patternScope.isPresent()) {
            Expression left = patternScope.get();
            Expression right = callScope.get();
            if (left.isMethodCallExpr() && right.isMethodCallExpr()) {
                Optional<List<Expression>> scopeArguments = advancedMockExpectedArguments(
                        left.asMethodCallExpr(), right.asMethodCallExpr());
                if (!scopeArguments.isPresent()) return Optional.empty();
                expected.addAll(scopeArguments.get());
            } else if (left.isMethodCallExpr() || right.isMethodCallExpr()
                    || !left.toString().equals(right.toString())) {
                return Optional.empty();
            }
        }

        if (pattern.getArguments().size() == 1
                && isAdvancedMockWildcard(pattern.getArgument(0))) {
            for (int i = 0; i < call.getArguments().size(); i++) expected.add(null);
        } else {
            if (pattern.getArguments().size() != call.getArguments().size()) {
                return Optional.empty();
            }
            for (Expression argument : pattern.getArguments()) {
                expected.add(isAdvancedMockWildcard(argument) ? null : argument);
            }
        }
        System.out.println("expected is " + expected);
        return Optional.of(expected);
    }

    private static String mockCallShape(MethodCallExpr call) {
        String scope = call.getScope().map(expression -> expression.isMethodCallExpr()
                ? mockCallShape(expression.asMethodCallExpr()) : expression.toString()).orElse("");
        return scope + "." + call.getNameAsString() + "#" + call.getArguments().size();
    }

    private static List<Expression> mockCallArguments(MethodCallExpr call) {
        List<Expression> arguments = new ArrayList<>();
        call.getScope().filter(Expression::isMethodCallExpr).ifPresent(
                scope -> arguments.addAll(mockCallArguments(scope.asMethodCallExpr())));
        arguments.addAll(call.getArguments());
        return arguments;
    }

    private static boolean isWithin(Node node, Node root) {
        for (Node current = node; current != null; current = current.getParentNode().orElse(null)) {
            if (current == root) return true;
        }
        return false;
    }

    private static String advancedMockReturnType(MethodCallExpr call, List<AdvancedMockRule> rules) {
        try {
            String resolved = call.calculateResolvedType().describe();
            if (!resolved.equals("void")) return boxedMockType(resolved);
        } catch (RuntimeException ignored) {}
        if (call.getParentNode().filter(VariableDeclarator.class::isInstance).isPresent()) {
            return boxedMockType(((VariableDeclarator) call.getParentNode().get()).getType().toString());
        }
        for (AdvancedMockRule rule : rules) {
            if (rule.values == null) continue;
            for (Expression value : rule.values) {
                if (value.isStringLiteralExpr()) return "java.lang.String";
                if (value.isIntegerLiteralExpr()) return "java.lang.Integer";
                if (value.isLongLiteralExpr()) return "java.lang.Long";
                if (value.isDoubleLiteralExpr()) return "java.lang.Double";
                if (value.isBooleanLiteralExpr()) return "java.lang.Boolean";
                if (value.isCharLiteralExpr()) return "java.lang.Character";
                if (value.isCastExpr()) return boxedMockType(value.asCastExpr().getType().toString());
                if (value.isObjectCreationExpr()) return value.asObjectCreationExpr().getType().toString();
            }
        }
        return "java.lang.Object";
    }

    private static String boxedMockType(String type) {
        switch (type) {
            case "boolean": return "java.lang.Boolean";
            case "byte": return "java.lang.Byte";
            case "short": return "java.lang.Short";
            case "char": return "java.lang.Character";
            case "int": return "java.lang.Integer";
            case "long": return "java.lang.Long";
            case "float": return "java.lang.Float";
            case "double": return "java.lang.Double";
            default: return type;
        }
    }

}
