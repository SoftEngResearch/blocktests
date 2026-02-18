package org.blocktest.visitors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import org.blocktest.BlockTest;
import org.blocktest.types.Flow;
import org.blocktest.utils.Util;

public class FlowControllingVisitor extends ModifierVisitor<Void> {

    private String flowText = "";
    private final List<Flow> flows;
    private String testName;
    private int counter = 0;

    private Set<Statement> processedAsElseIf;

    public FlowControllingVisitor(BlockTest blockTest) {
        this.flows = new ArrayList<>(blockTest.flows);
        this.testName = blockTest.testName;
        this.processedAsElseIf = new HashSet<>();

        StringBuilder sb = new StringBuilder();
        for (Flow flow : flows) {
            if (sb.length() > 0) {
                sb.append(" -> ").append(flow.toString());
            } else {
                sb.append(flow.toString());
            }
        }
        flowText = sb.toString();
    }

    public ExpressionStmt increaseByOne() {
        NameExpr arrayName = new NameExpr("_blocktest_flow_" + testName);
        IntegerLiteralExpr index = new IntegerLiteralExpr(counter++);
        ArrayAccessExpr arrayAccess = new ArrayAccessExpr(arrayName, index);

        BooleanLiteralExpr trueValue = new BooleanLiteralExpr(true);
        AssignExpr assign = new AssignExpr(arrayAccess, trueValue, AssignExpr.Operator.ASSIGN);

        return new ExpressionStmt(assign);
    }

