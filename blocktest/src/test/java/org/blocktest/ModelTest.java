package org.blocktest;

import static org.junit.Assert.*;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.PrimitiveType;
import java.util.*;
import org.blocktest.support.AstTestSupport;
import org.blocktest.types.*;
import org.junit.Test;

public class ModelTest extends AstTestSupport {
  @Test
  public void givenVariableCreatesDeclarationsAndRejectsMissingType() {
    GivenVariable given =
        new GivenVariable(
            PrimitiveType.intType(), "n", new NameExpr("n"), new IntegerLiteralExpr(2));
    assertEquals("int n = 2", given.toString());
    given.value = null;
    assertEquals("int n = null", given.toAssignExpr().toString());
    given.type = null;
    assertEquals(
        "Type cannot be null for GivenVariable: n",
        assertThrows(RuntimeException.class, given::toAssignExpr).getMessage());
  }

  @Test
  public void flowFactoriesAndStaticMethodDescription() {
    assertSame(Flow.IfStmt, Flow.IfStmt());
    assertSame(Flow.Then, Flow.Then());
    assertSame(Flow.Else, Flow.Else());
    assertSame(Flow.ElseIf, Flow.ElseIf());
    StaticMethod method = new StaticMethod("Owner", "call", 2);
    assertEquals("Owner", method.className);
    assertEquals("call", method.methodName);
    assertEquals(2, method.argumentCount);
    assertEquals("Owner.call(2)", method.toString());
  }

  @Test
  public void blockDefaultsAndDescriptions() throws Exception {
    BlockTest b = new BlockTest();
    assertEquals(EndAt.LAST_REFERENCE, b.endAt);
    assertEquals(1, b.endAtInstances);
    assertTrue(b.endAfter);
    assertFalse(b.endReversed);
    assertFalse(b.notReturn);
    assertEquals(0, b.delay);
    assertEquals(fixtureText("model/default-block.txt"), b.toString());
    b.testName = "sample";
    b.lineNo = 8;
    b.givens.add(
        new GivenVariable(
            PrimitiveType.intType(), "n", new NameExpr("n"), new IntegerLiteralExpr(2)));
    b.statements.addStatement("n++;");
    b.junitAssertions.add(StaticJavaParser.parseExpression("assertTrue(n > 0)"));
    b.junitLambdaAssertions.add(StaticJavaParser.parseExpression("assertTrue(methodReturn)"));
    b.givenVariables.add("n");
    b.assertedVariables.add("n");
    b.controlFlow = "if,";
    assertEquals(fixtureText("model/populated-block.txt"), b.toString());
    b.flows.addAll(Arrays.asList(Flow.IfStmt, Flow.Then));
    assertTrue(b.toString().contains("Required flow:\n -> IfStmt -> Then\n"));
    assertFalse(b.toString().contains("Required control flow"));
  }

  @Test
  public void cloneSeparatesMutableContainersAndSyntaxTrees() throws Exception {
    BlockTest b = new BlockTest();
    b.testName = "clone";
    b.controlFlow = "if,";
    b.setupBlock = StaticJavaParser.parseBlock("{ init(); }");
    b.statements.addStatement("work();");
    b.exception = StaticJavaParser.parseExpression("Exception.class");
    b.mockMethods.put("call()", new ArrayList<>(Arrays.asList(new IntegerLiteralExpr(1))));
    BlockTest copy = b.clone();
    assertEquals(b.toString(), copy.toString());
    for (java.lang.reflect.Field field : BlockTest.class.getFields()) {
      Object original = field.get(b), cloned = field.get(copy);
      if (original instanceof Collection
          || original instanceof Map
          || original instanceof com.github.javaparser.ast.Node) {
        assertNotSame(field.getName(), original, cloned);
        assertEquals(field.getName(), original, cloned);
      }
    }
    copy.mockMethods.get("call()").clear();
    copy.setupBlock.addStatement("other();");
    copy.statements.addStatement("other();");
    ((ClassExpr) copy.exception).setType("RuntimeException");
    assertEquals(1, b.mockMethods.get("call()").size());
    assertEquals(1, b.setupBlock.getStatements().size());
    assertEquals(1, b.statements.getStatements().size());
    assertEquals("Exception.class", b.exception.toString());
    b.statements = null;
    assertNull(b.clone().statements);
    assertNull(new BlockTest().clone().setupBlock);
  }
}
