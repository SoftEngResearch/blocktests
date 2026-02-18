package org.blocktest.types;

import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.type.Type;

public class GivenVariable {
    public Type type;
    public String name;

    public Expression assignVar;
    public Expression value;

    public GivenVariable(Type type, String name, Expression assignVar, Expression value) {
        this.type = type;
        this.name = name;
        this.assignVar = assignVar;
        this.value = value;
    }

    @Override
    public String toString() {
        return toAssignExpr().toString();
    }

    public Expression toAssignExpr() {
        if (type == null) {
            throw new RuntimeException("Type cannot be null for GivenVariable: " + name);
        }

        if (this.value == null) {
            // There is actually a reason why we set to null instead of just declaring the variable
            /*
                Locale locale;
                if (null != currencyCode) {
                    if (S.eq("AUD", currencyCode)) {
                        locale = new Locale(lan, "AU");
                    } else if (S.eq("USD", currencyCode)) {
                        locale = Locale.US;
                    } else if (S.eq("GBP", currencyCode)) {
                        locale = Locale.UK;
                    }
                }
                assertEquals(Locale.US, locale);

                the above code will not work because if currentCode is null,
                locale is not initialized and compiler will complain
             */
//            return new VariableDeclarationExpr(type, name);
            return new AssignExpr(new VariableDeclarationExpr(type, name),
                    new NullLiteralExpr(),
                    AssignExpr.Operator.ASSIGN);
        }

        return new AssignExpr(new VariableDeclarationExpr(type, name),
                value,
                AssignExpr.Operator.ASSIGN);
    }
}
