package org.blocktest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.LongLiteralExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import org.blocktest.utils.Constant;
import org.blocktest.utils.TypeResolver;
import org.blocktest.utils.Util;
import org.blocktest.types.GivenParameterizedVariable;
import org.blocktest.visitors.*;

public class TestConverter {
    private HashMap<String, HashSet<Type>> globalSymbolTable;

    public TestConverter(HashMap<String, HashSet<Type>> globalSymbolTable) {
        this.globalSymbolTable = globalSymbolTable;
    }
    /**
     * Converts a BlockTest to a JUnit test method.
     *
     * @param blockTest The BlockTest to convert.
     */
    public MethodDeclaration toJUnit(BlockTest blockTest, boolean coverage) {
        System.out.println(blockTest);
        
        BlockStmt body = new BlockStmt();
        
        if (coverage) {
            body.addStatement(new ExpressionStmt(new MethodCallExpr("src_" + blockTest.testName + "_src")));
        } else {
            BlockStmt givenBody = body;
            BlockStmt targetBlockBody = new BlockStmt();
            BlockStmt assertionBody = new BlockStmt();
            
            // Special handling for parameterized tests with expected-exception
            // This is useful when paramterized tests is used with expected-exception in JUnit5
            if (blockTest.is_parameterized && Util.junitVersion.equals("junit5") && blockTest.exception != null) {
                // Add a new assignment statement for each parameterized given variable for expected-exception tests
                for (GivenParameterizedVariable parameterizedGiven : blockTest.parameterizedGivens) {
                    Type paramType = parameterizedGiven.type.clone();
                    NameExpr paramNewName = new NameExpr(parameterizedGiven.name + "BlockTestParam");
                    VariableDeclarator declarator = new VariableDeclarator(paramType, parameterizedGiven.name, paramNewName);
                    givenBody.addStatement(new VariableDeclarationExpr(declarator));
                }
            }

            // Add given statements
            if (!blockTest.junitLambdaAssertions.isEmpty()) {
                body.addStatement(new ExpressionStmt(new VariableDeclarationExpr(
                        new VariableDeclarator(Util.getTypeFromStr("boolean"), Constant.RETURN_STMT_REACHED, new NameExpr(blockTest.notReturn ? "true" : "false"))
                )));
                MethodCallExpr assertReturned = new MethodCallExpr(new NameExpr("org.blocktest.Assertion"),
                        "assertTrue", new NodeList<>(new NameExpr(Constant.RETURN_STMT_REACHED)));
                blockTest.junitAssertions.add(assertReturned);
            }
            addGivenStatements(givenBody, blockTest);
    
            TryStmt tryStmt = new TryStmt();
            tryStmt.setTryBlock(targetBlockBody);
            tryStmt.setFinallyBlock(assertionBody);
    
    
            addTargetBlock(targetBlockBody, givenBody, assertionBody, blockTest, coverage);
            addAssertion(assertionBody, blockTest);
    
            if (targetBlockBody.findFirst(ReturnStmt.class).isPresent()) {
                // There is a return, so we need try/finally
                body.addStatement(tryStmt);
            } else {
                // No return, we can use the same block
                for (Statement statement : targetBlockBody.getStatements()) {
                    body.addStatement(statement);
                }
    
                for (Statement statement : assertionBody.getStatements()) {
                    body.addStatement(statement);
                }
            }
        }

        MethodDeclaration method = new MethodDeclaration();
        method.setPublic(true);

        if (blockTest.exception == null) { // `exception` flag to handle tests throwing exceptions
            if (Util.junitVersion.equals("junit5")) {
                method.setName(blockTest.testName).setType(new VoidType()).setBody(body);
                applyJUnit5TestAnnotation(method, blockTest);
            } else {
                method.setName(blockTest.testName).setType(new VoidType()).setBody(body);
                method.addMarkerAnnotation("Test");
            }
        } else {
            if (Util.junitVersion.equals("junit4") || Util.junitVersion.equals("testng")) {
                // JUnit 4 style
                MemberValuePair pair = new MemberValuePair(Util.junitVersion.equals("junit4") ? "expected" : "expectedExceptions", (ClassExpr) blockTest.exception);
                NormalAnnotationExpr annotation = new NormalAnnotationExpr();
                annotation.setName("Test");
                annotation.setPairs(new NodeList<>(pair));
                method.setName(blockTest.testName).setType(new VoidType()).setBody(body).addAnnotation(annotation);
            } else {
                // JUnit 5 style
                LambdaExpr lambda = new LambdaExpr();
                lambda.setEnclosingParameters(true);
                lambda.setBody(body);

                MethodCallExpr assertThrowsCall = new MethodCallExpr(
                        null,
                        "assertThrows",
                        NodeList.nodeList((ClassExpr) blockTest.exception, lambda)
                );

                BlockStmt newBody = new BlockStmt().addStatement(assertThrowsCall);
                method.setName(blockTest.testName).setType(new VoidType()).setBody(newBody);
                applyJUnit5TestAnnotation(method, blockTest);
            }
        }
        new JavaParser().parseClassOrInterfaceType("Exception").getResult().ifPresent(method::addThrownException);

        if (blockTest.controlFlow != null && !coverage) {
            TryStmt tryStmt = new TryStmt();
            BlockStmt tmp = new BlockStmt();
            tryStmt.setTryBlock(method.getBody().orElse(new BlockStmt()));

            MethodCallExpr flowAssertCall = new MethodCallExpr(
                    new NameExpr("org.blocktest.Assertion"),
                    "assertEquals",
                    new NodeList<>(new NameExpr(blockTest.controlFlow), new MethodCallExpr(new NameExpr("BLOCKGEN_FLOW_INTERNAL"), "toString"))
            );
            tmp.addStatement(new ExpressionStmt(flowAssertCall));
            tryStmt.setFinallyBlock(tmp);
            BlockStmt tmp2 = new BlockStmt();
            ClassOrInterfaceType sbType = new ClassOrInterfaceType(null, "java.lang.StringBuilder");
            tmp2.addStatement(new ExpressionStmt(
                    new VariableDeclarationExpr(
                            new VariableDeclarator(
                                    sbType,
                                    "BLOCKGEN_FLOW_INTERNAL",
                                    new ObjectCreationExpr(null, sbType.clone(), new NodeList<>())
                            )
                    )
            ));
            tmp2.addStatement(tryStmt);
            method.setBody(tmp2);
        }

        System.out.println("\n===== Converted JUnit Test Method =====");
        System.out.println(method);
        return method;
    }

