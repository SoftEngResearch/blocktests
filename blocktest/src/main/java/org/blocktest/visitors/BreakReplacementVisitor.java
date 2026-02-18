package org.blocktest.visitors;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.BreakStmt;
import com.github.javaparser.ast.stmt.ContinueStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class BreakReplacementVisitor extends VoidVisitorAdapter<Void> {
    @Override
    public void visit(BreakStmt breakStmt, Void arg) {
        super.visit(breakStmt, arg);

        System.out.println("Found breakStmt @ " + breakStmt);
        if (withoutLoop(breakStmt)) {
            // Replace break with return
            ReturnStmt returnStmt = new ReturnStmt();

//            // Copy the label if the break statement has one
//            if (breakStmt.getLabel().isPresent()) {
//                // Note: return statements don't have labels, so you might want to handle this differently
//                // For now, we'll create a return without the label
//            }

            System.out.println("Replacing break statement with return statement: " + breakStmt);
            breakStmt.replace(returnStmt);
        }
    }

    @Override
    public void visit(ContinueStmt continueStmt, Void arg) {
        super.visit(continueStmt, arg);

        System.out.println("Found continue @ " + continueStmt);
        if (withoutLoop(continueStmt)) {
            // Replace break with return
            ReturnStmt returnStmt = new ReturnStmt();

            System.out.println("Replacing continue statement with return statement: " + continueStmt);
            continueStmt.replace(returnStmt);
        }
    }

    private boolean withoutLoop(Statement breakStmt) {
        Node parent = breakStmt.getParentNode().orElse(null);

        while (parent != null) {
            if (parent instanceof ForStmt ||
                    parent instanceof ForEachStmt ||
                    parent instanceof WhileStmt ||
                    parent instanceof DoStmt) {
                System.out.println("Break is in LOOP");
                return false;
            }

            System.out.println("BREAK PARENT IS " + parent);
            // Stop if we hit a switch statement
            if (parent instanceof SwitchStmt) {
                System.out.println("Break is not in LOOP (it is in Switch)");
                return false;
            }
            parent = parent.getParentNode().orElse(null);
        }
        System.out.println("Break is not in LOOP/SWITCH");
        return true;
    }
}
