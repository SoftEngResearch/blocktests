package org.blocktest.visitors;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import javassist.expr.Expr;

import static org.blocktest.utils.Constant.*;

public class GivenTransformationVisitor extends ModifierVisitor<Void> {

    @Override
    public Visitable visit(MethodCallExpr methodCall, Void arg) {
        // First, recursively visit child nodes
        super.visit(methodCall, arg);

        // Check if this is a .given() call with at least one argument
        if ((methodCall.getNameAsString().equals(GIVEN) || methodCall.getNameAsString().equals(CHECK_EQ)) &&
                !methodCall.getArguments().isEmpty()) {

            // Check if this .given() is part of a blocktest chain
            if (isInBlockTestChain(methodCall)) {
                Expression firstArg = methodCall.getArgument(0);

                // If first argument is a string literal, convert to name expression
                if (firstArg instanceof StringLiteralExpr) {
                    StringLiteralExpr stringLiteral = (StringLiteralExpr) firstArg;
                    String value = stringLiteral.getValue();

                    // Replace with NameExpr (removes quotes)
                    Expression nameExpr = StaticJavaParser.parseExpression(value);
                    methodCall.setArgument(0, nameExpr);
                }
            }
        }

        // Return the (potentially modified) method call
        return methodCall;
    }

    /**
     * Check if this method call is part of a chain that starts with blocktest()
     */
    private boolean isInBlockTestChain(MethodCallExpr methodCall) {
        // Traverse up the chain to find the root
        MethodCallExpr current = methodCall;

        while (current != null) {
            if (current.getNameAsString().equals(DECLARE_NAME)) {
                return true;
            }

            // Move to the scope (previous method in chain)
            if (current.getScope().isPresent() &&
                    current.getScope().get().isMethodCallExpr()) {
                current = current.getScope().get().asMethodCallExpr();
            } else {
                break;
            }
        }

        return false;
    }
}
