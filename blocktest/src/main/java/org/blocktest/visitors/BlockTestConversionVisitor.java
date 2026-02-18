package org.blocktest.visitors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.javaparser.Range;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.nodeTypes.NodeWithStatements;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import org.blocktest.types.EndAt;

public class BlockTestConversionVisitor extends ModifierVisitor<Void> {

    private List<Statement> getBlock(NodeList<Statement> statements) {
        // Work on a copy of statements (to avoid concurrent modification)
        List<Statement> newStmts = new ArrayList<>();
        List<Statement> newStmtsTmp = new ArrayList<>();
        boolean modified = false;

        for (Statement stmt : statements) {
            // Check all comments on this statement
            Optional<Comment> maybeComment = stmt.getComment();

            if (maybeComment.isPresent()) {
                Comment comment = maybeComment.get();
                String content = comment.getContent().trim();

                stmt.getParentNode().get().getOrphanComments().stream()
                        .filter(i -> stmt.toString().contains(i.toString())).forEach(i -> {
                            String content2 = i.getContent().trim();
                            if (content2.startsWith("@blocktest") || content2.startsWith("@lambdatest")) {
                                String code2 = content2.substring(1).trim();
                                Statement newStmt2 = code2.endsWith(";") ? StaticJavaParser.parseStatement(code2) : StaticJavaParser.parseStatement(code2 + ";");
                                newStmts.add(newStmt2);
                                newStmtsTmp.add(newStmt2);
                                System.out.println("newStmt2 is " + newStmt2);
                            }
                        });

                if (content.startsWith("@blocktest") || content.startsWith("@lambdatest")) {
                    System.out.println("@blocktest! CONVERTING blocktest in comment mode: " + content);

                    // Parse comment into code
                    String code = content.substring(1).trim(); // drop leading "@"
                    Statement newStmt = code.endsWith(";") ? StaticJavaParser.parseStatement(code) : StaticJavaParser.parseStatement(code + ";");

                    // Insert the new statement BEFORE the original one
                    newStmts.add(newStmt);
                    newStmtsTmp.add(newStmt);

                    // Remove the comment so it doesn't stay attached
                    stmt.removeComment();
                }
            }

            // Always add the original statement back
            newStmts.add(stmt);
        }
        return newStmtsTmp.isEmpty() ? statements : newStmts;
    }

    private List<Statement> getBlock(BlockStmt block) {
        return getBlock(block.getStatements());
    }

    @Override
    public Visitable visit(BlockStmt block, Void arg) {
        block.setStatements(new NodeList<>(getBlock(block)));
        return super.visit(block, arg);
    }

    @Override
    public Visitable visit(ConstructorDeclaration n, Void arg) {
        n.setBody(new BlockStmt(new NodeList<>(getBlock(n.getBody()))));
        return super.visit(n, arg);
    }

    @Override
    public Visitable visit(SwitchEntry n, Void arg) {
        n.setStatements(new NodeList<>(getBlock(n.getStatements())));
        return super.visit(n, arg);
    }

    @Override
    public Visitable visit(TryStmt n, Void arg) {
        n.setTryBlock(new BlockStmt(new NodeList<>(getBlock(n.getTryBlock()))));
        if (n.getFinallyBlock().isPresent()) {
            BlockStmt finallyBlock = n.getFinallyBlock().get();
            n.setFinallyBlock(new BlockStmt(new NodeList<>(getBlock(finallyBlock))));
        }
        return super.visit(n, arg);
    }

    @Override
    public Visitable visit(ForEachStmt n, Void arg) {
        n.getBody().ifBlockStmt(block -> {
            n.setBody(new BlockStmt(new NodeList<>(getBlock(block))));
        });
        return super.visit(n, arg);
    }

    @Override
    public Visitable visit(ForStmt n, Void arg) {
        n.getBody().ifBlockStmt(block -> {
            n.setBody(new BlockStmt(new NodeList<>(getBlock(block))));
        });
        return super.visit(n, arg);
    }

    @Override
    public Visitable visit(DoStmt n, Void arg) {
        n.getBody().ifBlockStmt(block -> {
            n.setBody(new BlockStmt(new NodeList<>(getBlock(block))));
        });
        return super.visit(n, arg);
    }

    @Override
    public Visitable visit(WhileStmt n, Void arg) {
        n.getBody().ifBlockStmt(block -> {
            n.setBody(new BlockStmt(new NodeList<>(getBlock(block))));
        });
        return super.visit(n, arg);
    }

    @Override
    public Visitable visit(IfStmt n, Void arg) {
        n.getThenStmt().ifBlockStmt(block -> {
            n.setThenStmt(new BlockStmt(new NodeList<>(getBlock(block))));
        });
        n.getElseStmt().ifPresent(block -> {
            block.ifBlockStmt(b -> {
                n.setElseStmt(new BlockStmt(new NodeList<>(getBlock(b))));
            });
        });
        return super.visit(n, arg);
    }

    /*
    @Override
    public Visitable visit(ExpressionStmt stmt, Void arg) {
        if (stmt.getComment().isPresent()) {
            Comment comment = stmt.getComment().get();
            String content = comment.getContent().trim();

            if (content.startsWith("@blocktest")) {
                String code = content.substring(1).trim();
                System.out.println("Found blocktest in comment mode: " + code);

                Statement newStmt = code.endsWith(";") ? StaticJavaParser.parseStatement(code) : StaticJavaParser.parseStatement(code + ";");

                Node parentNode = stmt.getParentNode().get();
                while (!(parentNode instanceof BlockStmt) && !(parentNode instanceof SwitchEntry)) {
                    parentNode = parentNode.getParentNode().get();
                }
                NodeWithStatements blockStmt = (NodeWithStatements) parentNode;
                int endIndex = blockStmt.getStatements().indexOf(stmt);
                return newStmt.asExpressionStmt();
            }
        }

        return super.visit(stmt, arg);
    }
     */
}
