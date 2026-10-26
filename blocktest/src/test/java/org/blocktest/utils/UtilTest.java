package org.blocktest.utils;

import static org.junit.Assert.*;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.type.Type;
import java.util.*;
import org.blocktest.support.AstTestSupport;
import org.junit.Test;

public class UtilTest extends AstTestSupport {
  private static final String FIXTURE = "utils/UtilCases.java";

  private BlockStmt mockBlock(String method, String expression) throws Exception {
    return StaticJavaParser.parseBlock(
        blockFixture(FIXTURE, method).toString().replace("TEST_EXPRESSION", expression));
  }

  @Test
  public void parsesPrimitivesArraysWildcardsAndNestedGenerics() {
    for (String primitive :
        Arrays.asList("int", "boolean", "char", "byte", "short", "long", "float", "double")) {
      Type type = Util.getTypeFromStr(" " + primitive + " ");
      assertTrue(type.isPrimitiveType());
      assertEquals(primitive, type.asString());
    }
    for (String generic : Arrays.asList("T", "? extends T", "? super T"))
      assertEquals("Object", Util.getTypeFromStr(generic).asString());
    for (String type :
        Arrays.asList(
            "String",
            "int[][]",
            "?",
            "? extends Number",
            "? super String",
            "Map<String, List<Map<Integer, Long>>>",
            "List<? extends Number>")) assertEquals(type, Util.getTypeFromStr(type).toString());
    assertEquals("Number[]", Util.getTypeFromStr("? extends Number[]").asString());
    assertEquals("String[][]", Util.getTypeFromStr("? super String[][]").asString());
    assertEquals("?[]", Util.getTypeFromStr("?[]").asString());
    assertTrue(Util.isConstant("UPPER_1"));
    assertFalse(Util.isConstant("lower"));
  }

  @Test
  public void recognizesOnlyInputBearingBlockTestChains() {
    for (String code :
        Arrays.asList(
            "blocktest().given(x, 1)",
            "lambdatest().args(1)",
            "BTest.blocktest().mock(x).checkEq(x, 1)")) {
      assertTrue(code, Util.isTestStatement(StaticJavaParser.parseExpression(code)));
      assertTrue(code, Util.isTestStatement(StaticJavaParser.parseStatement(code + ";")));
    }
    for (String code :
        Arrays.asList(
            "blocktest()",
            "lambdatest()",
            "blocktest().checkEq(x,1)",
            "given(x,1)",
            "other().given(x,1)",
            "obj.given(x,1)",
            "42")) assertFalse(code, Util.isTestStatement(StaticJavaParser.parseExpression(code)));
    assertFalse(Util.isTestStatement(StaticJavaParser.parseBlock("{}")));
    assertTrue(Util.isTestStatement(StaticJavaParser.parseExpression("blocktest()"), true));
  }

  @Test
  public void distinguishesBlocktestRootsFromOtherCalls() {
    for (String s : Arrays.asList("blocktest()", "BTest.blocktest().given(x,1).checkEq(x,2)")) {
      assertTrue(Util.isBlockTest(StaticJavaParser.parseExpression(s)));
      assertTrue(Util.isBlockTest(StaticJavaParser.parseStatement(s + ";")));
    }
    for (String s : Arrays.asList("lambdatest()", "foo().given(x,1)", "42"))
      assertFalse(Util.isBlockTest(StaticJavaParser.parseExpression(s)));
    assertFalse(Util.isBlockTest(StaticJavaParser.parseStatement("x = 1;")));
    assertFalse(Util.isBlockTest(new BlockStmt()));
  }

  @Test
  public void endMarkersRequireANameAndDirectEndOrStartCall() {
    for (String s : Arrays.asList("blocktest(\"a\").end()", "lambdatest(\"a\").start()")) {
      assertEquals("a", Util.isEndStatement(StaticJavaParser.parseExpression(s)));
      assertEquals("a", Util.isEndStatement(StaticJavaParser.parseStatement(s + ";")));
    }
    for (String s :
        Arrays.asList(
            "end()",
            "start()",
            "blocktest().end()",
            "blocktest(1).end()",
            "blocktest(\"a\", 1).end()",
            "blocktest(\"a\")",
            "other(\"a\").end()",
            "blocktest(\"a\").given(x,1).end()",
            "42")) assertNull(s, Util.isEndStatement(StaticJavaParser.parseExpression(s)));
  }

  @Test
  public void renamesEveryOperatorAndReplacesExpressionsOutsideDslOnly() throws Exception {
    assertEquals(
        "timeplusminusdivideequalnotgreaterlessandorxormodquestion____x__y__",
        Util.renameGiven("*+-/=!><&|^%?() []x.y\",'"));
    BlockStmt block = blockFixture(FIXTURE, "renameBefore");
    Util.replaceExpressionsWithVariables(block, new HashSet<>(Arrays.asList("a[0]", "obj.field")));
    assertBlockFixture(FIXTURE, "renameAfter", block);
    BlockStmt copy = block.clone();
    Util.replaceExpressionsWithVariables(block, Collections.emptySet());
    assertEquals(copy, block);
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
          Util.substituteMockedValues(
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
        Util.substituteMockedValues(b, map);
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
    Util.substituteMockedValues(b, map);
    assertBlock("{ int n = 9; consume(8); }", b);
    assertSame(b, Util.substituteMockedValues(b, null));
    assertSame(b, Util.substituteMockedValues(b, Collections.emptyMap()));
  }
}