    public List<BodyDeclaration<?>> toParamJUnit(BlockTest blockTest, boolean coverage) {
        List<BodyDeclaration<?>> declarations = new ArrayList<>();

        if (blockTest.parameterizedGivens == null || blockTest.parameterizedGivens.isEmpty()
                || blockTest.parameterValues == null) {
            throw new RuntimeException("Parameterized block test is missing variables or parameter values");
        }

        // JUnit 5 parameterized tests
        if (Util.junitVersion.equals("junit5")) {
            MethodDeclaration method = new MethodDeclaration();
            method.setPublic(true);
            method.setStatic(true);
            method.setName(generateJUnit5ParameterSourceMethodName(blockTest));
            ClassOrInterfaceType streamType = new ClassOrInterfaceType(null, "Stream");
            streamType.setTypeArguments(new ClassOrInterfaceType(null, "Arguments"));
            method.setType(streamType);

            if (!blockTest.parameterValues.isArrayCreationExpr()
                    || !blockTest.parameterValues.asArrayCreationExpr().getInitializer().isPresent()) {
                throw new RuntimeException("JUnit 5 parameterized block test parameters must be an initialized array");
            }

            NodeList<Expression> rows = new NodeList<>();
            for (Expression row : blockTest.parameterValues.asArrayCreationExpr().getInitializer().get().getValues()) {
                NodeList<Expression> values = new NodeList<>();

                // Check if the row is an initialized array and get the initializer
                ArrayInitializerExpr initializer = null;
                if (row.isArrayInitializerExpr()) {  // For array literals like {1, 2, 3}
                    initializer = row.asArrayInitializerExpr();
                } else if (row.isArrayCreationExpr() && row.asArrayCreationExpr().getInitializer().isPresent()) {  // For array creations like new int[] {1, 2, 3}
                    initializer = row.asArrayCreationExpr().getInitializer().get();
                } else {
                    throw new RuntimeException("JUnit 5 parameterized block test rows must be arrays");
                }

                // Iterate through the initializer values and create a new Object array for each value
                for (Expression value : initializer.getValues()) {
                    if (value.isArrayInitializerExpr()) {  // For array literals like {1, 2, 3}
                        values.add(new ArrayCreationExpr(
                                new ClassOrInterfaceType(null, "Object"),
                                NodeList.nodeList(new ArrayCreationLevel()),
                                value.asArrayInitializerExpr().clone()));
                    } else {  // For non-array literals like Arrays.asList("a", "b", "c")
                        values.add(value.clone());
                    }
                }
                rows.add(new MethodCallExpr(new NameExpr("Arguments"), "of", values));
            }

            Expression returned;
            if (rows.isEmpty()) {
                returned = new MethodCallExpr(new NameExpr("Stream"), "empty");
            } else {
                returned = new MethodCallExpr(new NameExpr("Stream"), "of", rows);
            }

            // Set the body of the method to return the Stream of Arguments and add it to the declarations
            method.setBody(new BlockStmt().addStatement(new ReturnStmt(returned)));
            declarations.add(method);
        }
        
        // JUnit 4 parameterized tests
        else if (Util.junitVersion.equals("junit4")) {
            for (GivenParameterizedVariable parameterizedGiven : blockTest.parameterizedGivens) {
                FieldDeclaration field = new FieldDeclaration();
                field.setPublic(true);
                field.addVariable(new VariableDeclarator(parameterizedGiven.type.clone(), parameterizedGiven.name));

                SingleMemberAnnotationExpr parameterAnnotation = new SingleMemberAnnotationExpr();
                parameterAnnotation.setName("Parameter");
                parameterAnnotation.setMemberValue(new IntegerLiteralExpr(String.valueOf(parameterizedGiven.index)));
                field.addAnnotation(parameterAnnotation);
                declarations.add(field);
            }

            MethodDeclaration parametersMethod = new MethodDeclaration();
            parametersMethod.setPublic(true);
            parametersMethod.setStatic(true);
            parametersMethod.setName("data");

            // Collection<Object[]>
            ClassOrInterfaceType collectionType =
                new ClassOrInterfaceType(null, "Collection");

            ClassOrInterfaceType objectType =
                new ClassOrInterfaceType(null, "Object");

            collectionType.setTypeArguments(
                new ArrayType(objectType)
            );

            parametersMethod.setType(collectionType);

            // @Parameterized.Parameters
            parametersMethod.addAnnotation(
                new MarkerAnnotationExpr("Parameterized.Parameters")
            );

            parametersMethod.setBody(
                new BlockStmt()
                    .addStatement(new ReturnStmt(new MethodCallExpr(
                            new NameExpr("java.util.Arrays"),
                            "asList",
                            NodeList.nodeList(blockTest.parameterValues.clone())
                    )))
            );

            declarations.add(parametersMethod);
        }

        return declarations;
    }