    @Override
    public Visitable visit(IfStmt n, Void arg) {
        System.out.println("FlowControllingVisitor: Visit ifStmt: " + n);
        if (flows.isEmpty()) {
            return super.visit(n, arg);
        }

        if (processedAsElseIf.contains(n)) {
            // Skip this node, it was already handled
            return n;
        }

        Flow flow = flows.remove(0);
        System.out.println("FIRST Next step should be " + flow + ", remaining flows: " + flows);
        if (flow.equals(Flow.IfStmt)) {

            if (flows.isEmpty() || (!flows.get(0).equals(Flow.Then) && !flows.get(0).equals(Flow.Else) && !flows.get(0).equals(Flow.ElseIf))) {
                return super.visit(n, arg);
            }

            Flow flow2 = flows.remove(0);
            System.out.println("SECOND Next step should be " + flow2 + ", remaining flows: " + flows);

            if (flow2.equals(Flow.Then)) {
                n.getThenStmt().asBlockStmt().addStatement(0, increaseByOne());

                if (n.getElseStmt().isPresent()) {
                    List<IfStmt> add = n.getElseStmt().get().findAll(IfStmt.class);
                    processedAsElseIf.addAll(add);
                    System.out.println("See Then, add else to processed: " + add);
                }
            } else if (flow2.equals(Flow.Else)) {
                n.getElseStmt().get().asBlockStmt().addStatement(0, increaseByOne());

                List<IfStmt> add = n.getThenStmt().findAll(IfStmt.class);
                processedAsElseIf.addAll(n.getThenStmt().findAll(IfStmt.class));
                System.out.println("See Else, add then to processed: " + add);
            } else if (flow2.equals(Flow.ElseIf)) {
                List<IfStmt> add = n.getThenStmt().findAll(IfStmt.class);
                processedAsElseIf.addAll(add);
                System.out.println("See ElseIf, add first then to processed: " + add);

                IfStmt current = n;
                while (true) {
                    if (current.getElseStmt().isPresent() && current.getElseStmt().get() instanceof IfStmt) {
                        current = (IfStmt) current.getElseStmt().get();

                        System.out.println("CURRENT IS " + current.getThenStmt());
                        if (!flows.isEmpty() && !flows.get(0).equals(Flow.Then)) {
                            add = current.getThenStmt().findAll(IfStmt.class);
                            processedAsElseIf.addAll(add);
                            System.out.println("See ElseIf, add then to processed: " + add);
                        }

                        if (!flows.isEmpty() && (flows.get(0).equals(Flow.Then) || flows.get(0).equals(Flow.Else) || flows.get(0).equals(Flow.ElseIf))) {
                            Flow flow3 = flows.remove(0);
                            System.out.println("LOOP: Next step should be " + flow3 + ", remaining flows: " + flows);

                            if (!flow3.equals(Flow.Then)) {
                                add = current.getThenStmt().findAll(IfStmt.class);
                                processedAsElseIf.addAll(add);
                                System.out.println("Next one is not then, add then to processed: " + add);
                            }

                            if (flow3.equals(Flow.Then)) {
                                current.getThenStmt().asBlockStmt().addStatement(0, increaseByOne());

                                current.getThenStmt().accept(this, arg);
                                if (current.getElseStmt().isPresent()) {
                                    add = current.getElseStmt().get().findAll(IfStmt.class);
                                    processedAsElseIf.addAll(add);
                                    System.out.println("See Then, add else to processed: " + add);

                                    current.getElseStmt().get().accept(this, arg);
                                }
                                break;
                            } else if (flow3.equals(Flow.Else)) {
                                current.getElseStmt().get().asBlockStmt().addStatement(0, increaseByOne());

                                current.getThenStmt().accept(this, arg);
                                current.getElseStmt().get().accept(this, arg);
                                break;
                            } else if (flow3.equals(Flow.ElseIf)) {
//                                System.out.println("CURRENT IS " + current.getThenStmt());

                                // Continue to next else-if
//                                processedAsElseIf.addAll(current.getThenStmt().findAll(IfStmt.class));
                                current.getThenStmt().accept(this, arg);
//                                System.out.println("Adding processed");
//                                System.out.println(processedAsElseIf);
                                continue;
                            }
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                }
                return n;
            }
        }
        return super.visit(n, arg);
    }

    public Statement buildInitialization() {
        ArrayType arrayType = new ArrayType(PrimitiveType.booleanType());

        ArrayCreationLevel levelWithSize = new ArrayCreationLevel(new IntegerLiteralExpr(counter));
        ArrayCreationExpr arrayCreation = new ArrayCreationExpr(
                PrimitiveType.booleanType(),
                NodeList.nodeList(levelWithSize),
                null
        );

        VariableDeclarator varDecl = new VariableDeclarator(arrayType, "_blocktest_flow_" + testName, arrayCreation);
        VariableDeclarationExpr varDeclExpr = new VariableDeclarationExpr(varDecl);
        return new ExpressionStmt(varDeclExpr);
    }

    public Statement buildAssertion() {
        String indexVar = "_blocktest_" + testName + "_i";
        NameExpr arrayName = new NameExpr("_blocktest_flow_" + testName);
        // for (int _internal_i = 0;
        VariableDeclarator initVar = new VariableDeclarator(PrimitiveType.intType(), indexVar, new IntegerLiteralExpr(0));
        VariableDeclarationExpr init = new VariableDeclarationExpr(initVar);

        // _internal_i < x
        BinaryExpr compare = new BinaryExpr(new NameExpr(indexVar), new IntegerLiteralExpr(counter), BinaryExpr.Operator.LESS);

        // _internal_i += 1
        AssignExpr increment = new AssignExpr(new NameExpr(indexVar), new IntegerLiteralExpr(1), AssignExpr.Operator.PLUS);

        // assertTrue(_internal_abc[_internal_i]);

        StringBuilder sb = new StringBuilder();
        sb.append("Required flow: (");
        sb.append(flowText);
        sb.append(") fails at step ");
        BinaryExpr message = new BinaryExpr(
            new StringLiteralExpr(sb.toString()),
            new NameExpr(indexVar),
            BinaryExpr.Operator.PLUS
        );

        MethodCallExpr assertCall = new MethodCallExpr(null, "assertTrue");
        ArrayAccessExpr arrayAccess = new ArrayAccessExpr(arrayName, new NameExpr(indexVar));
        if (Util.junitVersion.equals("junit4")) {
            assertCall.addArgument(message);
            assertCall.addArgument(arrayAccess);
        } else {
            assertCall.addArgument(arrayAccess);
            assertCall.addArgument(message);
        }
        ExpressionStmt assertStmt = new ExpressionStmt(assertCall);

        BlockStmt body = new BlockStmt();
        body.addStatement(assertStmt);

        // Construct ForStmt
        ForStmt forStmt = new ForStmt();
        NodeList<Expression> initList = new NodeList<>();
        initList.add(init);
        forStmt.setInitialization(initList);
        forStmt.setCompare(compare);

        NodeList<Expression> incrementList = new NodeList<>();
        incrementList.add(increment);
        forStmt.setUpdate(incrementList);
        forStmt.setBody(body);

        return forStmt;
    }
}
