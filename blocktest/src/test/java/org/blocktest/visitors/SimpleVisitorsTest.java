package org.blocktest.visitors;

import static org.junit.Assert.*;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import java.util.Arrays;
import java.util.HashSet;
import org.blocktest.support.AstTestSupport;
import org.junit.Test;

public class SimpleVisitorsTest extends AstTestSupport {
  private static final String BLOCKS = "visitors/SimpleVisitors.java";

  @Test
  public void removesOnlyGivenDeclarationsIncludingNestedAndMixedDeclarations() throws Exception {
    BlockStmt block = blockFixture(BLOCKS, "declarationsBefore");
    block.accept(new DeclarationRemovalVisitor(Arrays.asList("x", "x2")), null);
    assertBlockFixture(BLOCKS, "declarationsAfter", block);
  }

  @Test
  public void collectsSimpleAssignmentsDeclarationsAndCatchParameters() throws Exception {
    BlockStmt block = blockFixture(BLOCKS, "variablesToCollect");
    VariablesCollectionVisitor collector = new VariablesCollectionVisitor();
    block.accept(collector, null);
    assertEquals(new HashSet<>(Arrays.asList("x", "y", "z", "e")), collector.declaredVariables);
    assertEquals(new HashSet<>(Arrays.asList("x", "y")), collector.assignedVariables);
  }

  @Test
  public void replacesOnlyThisFieldsAndUnqualifiedThis() throws Exception {
    BlockStmt block = blockFixture(BLOCKS, "thisBefore");
    block.accept(new ThisFieldReplacementVisitor(), null);
    assertBlockFixture(BLOCKS, "thisFieldsAfter", block);
    block.accept(new ThisVariableReplacementVisitor("Owner"), null);
    assertBlockFixture(BLOCKS, "thisVariablesAfter", block);
  }

  @Test
  public void convertsQuotedGivenAndEqualityTargetsOnlyInBlocktestChains() throws Exception {
    BlockStmt block = blockFixture(BLOCKS, "quotedArgumentsBefore");
    block.accept(new GivenTransformationVisitor(), null);
    assertBlockFixture(BLOCKS, "quotedArgumentsAfter", block);
  }

  @Test
  public void replacesOrphanBreakAndContinueButPreservesEveryLoopAndSwitch() throws Exception {
    BlockStmt block = blockFixture(BLOCKS, "breaksBefore");
    block.accept(new BreakReplacementVisitor(), null);
    assertBlockFixture(BLOCKS, "breaksAfter", block);
  }

  @Test
  public void movesLambdaTestsIntoBlockBodiesAndAppendsParameters() throws Exception {
    BlockStmt block = blockFixture(BLOCKS, "lambdaBefore");
    block.accept(new LambdaTransformationVisitor(), null);
    assertBlockFixture(BLOCKS, "lambdaAfter", block);

    BlockStmt arguments = blockFixture(BLOCKS, "lambdaArgumentsBefore");
    arguments.accept(new LambdaTransformationVisitor(), null);
    assertBlockFixture(BLOCKS, "lambdaArgumentsAfter", arguments);
  }

  @Test
  public void leavesOrdinaryTestsAndRejectsEmptyLambdaEnd() throws Exception {
    BlockStmt ordinary = blockFixture(BLOCKS, "ordinaryEnd");
    BlockStmt original = ordinary.clone();
    ordinary.accept(new LambdaTransformationVisitor(), null);
    assertEquals(original, ordinary);

    BlockStmt invalid = blockFixture(BLOCKS, "invalidLambdaEnd");
    RuntimeException error =
        assertThrows(
            RuntimeException.class, () -> invalid.accept(new LambdaTransformationVisitor(), null));
    assertTrue(error.getMessage().contains("without arg"));
  }

  @Test
  public void convertsCommentsInConstructorsBranchesLoopsSwitchAndTryFinally() throws Exception {
    CompilationUnit unit = parseFixture("visitors/CommentConversion.java");
    unit.accept(new BlockTestConversionVisitor(), null);
    assertEquals(20, unit.findAll(MethodCallExpr.class).size());
    for (int i = 1; i <= 10; i++) {
      String call = i == 3 ? "lambdatest().args(3);" : "blocktest().given(x, " + i + ");";
      assertTrue(unit.toString(), unit.toString().contains(call));
    }
    assertTrue(unit.toString().contains("// ordinary comment"));
    CompilationUnit once = unit.clone();
    unit.accept(new BlockTestConversionVisitor(), null);
    assertEquals(once, unit);
  }

  @Test
  public void lambdaTransformationSearchesPastExpressionBodiesForABlockLambda() throws Exception {
    BlockStmt block = blockFixture(BLOCKS, "expressionLambdaBefore");
    block.accept(new LambdaTransformationVisitor(), null);
    assertBlockFixture(BLOCKS, "expressionLambdaAfter", block);

    BlockStmt noLambda = blockFixture(BLOCKS, "noLambda");
    noLambda.accept(new LambdaTransformationVisitor(), null);
    assertBlockFixture(BLOCKS, "noLambdaAfter", noLambda);

    BlockStmt nested = blockFixture(BLOCKS, "nestedLambda");
    nested.accept(new LambdaTransformationVisitor(), null);
    assertTrue(nested.toString().contains("argsVar(x)"));
  }

  @Test
  public void preservesAllStackedCommentTestsInTheirOriginalOrder() throws Exception {
    String fixture = "visitors/StackedComments.java";
    for (String first : Arrays.asList("blocktestFirst", "lambdatestFirst")) {
      BlockStmt block = blockFixture(fixture, first);
      block.accept(new BlockTestConversionVisitor(), null);
      assertEquals(3, block.getStatements().size());
      String expected =
          first.equals("blocktestFirst") ? "blocktest().given(x, 1);" : "lambdatest().args(1);";
      assertEquals(StaticJavaParser.parseStatement(expected), block.getStatement(0));
      assertEquals(
          StaticJavaParser.parseStatement("blocktest().given(x, 2);"), block.getStatement(1));
      assertEquals("x++", block.getStatement(2).asExpressionStmt().getExpression().toString());
    }

    BlockStmt ordinary = blockFixture(fixture, "ordinaryThenBlocktest");
    ordinary.accept(new BlockTestConversionVisitor(), null);
    assertEquals(2, ordinary.getStatements().size());
    assertTrue(ordinary.toString().contains("ordinary explanation"));

    BlockStmt noTest = blockFixture(fixture, "ordinaryOnly");
    noTest.accept(new BlockTestConversionVisitor(), null);
    assertEquals(1, noTest.getStatements().size());
  }
}