    private void applyJUnit5TestAnnotation(MethodDeclaration method, BlockTest blockTest) {
        // If the test is not parameterized, add the Test annotation
        if (!blockTest.is_parameterized) {
            method.addMarkerAnnotation("Test");
            return;
        }

        // Add ParameterizedTest annotation
        method.addMarkerAnnotation("ParameterizedTest");

        // Add MethodSource annotation
        SingleMemberAnnotationExpr methodSource = new SingleMemberAnnotationExpr();
        methodSource.setName("MethodSource");
        methodSource.setMemberValue(new StringLiteralExpr(generateJUnit5ParameterSourceMethodName(blockTest)));
        method.addAnnotation(methodSource);

        // Add parameters into the test method signature
        for (GivenParameterizedVariable parameterizedGiven : blockTest.parameterizedGivens) {
            Type paramType = parameterizedGiven.type.clone();
            String paramName;
            if (blockTest.exception != null) {
                paramName = parameterizedGiven.name + "BlockTestParam";
            } else {
                paramName = parameterizedGiven.name;
            }
            method.addParameter(new Parameter(paramType, paramName));
        }
    }

    private String generateJUnit5ParameterSourceMethodName(BlockTest blockTest) {
        // Generate the name of the method that for parameter source of JUnit5
        String testName = blockTest.testName == null || blockTest.testName.isEmpty()
                ? "TestLine" + blockTest.lineNo
                : blockTest.testName;
        return "blockTestParamSource" + testName;
    }

