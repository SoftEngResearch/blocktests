package org.blocktest.visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.nodeTypes.NodeWithStatements;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import javassist.compiler.ast.Variable;
import org.blocktest.BlockTest;
import org.blocktest.TestExtraction;
import org.blocktest.types.EndAt;
import org.blocktest.types.Flow;
import org.blocktest.types.GivenVariable;
import org.blocktest.utils.Constant;
import org.blocktest.utils.TypeResolver;
import org.blocktest.utils.Util;

import static org.blocktest.types.EndAt.*;

public class ExtractionVisitor extends VoidVisitorAdapter<ExtractionVisitor.Context> {

    public HashMap<String, HashSet<Type>> globalSymbolTable = new HashMap<>();
    private List<BlockTest> blockTests;
    private Set<String> blockTestNames;

    public static class Context implements Cloneable {
        public HashMap<String, Type> symbolTable = new HashMap<>();
        public HashMap<String, Expression> assignments = new HashMap<>();

        @Override
        public Context clone() {
            try {
                Context cloned = (Context) super.clone();
                cloned.symbolTable = new HashMap<>(symbolTable);
                cloned.assignments = new HashMap<>(assignments);
                return cloned;
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ExtractionVisitor(List<BlockTest> blockTests) {
        this.blockTests = blockTests;
        this.blockTestNames = new HashSet<>();
    }

    @Override
    public void visit(ExpressionStmt expressionStmt, Context ctx) {
        System.out.println("Visit ExpressionStmt: (" + expressionStmt + ")");
        super.visit(expressionStmt, ctx);

        if (Util.isTestStatement(expressionStmt)) {
            BlockTest blockTest = new BlockTest();
            parseBlockTest(expressionStmt, blockTest, ctx.symbolTable);

            // Find target statements
            findTargetStatements(expressionStmt, blockTest, ctx.symbolTable);
            // Substitute with mocked values
            blockTest.statements = Util.substituteMockedValues(blockTest.statements, blockTest.mockMethods);

            blockTests.add(blockTest);
            blockTest.localSymbolTable = new HashMap<>(ctx.symbolTable);
            blockTest.localAssignments = new HashMap<>(ctx.assignments);
            
            Optional<ClassOrInterfaceDeclaration> classDecl = 
                expressionStmt.findAncestor(ClassOrInterfaceDeclaration.class);
            if (classDecl.isPresent()) {
                String className = classDecl.get().getNameAsString();
                blockTest.className = className;
            }
        }
    }

    private void parseBlockTest(Node node, BlockTest blockTest, HashMap<String, Type> symbolTable) {
        if (node instanceof ExpressionStmt) {
            parseBlockTest(((ExpressionStmt) node).getExpression(), blockTest, symbolTable);
            return;
        } else if (!(node instanceof MethodCallExpr)) {
            return;
        }

        MethodCallExpr methodCall = (MethodCallExpr) node;
        if (methodCall.getName().asString().equals(Constant.DECLARE_NAME) || methodCall.getName().asString().equals(Constant.DECLARE_NAME_LAMBDA)) {
            if (methodCall.getArguments().size() >= 3) {
                throw new RuntimeException(methodCall.getName().asString() + " should have arguments less than 3");
            }

            if (methodCall.getArguments().size() == 1) {
                Expression arg = methodCall.getArguments().get(0);
                if (arg instanceof StringLiteralExpr) {
                    StringLiteralExpr stringLiteralExpr = (StringLiteralExpr) arg;
                    blockTest.testName = stringLiteralExpr.getValue();
                    if (blockTestNames.contains(blockTest.testName)) {
                        throw new RuntimeException(methodCall.getName().asString() + " should not have duplicate test name: " + blockTest.testName);
                    }
                    blockTestNames.add(blockTest.testName);
                } else {
                    throw new RuntimeException(
                            methodCall.getName().asString() + " should not have" + arg.getClass().getName() + " as argument");
                }
            }

//            if (methodCall.getName().asString().equals(Constant.DECLARE_NAME_LAMBDA)) {
//                blockTest.endAt = LAMBDA;
//            }

            blockTest.lineNo = methodCall.getBegin().get().line;
            return;
        } else if (methodCall.getName().asString().equals(Constant.END) ||
                methodCall.getName().asString().equals(Constant.START)) {
            if (methodCall.getArguments().isEmpty()) {
                throw new RuntimeException("Test statement should not contain end() or start() method call without arg: " + methodCall);
            }

            if (methodCall.getName().asString().equals(Constant.START)) {
                blockTest.endReversed = true;
            }

            String arg = methodCall.getArguments().get(0).toString();
            blockTest.endAt = EndAt.valueOf(arg.toUpperCase());
            if (methodCall.getArguments().size() >= 2) {
                String arg2 = methodCall.getArguments().get(1).toString();
                if (arg2.equals("true") || arg2.equals("false")) {
                    blockTest.endAfter = Boolean.parseBoolean(arg2);
                } else {
                    blockTest.endAtInstances = Integer.parseInt(arg2);
                }

                if (methodCall.getArguments().size() >= 3) {
                    String arg3 = methodCall.getArguments().get(2).toString();
                    blockTest.endAfter = Boolean.parseBoolean(arg3);
                }
            }
        } else if (methodCall.getName().asString().equals(Constant.CHECK_FLOW)) {
            List<Expression> args = methodCall.getArguments();
            if (args.isEmpty()) {
                throw new RuntimeException(Constant.CHECK_FLOW + " should have more than 0 arguments");
            }

            if (args.size() > 1 || !args.get(0).toString().contains("()")) {
                for (Expression expr : args) {
                    String flowStr = expr.toString();
                    try {
                        blockTest.flows.add(Flow.valueOf(flowStr));
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException("Invalid flow type: " + flowStr);
                    }
                }
            } else {
                // Use IfStmt().Then() format
                for (String flowStr : args.get(0).toString().replace("()", "").split("\\.")) {
                    try {
                        blockTest.flows.add(Flow.valueOf(flowStr));
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException("Invalid flow type: " + flowStr);
                    }
                }
                System.out.println("Parsed flow: " + blockTest.flows);
            }
        } else if (methodCall.getName().asString().equals(Constant.CHECK_EQ) ||
                methodCall.getName().asString().equals(Constant.CHECK_RETURN_EQ)) {
            List<Expression> args = methodCall.getArguments();
            if (methodCall.getName().asString().equals(Constant.CHECK_EQ) && args.size() < 2) {
                throw new RuntimeException(Constant.CHECK_EQ + " should have 2+ arguments");
            } else if (methodCall.getName().asString().equals(Constant.CHECK_RETURN_EQ) && args.isEmpty()) {
                throw new RuntimeException(Constant.CHECK_RETURN_EQ + " should have 1+ arguments");
            }

            Expression left;
            Expression right;
            Expression delta = null;

            if (methodCall.getName().asString().equals(Constant.CHECK_EQ)) {
                left = args.get(0); // actual value
                right = args.get(1); // expected value
                if (args.size() == 3) {
                    delta = args.get(2); // delta for floating point comparison
                }
            } else {
                left = new NameExpr(Constant.METHOD_RETURN);
                right = args.get(0); // expected value
                if (args.size() == 2) {
                    delta = args.get(1); // delta for floating point comparison
                }
            }

            if (left instanceof StringLiteralExpr) {
                left = new NameExpr(((StringLiteralExpr) left).asString());
                blockTest.assertedVariables.add(left.toString());
            } else {
                left.findAll(NameExpr.class).stream()
                        .filter(name -> {
                            try {
                                return name.resolve() != null;
                            } catch (Exception e) {
                                return false;
                            }
                        })
                        .map(NameExpr::getNameAsString)
                        .forEach(name -> blockTest.assertedVariables.add(name));
            }

            MethodCallExpr assertEquals = delta == null ? new MethodCallExpr("assertEquals", right, left) :
                    new MethodCallExpr("assertEquals", right, left, delta);
            System.out.println("ASSERT: " + assertEquals);
            if (left.toString().contains(Constant.LAMBDA_RETURN)) {
                blockTest.assertedVariables.add(Constant.LAMBDA_RETURN);
                blockTest.junitLambdaAssertions.add(assertEquals);
            } else if (left.toString().contains(Constant.METHOD_RETURN)) {
                blockTest.assertedVariables.add(Constant.METHOD_RETURN);
                blockTest.junitLambdaAssertions.add(assertEquals);
            } else {
                blockTest.junitAssertions.add(assertEquals);
            }
        } else if (methodCall.getName().asString().equals(Constant.CHECK_TRUE) ||
                methodCall.getName().asString().equals(Constant.CHECK_RETURN_TRUE)) {
            List<Expression> args = methodCall.getArguments();
            if (methodCall.getName().asString().equals(Constant.CHECK_TRUE) && args.size() != 1) {
                throw new RuntimeException(Constant.CHECK_TRUE + " should have 1 argument");
            }

            Expression booleanValue;
            if (methodCall.getName().asString().equals(Constant.CHECK_TRUE)) {
                booleanValue = args.get(0); // actual value
                booleanValue.findAll(NameExpr.class).stream()
                        .filter(name -> {
                            try {
                                return name.resolve() != null;
                            } catch (Exception e) {
                                return false;
                            }
                        })
                        .map(NameExpr::getNameAsString)
                        .forEach(name -> blockTest.assertedVariables.add(name));
            } else {
                booleanValue = new NameExpr(Constant.METHOD_RETURN);
            }

            MethodCallExpr assertTrue = new MethodCallExpr("assertTrue", booleanValue);
            if (booleanValue.toString().contains(Constant.LAMBDA_RETURN)) {
                blockTest.assertedVariables.add(Constant.LAMBDA_RETURN);
                blockTest.junitLambdaAssertions.add(assertTrue);
            } else if (booleanValue.toString().contains(Constant.METHOD_RETURN)) {
                blockTest.assertedVariables.add(Constant.METHOD_RETURN);
                blockTest.junitLambdaAssertions.add(assertTrue);
            } else {
                blockTest.junitAssertions.add(assertTrue);
            }
        } else if (methodCall.getName().asString().equals(Constant.CHECK_FALSE) ||
                methodCall.getName().asString().equals(Constant.CHECK_RETURN_FALSE)) {
            List<Expression> args = methodCall.getArguments();
            if (methodCall.getName().asString().equals(Constant.CHECK_FALSE) && args.size() != 1) {
                throw new RuntimeException(Constant.CHECK_FALSE + " should have 1 argument");
            }

            Expression booleanValue;
            if (methodCall.getName().asString().equals(Constant.CHECK_FALSE)) {
                booleanValue = args.get(0); // actual value
                booleanValue.findAll(NameExpr.class).stream()
                        .filter(name -> {
                            try {
                                return name.resolve() != null;
                            } catch (Exception e) {
                                return false;
                            }
                        })
                        .map(NameExpr::getNameAsString)
                        .forEach(name -> blockTest.assertedVariables.add(name));
            } else {
                booleanValue = new NameExpr(Constant.METHOD_RETURN);
            }

            MethodCallExpr assertFalse = new MethodCallExpr("assertFalse", booleanValue);
            if (booleanValue.toString().contains(Constant.LAMBDA_RETURN)) {
                blockTest.assertedVariables.add(Constant.LAMBDA_RETURN);
                blockTest.junitLambdaAssertions.add(assertFalse);
            } else if (booleanValue.toString().contains(Constant.METHOD_RETURN)) {
                blockTest.assertedVariables.add(Constant.METHOD_RETURN);
                blockTest.junitLambdaAssertions.add(assertFalse);
            } else {
                blockTest.junitAssertions.add(assertFalse);
            }
        } else if (methodCall.getName().asString().equals(Constant.CHECK_EXP)) {
            List<Expression> args = methodCall.getArguments();
            if (args.size() != 1) {
                throw new RuntimeException(Constant.CHECK_FALSE + " should have 1 argument");
            }

            Expression exception = args.get(0); // actual value
            if (!exception.isNullLiteralExpr()) {
                blockTest.exception = exception;
                System.out.println("The expected exception is " + exception);
            } else {
                System.out.println("The expected exception is no exception");
            }
        } else if (methodCall.getName().asString().equals(Constant.GIVEN)) {
            // given(variable, value);
            List<Expression> args = methodCall.getArguments();
            if (args.size() != 2 && args.size() != 3) {
                throw new RuntimeException(Constant.GIVEN + " should have 2 or 3 arguments");
            }

            // given(a, 1) turns into int a = 1;, so it needs to know the type of `a`, which is an int
            Expression left = args.get(0);
            if (left instanceof StringLiteralExpr) {
                left = new NameExpr(((StringLiteralExpr) left).asString());
            }

            if (left.toString().contains(".") || left.toString().contains("[")) {
                // need to perform replacement
                blockTest.renameGivenVariables.add(left.toString());
                left = new NameExpr(Util.renameGiven(left.toString()));
            }

            // Infer the type of the left expression
            Type leftType;
            if (args.size() == 3) {
                leftType = Util.getTypeFromStr(args.get(2).toString().replace("\"", ""));
                symbolTable.put(left.toString(), leftType);
                System.out.println("DEBUG: Type is provided for " + left.toString() + ": " + leftType.toString());
            }

            Expression right = args.get(1);

            leftType = getVariableType(symbolTable, left, right);

            GivenVariable givenVariable = new GivenVariable(leftType, left.toString(), left, right);
            blockTest.givens.add(givenVariable);
            blockTest.givenVariables.add(left.toString());
        } else if (methodCall.getName().asString().equals(Constant.ARGS)) {
            List<Expression> args = methodCall.getArguments();
            if (args.isEmpty()) {
                throw new RuntimeException(Constant.ARGS + " should have more than 0 arguments");
            }

            if (args.size() != blockTest.lambdaParameters.size()) {
                throw new RuntimeException(Constant.ARGS + " should have the same number of arguments as lambda parameters");
            }

            int i = 0;
            for (Expression expr : args) {
                Expression right = expr;

                Expression left = blockTest.lambdaParameters.get(i);
                // Infer the type of the left expression
                Type leftType;
                if (args.size() == 3) {
                    leftType = Util.getTypeFromStr(args.get(2).toString().replace("\"", ""));
                    symbolTable.put(left.toString(), leftType);
                }

                leftType = getVariableType(symbolTable, left, right);

                GivenVariable givenVariable = new GivenVariable(leftType, left.toString(), left, right);
                blockTest.givens.add(givenVariable);
                blockTest.givenVariables.add(left.toString());

                i += 1;
            }
        } else if (methodCall.getName().asString().equals(Constant.ARGS_VAR)) {
            // This method call is generated by LambdaTransformationVisitor
            // which put all the lambda parameters into ARGS_VAR method call
            List<Expression> args = methodCall.getArguments();
            blockTest.lambdaParameters.addAll(args);
        } else if (methodCall.getName().asString().equals(Constant.DELAY)) {
            List<Expression> args = methodCall.getArguments();
            if (args.size() != 1) {
                throw new RuntimeException(Constant.DELAY + " should have 1 argument");
            }

            blockTest.delay = Long.parseLong(args.get(0).toString());
        } else if (methodCall.getName().asString().equals(Constant.SETUP)) {
            List<Expression> args = methodCall.getArguments();
            if (args.size() != 1) {
                throw new RuntimeException(Constant.SETUP + " should have 1 argument");
            }

            if (args.get(0) instanceof StringLiteralExpr) {
                blockTest.setupBlock = StaticJavaParser.parseStatement("{" + args.get(0).asStringLiteralExpr().asString() + "}").asBlockStmt();
            } else if (args.get(0) instanceof LambdaExpr && ((LambdaExpr) args.get(0)).getBody() instanceof BlockStmt) {
                blockTest.setupBlock = (BlockStmt) ((LambdaExpr) args.get(0)).getBody();
            } else {
                throw new RuntimeException(Constant.SETUP + " should have string or lambda expression as argument");
            }
        } else if (methodCall.getName().asString().equals(Constant.NO_INIT)) {
            List<Expression> args = methodCall.getArguments();
            if (args.isEmpty()) {
                throw new RuntimeException(Constant.NO_INIT + " should have 1+ arguments");
            }

            for (Expression left : args) {
                Type leftType = getVariableType(symbolTable, left, null);
                GivenVariable givenVariable = new GivenVariable(leftType, left.toString(), left, null);
                blockTest.givens.add(givenVariable);
                blockTest.givenVariables.add(left.toString());
            }
        } else if (methodCall.getName().asString().equals(Constant.MOCK) ||
                methodCall.getName().asString().equals(Constant.MOCK_STR)) {
            // mock method calls
            List<Expression> args = methodCall.getArguments();
            if (args.isEmpty()) {
                throw new RuntimeException(methodCall.getName() + " should have at least 1 argument");
            }

            String mockMethod;
            if (args.get(0) instanceof StringLiteralExpr) {
                // TestExtraction will replace static fields with ClassName.field, but it cannot replace fields that
                // are used in mock method string, so we need to do it here
                Expression mockMethodCU = StaticJavaParser.parseExpression(args.get(0).asStringLiteralExpr().asString()
                        .replace("(..)", "(BLOCKTEST_ANYTHING)"));
                mockMethodCU.accept(new VoidVisitorAdapter<Void>() {
                    @Override
                    public void visit(NameExpr n, Void arg) {
                        super.visit(n, arg);

                        String name = n.getNameAsString();
                        if (TestExtraction.staticFields.containsKey(name)) {
                            String className = TestExtraction.staticFields.get(name);
                            n.replace(new FieldAccessExpr(new NameExpr(className), name));
                        }
                    }
                }, null);
                // similarly, TestExtraction also uses ThisFieldReplacementVisitor to replace this.field with field,
                mockMethodCU.accept(new ThisFieldReplacementVisitor(), null);

                mockMethod = mockMethodCU.toString();
            } else {
                mockMethod = args.get(0).toString();
            }

            List<Expression> values = new ArrayList<>();
            blockTest.mockMethods.put(mockMethod, values);

            for (int i = 1; i < args.size(); i++) {
                values.add(args.get(i));
            }
        }

        if (methodCall.getScope().isPresent()) {
            parseBlockTest(methodCall.getScope().get(), blockTest, symbolTable);
        }
    }

    private static Type getVariableType(HashMap<String, Type> symbolTable, Expression left, Expression right) {
        Type leftType;
        leftType = symbolTable.getOrDefault(left.toString(), null);
        boolean wrongType = leftType != null && (leftType.toString().equals("java.lang.Class<?>") || leftType.toString().equals("U") || leftType.toString().contains("InferenceVariable_"));
        if (leftType == null || leftType.isUnknownType() || wrongType) {
            // Cannot find the type of lhs using symbolTable
            try {
                if (wrongType) {
                    System.out.println("JavaParser returned wrong type");
                    throw new RuntimeException("Unknown type");
                }
                System.out.println("Using TypeResolver to find the type of " + left);
                String leftTypeStr = TypeResolver.sSymbolResolver.calculateType(left).describe();
                if (leftType != null && (leftTypeStr.equals("java.lang.Class<?>") || leftTypeStr.equals("U") || leftTypeStr.contains("InferenceVariable_"))) {
                    System.out.println("JavaParser returned wrong type");
                    throw new RuntimeException("Unknown type");
                }

                TypeResolver.sSymbolResolver.calculateType(left).describe();
                leftType = Util.getTypeFromStr(leftTypeStr);
                symbolTable.put(left.toString(), leftType);
            } catch (Exception e) {
                if (right != null) {
                    System.out.println("Failed to use TypeResolver to find the type of " + left + ". Guessing type...");

                    leftType = inferElementType(right, true);
                    if (leftType != null) {
                        symbolTable.put(left.toString(), leftType);
                        System.out.println("Predicted type is " + leftType);
                    } else {
                        System.out.println("Failed to predcit type from RHS");
                    }
                }

                if (leftType == null || leftType.isUnknownType()) {
                    throw new RuntimeException(
                            "left expression in " + Constant.GIVEN + " should be a variable: " + left + " " + e);
                }
            }
        }
        if (leftType != null && leftType.isWildcardType()) {
            // change ? super java.lang.String to java.lang.String
            if (leftType.asWildcardType().getSuperType().isPresent()) {
                leftType = leftType.asWildcardType().getSuperType().get();
            } else if (leftType.asWildcardType().getExtendedType().isPresent()) {
                leftType = leftType.asWildcardType().getExtendedType().get();
            } else {
                throw new RuntimeException(
                        "left expression in " + Constant.GIVEN + " should be a variable: " + left);
            }
        }
        return leftType;
    }

    private static Type inferElementType(Expression right, boolean primitive) {
        if (right.isObjectCreationExpr()) {
            ObjectCreationExpr oce = right.asObjectCreationExpr();

            if (oce.getTypeAsString().contains("<>")) {
                // new X<>;
                Expression firstArg = oce.getArgument(0);
                if (firstArg.isMethodCallExpr()) {
                    MethodCallExpr mce = firstArg.asMethodCallExpr();
                    boolean isArraysAsList =
                            (mce.getScope().isPresent()
                                    && mce.getScope().get().isNameExpr()
                                    && (mce.getScope().get().asNameExpr().getNameAsString().equals("Arrays") || mce.getScope().get().asNameExpr().getNameAsString().equals("java.util.Arrays"))
                                    && mce.getNameAsString().equals("asList")) || (mce.getScope().isPresent() && mce.getScope().get().toString().equals("java.util.Arrays") && mce.getNameAsString().equals("asList"));
                    if (isArraysAsList && !mce.getArguments().isEmpty()) {
                        Type elementType = inferElementType(mce.getArgument(0), false);
                        if (elementType == null) return null;
                        String containerType = oce.getType().toString(); // ArrayList, HashSet, LinkedList
                        if (containerType.endsWith("<>")) {
                            containerType = containerType.substring(0, containerType.length() - 2);
                        }
                        return Util.getTypeFromStr(containerType + "<" + elementType + ">");
                    }
                }
            }

            return oce.getType();
        } else if (right.isStringLiteralExpr()) {
            return Util.getTypeFromStr("java.lang.String");
        } else if (right.isIntegerLiteralExpr()) {
            if (right.asIntegerLiteralExpr().getValue().endsWith("L") || right.asIntegerLiteralExpr().getValue().endsWith("l")) {
                return primitive ? Util.getTypeFromStr("long") : Util.getTypeFromStr("java.lang.Long");
            } else {
                return primitive ? Util.getTypeFromStr("int") : Util.getTypeFromStr("java.lang.Integer");
            }
        } else if (right.isDoubleLiteralExpr()) {
            if (right.asDoubleLiteralExpr().getValue().endsWith("f") || right.asDoubleLiteralExpr().getValue().endsWith("F")) {
                return primitive ? Util.getTypeFromStr("float") : Util.getTypeFromStr("java.lang.Float");
            } else {
                return primitive ? Util.getTypeFromStr("double") : Util.getTypeFromStr("java.lang.Double");
            }
        }  else if (right.isBooleanLiteralExpr()) {
            return primitive ? Util.getTypeFromStr("boolean") : Util.getTypeFromStr("java.lang.Boolean");
        }  else if (right.isCharLiteralExpr()) {
            return primitive ? Util.getTypeFromStr("char") : Util.getTypeFromStr("java.lang.Character");
        }  else if (right.isLongLiteralExpr()) {
            return primitive ? Util.getTypeFromStr("long") : Util.getTypeFromStr("java.lang.Long");
        } else if (right.isArrayCreationExpr()) {
            ArrayCreationExpr ace = right.asArrayCreationExpr();
            Type elementType = ace.getElementType();
            return Util.getTypeFromStr(elementType.toString() + "[]");
        } else if (right.isClassExpr()) {
            return Util.getTypeFromStr("java.lang.Class");
        } else if (right.isCastExpr()) {
            CastExpr cast = right.asCastExpr();
            return Util.getTypeFromStr(cast.getType().toString());
        }

        if (right.isMethodCallExpr()) {
            MethodCallExpr mce = right.asMethodCallExpr();
            if (mce.getNameAsString().equals("getAnnotation")
                    && mce.getArguments().size() == 1
                    && mce.getArgument(0).isClassExpr()) {
                return Util.getTypeFromStr("java.lang.annotation.Annotation");
            }
        }

        if (predictBuilderType(right).isPresent()) {
            return Util.getTypeFromStr(predictBuilderType(right).get());
        }

        if (inferPathsGet(right).isPresent()) {
            return Util.getTypeFromStr(inferPathsGet(right).get());
        }

        if (inferEnumType(right).isPresent()) {
            return Util.getTypeFromStr(inferEnumType(right).get());
        }
        return null;
    }

    private static Optional<String> predictBuilderType(Expression expr) {
        // Predict builder type
        // TypedData.newBuilder().setString("foo").build() -> TypedData
        if (!expr.isMethodCallExpr()) return Optional.empty();

        MethodCallExpr mce = expr.asMethodCallExpr();
        if (!mce.getNameAsString().equals("build")) return Optional.empty();

        MethodCallExpr current = mce;

        while (current.getScope().isPresent()
                && current.getScope().get().isMethodCallExpr()) {

            current = current.getScope().get().asMethodCallExpr();

            if (current.getNameAsString().toLowerCase().contains("build")) {
                if (current.getScope().isPresent()
                        && current.getScope().get().isNameExpr()) {

                    return Optional.of(
                            current.getScope().get().asNameExpr().getNameAsString()
                    );
                }
            }
        }
        return Optional.empty();
    }

    private static Optional<String> inferPathsGet(Expression expr) {
        if (!expr.isMethodCallExpr()) return Optional.empty();

        MethodCallExpr mce = expr.asMethodCallExpr();
        if (!mce.getNameAsString().equals("get")) return Optional.empty();
        if (!mce.getScope().isPresent()) return Optional.empty();

        Expression scope = mce.getScope().get();

        // Paths.get(...)
        if (scope.isNameExpr()
                && scope.asNameExpr().getNameAsString().equals("Paths")) {
            return Optional.of("Path");
        }

        // java.nio.file.Paths.get(...)
        if (scope.isFieldAccessExpr()) {
            String scopeStr = scope.toString();
            if (scopeStr.endsWith(".Paths")) {
                return Optional.of("Path");
            }
        }

        return Optional.empty();
    }

    private static Optional<String> inferEnumType(Expression expr) {
        if (!expr.isFieldAccessExpr()) return Optional.empty();

        FieldAccessExpr fae = expr.asFieldAccessExpr();

        Expression scope = fae.getScope(); // scope of the constant

        // If nested (TermsAggregation.Order.COUNT_ASC)
        if (scope.isFieldAccessExpr()) {
            FieldAccessExpr inner = scope.asFieldAccessExpr();
            // return the fully qualified enum type: TermsAggregation.Order
            return Optional.of(inner.toString());
        }

        // Simple case: Enum.CONSTANT
        if (scope.isNameExpr()) {
            return Optional.of(scope.asNameExpr().getNameAsString());
        }

        return Optional.empty();
    }

    private void findTargetStatements(Node expressionStmt, BlockTest blockTest, HashMap<String, Type> symbolTable) {
        if (expressionStmt.getParentNode().isPresent()) {
            Node parentNode = expressionStmt.getParentNode().get();
            while (!(parentNode instanceof BlockStmt) && !(parentNode instanceof SwitchEntry)) {
                // Find parent block
                parentNode = parentNode.getParentNode().get();
            }

            // Renamed variable based on given's renameGivenVariables
            Util.replaceExpressionsWithVariables(parentNode, blockTest.renameGivenVariables);

            // Searching target statement
            NodeWithStatements blockStmt = (NodeWithStatements) parentNode;

            if (!blockTest.endReversed) {
                // regular order, using end()
                if (blockTest.endAt == EndAt.LAST_REFERENCE) {
                    // If endAt is LAST_REFERENCE, we need to find the last reference to the asserted variable
                    int currentIdx = blockStmt.getStatements().size() - 1; // Starting from end-of-block
                    int endIndex = blockStmt.getStatements().indexOf(expressionStmt) + 1;
                    // end of block to test statement
                    targetBlockSearchingLast(blockStmt, blockTest, currentIdx, endIndex, blockTest.endReversed);
                } else {
                    int endIndex = blockStmt.getStatements().size() - 1;
                    int currentIdx = blockStmt.getStatements().indexOf(expressionStmt) + 1;
                    // test statement to end of block
                    targetBlockSearchingFirst(blockStmt, blockTest, currentIdx, endIndex, blockTest.endReversed);
                }
            } else {
                // reversed order, using start()
                if (blockTest.endAt == EndAt.LAST_REFERENCE) {
                    // If endAt is LAST_REFERENCE, we need to find the first reference to the asserted variable
                    int currentIdx = 0; // Starting from front-of-block
                    int endIndex = blockStmt.getStatements().indexOf(expressionStmt) - 1;
                    // front of block to test statement
                    targetBlockSearchingLast(blockStmt, blockTest, currentIdx, endIndex, blockTest.endReversed);

                    // Need to reverse the statements order, because we copy it from test statement to front
                    BlockStmt tmp = new BlockStmt();
                    NodeList<Statement> stmts = blockTest.statements.getStatements();
                    for (int i = stmts.size() - 1; i >= 0; i--) {
                        tmp.addStatement(stmts.get(i));
                    }
                    blockTest.statements = tmp;
                } else {
                    int endIndex = 0;
                    int currentIdx = blockStmt.getStatements().indexOf(expressionStmt) - 1;
                    // test statement to front of block
                    targetBlockSearchingFirst(blockStmt, blockTest, currentIdx, endIndex, blockTest.endReversed);

                    // Need to reverse the statements order, because we copy it from test statement to front
                    BlockStmt tmp = new BlockStmt();
                    NodeList<Statement> stmts = blockTest.statements.getStatements();
                    for (int i = stmts.size() - 1; i >= 0; i--) {
                        tmp.addStatement(stmts.get(i));
                    }
                    blockTest.statements = tmp;
                }
            }
        } else {
            System.out.println("PARENT NODE IS MISSING!!!!!");
        }
    }

    private void targetBlockSearchingFirst(NodeWithStatements blockStmt, BlockTest blockTest, int currentIdx, int endIndex, boolean endReversed) {
        System.out.println("targetBlockSearchingLast from " + currentIdx + " to " + endIndex);
        Set<String> assertedVariables = new HashSet<>(blockTest.assertedVariables);
        int matched = 0;
        while (!endReversed ? (currentIdx <= endIndex) : (currentIdx >= endIndex)) {
            Node node = blockStmt.getStatements().get(currentIdx);

            if (blockTest.endAt == FIRST_ASSIGN) {
                Set<String> assignedVars = node.stream()
                        .flatMap(n -> {
                            if (n instanceof AssignExpr) {
                                Expression target = ((AssignExpr) n).getTarget();
                                if (target instanceof NameExpr) {
                                    return Stream.of(((NameExpr) target).getNameAsString());
                                }
                            } else if (n instanceof UnaryExpr) {
                                Expression expr = ((UnaryExpr) n).getExpression();
                                if (expr instanceof NameExpr) {
                                    return Stream.of(((NameExpr) expr).getNameAsString());
                                }
                            }
                            return Stream.empty();
                        })
                        .collect(Collectors.toSet());
                System.out.println("Assigned variables in " + currentIdx + ": " + assignedVars);
                assertedVariables.removeAll(assignedVars);

                addStatementToTargetBlock(blockTest, blockStmt, currentIdx);

                if (assertedVariables.isEmpty()) {
                    System.out.println("All asserted variables are assigned, stopping search at " + currentIdx);
                    break;
                }
            } else if (blockTest.endAt == FIRST_BLOCK) {
                if (!blockTest.endAfter && node.findFirst(BlockStmt.class).isPresent() &&
                        matched + 1 >= blockTest.endAtInstances) {
                    // endBefore
                    System.out.println("First " + matched + " block statement(s) is found, stopping search before " + currentIdx);
                    break;
                }

                addStatementToTargetBlock(blockTest, blockStmt, currentIdx);

                if (node.findFirst(BlockStmt.class).isPresent()) {
                    matched += 1;
                    if (matched >= blockTest.endAtInstances) {
                        System.out.println("First " + matched + " block statement(s) is found, stopping search at " + currentIdx);
                        break;
                    }
                }
            } else if (blockTest.endAt == FIRST_RETURN) {
                if (!blockTest.endAfter && node.findFirst(ReturnStmt.class).isPresent() &&
                        matched + 1 >= blockTest.endAtInstances) {
                    // endBefore
                    System.out.println("First " + matched + " return statement(s) is found, stopping search before " + currentIdx);
                    break;
                }

                addStatementToTargetBlock(blockTest, blockStmt, currentIdx);

                if (node.findFirst(ReturnStmt.class).isPresent()) {
                    matched += 1;
                    if (matched >= blockTest.endAtInstances) {
                        System.out.println("First " + matched + " return statement(s) is found, stopping search at " + currentIdx);
                        break;
                    }
                }
            } else if (blockTest.endAt == FIRST_THROW) {
                if (!blockTest.endAfter && node.findFirst(ThrowStmt.class).isPresent() &&
                        matched + 1 >= blockTest.endAtInstances) {
                    // endBefore
                    System.out.println("First " + matched + " throw statement(s) is found, stopping search before " + currentIdx);
                    break;
                }

                addStatementToTargetBlock(blockTest, blockStmt, currentIdx);

                if (node.findFirst(ThrowStmt.class).isPresent()) {
                    matched += 1;
                    if (matched >= blockTest.endAtInstances) {
                        System.out.println("First " + matched + " throw statement(s) is found, stopping search at " + currentIdx);
                        break;
                    }
                }
            } else if (blockTest.endAt == FIRST_STATEMENT) {
                if (!blockTest.endAfter &&
                        matched + 1 >= blockTest.endAtInstances) {
                    // endBefore
                    System.out.println("First " + matched + " statement(s) is found, stopping search before " + currentIdx);
                    break;
                }

                addStatementToTargetBlock(blockTest, blockStmt, currentIdx);

                matched += 1;
                if (matched >= blockTest.endAtInstances) {
                    System.out.println("First " + matched + " statement(s) is found, stopping search at " + currentIdx);
                    break;
                }
            }

            if (!endReversed)
                currentIdx += 1;
            else
                currentIdx -= 1;
        }
    }

    private void targetBlockSearchingLast(NodeWithStatements blockStmt, BlockTest blockTest, int currentIdx, int endIndex, boolean endReversed) {
        System.out.println("targetBlockSearchingLast from " + currentIdx + " to " + endIndex);
        int foundLastRef = -1; // If foundLastRef is > 0, it means we have found last reference to var
        int foundEnd = -1; // If foundEnd is > 0, it means we have found blocktest().end()
        // If foundLastRef is > 0, we still need to check to make sure there is no blocktest().end()
        // If there is one, we use targetBlockBasedOnEnd, otherwise we use targetBlockBasedOnLastRef

        while (!endReversed ? (currentIdx >= endIndex) : (currentIdx <= endIndex)) {
            // Until we reach the end of the block OR until we see blocktest().end()
            Node node = blockStmt.getStatements().get(currentIdx);

            System.out.println("Checking node " + node + " for " + blockTest.assertedVariables);

            if (foundLastRef == -1 && !(node instanceof ReturnStmt) && !Util.isBlockTest(node)) {
                // Check if this node is a reference to an asserted variable, and statement is not return
                long matchAssertedVar = node.findAll(NameExpr.class).stream()
                        .map(NameExpr::getNameAsString)
                        .filter(name -> blockTest.assertedVariables.contains(name)).count();

                if (matchAssertedVar > 0) {
                    // This statement used asserted variable, so it is part of the target block
                    foundLastRef = currentIdx;
                    System.out.println("Found last reference to asserted variable in " + blockTest.testName
                            + " at location " + foundLastRef + ", which is: " + node);
                }
            }

            String isEndStatement = Util.isEndStatement(node);
            System.out.println("isENdStatement for " + isEndStatement);
            if (isEndStatement != null && isEndStatement.equals(blockTest.testName)) {
                // Check if node is blocktest().end()
                foundEnd = currentIdx;
                System.out.println("Found blocktest().end() for " + blockTest.testName + " at line " + node);
                break;
            }

            if (!endReversed)
                currentIdx -= 1;
            else
                currentIdx += 1;
        }

        if (foundEnd > 0) {
            if (!endReversed) {
                // There is a blocktest().end() in the block
                // put endIndex ... (foundEnd - 1) to targetBlockBasedOnEnd
                for (int i = endIndex; i < foundEnd; i++) {
                    if (addStatementToTargetBlock(blockTest, blockStmt, i)) break;
                }
                System.out.println("Found blocktest().end() for " + blockTest.testName
                        + ", using range " + endIndex + "..." + (foundEnd - 1));
            } else {
                for (int i = endIndex; i > foundEnd; i--) {
                    if (addStatementToTargetBlock(blockTest, blockStmt, i)) break;
                }
            }
        } else if (foundLastRef > 0) {
            if (!endReversed) {
                // put endIndex ... foundLastRef to targetBlockBasedOnEnd
                for (int i = endIndex; i <= foundLastRef; i++) {
                    if (addStatementToTargetBlock(blockTest, blockStmt, i)) break;
                }
                System.out.println("No blocktest().end() for " + blockTest.testName
                        + ", using range " + endIndex + "..." + foundLastRef);
            } else {
                for (int i = endIndex; i >= foundLastRef; i--) {
                    if (addStatementToTargetBlock(blockTest, blockStmt, i)) break;
                }
            }
        } else {
            if (!blockTest.assertedVariables.isEmpty() && blockTest.junitLambdaAssertions.isEmpty()) {
                // If there is no asserted variables (i.e., flow assertion), and it is not lambda (which test return)
                throw new RuntimeException("No blocktest().end() and no last reference for " + blockTest.testName);
            } else {
                if (!endReversed) {
                    foundLastRef = blockStmt.getStatements().size() - 1; // use the entire block
                    for (int i = endIndex; i <= foundLastRef; i++) {
                        if (addStatementToTargetBlock(blockTest, blockStmt, i)) break;
                    }
                    System.out.println("No blocktest().end() for " + blockTest.testName
                            + ", using range " + endIndex + "..." + foundLastRef);
                } else {
                    foundLastRef = 0; // use the entire block
                    for (int i = endIndex; i >= foundLastRef; i--) {
                        if (addStatementToTargetBlock(blockTest, blockStmt, i)) break;
                    }
                }
            }
        }
    }

    private boolean addStatementToTargetBlock(BlockTest blockTest, NodeWithStatements blockStmt, int i) {
        Node node = blockStmt.getStatements().get(i);
        if (Util.isEndStatement(node) == null && !Util.isTestStatement(node)) {
            // not blocktest().end() and not blocktest().given()
            if (!(node instanceof ReturnStmt)) {
                // Get all the variables used in the target block, so we need declare them if necessary
                List<String> usedVariables = node.findAll(NameExpr.class).stream()
                        .filter(name -> {
                            try {
                                return name.resolve() != null;
                            } catch (Exception e) {
                                return false;
                            }
                        })
                        .map(NameExpr::getNameAsString)
                        .distinct()
                        .collect(Collectors.toList());
                blockTest.blockVariables.addAll(usedVariables);
            }

            Node newStatement = node.removeComment().clone();

            blockTest.statements.addStatement((Statement) newStatement);
        }
        return false;
    }


    @Override
    public void visit(BlockStmt blockStmt, Context ctx) {
        Context newCtx = ctx.clone();
        super.visit(blockStmt, newCtx);
    }

    @Override
    public void visit(ForStmt forStmt, Context arg) {
        forStmt.getCompare().ifPresent(l -> l.accept(this, arg));
        forStmt.getInitialization().forEach(p -> p.accept(this, arg));
        forStmt.getUpdate().forEach(p -> p.accept(this, arg));
        forStmt.getComment().ifPresent(l -> l.accept(this, arg));
        forStmt.getBody().accept(this, arg);
    }

    @Override
    public void visit(ForEachStmt forEachStmt, Context ctx) {
        forEachStmt.getIterable().accept(this, ctx);
        forEachStmt.getVariable().accept(this, ctx);
        forEachStmt.getBody().accept(this, ctx);
        forEachStmt.getComment().ifPresent(l -> l.accept(this, ctx));
    }

    @Override
    public void visit(VariableDeclarator variableDeclarator, Context ctx) {
        super.visit(variableDeclarator, ctx);
        String varName = variableDeclarator.getNameAsString();
        Type varType = variableDeclarator.getType();
        ctx.symbolTable.put(varName, varType);
        ctx.assignments.put(varName, variableDeclarator.getInitializer().orElse(null));

        if (globalSymbolTable.containsKey(varName)) {
            globalSymbolTable.get(varName).add(varType);
        } else {
            globalSymbolTable.put(varName, new HashSet<>());
            globalSymbolTable.get(varName).add(varType);
        }
        System.out.println("ADD TO SYMBOL TABLE!!! " + varName + " : " + varType);
    }

    @Override
    public void visit(AssignExpr ae, Context ctx) {
        Expression target = ae.getTarget();
        if (target.isNameExpr()) {
            ctx.assignments.put(target.asNameExpr().getNameAsString(), ae.getValue());
        }
        super.visit(ae, ctx);
    }

    @Override
    public void visit(final ConstructorDeclaration n, final Context arg) {
        n.getModifiers().forEach(p -> p.accept(this, arg));
        n.getName().accept(this, arg);
        n.getParameters().forEach(p -> p.accept(this, arg));
        n.getReceiverParameter().ifPresent(l -> l.accept(this, arg));
        n.getThrownExceptions().forEach(p -> p.accept(this, arg));
        n.getTypeParameters().forEach(p -> p.accept(this, arg));
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
        n.getBody().accept(this, arg);
    }

    @Override
    public void visit(MethodDeclaration methodDeclaration, Context arg) {
        methodDeclaration.getType().accept(this, arg);
        methodDeclaration.getModifiers().forEach(p -> p.accept(this, arg));
        methodDeclaration.getName().accept(this, arg);
        methodDeclaration.getParameters().forEach(p -> p.accept(this, arg));
        methodDeclaration.getReceiverParameter().ifPresent(l -> l.accept(this, arg));
        methodDeclaration.getThrownExceptions().forEach(p -> p.accept(this, arg));
        methodDeclaration.getTypeParameters().forEach(p -> p.accept(this, arg));
        methodDeclaration.getAnnotations().forEach(p -> p.accept(this, arg));
        methodDeclaration.getComment().ifPresent(l -> l.accept(this, arg));
        methodDeclaration.getBody().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final LambdaExpr n, final Context arg) {
        n.getParameters().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
        n.getBody().accept(this, arg);
    }

    @Override
    public void visit(Parameter n, Context ctx) {
        String varName = n.getNameAsString();
        Type varType = n.getType();

        ctx.symbolTable.put(varName, varType);
        if (globalSymbolTable.containsKey(varName)) {
            globalSymbolTable.get(varName).add(varType);
        } else {
            globalSymbolTable.put(varName, new HashSet<>());
            globalSymbolTable.get(varName).add(varType);
        }

        n.getAnnotations().forEach(p -> p.accept(this, ctx));
        n.getModifiers().forEach(p -> p.accept(this, ctx));
        n.getName().accept(this, ctx);
        n.getType().accept(this, ctx);
        n.getVarArgsAnnotations().forEach(p -> p.accept(this, ctx));
        n.getComment().ifPresent(l -> l.accept(this, ctx));
    }
}
