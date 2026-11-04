package org.blocktest.utils;

import static org.junit.Assert.*;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import java.nio.file.Path;
import java.util.*;
import org.blocktest.support.AstTestSupport;
import org.blocktest.support.GeneratedCode;
import org.junit.Test;
import org.junit.runner.Result;

public class MockUtilTest extends AstTestSupport {
  private static final String FIXTURE = "utils/UtilCases.java";

  private BlockStmt mockBlock(String method, String expression) throws Exception {
    return StaticJavaParser.parseBlock(
        blockFixture(FIXTURE, method).toString().replace("TEST_EXPRESSION", expression));
  }

  @Test
  public void generatesAllArgumentWildcardCombinationsInStableOrder() {
    MethodCallPatternGenerator g = new MethodCallPatternGenerator();
    assertEquals(
        Arrays.asList(
            "a.b(1, 2).c(3).d()",
            "a.b(1, 2).c(BLOCKTEST_ANYTHING).d()",
            "a.b(BLOCKTEST_ANYTHING).c(3).d()",
            "a.b(BLOCKTEST_ANYTHING).c(BLOCKTEST_ANYTHING).d()"),
        g.generateAllPatterns(
            StaticJavaParser.parseExpression("a.b(1,2).c(3).d()").asMethodCallExpr()));
    assertEquals(
        Collections.singletonList("run()"),
        g.generateAllPatterns(StaticJavaParser.parseExpression("run()").asMethodCallExpr()));
    assertEquals(
        Arrays.asList("run(1)", "run(BLOCKTEST_ANYTHING)"),
        g.generateAllPatterns(StaticJavaParser.parseExpression("run(1)").asMethodCallExpr()));
  }

  private static Map<String, List<Expression>> mocks(String key, Expression... values) {
    Map<String, List<Expression>> map = new HashMap<>();
    map.put(key, Arrays.asList(values));
    return map;
  }

  @Test
  public void substitutesCallsNamesFieldsAndExpressionsSequentiallyThenRepeatsLastValue()
      throws Exception {
    for (String expression : Arrays.asList("call()", "value", "obj.field", "x + y")) {
      BlockStmt b = mockBlock("repeatedMock", expression);
      assertSame(
          b,
          MockUtil.substituteMockedValues(
              b, mocks(expression, new IntegerLiteralExpr(10), new IntegerLiteralExpr(20))));
      assertBlock("{ int a = 10; int b = 20; int c = 20; untouched(); }", b);
    }
  }

  @Test
  public void emptyAndNullMocksRemoveContainingStatements() throws Exception {
    for (String expression : Arrays.asList("call()", "value", "obj.field", "x + y")) {
      for (List<Expression> values : Arrays.asList(Collections.<Expression>emptyList(), null)) {
        BlockStmt b = mockBlock("removedMock", expression);
        Map<String, List<Expression>> map = new HashMap<>();
        map.put(expression, values);
        MockUtil.substituteMockedValues(b, map);
        assertBlock("{ untouched(); }", b);
      }
    }
  }

  @Test
  public void wildcardMocksReplaceWholeChainsAndVisitCallArguments() throws Exception {
    BlockStmt b = blockFixture(FIXTURE, "wildcardMock");
    Map<String, List<Expression>> map =
        mocks("obj.a(BLOCKTEST_ANYTHING).b(2)", new IntegerLiteralExpr(9));
    map.put("other(BLOCKTEST_ANYTHING)", Arrays.asList(new IntegerLiteralExpr(8)));
    MockUtil.substituteMockedValues(b, map);
    assertBlock("{ int n = 9; consume(8); }", b);
    assertSame(b, MockUtil.substituteMockedValues(b, null));
    assertSame(b, MockUtil.substituteMockedValues(b, Collections.emptyMap()));
  }