    public MethodDeclaration toSourceCode(BlockTest blockTest) {
        System.out.println(blockTest);
        BlockStmt body = new BlockStmt();
        BlockStmt givenBody = body;
        BlockStmt targetBlockBody = new BlockStmt();
        BlockStmt assertionBody = new BlockStmt();
        // Add given statements
        addGivenStatements(givenBody, blockTest);

        System.out.println(body);

        TryStmt tryStmt = new TryStmt();
        tryStmt.setTryBlock(targetBlockBody);
        tryStmt.setFinallyBlock(new BlockStmt());


        addTargetBlock(targetBlockBody, givenBody, assertionBody, blockTest, true);
        addAssertion(assertionBody, blockTest);

        if (targetBlockBody.findFirst(ReturnStmt.class).isPresent()) {
            // There is a return, so we need try/finally
            body.addStatement(tryStmt);
        } else {
            // No return, we can use the same block
            for (Statement statement : targetBlockBody.getStatements()) {
                body.addStatement(statement);
            }
        }

        MethodDeclaration method = new MethodDeclaration();
        method.setPublic(true);
        method.setName("src_" + blockTest.testName + "_src").setType(new VoidType()).setBody(body);
        new JavaParser().parseClassOrInterfaceType("Exception").getResult().ifPresent(method::addThrownException);

        System.out.println("\n===== Converted JUnit Test Method (toSourceCode) =====");
        System.out.println(method);
        
        method.setStatic(true);
        return method;
    }

    private void addGivenStatements(BlockStmt body, BlockTest blockTest) {
        for (int i = blockTest.givens.size() - 1; i >= 0; i--) {
            Expression expression = blockTest.givens.get(i).toAssignExpr().clone();
            body.addStatement(expression);
        }

        // Declare variables in statements that are not in given
        System.out.println("\n===== Declare variables in statements that are not in given =====");
        VariablesCollectionVisitor collector = new VariablesCollectionVisitor();
        blockTest.statements.accept(collector, null);

        // Need to be declared
        // (1): all the variables referenced in target block that are not in given
        // (2): all the variables that are assigned in target block
        // (3): then remove all the variables that are declared in target block
        HashSet<String> inBothLocalSymbolTableAndBlock = new HashSet<>(blockTest.localSymbolTable.keySet());
        System.out.println("localSymbolTable: " + blockTest.localSymbolTable.keySet());
        inBothLocalSymbolTableAndBlock.retainAll(blockTest.blockVariables);
        System.out.println("blockTest.blockVariables: " + blockTest.blockVariables);
        inBothLocalSymbolTableAndBlock.removeAll(blockTest.givenVariables);
        System.out.println("blockTest.givenVariables: " + blockTest.givenVariables);
        System.out.println("inBothLocalSymbolTableAndBlock: " + inBothLocalSymbolTableAndBlock);

        System.out.println("before collector.assignedVariables: " + collector.assignedVariables);
        collector.assignedVariables.addAll(inBothLocalSymbolTableAndBlock);
        collector.assignedVariables.removeAll(collector.declaredVariables);
        System.out.println("collector.declaredVariables: " + collector.declaredVariables);
        collector.assignedVariables.removeAll(blockTest.givenVariables); // inBothLocalSymbolTableAndBlock's removeAll will not remove given if the variable is already in collector.assignedVariables due to VariablesCollectionVisitor
        collector.assignedVariables.removeAll(blockTest.parameterizedGivenVariables);
        System.out.println("AAABBB " + collector.assignedVariables + ", " + inBothLocalSymbolTableAndBlock);

        for (String var : collector.assignedVariables) {
            Type varType = blockTest.localSymbolTable.getOrDefault(var, null);
            Expression initValue = null;

            if (varType == null) {
                // Try to find the type in global symbol table
                HashSet<Type> tmp = globalSymbolTable.getOrDefault(var, new HashSet<>());
                if (tmp.size() == 1) {
                    varType = tmp.iterator().next();
                }
            } else {
                initValue = blockTest.localAssignments.get(var);
            }

            if (varType == null || varType.isUnknownType()) {
                //Cannot find the type using symbolTable
                try {
                    String leftTypeStr = TypeResolver.sSymbolResolver.calculateType(new NameExpr(var)).describe();
                    varType = Util.getTypeFromStr(leftTypeStr);
                } catch (Exception e) {
                   System.err.println("Unable to resolve type for variable " + var + ": " + e.getMessage() + " in collector.assignedVariables");
                   System.err.println("Default to object!");
                   varType = Util.getTypeFromStr("java.lang.Object");
                }
            }

            VariableDeclarator varDec = new VariableDeclarator(varType, var);
            // Original condition: (initValue != null && !initValue.isNullLiteralExpr())
            // Even if the initializer is null, we still need to declare the variable and assign it to null
            // Otherwise compiler will complain that "XXX might not have been initialized"
            if (initValue != null && Util.predictValue) {
                // We have an initializer for this variable (var is init before the block test)
                varDec.setInitializer(initValue);
                System.out.println("Predicting variables value for " + var);
            } else if (initValue != null && !Util.predictValue) {
                System.out.println("Will not predict value for " + var);
            }
            VariableDeclarationExpr decl = new VariableDeclarationExpr(new NodeList<>(varDec));
            body.addStatement(decl);
            System.out.println("Assigning " + varDec + " to the beginning of the test method");
        }

        // Add user-defined setup block
        if (blockTest.setupBlock != null) {
            for (Statement statement : blockTest.setupBlock.getStatements()) {
                body.addStatement(statement);
            }
        }

        // Add assumptions
        if (blockTest.assumptions != null && !blockTest.assumptions.isEmpty()) {
            for (int i = blockTest.assumptions.size() - 1; i >= 0; i--) {
                // Generate the assume statement
                Expression condition = blockTest.assumptions.get(i).clone();
                MethodCallExpr assumeTrue = new MethodCallExpr(null, "assumeTrue", NodeList.nodeList(condition));
                ExpressionStmt statement = new ExpressionStmt(assumeTrue);

                // Replace the variables with the renamed variables
                Util.replaceExpressionsWithVariables(statement, blockTest.renameGivenVariables);

                // Add the assume statement to the body
                body.addStatement(statement);
            }
        }
    }

