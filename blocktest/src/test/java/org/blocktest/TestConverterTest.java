package org.blocktest;

import static org.junit.Assert.*;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.*;
import java.nio.file.Path;
import java.util.*;
import org.blocktest.support.*;
import org.blocktest.types.*;
import org.blocktest.utils.Util;
import org.junit.Test;
import org.junit.runner.Result;

public class TestConverterTest extends AstTestSupport {
  private static final String CASES = "converter/ConverterCases.java";

  private BlockTest block(String body) {
    BlockTest b = new BlockTest();
    b.testName = "generated";
    b.statements = StaticJavaParser.parseBlock(body);
    return b;
  }

  private BlockTest blockCase(String method) throws Exception {
    BlockTest block = new BlockTest();
    block.testName = "generated";
    block.statements = blockFixture(CASES, method);
    return block;
  }

  private TestConverter converter() {
    return new TestConverter(new HashMap<>());
  }

  private void given(BlockTest b, String name, String type, String value) {
    b.givens.add(
        new GivenVariable(
            StaticJavaParser.parseType(type),
            name,
            new NameExpr(name),
            StaticJavaParser.parseExpression(value)));
    b.givenVariables.add(name);
  }

  private MethodDeclaration junit(BlockTest b) {
    return converter().toJUnit(b, false);
  }

  private Result run(MethodDeclaration method) throws Exception {
    String sourceText =
        fixtureText("generated/Generated.java")
            .replace("// GENERATED_TEST_METHOD", method.toString());
    Path file = source("Generated.java", sourceText);
    return GeneratedCode.run(temporary.getRoot().toPath(), "Generated", file);
  }

  @Test
  public void convertsGivensSetupTargetAndAssertionsInExecutionOrder() throws Exception {
    BlockTest b = blockCase("targetWithGivens");
    given(b, "y", "int", "2");
    given(b, "x", "int", "1");
    b.setupBlock = blockFixture(CASES, "setupWithGivens");
    b.junitAssertions.add(StaticJavaParser.parseExpression("assertEquals(4,x)"));
    MethodDeclaration m = junit(b);
    assertTrue(m.isPublic());
    assertTrue(m.getType().isVoidType());
    assertEquals("generated", m.getNameAsString());
    assertTrue(m.getAnnotationByName("Test").isPresent());
    assertEquals("Exception", m.getThrownException(0).asString());
    assertBlockFixture(CASES, "expectedWithGivens", m.getBody().get());
    Result result = run(m);
    assertEquals(1, result.getRunCount());
    assertTrue(result.getFailures().toString(), result.wasSuccessful());
  }

  @Test
  public void declaresMissingLocalsUsesGlobalTypesAndFallsBackToObject() throws Exception {
    BlockTest b = blockCase("missingLocals");
    b.localSymbolTable.put("x", PrimitiveType.intType());
    b.localAssignments.put("x", new IntegerLiteralExpr(9));
    b.localSymbolTable.put("unknown", new UnknownType());
    HashMap<String, HashSet<Type>> globals = new HashMap<>();
    globals.put("global", new HashSet<>(Arrays.asList(PrimitiveType.longType())));
    MethodDeclaration m = new TestConverter(globals).toJUnit(b, false);
    String text = m.toString();
    assertTrue(text, text.contains("int x;"));
    assertTrue(text.contains("long global;"));
    assertTrue(text.contains("java.lang.Object missing;"));
    assertTrue(text.contains("java.lang.Object unknown;"));
    assertFalse(text.contains("int declared;"));
    Util.predictValue = true;
    b = block("{ use(x); }");
    b.localSymbolTable.put("x", PrimitiveType.intType());
    b.localAssignments.put("x", new IntegerLiteralExpr(9));
    b.blockVariables.add("x");
    assertBlock("{ int x=9; use(x); }", junit(b).getBody().get());
  }

  @Test
  public void movesOnlyUninitializedTopLevelDeclarations() throws Exception {
    BlockTest b = blockCase("topLevelDeclarations");
    assertBlockFixture(CASES, "expectedTopLevelDeclarations", junit(b).getBody().get());
  }