  @Test
  public void advancedMocksGenerateTypedHelpersAndCheckArgumentsAtRuntime() throws Exception {
    CompilationUnit unit = StaticJavaParser.parse(fixtureText("utils/AdvancedMockGenerated.java"));
    MethodDeclaration sequential = unit.getClassByName("AdvancedMockGenerated").get()
        .getMethodsByName("sequential").get(0);
    Map<String, List<Expression>> sequentialMocks = mocks("foo(1)",
        new StringLiteralExpr("A"), new StringLiteralExpr("BB"));
    BlockStmt body = sequential.getBody().get();
    assertSame(body, MockUtil.substituteMockedValuesAdvanced(body, sequentialMocks));
    assertTrue(unit.toString().contains("java.util.Objects.deepEquals(args[0], 1)"));
    assertTrue(unit.toString().contains("private static java.lang.String sequential_mock0(Object... args)"));
    assertTrue(unit.toString().contains("private static int[] sequential_mock0_count"));
    assertFalse(body.toString().contains("BLOCKTEST_MOCK_COUNTS"));
    assertFalse(body.toString().contains("foo(j)"));

    MethodDeclaration wildcard = unit.getClassByName("AdvancedMockGenerated").get()
        .getMethodsByName("wildcard").get(0);
    Map<String, List<Expression>> wildcardMocks = new LinkedHashMap<>();
    wildcardMocks.put("foo(..)", Arrays.asList(new StringLiteralExpr("any")));
    wildcardMocks.put("foo(1)", Arrays.asList(new StringLiteralExpr("exact")));
    MockUtil.substituteMockedValuesAdvanced(wildcard.getBody().get(), wildcardMocks);
    String wildcardHelper = unit.getClassByName("AdvancedMockGenerated").get()
        .getMethodsByName("wildcard_mock0").get(0).toString();
    assertTrue(wildcardHelper,
        wildcardHelper.indexOf("deepEquals(args[0], 1)") < wildcardHelper.indexOf("if (true)"));

    MethodDeclaration booleanPriority = unit.getClassByName("AdvancedMockGenerated").get()
        .getMethodsByName("booleanPriority").get(0);
    Map<String, List<Expression>> booleanMocks = new LinkedHashMap<>();
    booleanMocks.put("flag(..)", Arrays.asList(new StringLiteralExpr("any")));
    booleanMocks.put("flag(true)", Arrays.asList(new StringLiteralExpr("exact")));
    MockUtil.substituteMockedValuesAdvanced(booleanPriority.getBody().get(), booleanMocks);
    String booleanHelper = unit.getClassByName("AdvancedMockGenerated").get()
        .getMethodsByName("booleanPriority_mock0").get(0).toString();
    assertTrue(booleanHelper,
        booleanHelper.indexOf("deepEquals(args[0], true)") < booleanHelper.indexOf("if (true)"));

    MethodDeclaration separate = unit.getClassByName("AdvancedMockGenerated").get()
        .getMethodsByName("separateArgumentCounts").get(0);
    Map<String, List<Expression>> separateMocks = new LinkedHashMap<>();
    separateMocks.put("word(\"foo\")", Arrays.asList(new StringLiteralExpr("A")));
    separateMocks.put("word(\"baz\")", Arrays.asList(new StringLiteralExpr("C")));
    separateMocks.put("word(\"bar\")", Arrays.asList(new StringLiteralExpr("B")));
    MockUtil.substituteMockedValuesAdvanced(separate.getBody().get(), separateMocks);
    assertTrue(unit.toString().contains("private static int[] separateArgumentCounts_mock0_count = new int[3]"));

    MethodDeclaration nullValue = unit.getClassByName("AdvancedMockGenerated").get()
        .getMethodsByName("nullValue").get(0);
    MockUtil.substituteMockedValuesAdvanced(nullValue.getBody().get(),
        mocks("foo(1)", new NullLiteralExpr()));

    MethodDeclaration multiple = unit.getClassByName("AdvancedMockGenerated").get()
        .getMethodsByName("multiArgumentWildcard").get(0);
    MockUtil.substituteMockedValuesAdvanced(multiple.getBody().get(),
        mocks("combine(BLOCKTEST_ANYTHING)", new StringLiteralExpr("any")));

    MethodDeclaration chain = unit.getClassByName("AdvancedMockGenerated").get()
        .getMethodsByName("wholeCallChain").get(0);
    MockUtil.substituteMockedValuesAdvanced(chain.getBody().get(),
        mocks("obj.a(1).b(BLOCKTEST_ANYTHING)", new StringLiteralExpr("mapped")));

    MethodDeclaration nested = unit.getClassByName("AdvancedMockGenerated").get()
        .getMethodsByName("nestedArgument").get(0);
    Map<String, List<Expression>> nestedMocks = mocks("outer(1)", new StringLiteralExpr("yes"));
    nestedMocks.put("inner()", Arrays.asList(new IntegerLiteralExpr(1)));
    MockUtil.substituteMockedValuesAdvanced(nested.getBody().get(), nestedMocks);
    assertFalse(nested.getBody().get().toString().contains("outer(inner())"));

    Path generated = source("AdvancedMockGenerated.java", unit.toString());
    Result result = GeneratedCode.run(temporary.getRoot().toPath(),
        "AdvancedMockGenerated", generated);
    assertEquals(8, result.getRunCount());
    assertTrue(result.getFailures().toString(), result.wasSuccessful());
    BlockStmt empty = new BlockStmt();
    assertSame(empty, MockUtil.substituteMockedValuesAdvanced(empty, null));
    assertTrue(assertThrows(IllegalArgumentException.class,
        () -> MockUtil.substituteMockedValuesAdvanced(
            StaticJavaParser.parseBlock("{ foo(1); }"),
            mocks("foo(1)", new IntegerLiteralExpr(7))))
        .getMessage().contains("Detached blocks"));
  }

