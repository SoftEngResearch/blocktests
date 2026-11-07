package org.blocktest.visitors;

import static org.junit.Assert.*;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.*;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.*;
import org.blocktest.BlockTest;
import org.blocktest.TestExtractor;
import org.blocktest.support.AstTestSupport;
import org.blocktest.types.*;
import org.blocktest.utils.Util;
import org.junit.Test;

public class ExtractionVisitorTest extends AstTestSupport {
  private List<BlockTest> extract(String body) {
    List<BlockTest> out = new ArrayList<>();
    String source;
    try {
      source = fixtureText("extraction-visitor/Sample.java").replace("// TEST_BODY", body);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
    new ExtractionVisitor(out)
        .visit(StaticJavaParser.parse(source), new ExtractionVisitor.Context());
    return out;
  }

  private BlockTest one(String body) {
    List<BlockTest> out = extract(body);
    assertEquals(1, out.size());
    return out.get(0);
  }

  private List<BlockTest> extractCase(String method) throws IOException {
    CompilationUnit unit = parseFixture("extraction-visitor/Cases.java");
    for (MethodDeclaration candidate : unit.findAll(MethodDeclaration.class)) {
      if (!candidate.getNameAsString().equals(method)) candidate.remove();
    }
    List<BlockTest> tests = new ArrayList<>();
    new ExtractionVisitor(tests).visit(unit, new ExtractionVisitor.Context());
    return tests;
  }

  private BlockTest oneCase(String method) throws IOException {
    List<BlockTest> tests = extractCase(method);
    assertEquals(1, tests.size());
    return tests.get(0);
  }

  private BlockTest chain(String chain) {
    return one("blocktest(\"caseName\").given(x,1)." + chain + "; x++; return x;");
  }

  private void invalid(String chain, String message) {
    RuntimeException e = assertThrows(RuntimeException.class, () -> chain(chain));
    assertTrue(e.getMessage(), e.getMessage().contains(message));
  }

  @Test
  public void recordsInputsAssertionsSymbolsAndAssignments() throws Exception {
    BlockTest b = oneCase("recordsInputs");
    assertEquals("increment", b.testName);
    assertEquals("Sample", b.className);
    assertEquals(7, b.lineNo);
    assertEquals(Arrays.asList("y", "x"), b.givenVariables);
    assertEquals("int y = 3", b.givens.get(0).toString());
    assertEquals("assertEquals(6, x + y)", b.junitAssertions.get(0).toString());
    assertEquals(new HashSet<>(Arrays.asList("x", "y")), b.assertedVariables);
    assertTrue(b.blockVariables.containsAll(Arrays.asList("x", "y")));
    assertEquals("4", b.localAssignments.get("y").toString());
    assertEquals("int", b.localSymbolTable.get("y").asString());
    assertBlock("{ x++; use(y); }", b.statements);
  }

  @Test
  public void markersOverrideLastReferenceAndSkipOtherTests() throws Exception {
    List<BlockTest> tests = extractCase("markersOverride");
    assertEquals(2, tests.size());
    assertBlock("{ x++; use(x); }", tests.get(0).statements);
    assertBlock("{ use(x); x++; }", tests.get(1).statements);
    BlockTest reversed = oneCase("reversedMarker");
    assertBlock("{ x=1; x++; }", reversed.statements);
  }

  @Test
  public void lastReferenceInReversePreservesExecutionOrder() throws Exception {
    assertBlock("{ use(x); x++; unrelated(); }", oneCase("reverseReference").statements);
    assertBlock(
        "{ first(); second(); }",
        one("first(); second(); blocktest().given(x,1).start(LAST_REFERENCE);").statements);
    assertBlock(
        "{ first(); second(); return x; }",
        one("blocktest().given(x,1); first(); second(); return x;").statements);
    invalid("checkEq(x,2).start(LAST_REFERENCE)", "no last reference");
  }

  @Test
  public void firstAssignmentWaitsForEveryAssertedVariable() throws Exception {
    BlockTest b = oneCase("firstAssignment");
    assertBlock("{ obj.f=1; arr[0]++; x++; y=2; }", b.statements);
    b = one("x=1; x++; blocktest().given(x,0).checkEq(x,2).start(FIRST_ASSIGN);");
    assertBlock("{ x++; }", b.statements);
  }

  @Test
  public void supportsBooleanReturnArrayAndDeltaAssertions() {
    BlockTest b =
        chain("checkTrue(flag).checkFalse(!flag).checkEq(x,2,0.25).checkEq(x,new int[]{1,2})");
    assertEquals(
        Arrays.asList(
            "org.blocktest.Assertion.assertArrayEquals(new int[] { 1, 2 }, x, 0.001)",
            "assertEquals(2, x, 0.25)",
            "assertFalse(!flag)",
            "assertTrue(flag)"),
        strings(b.junitAssertions));
    for (String expression :
        Arrays.asList(
            "checkReturnEq(2)",
            "checkReturnEq(2,0.1)",
            "checkReturnTrue()",
            "checkReturnFalse()",
            "checkEq(lambdaReturn,2)",
            "checkTrue(lambdaReturn)",
            "checkFalse(lambdaReturn)")) {
      b = chain(expression);
      assertEquals(1, b.junitLambdaAssertions.size());
      assertTrue(b.junitAssertions.isEmpty());
      assertTrue(
          b.assertedVariables.contains(
              expression.contains("lambdaReturn") ? "lambdaReturn" : "methodReturn"));
    }
    b = chain("checkReturnEq(new int[]{1})");
    assertTrue(b.junitLambdaAssertions.get(0).toString().contains("assertArrayEquals"));
    b = chain("checkNotReturn()");
    assertTrue(b.notReturn);
    assertEquals(
        "org.blocktest.Assertion.assertEquals(null, methodReturn)",
        b.junitLambdaAssertions.get(0).toString());
  }

  private static List<String> strings(List<? extends Object> nodes) {
    List<String> out = new ArrayList<>();
    for (Object n : nodes) out.add(n.toString());
    return out;
  }

  @Test
  public void assertionScopesFollowCoverageAndGeneratedTestOptions() {
    Util.coverage = true;
    BlockTest b = chain("checkTrue(flag).checkFalse(flag).checkEq(x,2).checkReturnEq(2,.1)");
    for (Object assertion : b.junitAssertions)
      assertTrue(assertion.toString(), assertion.toString().startsWith("org.blocktest.Assertion."));
    assertTrue(
        b.junitLambdaAssertions
            .get(0)
            .toString()
            .startsWith("org.blocktest.Assertion.assertEquals"));
    Util.coverage = false;
    Util.generatedTests = true;
    b = chain("checkEq(x,2,.1)");
    assertEquals(
        "org.blocktest.Assertion.assertEquals(2, x, .1)", b.junitAssertions.get(0).toString());
  }

  @Test
  public void supportsQuotedFieldsRenamedExpressionsExplicitTypesAndNoInit() throws Exception {
    BlockTest b = oneCase("quotedFields");
    assertEquals(Arrays.asList("array__0", "missing", "field"), b.givenVariables);
    assertEquals(Collections.singleton("array[0]"), b.renameGivenVariables);
    assertEquals("long", b.localSymbolTable.get("missing").asString());
    assertEquals("assertEquals(2, field)", b.junitAssertions.get(0).toString());
    b = one("String text; blocktest().given(x,1).noInit(text).end(FIRST_STATEMENT); use(text);");
    assertEquals("String text = null", b.givens.get(0).toString());
  }

  @Test
  public void acceptsSetupDelayExceptionAndFlowOptions() throws Exception {
    BlockTest b = oneCase("setupAndFlow");
    assertBlock("{ x=5; }", b.setupBlock);
    assertEquals(7, b.delay);
    assertEquals("IllegalArgumentException.class", b.exception.toString());
    assertEquals(Arrays.asList(Flow.IfStmt, Flow.Then), b.flows);
    assertEquals("\"if,\"", b.controlFlow);
    b = chain("setup(() -> { x=6; }).expect(null).checkFlow(IfStmt().ElseIf().Then())");
    assertBlock("{ x=6; }", b.setupBlock);
    assertNull(b.exception);
    assertEquals(Arrays.asList(Flow.IfStmt, Flow.ElseIf, Flow.Then), b.flows);
    b = chain("end(FIRST_STATEMENT,false)");
    assertFalse(b.endAfter);
    assertEquals(1, b.endAtInstances);
    assertTrue(b.statements.isEmpty());
  }

  @Test
  public void rejectsMalformedDslWithUsefulDiagnostics() {
    invalid("end()", "without arg");
    invalid("start()", "without arg");
    invalid("checkFlow()", "more than 0");
    invalid("checkFlow(Missing)", "Invalid flow");
    invalid("checkFlow(IfStmt().Missing())", "Invalid flow");
    invalid("checkControlFlow()", "1 argument");
    invalid("checkEq(x)", "2+");
    invalid("checkReturnEq()", "1+");
    invalid("checkTrue()", "1 argument");
    invalid("checkFalse(x,1)", "1 argument");
    invalid("expect()", "1 argument");
    invalid("given(x)", "2 or 3");
    invalid("args()", "more than 0");
    invalid("args(1)", "same number");
    invalid("delay()", "1 argument");
    invalid("setup()", "1 argument");
    invalid("setup(x)", "string or lambda");
    invalid("setup(() -> work())", "string or lambda");
    invalid("noInit()", "1+");
    invalid("mock()", "at least 1");
    invalid("mockString()", "at least 1");
    assertTrue(
        assertThrows(RuntimeException.class, () -> one("blocktest(1).given(x,1);"))
            .getMessage()
            .contains("as argument"));
    assertTrue(
        assertThrows(RuntimeException.class, () -> one("blocktest(1,2,3).given(x,1);"))
            .getMessage()
            .contains("less than 3"));
    assertTrue(
        assertThrows(
                RuntimeException.class,
                () -> extract("blocktest(\"same\").given(x,1); blocktest(\"same\").given(x,2);"))
            .getMessage()
            .contains("duplicate test name"));
  }

  @Test
  public void infersUnresolvedInputTypesFromLiteralAndConstructedValues() {
    String[][] cases = {
      {"-1", "int"},
      {"1L", "long"},
      {"1.0", "double"},
      {"1.0f", "float"},
      {"1.0F", "float"},
      {"true", "boolean"},
      {"'a'", "char"},
      {"\"s\"", "java.lang.String"},
      {"new int[]{1}", "int[]"},
      {"String.class", "java.lang.Class"},
      {"(Number)1", "Number"},
      {"new Widget(1)", "Widget"},
      {"obj.getAnnotation(Marker.class)", "java.lang.annotation.Annotation"},
      {"Thing.newBuilder().value(1).build()", "Thing"},
      {"Paths.get(\"x\")", "Path"},
      {"java.nio.file.Paths.get(\"x\")", "Path"},
      {"Color.RED", "Color"},
      {"Outer.Color.RED", "Outer.Color"},
      {"new ArrayList<>(Arrays.asList(1))", "ArrayList<java.lang.Integer>"},
      {"new ArrayList<>(java.util.Arrays.asList(true))", "ArrayList<java.lang.Boolean>"}
    };
    for (String[] c : cases) {
      BlockTest b = one("blocktest().given(missing," + c[0] + ").end(FIRST_STATEMENT); work();");
      assertEquals(c[0], c[1], b.givens.get(0).type.toString());
    }
    assertThrows(
        RuntimeException.class,
        () -> one("blocktest().given(missing,unknown()).end(FIRST_STATEMENT); work();"));
  }

  @Test
  public void lambdaArgsBecomeTypedGivens() throws Exception {
    BlockTest b = oneCase("lambdaArguments");
    assertEquals(Arrays.asList("a", "b"), b.givenVariables);
    assertEquals("int a = 1", b.givens.get(0).toString());
    assertEquals("java.lang.String b = \"two\"", b.givens.get(1).toString());
    assertBlock("{ return a; }", b.statements);
  }

  @Test
  public void mocksSupportWildcardsFieldsMultipleValuesAndRemoval() throws Exception {
    BlockTest b = oneCase("mockReplacement");
    assertBlock("{ int a=1; int b=2; int c=3; int d=4; }", b.statements);
    TestExtractor.staticFields.put("VALUE", "Owner");
    b = chain("mockString(\"VALUE + 1\",9).end(FIRST_STATEMENT)");
    assertTrue(b.mockMethods.containsKey("Owner.VALUE + 1"));
    Util.loadXml = true;
    b = chain("mock(call(),1,2,\"int\").mock(other(),3).mock(empty()).end(FIRST_STATEMENT)");
    assertEquals(Arrays.asList("1", "2"), strings(b.mockMethods.get("call()")));
    assertEquals(Collections.singletonList("3"), strings(b.mockMethods.get("other()")));
    assertTrue(b.mockMethods.get("empty()").isEmpty());
  }

  @Test
  public void serializedInputsSupportXmlTextAndJavaExpressions() throws Exception {
    source(".blocktests/serialized-data/value.txt", "hello world");
    source(".blocktests/serialized-data/value.java", "new int[]{1,2}");
    source(".blocktests/serialized-data/flow.txt", "if,else,");
    Path app = sourceFixture("extraction/empty/Sample.java", "src/main/java/Sample.java");
    Util.appSrcPath = app.toString();
    Util.loadXml = true;
    BlockTest b = oneCase("serializedInputs");
    assertEquals("\"if,else,\"", b.controlFlow);
    assertTrue(b.givens.get(0).value.toString().contains("BTest.xstream.fromXML"));
    assertEquals("new int[] { 1, 2 }", b.givens.get(1).value.toString());
    assertEquals("\"hello world\"", b.givens.get(2).value.toString());
    assertEquals("assertEquals(\"hello world\", x)", b.junitAssertions.get(0).toString());
    assertTrue(b.junitLambdaAssertions.get(0).toString().contains("assertArrayEquals"));
    b = oneCase("serializedComparisons");
    assertTrue(b.junitAssertions.get(0).toString().contains("assertArrayEquals"));
    assertEquals(
        "assertEquals(\"hello world\", methodReturn)", b.junitLambdaAssertions.get(0).toString());
    assertEquals("\"hello world\"", b.mockMethods.get("call()").get(0).toString());
    assertEquals("new int[] { 1, 2 }", b.mockMethods.get("other()").get(0).toString());
  }

  @Test
  public void tracksScopesConstructorAndLoopParametersWithoutLosingInitializers() throws Exception {
    CompilationUnit cu = parseFixture("extraction-visitor/Scopes.java");
    List<BlockTest> out = new ArrayList<>();
    ExtractionVisitor v = new ExtractionVisitor(out);
    v.visit(cu, new ExtractionVisitor.Context());
    assertEquals(2, out.size());
    assertEquals("int", out.get(0).localSymbolTable.get("x").asString());
    assertEquals("int", out.get(0).localSymbolTable.get("i").asString());
    assertEquals("java.lang.String", out.get(1).localSymbolTable.get("s").asString());
    assertEquals(2, v.globalSymbolTable.get("x").size());
    assertEquals(1, v.globalSymbolTable.get("p").size());
    ExtractionVisitor.Context ctx = new ExtractionVisitor.Context();
    ctx.symbolTable.put("x", PrimitiveType.intType());
    ctx.assignments.put("x", new IntegerLiteralExpr(1));
    ExtractionVisitor.Context copy = ctx.clone();
    copy.symbolTable.clear();
    copy.assignments.clear();
    assertEquals(1, ctx.symbolTable.size());
    assertEquals(1, ctx.assignments.size());
  }

  @Test
  public void rejectsMalformedConfigAndParameterizedDeclarations() {
    String[] invalidConfigs = {
      "configParam()",
      "configParam(\"cases\", \"int\")",
      "configParam(label, \"int\", new Object[][] {{1}})",
      "configParam(\"cases\", types, new Object[][] {{1}})",
      "configParam(\"cases\", \"int\", new Object[1][1])",
      "configParam(\"cases\", \"int\", new Object[][] {1})",
      "configParam(\"cases\", \"int\", new Object[][] {{1, 2}})"
    };
    for (String config : invalidConfigs) {
      assertThrows(config, RuntimeException.class, () -> one("blocktest()." + config + ";"));
    }

    String[] invalidParameterized = {
      "parameterized()",
      "parameterized(new Object[][] {{1}}, \"int int\", x)",
      "parameterized(new Object[1][1], \"int\", x)",
      "parameterized(new Object[][] {1}, \"int\", x)",
      "parameterized(new Object[][] {{1, 2}}, \"int\", x)",
      "parameterized(new Object[][] {{1}}, \"int\", x + 1)",
      "parameterized(\"cases\", x + 1)"
    };
    for (String parameterized : invalidParameterized) {
      assertThrows(
          parameterized, RuntimeException.class, () -> one("blocktest()." + parameterized + ";"));
    }
  }

  @Test
  public void parsesAssumptionsAndParameterizedNamesAsExpressionsOrStrings() {
    BlockTest assumptions =
        chain("assume(x > 0).assume(\"x < 5\").end(FIRST_STATEMENT)");
    assertEquals(
        Arrays.asList("x < 5", "x > 0"), strings(assumptions.assumptions));

    BlockTest parameterized =
        chain("parameterized(new Object[][] {{1}}, \"int\", \"x\").end(FIRST_STATEMENT)");
    assertEquals(Collections.singletonList("x"), parameterized.parameterizedGivenVariables);

    BlockTest configs =
        one(
            "blocktest().configParam(\"first\", \"int\", new Object[][] {{1}})"
                + ".configParam(\"second\", \"String\", new Object[][] {{\"value\"}});");
    assertEquals(new HashSet<>(Arrays.asList("first", "second")), configs.configs.keySet());
  }

  @Test
  public void acceptsParameterizedTypeExpressionsAndStringGivenNames() {
    BlockTest typed =
        chain("parameterized(new Object[][] {{1}}, Integer, x).end(FIRST_STATEMENT)");
    assertEquals("Integer", typed.parameterizedGivens.get(0).type.asString());

    BlockTest configured =
        chain("parameterized(\"cases\", \"x\").end(FIRST_STATEMENT)");
    assertEquals(Collections.singletonList("x"), configured.parameterizedGivenVariables);

    invalid(
        "parameterized(123, x)",
        "should have parameters, types, and variable names OR label and variable names");
  }

  @Test
  public void parsesThreeArgumentLambdaInputsWithAnExplicitType() {
    BlockTest block =
        one(
            "consume((int a, int b, int c) -> { "
                + "lambdatest().args(1, 2, \"int\").argsVar(a, b, c).end(FIRST_STATEMENT); "
                + "return a; });");
    assertEquals(Arrays.asList("a", "b", "c"), block.givenVariables);
    for (GivenVariable given : block.givens) {
      assertEquals("int", given.type.asString());
    }
  }

  @Test
  public void reportsMalformedAssumptionCallsAndStringConditions() {
    invalid("assume()", "assume should have 1 argument");
    invalid("assume(1,2)", "assume should have 1 argument");
    RuntimeException malformed =
        assertThrows(RuntimeException.class, () -> chain("assume(\"x >\")"));
    assertTrue(malformed.getMessage().contains("could not parse condition"));
  }
}
