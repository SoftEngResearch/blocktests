package org.blocktest.visitors;

import java.util.List;
import java.util.Set;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;

public class DeclarationRemovalVisitor extends ModifierVisitor<Void> {

    private final List<String> givenVariables;

    public DeclarationRemovalVisitor(List<String> givenVariables) {
        this.givenVariables = givenVariables;
    }

    @Override
    public Visitable visit(ExpressionStmt stmt, Void arg) {
        System.out.println("1. Adding target statement: " + stmt);

        if (stmt.getExpression().isVariableDeclarationExpr()) {
            // is variable declaration
            System.out.println("\t2. statement is a variable declaration statement");

            VariableDeclarationExpr expr = stmt.getExpression().asVariableDeclarationExpr();

            NodeList<Statement> declarationStmts = new NodeList<>();
            for (VariableDeclarator var : expr.getVariables()) {
                System.out.println("\t\t3. variable: " + var.getNameAsString());

                if (givenVariables.contains(var.getNameAsString())) {
                    System.out.println("\t\t\t4. variable " + var.getNameAsString() + " is already defined in givens");

                    /*
                    // The below code will change declaration to assignment, but for now let just remove the statement
                    // If the variable is already defined in givens
                    if (!var.getInitializer().isPresent() || var.getInitializer().get().isNullLiteralExpr()) {
                        // Skip re-declaration or skip setting to null
                        System.out.println("\t\t\t\t5. no initializer or null initializer for variable " + var.getNameAsString());
                        continue;
                    }

                    System.out.println("\t\t\t\t5. variable " + var.getNameAsString() + ", has initializer, " +
                            "changing to assignment");

                    AssignExpr assign = new AssignExpr(new NameExpr(var.getNameAsString()),
                            var.getInitializer().get(),
                            AssignExpr.Operator.ASSIGN
                    );

                    declarationStmts.add(new ExpressionStmt(assign));
                     */
                } else {
                    System.out.println("\t\t\t4. variable " + var.getNameAsString() + " is NOT defined in givens, keep it as declaration");

                    VariableDeclarationExpr vde = new VariableDeclarationExpr(new NodeList<>(var.clone()));
                    declarationStmts.add(new ExpressionStmt(vde));
                }
            }

            if (declarationStmts.isEmpty()) {
                return null; // Remove the statement entirely
            }
            if (declarationStmts.size() == 1) {
                return declarationStmts.get(0);
            }
            return new BlockStmt(declarationStmts);
        }

        return super.visit(stmt, arg);
    }
}
