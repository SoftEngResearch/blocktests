package org.blocktest.visitors;

import static org.junit.Assert.*;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.*;
import java.nio.file.*;
import java.util.*;
import org.blocktest.BlockTest;
import org.blocktest.support.AstTestSupport;
import org.blocktest.utils.Util;
import org.junit.Test;

public class ExtractionEdgeCasesTest extends AstTestSupport {
  private BlockTest extract(String body) {
    return extract(StaticJavaParser.parseBlock("{" + body + "}"), new ExtractionVisitor.Context());
  }

  private BlockTest extract(Node node, ExtractionVisitor.Context context) {
    List<BlockTest> tests = new ArrayList<>();
    node.accept(new ExtractionVisitor(tests), context);
    assertEquals(1, tests.size());
    return tests.get(0);
  }

  private BlockTest input(String value) {
    return extract("blocktest().given(missing," + value + ").end(FIRST_STATEMENT); work();");
  }

  @Test
  public void infersBoxedCollectionElementsAndRetainsExplicitConstructorTypes() {
    String[][] values = {
      {"1L", "Long"},
      {"1.0f", "Float"},
      {"1.0F", "Float"},
      {"1.0", "Double"},
      {"'x'", "Character"},
      {"\"text\"", "String"},
      {"false", "Boolean"}
    };
    for (String[] value : values)
      assertEquals(
          "ArrayList<java.lang." + value[1] + ">",
          input("new ArrayList<>(Arrays.asList(" + value[0] + "))").givens.get(0).type.toString());
    for (String constructor :
        Arrays.asList(
            "new ArrayList<>(2)",
            "new ArrayList<>(other())",
            "new ArrayList<>(Arrays.other())",
            "new ArrayList<>(Other.asList())",
            "new ArrayList<>(Arrays.asList())",
            "new ArrayList<>(java.util.Other.asList(1))"))
      assertEquals("ArrayList<>", input(constructor).givens.get(0).type.toString());
  }

  @Test
  public void rejectsUnsupportedInferenceInsteadOfInventingTypes() {
    for (String value :
        Arrays.asList(
            "unknown",
            "null",
            "obj.getAnnotation()",
            "obj.getAnnotation(x)",
            "obj.getAnnotation(A.class,B.class)",
            "build()",
            "obj.build()",
            "builder().build()",
            "pkg.Thing.newBuilder().build()",
            "get(1)",
            "Other.get(1)",
            "pkg.Other.get(1)",
            "factory().get(1)",
            "new Object().FIELD",
            "new ArrayList<>(Arrays.asList(unknown))"))
      assertThrows(value, RuntimeException.class, () -> input(value));
  }

  @Test
  public void unwrapsWildcardBoundsAndRecoversFromUnusableTypeHints() {
    for (boolean lower : new boolean[] {false, true}) {
      ExtractionVisitor.Context context = new ExtractionVisitor.Context();
      WildcardType type = new WildcardType();
      if (lower) type.setSuperType(StaticJavaParser.parseClassOrInterfaceType("Number"));
      else type.setExtendedType(StaticJavaParser.parseClassOrInterfaceType("Number"));
      context.symbolTable.put("n", type);
      BlockTest b =
          extract(
              StaticJavaParser.parseBlock(
                  "{ blocktest().given(\"n\",1).end(FIRST_STATEMENT); work(); }"),
              context);
      assertEquals("Number", b.givens.get(0).type.asString());
    }
    for (Type hint :
        Arrays.asList(
            new UnknownType(),
            new ClassOrInterfaceType(null, "U"),
            new ClassOrInterfaceType(null, "InferenceVariable_0"),
            Util.getTypeFromStr("java.lang.Class<?>"))) {
      ExtractionVisitor.Context context = new ExtractionVisitor.Context();
      context.symbolTable.put("n", hint);
      BlockTest b =
          extract(
              StaticJavaParser.parseBlock(
                  "{ blocktest().given(\"n\",1).end(FIRST_STATEMENT); work(); }"),
              context);
      assertEquals("int", b.givens.get(0).type.asString());
    }
    ExtractionVisitor.Context context = new ExtractionVisitor.Context();
    context.symbolTable.put("n", new WildcardType());
    assertThrows(
        RuntimeException.class,
        () ->
            extract(
                StaticJavaParser.parseBlock(
                    "{ blocktest().given(\"n\",1).end(FIRST_STATEMENT); work(); }"),
                context));
    assertThrows(
        RuntimeException.class,
        () -> extract("blocktest().mock(call()).noInit(missing).end(FIRST_STATEMENT); work();"));
  }