  @Test
  public void earlyReturnsExecuteAssertionsInFinallyAndPreserveReturnExpressionEffects()
      throws Exception {
    BlockTest b = blockCase("returnExpressionEffects");
    given(b, "x", "int", "1");
    b.junitAssertions.add(StaticJavaParser.parseExpression("assertEquals(2,x)"));
    MethodDeclaration m = junit(b);
    assertEquals(1, m.findAll(TryStmt.class).size());
    assertTrue(m.toString().contains("Object RETURN_STATEMENT_1 = ++x;"));
    Result result = run(m);
    assertTrue(result.getFailures().toString(), result.wasSuccessful());
  }

  @Test
  public void replacesReturnPlaceholdersAndDetectsWrongOrMissingReturns() throws Exception {
    for (String expression : Arrays.asList("methodReturn", "lambdaReturn")) {
      BlockTest b = block("{ return 2; }");
      b.junitLambdaAssertions.add(
          StaticJavaParser.parseExpression("assertEquals(2," + expression + ")"));
      MethodDeclaration m = junit(b);
      assertTrue(m.toString().contains("returnStatementReached = true;"));
      assertFalse(m.toString().contains(expression));
      assertTrue(run(m).wasSuccessful());
    }
    BlockTest wrong = block("{ return 3; }");
    wrong.junitLambdaAssertions.add(
        StaticJavaParser.parseExpression("assertEquals(2,methodReturn)"));
    assertFalse(run(junit(wrong)).wasSuccessful());
    BlockTest missing = block("{ }");
    missing.junitLambdaAssertions.add(
        StaticJavaParser.parseExpression("assertEquals(2,methodReturn)"));
    assertFalse(run(junit(missing)).wasSuccessful());
  }

  @Test
  public void checkNotReturnPassesOnlyWhenNoValueIsReturned() throws Exception {
    for (boolean returns : new boolean[] {false, true}) {
      BlockTest b = block(returns ? "{ return null; }" : "{ }");
      b.notReturn = true;
      b.junitLambdaAssertions.add(
          StaticJavaParser.parseExpression(
              "org.blocktest.Assertion.assertEquals(null,methodReturn)"));
      assertEquals(!returns, run(junit(b)).wasSuccessful());
    }
  }

  @Test
  public void supportsJUnit4TestngAndJUnit5ExpectedExceptions() throws Exception {
    for (String version : Arrays.asList("junit4", "testng", "junit5")) {
      Util.junitVersion = version;
      BlockTest b = block("{ throw new IllegalArgumentException(); }");
      b.exception = StaticJavaParser.parseExpression("IllegalArgumentException.class");
      MethodDeclaration m = junit(b);
      if (version.equals("junit5"))
        assertTrue(m.toString().contains("assertThrows(IllegalArgumentException.class, () ->"));
      else
        assertEquals(
            "IllegalArgumentException.class",
            m.getAnnotation(0).asNormalAnnotationExpr().getPairs().get(0).getValue().toString());
      if (version.equals("junit4")) assertTrue(run(m).wasSuccessful());
      if (version.equals("testng"))
        assertEquals(
            "expectedExceptions",
            m.getAnnotation(0).asNormalAnnotationExpr().getPairs().get(0).getNameAsString());
    }
  }

  @Test
  public void flowAndControlFlowAssertionsExecuteAndRejectWrongPaths() throws Exception {
    for (boolean correct : new boolean[] {true, false}) {
      BlockTest b = blockCase("flowBranches");
      b.flows.addAll(Arrays.asList(Flow.IfStmt, correct ? Flow.Then : Flow.Else));
      assertEquals(correct, run(junit(b)).wasSuccessful());
      b = block("{ if (true) { return; } }");
      b.controlFlow = correct ? "\"if,\"" : "\"else,\"";
      MethodDeclaration m = junit(b);
      assertTrue(m.toString().contains("BLOCKGEN_FLOW_INTERNAL"));
      assertEquals(correct, run(m).wasSuccessful());
    }
  }

  @Test
  public void delaysAndRenamesAssertionExpressions() {
    BlockTest b = block("{}");
    b.delay = 1;
    b.renameGivenVariables.add("obj.value");
    b.junitAssertions.add(StaticJavaParser.parseExpression("assertEquals(1,obj.value)"));
    assertBlock(
        "{ java.lang.Thread.sleep(1); assertEquals(1,obj__value); }", junit(b).getBody().get());
  }

