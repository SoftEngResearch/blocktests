package org.blocktest.visitors;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;

/**
 * Instruments control flow entry points with <flowVar>.append(...):
 *   if, else if, else (synthesized if missing)
 *   for, enhanced for, while, do
 *   case_<label>, default
 *
 * Does NOT descend into nested type declarations (anonymous / inner / local
 * classes), since a local flow variable would not be in scope there and
 * would cause a compile error. Lambdas ARE still instrumented because they
 * capture the flow variable from the enclosing scope (effectively final).
 */
public class ControlFlowInstrumentVisitor extends ModifierVisitor<Void> {

    private final String flowVar;

    public ControlFlowInstrumentVisitor(String flowVar) {
        this.flowVar = flowVar;
    }

    // --- Helpers -----------------------------------------------------------

    private BlockStmt ensureBlock(Statement stmt) {
        if (stmt instanceof BlockStmt) return (BlockStmt) stmt;
        BlockStmt block = new BlockStmt();
        block.addStatement(stmt);
        return block;
    }

    private ExpressionStmt appendCall(String label) {
        return new ExpressionStmt(
                new MethodCallExpr(
                        new NameExpr(flowVar),
                        "append",
                        new NodeList<>(new StringLiteralExpr(label))
                )
        );
    }

    private void prepend(BlockStmt block, String label) {
        block.getStatements().addFirst(appendCall(label));
    }

    /**
     * Escape backslashes and double quotes so the label can be safely embedded
     * in a string literal. Needed for switch labels like: case "hello":
     */
    private String escapeForLiteral(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    // --- Stop recursion into nested type declarations ----------------------

    @Override
    public Visitable visit(ClassOrInterfaceDeclaration n, Void arg) {
        // Skip nested / inner / local classes entirely — flow var not in scope there.
        return n;
    }

    @Override
    public Visitable visit(ObjectCreationExpr n, Void arg) {
        // Skip anonymous class bodies, but still visit the constructor arguments
        // (they belong to the enclosing scope and may contain instrumentable code).
        n.getArguments().forEach(a -> a.accept(this, arg));
        return n;
    }

    // --- if / else if / else -----------------------------------------------

    @Override
    public Visitable visit(IfStmt n, Void arg) {
        super.visit(n, arg);

        // "then" branch → if,
        BlockStmt thenBlock = ensureBlock(n.getThenStmt());
        prepend(thenBlock, "if,");
        n.setThenStmt(thenBlock);

        if (n.getElseStmt().isPresent()) {
            Statement elseStmt = n.getElseStmt().get();
            if (elseStmt instanceof IfStmt) {
                // else-if: replace the "if," already set on the inner then-block with "else if,"
                IfStmt innerIf = (IfStmt) elseStmt;
                BlockStmt innerThen = (BlockStmt) innerIf.getThenStmt();
                innerThen.getStatements().set(0, appendCall("else if,"));
            } else {
                BlockStmt elseBlock = ensureBlock(elseStmt);
                prepend(elseBlock, "else,");
                n.setElseStmt(elseBlock);
            }
        } else {
            // No else — synthesize one
            BlockStmt elseBlock = new BlockStmt();
            prepend(elseBlock, "else,");
            n.setElseStmt(elseBlock);
        }

        return n;
    }

    // --- for ---------------------------------------------------------------

    @Override
    public Visitable visit(ForStmt n, Void arg) {
        super.visit(n, arg);
        BlockStmt body = ensureBlock(n.getBody());
        prepend(body, "for,");
        n.setBody(body);
        return n;
    }

    // --- enhanced for (for-each) -------------------------------------------

    @Override
    public Visitable visit(ForEachStmt n, Void arg) {
        super.visit(n, arg);
        BlockStmt body = ensureBlock(n.getBody());
        prepend(body, "enhanced for,");
        n.setBody(body);
        return n;
    }

    // --- while -------------------------------------------------------------

    @Override
    public Visitable visit(WhileStmt n, Void arg) {
        super.visit(n, arg);
        BlockStmt body = ensureBlock(n.getBody());
        prepend(body, "while,");
        n.setBody(body);
        return n;
    }

    // --- do-while ----------------------------------------------------------

    @Override
    public Visitable visit(DoStmt n, Void arg) {
        super.visit(n, arg);
        BlockStmt body = ensureBlock(n.getBody());
        prepend(body, "do,");
        n.setBody(body);
        return n;
    }

    // --- switch ------------------------------------------------------------

    @Override
    public Visitable visit(SwitchStmt n, Void arg) {
        super.visit(n, arg);
        for (SwitchEntry entry : n.getEntries()) {
            String label;
            if (entry.getLabels().isEmpty()) {
                label = "default,";
            } else {
                label = "case_" + escapeForLiteral(entry.getLabels().get(0).toString()) + ",";
            }
            entry.getStatements().addFirst(appendCall(label));
        }
        return n;
    }
}