    private void addTargetBlock(BlockStmt body, BlockStmt initBody, BlockStmt assertBody, BlockTest blockTest, boolean coverage) {
        System.out.println("\n===== Preventing variable redeclaration in target block =====");
        BlockStmt block = blockTest.statements;

        if (blockTest.junitLambdaAssertions.isEmpty()) {
            List<String> alreadyDeclared = new ArrayList<>(blockTest.givenVariables);

            // Add the parameterized given variables to the already declared variables
            if (blockTest.parameterizedGivenVariables != null) {
                alreadyDeclared.addAll(blockTest.parameterizedGivenVariables);
            }

            block = (BlockStmt) blockTest.statements.accept(new DeclarationRemovalVisitor(alreadyDeclared), null);
        }

        System.out.println("Post DeclarationRemovalVisitor:");
        System.out.println(block);

        System.out.println("\n===== Flow controlling =====");
        FlowControllingVisitor visitor = new FlowControllingVisitor(blockTest);
        if (!blockTest.flows.isEmpty() && !coverage) {
            block = (BlockStmt) block.accept(visitor, null);
            initBody.addStatement(visitor.buildInitialization());
        } else {
            System.out.println("SKIP... test does not have checkFlow()");
        }

        // If there is any declaration in the same level as the given block, move it to the front
        // TODO: think about what to do if a declaration is in a nested block, or if there is an initializer
        block = moveDeclarationsToFrontIfAble(block, initBody);

        if (!blockTest.junitLambdaAssertions.isEmpty() && !coverage) {
            block.accept(new ModifierVisitor<Void>() {
                @Override
                public Visitable visit(ReturnStmt rs, Void arg) {
                    if (rs.getExpression().isPresent()) {
                        BlockStmt block = new BlockStmt();

                        System.out.println("!! Replacing return !!");
                        for (Node expr : blockTest.junitLambdaAssertions) {
                            // Renamed variable based on given's renameGivenVariables
                            Util.replaceExpressionsWithVariables(expr, blockTest.renameGivenVariables);

                            MethodCallExpr assertCall = (MethodCallExpr) expr.clone();
                            // replace lambdaReturn with the return value
                            assertCall.accept(new ModifierVisitor<Void>() {
                                @Override
                                public Visitable visit(NameExpr n, Void arg) {
                                    if (n.getNameAsString().equals(Constant.LAMBDA_RETURN)
                                            || n.getNameAsString().equals(Constant.METHOD_RETURN)) {
                                        return new EnclosedExpr(rs.getExpression().get().clone());
                                    }
                                    return super.visit(n, arg);
                                }
                            }, null);

                            block.addStatement(assertCall);
                        }

                        if (blockTest.notReturn) {
                            block.addStatement(new AssignExpr(new NameExpr(Constant.RETURN_STMT_REACHED), new NameExpr("false"), AssignExpr.Operator.ASSIGN));
                        } else {
                            block.addStatement(new AssignExpr(new NameExpr(Constant.RETURN_STMT_REACHED), new NameExpr("true"), AssignExpr.Operator.ASSIGN));
                        }
                        block.addStatement(new ReturnStmt()); // bare return;
                        return block;
                    }
                    return super.visit(rs, arg);
                }
            }, null);
        } else {
            // replace return xxx; with return;
            block.accept(new ModifierVisitor<Void>() {
                int counter = 0;
                @Override
                public Visitable visit(ReturnStmt rs, Void arg) {
                    if (rs.getExpression().isPresent()) {
                        BlockStmt block = new BlockStmt();

                        VariableDeclarator decl = new VariableDeclarator(
                                new ClassOrInterfaceType(null, "Object"),
                                "RETURN_STATEMENT_" + (++counter),
                                rs.getExpression().get().clone()
                        );
                        block.addStatement(new ExpressionStmt(new VariableDeclarationExpr(decl)));
                        block.addStatement(new ReturnStmt());
                        return block;
                    }
                    return rs;
                }
            }, null);
        }

        BreakReplacementVisitor replacer = new BreakReplacementVisitor();
        block.accept(replacer, null);

        if (blockTest.controlFlow != null && !coverage) {
            block.accept(new ControlFlowInstrumentVisitor("BLOCKGEN_FLOW_INTERNAL"), null);
        }

        for (Statement statement : block.getStatements()) {
            body.addStatement(statement);
        }

        if (!blockTest.flows.isEmpty() && !coverage) {
            assertBody.addStatement(visitor.buildAssertion());
        }
    }

