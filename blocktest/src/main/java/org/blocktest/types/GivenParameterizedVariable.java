package org.blocktest.types;

import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.type.Type;

public class GivenParameterizedVariable {
    public Type type;
    public String name;

    public Expression declVar;
    public int index;

    public GivenParameterizedVariable(Type type, String name, Expression declVar, int index) {
        this.type = type;
        this.name = name;
        this.declVar = declVar;
        this.index = index;
    }

    @Override
    public String toString() {
        return toAssignExpr().toString();
    }

    public Expression toAssignExpr() {
        if (type == null) {
            throw new RuntimeException("Type cannot be null for GivenVariable: " + name);
        }

            return new AssignExpr(new VariableDeclarationExpr(type, name),
                    new NullLiteralExpr(),
                    AssignExpr.Operator.ASSIGN);
    }
}
