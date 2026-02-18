package org.blocktest.visitors;

import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ThisFieldReplacementVisitor extends VoidVisitorAdapter<Void> {

    @Override
    public void visit(FieldAccessExpr n, Void arg) {
        super.visit(n, arg);

        if (n.getScope() instanceof ThisExpr) {
            String fieldName = n.getNameAsString();
            NameExpr replacement = new NameExpr(fieldName);
            n.replace(replacement);
        }
    }
}
