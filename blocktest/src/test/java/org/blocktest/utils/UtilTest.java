package org.blocktest.utils;

import static org.junit.Assert.*;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.type.Type;
import java.util.*;
import org.blocktest.support.AstTestSupport;
import org.junit.Test;

public class UtilTest extends AstTestSupport {
  private static final String FIXTURE = "utils/UtilCases.java";

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


}
