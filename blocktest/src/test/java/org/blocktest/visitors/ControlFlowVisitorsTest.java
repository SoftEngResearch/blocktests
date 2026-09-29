package org.blocktest.visitors;

import static org.junit.Assert.*;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.*;
import java.util.*;
import org.blocktest.BlockTest;
import org.blocktest.support.AstTestSupport;
import org.blocktest.types.Flow;
import org.blocktest.utils.Util;
import org.junit.Test;

public class ControlFlowVisitorsTest extends AstTestSupport {
  private static final String FLOWS = "visitors/ControlFlow.java";

  @Test
  public void instrumentsIfElseIfAndSynthesizesMissingElse() throws Exception {
    BlockStmt b = blockFixture(FLOWS, "ifElseIfBefore");
    b.accept(new ControlFlowInstrumentVisitor("trace"), null);
    assertBlockFixture(FLOWS, "ifElseIfAfter", b);
  }

  @Test
  public void instrumentsEveryLoopAndSwitchEntryWithoutChangingOriginalBodies() throws Exception {
    BlockStmt b = blockFixture(FLOWS, "loopsBefore");
    b.accept(new ControlFlowInstrumentVisitor("trace"), null);
    assertBlockFixture(FLOWS, "loopsAfter", b);
  }

  @Test
  public void skipsNestedClassesButVisitsLambdasAndConstructorArguments() throws Exception {
    BlockStmt b = blockFixture(FLOWS, "nestedTypesBefore");
    b.accept(new ControlFlowInstrumentVisitor("trace"), null);
    assertEquals(
        3, b.findAll(MethodCallExpr.class, m -> m.getNameAsString().equals("append")).size());
    assertFalse(b.getStatement(0).toString().contains("trace"));
    assertFalse(b.getStatement(1).toString().contains("trace"));
    assertTrue(b.getStatement(2).toString().contains("trace.append(\"while,\")"));
  }

  private FlowControllingVisitor visitor(String name, Flow... flows) {
    BlockTest b = new BlockTest();
    b.testName = name;
    b.flows.addAll(Arrays.asList(flows));
    return new FlowControllingVisitor(b);
  }

  @Test
  public void tracksSelectedThenAndNestedThenWithoutInstrumentingOppositeBranch() throws Exception {
    BlockStmt b = blockFixture(FLOWS, "selectedThenBefore");
    FlowControllingVisitor v = visitor("path", Flow.IfStmt, Flow.Then, Flow.IfStmt, Flow.Then);
    b.accept(v, null);
    assertBlockFixture(FLOWS, "selectedThenAfter", b);
    assertEquals(
        "boolean[] _blocktest_flow_path = new boolean[2];", v.buildInitialization().toString());
    ForStmt assertion = v.buildAssertion().asForStmt();
    assertEquals("_blocktest_path_i < 2", assertion.getCompare().get().toString());
    assertEquals("_blocktest_path_i += 1", assertion.getUpdate().get(0).toString());
    MethodCallExpr call = assertion.findFirst(MethodCallExpr.class).get();
    assertEquals("assertTrue", call.getNameAsString());
    assertEquals(
        "\"Required flow: (IfStmt -> Then -> IfStmt -> Then) fails at step \" + _blocktest_path_i",
        call.getArgument(0).toString());
    assertEquals("_blocktest_flow_path[_blocktest_path_i]", call.getArgument(1).toString());
  }

  @Test
  public void tracksElseAndElseIfPaths() throws Exception {
    BlockStmt b = blockFixture(FLOWS, "elseIfPathBefore");
    b.accept(visitor("p", Flow.IfStmt, Flow.ElseIf, Flow.ElseIf, Flow.Then), null);
    assertTrue(b.toString(), b.toString().contains("_blocktest_flow_p[0] = true;\n        hit();"));
    assertEquals(1, b.findAll(com.github.javaparser.ast.expr.AssignExpr.class).size());
    b = blockFixture(FLOWS, "elseIfPathBefore");
    b.accept(visitor("p", Flow.IfStmt, Flow.ElseIf, Flow.ElseIf, Flow.Else), null);
    assertTrue(
        b.toString(), b.toString().contains("_blocktest_flow_p[0] = true;\n        miss();"));
    b = blockFixture(FLOWS, "elsePathBefore");
    b.accept(visitor("p", Flow.IfStmt, Flow.Else), null);
    assertBlockFixture(FLOWS, "elsePathAfter", b);
  }

  @Test
  public void incompleteFlowsLeaveUnselectedBranchesAlone() throws Exception {
    for (Flow[] flows :
        Arrays.asList(
            new Flow[0],
            new Flow[] {Flow.Then},
            new Flow[] {Flow.IfStmt},
            new Flow[] {Flow.IfStmt, Flow.IfStmt},
            new Flow[] {Flow.IfStmt, Flow.ElseIf})) {
      BlockStmt b = blockFixture(FLOWS, "unselectedBefore");
      BlockStmt original = b.clone();
      b.accept(visitor("p", flows), null);
      assertEquals(original, b);
    }
    BlockStmt b = blockFixture(FLOWS, "unselectedElseIfBefore");
    BlockStmt original = b.clone();
    b.accept(visitor("p", Flow.IfStmt, Flow.ElseIf), null);
    assertEquals(original, b);
  }

  @Test
  public void assertionArgumentsRespectJUnitVersionAndCoverageMode() {
    FlowControllingVisitor v = visitor("x", Flow.IfStmt, Flow.Then);
    assertEquals("_blocktest_flow_x[0] = true;", v.increaseByOne().toString());
    assertEquals("_blocktest_flow_x[1] = true;", v.increaseByOne().toString());
    Util.junitVersion = "junit5";
    MethodCallExpr call = v.buildAssertion().findFirst(MethodCallExpr.class).get();
    assertEquals("_blocktest_flow_x[_blocktest_x_i]", call.getArgument(0).toString());
    assertEquals(2, call.getArguments().size());
    Util.coverage = true;
    call = v.buildAssertion().findFirst(MethodCallExpr.class).get();
    assertEquals("org.blocktest.Assertion", call.getScope().get().toString());
    assertEquals(1, call.getArguments().size());
  }

  @Test
  public void elseIfCanEndWithoutElseOrStopAtAnUnselectedNestedCondition() throws Exception {
    BlockStmt b = blockFixture(FLOWS, "elseIfWithoutElseBefore");
    b.accept(visitor("p", Flow.IfStmt, Flow.ElseIf, Flow.Then), null);
    assertBlockFixture(FLOWS, "elseIfWithoutElseAfter", b);
    b = blockFixture(FLOWS, "elseIfWithoutElseBefore");
    BlockStmt original = b.clone();
    b.accept(visitor("p", Flow.IfStmt, Flow.ElseIf, Flow.IfStmt), null);
    assertEquals(original, b);
    b = blockFixture(FLOWS, "elseIfWithElseBefore");
    original = b.clone();
    b.accept(visitor("p", Flow.IfStmt, Flow.ElseIf), null);
    assertEquals(original, b);
  }
}