  @Test
  public void advancedMocksInferReturnTypesWithoutAContainingClass() {
    String[][] cases = {
        {"String value = missing();", "null", "String"},
        {"consume(missing());", "\"text\"", "java.lang.String"},
        {"consume(missing());", "1", "java.lang.Integer"},
        {"consume(missing());", "1L", "java.lang.Long"},
        {"consume(missing());", "1.5", "java.lang.Double"},
        {"consume(missing());", "true", "java.lang.Boolean"},
        {"consume(missing());", "'x'", "java.lang.Character"},
        {"consume(missing());", "(Number) 1", "Number"},
        {"consume(missing());", "new StringBuilder()", "StringBuilder"},
        {"consume(missing());", "null", "java.lang.Object"}
    };
    for (String[] testCase : cases) {
      BlockStmt block = StaticJavaParser.parseBlock("{ " + testCase[0] + " }");
      List<MethodDeclaration> helpers = MockUtil.substituteMockedValuesAdvanced(block,
          mocks("missing()", StaticJavaParser.parseExpression(testCase[1])), "generated");
      assertEquals(1, helpers.size());
      assertTrue(helpers.get(0).toString(), helpers.get(0).toString()
          .contains(testCase[2] + " generated_mock0("));
    }

    String[][] primitives = {
        {"boolean", "Boolean"}, {"byte", "Byte"}, {"short", "Short"},
        {"char", "Character"}, {"int", "Integer"}, {"long", "Long"},
        {"float", "Float"}, {"double", "Double"}
    };
    for (String[] primitive : primitives) {
      BlockStmt block = StaticJavaParser.parseBlock(
          "{ " + primitive[0] + " value = missing(); }");
      List<MethodDeclaration> helpers = MockUtil.substituteMockedValuesAdvanced(
          block, mocks("missing()", new NullLiteralExpr()), "generated");
      assertTrue(helpers.get(0).toString(), helpers.get(0).toString().contains(
          "java.lang." + primitive[1] + " generated_mock0("));
    }

    for (List<Expression> values : Arrays.asList(Collections.<Expression>emptyList(), null)) {
      BlockStmt block = StaticJavaParser.parseBlock("{ consume(missing()); }");
      Map<String, List<Expression>> map = new HashMap<>();
      map.put("missing()", values);
      List<MethodDeclaration> helpers = MockUtil.substituteMockedValuesAdvanced(
          block, map, "generated");
      assertTrue(helpers.get(0).toString(), helpers.get(0).toString().contains("return null;"));
    }
  }

