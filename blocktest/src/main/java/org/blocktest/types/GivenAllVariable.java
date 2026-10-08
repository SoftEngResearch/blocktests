package org.blocktest.types;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.type.Type;

public class GivenAllVariable {
    public Type type;
    public String name;

    public GivenAllVariable(Type type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public String toString() {
        return toAssignExpr().toString();
    }

    public Expression toAssignExpr() {
        // Check that the type is not null (which should never happen)
        if (type == null) {
            throw new RuntimeException("Type cannot be null for GivenAllVariable: " + name);
        }

        // Build a variable declaration expression with the @ForAll annotation from jqwik
        VariableDeclarationExpr declaration = new VariableDeclarationExpr(type.clone(), name);
        declaration.addMarkerAnnotation("ForAll");
        return declaration;
    }
}