  @Test
  public void coverageWrapperAndSourceMethodsSeparateExecutionFromAssertions() throws Exception {
    BlockTest b = blockCase("coverageTarget");
    given(b, "x", "int", "1");
    b.junitAssertions.add(StaticJavaParser.parseExpression("assertEquals(1,x)"));
    assertBlock("{ src_generated_src(); }", converter().toJUnit(b.clone(), true).getBody().get());
    MethodDeclaration src = converter().toSourceCode(b);
    assertTrue(src.isStatic());
    assertTrue(src.isPublic());
    assertEquals("src_generated_src", src.getNameAsString());
    assertTrue(src.getAnnotations().isEmpty());
    assertFalse(src.toString().contains("assertEquals"));
    assertEquals(1, src.findAll(TryStmt.class).size());
    assertTrue(src.findFirst(TryStmt.class).get().getFinallyBlock().get().isEmpty());
    b = block("{ work(); }");
    assertBlock("{ work(); }", converter().toSourceCode(b).getBody().get());
  }

  @Test
  public void bareReturnWithValueAssertionsFailsAndSourceCoverageSkipsInstrumentation()
      throws Exception {
    BlockTest b = block("{ return; }");
    b.junitLambdaAssertions.add(StaticJavaParser.parseExpression("assertEquals(1,methodReturn)"));
    assertFalse(run(junit(b)).wasSuccessful());
    b = block("{ if(true) { return 1; } }");
    b.controlFlow = "\"if,\"";
    b.flows.addAll(Arrays.asList(Flow.IfStmt, Flow.Then));
    b.junitLambdaAssertions.add(StaticJavaParser.parseExpression("assertEquals(1,methodReturn)"));
    assertBlock("{ src_generated_src(); }", converter().toJUnit(b.clone(), true).getBody().get());
    String source = converter().toSourceCode(b).toString();
    assertFalse(source.contains("_blocktest_flow"));
    assertFalse(source.contains("BLOCKGEN_FLOW_INTERNAL"));
    assertFalse(source.contains("assertEquals"));
  }

  private BlockTest parameterizedBlock(String values, boolean withException) {
    BlockTest b = block("{ use(n); }");
    b.testName = "numbers";
    b.is_parameterized = true;
    b.parameterValues = StaticJavaParser.parseExpression(values);
    b.parameterizedGivens.add(
        new GivenParameterizedVariable(PrimitiveType.intType(), "n", new NameExpr("n"), 0));
    if (withException) {
      b.exception = StaticJavaParser.parseExpression("IllegalArgumentException.class");
    }
    return b;
  }

  @Test
  public void generatesJUnit5ParameterizedSourcesForRowsAndEmptyData() {
    Util.junitVersion = "junit5";
    BlockTest b = parameterizedBlock("new Object[][] {{1}, {2}}", false);
    List<com.github.javaparser.ast.body.BodyDeclaration<?>> declarations =
        converter().toParamJUnit(b, false);
    assertEquals(1, declarations.size());
    MethodDeclaration source = (MethodDeclaration) declarations.get(0);
    assertEquals("blockTestParamSourcenumbers", source.getNameAsString());
    assertTrue(source.isStatic());
    assertEquals("Stream<Arguments>", source.getType().toString());
    assertTrue(source.toString().contains("Stream.of(Arguments.of(1), Arguments.of(2))"));

    b.parameterValues = StaticJavaParser.parseExpression("new Object[][] {}");
    source = (MethodDeclaration) converter().toParamJUnit(b, false).get(0);
    assertTrue(source.toString().contains("Stream.empty()"));
  }

  @Test
  public void junit5ParameterizedAnnotationsAndExpectedExceptionParametersAreGenerated() {
    Util.junitVersion = "junit5";
    BlockTest b = parameterizedBlock("new Object[][] {{1}}", false);
    MethodDeclaration test = junit(b);
    assertTrue(test.getAnnotationByName("ParameterizedTest").isPresent());
    assertTrue(test.toString().contains("@MethodSource(\"blockTestParamSourcenumbers\")"));
    assertEquals("int n", test.getParameter(0).toString());

    b = parameterizedBlock("new Object[][] {{1}}", true);
    test = junit(b);
    assertTrue(test.toString().contains("nBlockTestParam"));
    assertEquals("nBlockTestParam", test.getParameter(0).getNameAsString());
  }