  @Test
  public void advancedMocksKeepLegacyNonCallsAndRespectPatternBoundaries() {
    BlockStmt block = StaticJavaParser.parseBlock("{ int n = value; }");
    assertTrue(MockUtil.substituteMockedValuesAdvanced(
        block, mocks("value", new IntegerLiteralExpr(7)), "generated").isEmpty());
    assertBlock("{ int n = 7; }", block);

    block = StaticJavaParser.parseBlock("{ foo(j); }");
    String original = block.toString();
    assertTrue(MockUtil.substituteMockedValuesAdvanced(
        block, mocks("bar(1)", new IntegerLiteralExpr(7)), "generated").isEmpty());
    assertEquals(original, block.toString());
    assertTrue(MockUtil.substituteMockedValuesAdvanced(
        block, Collections.emptyMap(), "generated").isEmpty());
    List<com.github.javaparser.ast.body.FieldDeclaration> emptyFields = new ArrayList<>();
    assertTrue(MockUtil.substituteMockedValuesAdvanced(
        block, null, "generated", emptyFields).isEmpty());
    assertTrue(emptyFields.isEmpty());

    block = StaticJavaParser.parseBlock("{ int n = 1; }");
    String unchanged = block.toString();
    assertTrue(MockUtil.substituteMockedValuesAdvanced(
        block, mocks("foo(", new IntegerLiteralExpr(7)), "generated").isEmpty());
    assertEquals(unchanged, block.toString());

    for (String pattern : Arrays.asList("foo(1, 2)", "obj.foo(1)", "factory().foo(1)")) {
      block = StaticJavaParser.parseBlock("{ foo(j); }");
      MockUtil.substituteMockedValuesAdvanced(block,
          mocks(pattern, new IntegerLiteralExpr(7)), "generated");
      assertBlock("{ foo(j); }", block);
    }

    for (String[] mismatch : new String[][] {
        {"obj.a(1).b(2)", "obj.c(j).b(k)"},
        {"obj.a(1).b(2)", "obj.b(k)"},
        {"other.b(2)", "obj.b(k)"}}) {
      block = StaticJavaParser.parseBlock("{ " + mismatch[1] + "; }");
      String before = block.toString();
      assertTrue(MockUtil.substituteMockedValuesAdvanced(block,
          mocks(mismatch[0], new IntegerLiteralExpr(7)), "generated").isEmpty());
      assertEquals(before, block.toString());
    }

    block = StaticJavaParser.parseBlock("{ foo(); }");
    MockUtil.substituteMockedValuesAdvanced(block,
        mocks("foo(..)", new IntegerLiteralExpr(7)), "generated");
    assertTrue(block.toString(), block.toString().contains("generated_mock0("));

    block = StaticJavaParser.parseBlock("{ foo(j, k); }");
    List<com.github.javaparser.ast.body.FieldDeclaration> countFields = new ArrayList<>();
    List<MethodDeclaration> helpers = MockUtil.substituteMockedValuesAdvanced(block,
        mocks("foo(1, BLOCKTEST_ANYTHING)", new IntegerLiteralExpr(7)), "generated",
        countFields);
    assertFalse(block.toString(), block.toString().contains("generated_mock0_count"));
    assertEquals(1, countFields.size());
    assertTrue(countFields.get(0).toString().contains("private static int[] generated_mock0_count"));
    assertTrue(block.toString(), block.toString().contains("generated_mock0(j, k)"));
    assertTrue(helpers.get(0).toString(), helpers.get(0).toString()
        .contains("deepEquals(args[0], 1)"));
    assertFalse(helpers.get(0).toString().contains("deepEquals(args[1]"));

    block = StaticJavaParser.parseBlock("{ foo(j, k); }");
    helpers = MockUtil.substituteMockedValuesAdvanced(block,
        mocks("foo(1, 2)", new IntegerLiteralExpr(7)), "generated");
    assertTrue(helpers.get(0).toString(), helpers.get(0).toString()
        .contains("deepEquals(args[1], 1) && java.util.Objects.deepEquals(args[2], 2)"));

    CompilationUnit attached = StaticJavaParser.parse(
        "class Attached { void test() { foo(j); } }");
    MethodDeclaration attachedTest = attached.getClassByName("Attached").get()
        .getMethodsByName("test").get(0);
    helpers = MockUtil.substituteMockedValuesAdvanced(attachedTest.getBody().get(),
        mocks("foo(1)", new IntegerLiteralExpr(7)), "test");
    assertTrue(attached.getClassByName("Attached").get()
        .getFieldByName("test_mock0_count").isPresent());
    assertEquals("int[]", attached.getClassByName("Attached").get()
        .getFieldByName("test_mock0_count").get().getVariable(0).getType().toString());
    assertTrue(attachedTest.toString().contains("test_mock0(j)"));
    assertTrue(helpers.get(0).toString().contains("deepEquals(args[0], 1)"));

    Map<String, List<Expression>> noValues = new HashMap<>();
    noValues.put("foo(1)", null);
    CompilationUnit attachedNoValues = StaticJavaParser.parse(
        "class AttachedNoValues { Object test() { return foo(1); } }");
    helpers = MockUtil.substituteMockedValuesAdvanced(attachedNoValues
        .getClassByName("AttachedNoValues").get().getMethodsByName("test").get(0)
        .getBody().get(), noValues, "test");
    assertEquals(1, attachedNoValues.getClassByName("AttachedNoValues").get()
        .getFields().size());
    assertTrue(helpers.get(0).toString().contains("return null;"));
  }

