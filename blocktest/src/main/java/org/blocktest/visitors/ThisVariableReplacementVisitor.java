package org.blocktest.visitors;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ThisVariableReplacementVisitor extends ModifierVisitor<Void> {
    private String className;
    public ThisVariableReplacementVisitor(String className) {
        this.className = className;
    }
    @Override
    public Visitable visit(ThisExpr n, Void arg) {
        // Here, we want to replace this with new XXX(), where XXX is the class name without _Extracted
        if (!n.getTypeName().isPresent()) {
            return new ObjectCreationExpr(
                    null,
                    new ClassOrInterfaceType(null, className),
                    new NodeList<>()
            );
        }
        return super.visit(n, arg);
    }
}