  @Test
  public void rejectsIncompleteOrMalformedParameterizedJUnit5Data() {
    Util.junitVersion = "junit5";
    BlockTest missingValues = parameterizedBlock("new Object[][] {{1}}", false);
    missingValues.parameterizedGivens.clear();
    assertTrue(
        assertThrows(RuntimeException.class, () -> converter().toParamJUnit(missingValues, false))
            .getMessage()
            .contains("missing variables"));

    BlockTest notArray = parameterizedBlock("1", false);
    assertTrue(
        assertThrows(RuntimeException.class, () -> converter().toParamJUnit(notArray, false))
            .getMessage()
            .contains("initialized array"));

    BlockTest uninitializedArray = parameterizedBlock("new Object[1][]", false);
    assertTrue(
        assertThrows(
                RuntimeException.class, () -> converter().toParamJUnit(uninitializedArray, false))
            .getMessage()
            .contains("initialized array"));

    BlockTest nonArrayRow = parameterizedBlock("new Object[] {1}", false);
    assertTrue(
        assertThrows(RuntimeException.class, () -> converter().toParamJUnit(nonArrayRow, false))
            .getMessage()
            .contains("rows must be arrays"));

    BlockTest uninitializedArrayRow =
        parameterizedBlock("new Object[][] {new int[1]}", false);
    assertTrue(
        assertThrows(
                RuntimeException.class,
                () -> converter().toParamJUnit(uninitializedArrayRow, false))
            .getMessage()
            .contains("rows must be arrays"));

    BlockTest missingGivens = parameterizedBlock("new Object[][] {{1}}", false);
    missingGivens.parameterizedGivens = null;
    assertThrows(RuntimeException.class, () -> converter().toParamJUnit(missingGivens, false));

    BlockTest missingParameterValues = parameterizedBlock("new Object[][] {{1}}", false);
    missingParameterValues.parameterValues = null;
    assertThrows(
        RuntimeException.class, () -> converter().toParamJUnit(missingParameterValues, false));
  }

  @Test
  public void supportsCreatedAndNestedArrayRowsAndFallbackParameterSourceNames() {
    Util.junitVersion = "junit5";
    BlockTest createdArray = parameterizedBlock("new Object[][] {new int[] {1}}", false);
    MethodDeclaration source =
        (MethodDeclaration) converter().toParamJUnit(createdArray, false).get(0);
    assertTrue(source.toString().contains("Arguments.of(1)"));

    BlockTest nestedArray = parameterizedBlock("new Object[][] {{{1, 2}}}", false);
    source = (MethodDeclaration) converter().toParamJUnit(nestedArray, false).get(0);
    assertTrue(source.toString().contains("new Object[] { 1, 2 }"));

    BlockTest unnamed = parameterizedBlock("new Object[][] {{1}}", false);
    unnamed.testName = null;
    source = (MethodDeclaration) converter().toParamJUnit(unnamed, false).get(0);
    assertEquals("blockTestParamSourceTestLine0", source.getNameAsString());
    unnamed.testName = "";
    source = (MethodDeclaration) converter().toParamJUnit(unnamed, false).get(0);
    assertEquals("blockTestParamSourceTestLine0", source.getNameAsString());
  }

  @Test
  public void unsupportedFrameworkDoesNotGenerateParameterizedDeclarations() {
    Util.junitVersion = "testng";
    BlockTest block = parameterizedBlock("new Object[][] {{1}}", false);
    assertTrue(converter().toParamJUnit(block, false).isEmpty());
  }

  @Test
  public void emitsAssumptionsBeforeTheTargetAndRenamesTheirExpressions() {
    BlockTest block = block("{ use(x); }");
    block.assumptions.add(StaticJavaParser.parseExpression("obj.value > 0"));
    block.renameGivenVariables.add("obj.value");

    MethodDeclaration converted = junit(block);
    assertEquals(
        "assumeTrue(obj__value > 0);",
        converted.getBody().get().getStatement(0).toString());
    assertEquals("use(x);", converted.getBody().get().getStatement(1).toString());
  }
}