  @Test
  public void detachedAdvancedMocksPlaceDistinctCountersBeforeMultipleCalls() throws Exception {
    BlockStmt block = StaticJavaParser.parseBlock(
        "{ int first = foo(1); int second = bar(2); "
            + "if (first != 11 || second != 22) throw new AssertionError(); }");
    Map<String, List<Expression>> map = new LinkedHashMap<>();
    map.put("foo(1)", Arrays.asList(new IntegerLiteralExpr(11)));
    map.put("bar(2)", Arrays.asList(new IntegerLiteralExpr(22)));
    List<MethodDeclaration> helpers = MockUtil.substituteMockedValuesAdvanced(block, map, "test");
    assertEquals(2, helpers.size());
    assertTrue(block.getStatement(0).toString().contains("BLOCKTEST_MOCK_COUNTS_0"));
    assertTrue(block.getStatement(1).toString().contains("BLOCKTEST_MOCK_COUNTS_1"));

    CompilationUnit generated = StaticJavaParser.parse(
        "import org.junit.Test; public class DetachedMocks { @Test public void test() {} }");
    generated.getClassByName("DetachedMocks").get().getMethodsByName("test").get(0)
        .setBody(block);
    for (MethodDeclaration helper : helpers) {
      generated.getClassByName("DetachedMocks").get().addMember(helper);
    }
    Result result = GeneratedCode.run(temporary.getRoot().toPath(),
        "DetachedMocks", source("DetachedMocks.java", generated.toString()));
    assertEquals(1, result.getRunCount());
    assertTrue(result.getFailures().toString(), result.wasSuccessful());

    BlockStmt collision = StaticJavaParser.parseBlock(
        "{ int[] BLOCKTEST_MOCK_COUNTS_0 = new int[1]; foo(1); }");
    MockUtil.substituteMockedValuesAdvanced(collision,
        mocks("foo(1)", new IntegerLiteralExpr(7)), "test");
    assertTrue(collision.getStatement(0).toString().contains("BLOCKTEST_MOCK_COUNTS_0_"));
  }
}
