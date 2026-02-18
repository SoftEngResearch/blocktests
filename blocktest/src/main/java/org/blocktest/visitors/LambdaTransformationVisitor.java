package org.blocktest.visitors;

import java.util.List;
import java.util.Optional;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import org.blocktest.utils.Constant;
import org.blocktest.utils.Util;

/*
    The goal of this visitor is to move blocktest().given(...).checkEq(...).end(LAMBDA) statements from outside the lambda
    into the lambda body. So that type resolver can resolve the variables used in the lambda body.
 */

public class LambdaTransformationVisitor extends ModifierVisitor<Void> {

    @Override
    public Visitable visit(ExpressionStmt expressionStmt, Void ctx) {
        if (Util.isTestStatement(expressionStmt)) {
            Expression newBlockTestStatement = isLambda(expressionStmt);
            if (newBlockTestStatement != null) {
                rewriteLambda(expressionStmt, newBlockTestStatement);
                return null;
            }
        }
        return super.visit(expressionStmt, ctx);
    }

    private Expression isLambda(Node node) {
        if (node instanceof ExpressionStmt) {
            return isLambda(((ExpressionStmt) node).getExpression());
        } else if (!(node instanceof MethodCallExpr)) {
            return null;
        }

        MethodCallExpr methodCall = (MethodCallExpr) node;
        if (methodCall.getName().asString().equals(Constant.END)) {
            if (methodCall.getArguments().isEmpty()) {
                throw new RuntimeException("Test statement should not contain end() method call without arg: " + methodCall);
            }

            if (methodCall.getArgument(0).toString().equals(Constant.LAMBDA)) {
                // If the end() method call is with LAMBDA argument, then we can rewrite the lambda body
                return methodCall.getScope().get();
            } else {
                // If the end() method call is not with LAMBDA argument, then we cannot rewrite the lambda body
                return null;
            }
        } else if (methodCall.getName().asString().equals(Constant.ARGS_VAR)) {
            // We need to catch this before catching lambdatest() because `rewriteLambda` will cause `visit` to call
            // `isLambda` with the new method call (that ends with argsVar)
            return null;
        } else if (methodCall.toString().startsWith(Constant.DECLARE_NAME_LAMBDA + "()")) {
            return methodCall;
        }

        return null;
    }

    private void rewriteLambda(Node expressionStmt, Expression newBlockTestStatement) {
        if (expressionStmt.getParentNode().isPresent()) {
            Node parentNode = expressionStmt.getParentNode().get();
            while (!(parentNode instanceof BlockStmt) && !(parentNode instanceof SwitchEntry)) {
                // Find parent block
                parentNode = parentNode.getParentNode().get();
            }

            // Searching target statement
            if (parentNode instanceof BlockStmt) {
                BlockStmt blockStmt = (BlockStmt) parentNode;
                int startIndex = blockStmt.getStatements().indexOf(expressionStmt) + 1;
                for (int i = startIndex; i < blockStmt.getStatements().size(); i++) {
                    Node node = blockStmt.getStatements().get(i);
                    Optional<LambdaExpr> found = node.findFirst(LambdaExpr.class);
                    if (found.isPresent()) {
                        LambdaExpr lambda = found.get();
                        List<Parameter> params = lambda.getParameters();
                        MethodCallExpr finalBlockTestStatement = new MethodCallExpr(newBlockTestStatement, Constant.ARGS_VAR);
                        for (Parameter param : params) {
                            finalBlockTestStatement.addArgument(param.getNameAsString());
                        }

                        System.out.println("LAMBDA BLOCK:");
                        System.out.println(finalBlockTestStatement);

                        if (lambda.getBody().isBlockStmt()) {
                            lambda.getBody().asBlockStmt().addStatement(0, finalBlockTestStatement);
                            return;
                        }
                    }
                }
            }
        }
    }
}