    private static BlockStmt moveDeclarationsToFrontIfAble(BlockStmt targetBlock, BlockStmt initBody) {
        // If there is an initializer, we need a better way to handle it
        BlockStmt cleanedBlock = new BlockStmt();

        for (Statement stmt : targetBlock.clone().getStatements()) {
            if (stmt.isExpressionStmt() && stmt.asExpressionStmt().getExpression().isVariableDeclarationExpr()) {
                // If the statement is a variable declaration, move it to the front
                VariableDeclarationExpr varDecl = stmt.asExpressionStmt().getExpression().asVariableDeclarationExpr();
                for (VariableDeclarator var : varDecl.getVariables()) {
                    if (!var.getInitializer().isPresent()) {
                        // declared without initializer, remove directly
                        VariableDeclarationExpr decl = new VariableDeclarationExpr(new NodeList<>(var.clone()));
                        initBody.addStatement(new ExpressionStmt(decl));
                    } else {
                        VariableDeclarationExpr decl = new VariableDeclarationExpr(new NodeList<>(var.clone()));
                        cleanedBlock.addStatement(new ExpressionStmt(decl));
                    }
                }
            } else {
                cleanedBlock.addStatement(stmt.clone());
            }
        }

        System.out.println("^^** Cleaned block: ");
        System.out.println(cleanedBlock);
        return cleanedBlock;
    }

    private static void addAssertion(BlockStmt body, BlockTest blockTest) {
        if (blockTest.delay > 0) {
            // If there is a delay, we need to add a sleep statement
            MethodCallExpr sleepCall = new MethodCallExpr(
                    new NameExpr("java.lang.Thread"), "sleep", NodeList.nodeList(new LongLiteralExpr(blockTest.delay))
            );
            body.addStatement(new ExpressionStmt(sleepCall));
        }

        for (int i = 0; i < blockTest.junitAssertions.size(); i++) {
            ExpressionStmt expressionStmt = new ExpressionStmt((Expression) blockTest.junitAssertions.get(i)).clone();

            // Renamed variable based on given's renameGivenVariables
            Util.replaceExpressionsWithVariables(expressionStmt, blockTest.renameGivenVariables);

            body.addStatement(expressionStmt);
            System.out.println("ADDING ASSERTION: " + expressionStmt);
        }

//        if (blockTest.controlFlow != null) {
//            MethodCallExpr flowAssertCall = new MethodCallExpr(
//                    new NameExpr("org.blocktest.Assertion"),
//                    "assertEquals",
//                    new NodeList<>(new NameExpr(blockTest.controlFlow), new MethodCallExpr(new NameExpr("BLOCKGEN_FLOW_INTERNAL"), "toString"))
//            );
//            body.addStatement(new ExpressionStmt(flowAssertCall));
//        }
    }
}