  @Test
  public void resolvesNoInitFromAnExpressionAndInfersUntypedLambdaArguments() throws Exception {
    BlockTest b =
        extract(parseFixture("extraction-edge/NoInit.java"), new ExtractionVisitor.Context());
    assertEquals("java.lang.String", b.givens.get(0).type.asString());
    assertNull(b.givens.get(0).value);
    b = extract("lambdatest().args(7).argsVar(missing).end(FIRST_STATEMENT); use(missing);");
    assertEquals("int missing = 7", b.givens.get(0).toString());
    b =
        extract(
            parseFixture("extraction-edge/ClassArgument.java"), new ExtractionVisitor.Context());
    assertEquals("java.lang.Class", b.givens.get(0).type.asString());
  }

  @Test
  public void handlesSwitchEntryAndNestedStatementParents() {
    BlockTest b =
        extract(
            "switch(x) { case 1: blocktest().given(x,1).checkEq(\"x\",2); x++; break; default: ignore(); }");
    assertBlock("{ x++; }", b.statements);
    b = extract("if (true) blocktest().given(x,1).end(FIRST_STATEMENT); work();");
    // The enclosing if is the first statement in this block.
    assertEquals(1, b.statements.getStatements().size());
    assertTrue(b.statements.getStatement(0).isIfStmt());
    b = extract("blocktest().given(x,1).unknownOption().end(FIRST_STATEMENT,true); work();");
    assertBlock("{ work(); }", b.statements);
    b = extract("BTest.blocktest().given(x,1).end(FIRST_STATEMENT); work();");
    assertEquals("", b.className);
  }

  @Test
  public void toleratesMissingSerializedDataAndReadErrorsAsUninitializedInputs() throws Exception {
    Util.loadXml = true;
    BlockTest b = extract("blocktest().given(x,\"@absent.txt\").end(FIRST_STATEMENT); work();");
    assertNull(b.givens.get(0).value);
    source(".blocktests/serialized-data/present.txt", "text");
    Util.appSrcPath = temporary.getRoot().toString();
    b = extract("blocktest().given(x,\"@absent.txt\").end(FIRST_STATEMENT); work();");
    assertNull(b.givens.get(0).value);
    Files.createDirectory(
        temporary.getRoot().toPath().resolve(".blocktests/serialized-data/directory.txt"));
    b = extract("blocktest().given(x,\"@directory.txt\").end(FIRST_STATEMENT); work();");
    assertTrue(b.givens.get(0).value.isNullLiteralExpr());
  }

  @Test
  public void serializedFileSuffixAndArityChecksPreserveOrdinaryStrings() throws Exception {
    Util.loadXml = true;
    source(".blocktests/serialized-data/value.txt", "text");
    source(".blocktests/serialized-data/value.java", "7");
    Util.appSrcPath = temporary.getRoot().toString();
    String[] values = {"\"plain\"", "\"value.txt\"", "\"@value.bin\"", "7"};
    for (String value : values) {
      BlockTest b =
          extract(
              "blocktest().given(x,"
                  + value
                  + ").checkEq(\"x\","
                  + value
                  + ").checkReturnEq("
                  + value
                  + ").mock(call(),"
                  + value
                  + ").checkControlFlow(\"@flow.bin\").end(FIRST_STATEMENT); work();");
      assertEquals(StaticJavaParser.parseExpression(value), b.givens.get(0).value);
      assertEquals(value, b.mockMethods.get("call()").get(0).toString());
      assertEquals(value, ((MethodCallExpr) b.junitAssertions.get(0)).getArgument(0).toString());
      assertEquals(
          value, ((MethodCallExpr) b.junitLambdaAssertions.get(0)).getArgument(0).toString());
      assertEquals("\"@flow.bin\"", b.controlFlow);
      b =
          extract(
              "blocktest().given(x,"
                  + value
                  + ",\"Object\").checkEq(\"x\","
                  + value
                  + ",0.1).checkReturnEq("
                  + value
                  + ",0.1).mock(call(),"
                  + value
                  + ",\"Object\").end(FIRST_STATEMENT); work();");
      assertEquals(3, ((MethodCallExpr) b.junitAssertions.get(0)).getArguments().size());
      assertEquals(3, ((MethodCallExpr) b.junitLambdaAssertions.get(0)).getArguments().size());
    }
    for (String suffix : Arrays.asList("txt", "java", "xml")) {
      BlockTest b =
          extract(
              "blocktest().given(x,\"@value."
                  + suffix
                  + "\",\"Object\").checkEq(\"x\",\"@value."
                  + suffix
                  + "\",\"Object\").checkReturnEq(\"@value."
                  + suffix
                  + "\",\"Object\").mock(call(),\"@value."
                  + suffix
                  + "\",\"Object\").end(FIRST_STATEMENT); work();");
      assertEquals(2, ((MethodCallExpr) b.junitAssertions.get(0)).getArguments().size());
      assertEquals(2, ((MethodCallExpr) b.junitLambdaAssertions.get(0)).getArguments().size());
      assertEquals(b.givens.get(0).value, b.mockMethods.get("call()").get(0));
    }
  }
}
