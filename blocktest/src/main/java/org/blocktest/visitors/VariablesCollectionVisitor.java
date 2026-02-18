package org.blocktest.visitors;

import java.util.HashSet;
import java.util.Set;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class VariablesCollectionVisitor extends VoidVisitorAdapter<Void> {
    public Set<String> assignedVariables = new HashSet<>();
    public Set<String> declaredVariables = new HashSet<>();
    // TODO: add used variables (variables that are read but not assigned), see if they are constants

    @Override
    public void visit(VariableDeclarator vd, Void arg) {
        declaredVariables.add(vd.getNameAsString());
        super.visit(vd, arg);
    }

    @Override
    public void visit(AssignExpr ae, Void arg) {
        Expression target = ae.getTarget();
        if (target.isNameExpr()) {
            assignedVariables.add(target.asNameExpr().getNameAsString());
        }
        super.visit(ae, arg);
    }
}
