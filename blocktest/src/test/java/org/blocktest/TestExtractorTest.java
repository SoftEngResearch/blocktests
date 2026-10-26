package org.blocktest;

import static org.junit.Assert.*;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.blocktest.support.AstTestSupport;
import org.blocktest.support.GeneratedCode;
import org.blocktest.utils.Util;
import org.junit.Test;
import org.junit.runner.Result;

public class TestExtractorTest extends AstTestSupport {
  private static final String BASIC = "extraction/basic/Sample.java";
  private static final String INPUT = "sample/Sample.java";
  private static final String OUTPUT = "generated/SampleBlockTest.java";

  private Path input() {
    return temporary.getRoot().toPath().resolve(INPUT);
  }

  private Path output() {
    return temporary.getRoot().toPath().resolve(OUTPUT);
  }

  private CompilationUnit extractFixture(
      String fixture, int duplicates, boolean coverage, boolean rewrite, boolean compile)
      throws Exception {
    sourceFixture(fixture, INPUT);

    TestExtractor testExtractor = new TestExtractor(input().toString(), "Sample", 
                                coverage, rewrite, compile);


    testExtractor.extractBlockTests();
    testExtractor.extractTestsToSameClass(output().toString(), "Sample", duplicates);

    assertTrue(Files.exists(output()));
    return StaticJavaParser.parse(output());
  }

  private CompilationUnit extractFixture(String fixture) throws Exception {
    return extractFixture(fixture, 0, false, false, true);
  }

  private List<String> methodNames(CompilationUnit unit) {
    List<String> names = new ArrayList<>();
    for (MethodDeclaration method : unit.findAll(MethodDeclaration.class)) {
      names.add(method.getNameAsString());
    }
    return names;
  }

  @Test
  public void extractedJUnit4TestsCompileAndRunWithoutRewritingInput() throws Exception {
    CompilationUnit generated = extractFixture(BASIC);
    assertEquals("sample", generated.getPackageDeclaration().get().getNameAsString());
    assertTrue(generated.getClassByName("SampleBlockTest").get().isPublic());
    assertEquals(Collections.singletonList("increment"), methodNames(generated));
    assertEquals(
        fixtureText(BASIC), new String(Files.readAllBytes(input()), StandardCharsets.UTF_8));

    Result result =
        GeneratedCode.run(
            temporary.getRoot().toPath(), "sample.SampleBlockTest", input(), output());
    assertEquals(1, result.getRunCount());
    assertTrue(result.getFailures().toString(), result.wasSuccessful());
  }

  @Test
  public void duplicateCountHasZeroOneAndMultipleBoundaries() throws Exception {
    for (int count : new int[] {0, 1, 2, 3}) {
      List<String> methods = methodNames(extractFixture(BASIC, count, false, false, true));
      assertEquals(count <= 1 ? 1 : count + 1, methods.size());
      if (count > 1) {
        assertTrue(methods.contains("increment_dup1"));
        assertTrue(methods.contains("testEmptyForDuplicateExp"));
      }
      if (count == 3) {
        assertTrue(methods.contains("increment_dup2"));
      }
    }
  }

  @Test
  public void filtersFrameworkImportsAndSelectsRequestedFramework() throws Exception {
    String fixture = "extraction/imports/Sample.java";
    for (String version : Arrays.asList("junit4", "testng", "junit5")) {
      Util.junitVersion = version;
      CompilationUnit generated = extractFixture(fixture);
      assertEquals(
          1,
          generated.getImports().stream()
              .filter(i -> i.getNameAsString().equals("org.blocktest.BTest"))
              .count());
      assertFalse(generated.toString().contains("org.junit.Ignore"));
      assertTrue(generated.toString().contains("java.util.List"));
      String testImport =
          version.equals("junit4")
              ? "org.junit.Test"
              : version.equals("testng")
                  ? "org.testng.annotations.Test"
                  : "org.junit.jupiter.api.Test";
      assertTrue(generated.toString().contains("import " + testImport + ";"));
    }

    CompilationUnit withoutApplicationImports = extractFixture(fixture, 0, false, false, false);
    assertFalse(withoutApplicationImports.getPackageDeclaration().isPresent());
    assertTrue(withoutApplicationImports.toString().contains("import java.util.List;"));
    assertFalse(withoutApplicationImports.toString().contains("import static sample.Sample"));
    assertFalse(
        withoutApplicationImports
            .toString()
            .contains("import static org.blocktest.BTest.blocktest"));
  }

  @Test
  public void unnamedTestsReceiveSourceLineNames() throws Exception {
    CompilationUnit unnamed = extractFixture("extraction/unnamed/Sample.java");
    assertTrue(methodNames(unnamed).get(0).matches("testLine[1-9][0-9]*"));

    CompilationUnit defaultPackage = extractFixture("extraction/default-package/Sample.java");
    assertFalse(defaultPackage.getPackageDeclaration().isPresent());
    assertTrue(defaultPackage.toString().contains("import static Sample.*;"));
  }

  @Test
  public void coverageMovesStaticExecutionMethodIntoSourceAndKeepsWrapperInTests()
      throws Exception {
    Util.coverage = true;
    CompilationUnit generated = extractFixture(BASIC, 0, true, true, true);
    assertEquals(Collections.singletonList("increment"), methodNames(generated));
    assertTrue(generated.toString().contains("src_increment_src();"));

    CompilationUnit rewrittenSource = StaticJavaParser.parse(input());
    MethodDeclaration sourceMethod =
        rewrittenSource.findAll(MethodDeclaration.class).stream()
            .filter(m -> m.getNameAsString().equals("src_increment_src"))
            .findFirst()
            .get();
    assertTrue(sourceMethod.isStatic());
    assertTrue(sourceMethod.isPublic());
    assertTrue(sourceMethod.toString().contains("x++"));
    assertFalse(
        rewrittenSource.findAll(MethodCallExpr.class).stream()
            .anyMatch(m -> m.getNameAsString().equals("blocktest")));
    assertTrue(
        GeneratedCode.run(temporary.getRoot().toPath(), "sample.SampleBlockTest", input(), output())
            .wasSuccessful());
  }

  @Test
  public void rewritesPrivateStaticHelpersAndFieldsOnlyWhenNeeded() throws Exception {
    CompilationUnit generated = extractFixture("extraction/static-helpers/Sample.java");
    assertTrue(generated.toString().contains("Sample.helper(x)"));
    assertEquals("Sample", TestExtractor.staticFields.get("VALUE"));
    assertTrue(TestExtractor.staticPrivateFields.contains("Sample"));

    CompilationUnit rewrittenSource = StaticJavaParser.parse(input());
    assertFalse(rewrittenSource.findFirst(FieldDeclaration.class).get().isPrivate());
    for (MethodDeclaration method : rewrittenSource.findAll(MethodDeclaration.class)) {
      if (method.getNameAsString().equals("helper")) {
        assertEquals(method.getParameters().isEmpty(), method.isPrivate());
      }
      if (method.getNameAsString().equals("unused")) {
        assertTrue(method.isPrivate());
      }
    }
  }

  @Test
  public void staticFieldRewritingCanBeDisabled() throws Exception {
    Util.rewriteStaticVar = false;
    CompilationUnit generated = extractFixture("extraction/static-disabled/Sample.java");
    assertTrue(TestExtractor.staticFields.isEmpty());
    assertTrue(generated.toString().contains("x = VALUE;"));
  }

  @Test
  public void allPublicRewritesNestedTypesMethodsFieldsAndEnums() throws Exception {
    Util.allPublic = true;
    extractFixture("extraction/visibility/Sample.java");
    CompilationUnit rewritten = StaticJavaParser.parse(input());
    assertTrue(rewritten.getClassByName("Sample").get().isPublic());
    assertFalse(rewritten.getClassByName("Other").get().isPublic());
    assertTrue(
        rewritten.findAll(ClassOrInterfaceDeclaration.class).stream()
            .filter(c -> c.getNameAsString().equals("Inner"))
            .findFirst()
            .get()
            .isPublic());
    assertTrue(rewritten.findFirst(FieldDeclaration.class).get().isPublic());
    for (MethodDeclaration method : rewritten.findAll(MethodDeclaration.class)) {
      assertTrue(method.isPublic());
    }
    assertTrue(
        rewritten.findAll(EnumDeclaration.class).stream()
            .filter(e -> e.getNameAsString().equals("Color"))
            .findFirst()
            .get()
            .isPublic());
    assertFalse(rewritten.getEnumByName("OtherEnum").get().isPublic());

    extractFixture("extraction/enum-sample/Sample.java");
    assertTrue(StaticJavaParser.parse(input()).getEnumByName("Sample").get().isPublic());
  }

  @Test
  public void missingInputAndAbsentOutputDoNotCreateFilesOrRewriteSource() throws Exception {
    sourceFixture(BASIC, "Sample.java");
    Path original = temporary.getRoot().toPath().resolve("Sample.java");
    Path output = temporary.getRoot().toPath().resolve("out.java");
    
    TestExtractor testExtractor = new TestExtractor(original + ".missing", "Sample", 
                                false, false, true);


    testExtractor.extractBlockTests();
    testExtractor.extractTestsToSameClass(output().toString(), "Out", 0);
    
    assertFalse(Files.exists(output));
    for (String destination : Arrays.asList(null, "")) {
      TestExtractor te = new TestExtractor(original.toString(), "Sample", 
                                true, true, true);


      te.extractBlockTests();
      te.extractTestsToSameClass(destination, "Out", 0);
    }
    assertEquals(
        fixtureText(BASIC), new String(Files.readAllBytes(original), StandardCharsets.UTF_8));
  }

  @Test
  public void rewritesStaticReferencesInTargetsAndRevertsExplicitGivenNames() throws Exception {
    CompilationUnit generated = extractFixture("extraction/static-references/Sample.java");
    assertTrue(generated.toString().contains("use(VALUE);"));
    assertTrue(generated.toString().contains("use(Sample.OTHER);"));
    assertTrue(generated.toString().contains("use(obj.field);"));
  }

  @Test
  public void publicVisibilityRewriteIsIdempotentAndHandlesEnumMethods() throws Exception {
    Util.allPublic = true;
    String fixture = "extraction/visibility-idempotent/Sample.java";
    extractFixture(fixture);
    assertEquals(
        fixtureText(fixture), new String(Files.readAllBytes(input()), StandardCharsets.UTF_8));

    extractFixture("extraction/visibility-enum/Sample.java");
    assertTrue(StaticJavaParser.parse(input()).getEnumByName("Sample").get().isPublic());
  }

  @Test
  public void rewriteRemovesDslWhilePreservingOrdinaryCalls() throws Exception {
    extractFixture("extraction/rewrite/Sample.java", 0, false, true, true);
    CompilationUnit rewritten = StaticJavaParser.parse(input());
    assertFalse(rewritten.toString().contains("blocktest(\"increment\")"));
    assertTrue(rewritten.toString().contains("System.out.println(x)"));
  }

  @Test
  public void reportsOutputWriteFailuresWithoutClaimingAFileWasCreated() throws Exception {
    Path original = sourceFixture(BASIC, "Sample.java");
    Path blocker = source("not-a-directory", "occupied");
    Path output = blocker.resolve("Test.java");
    PrintStream originalError = System.err;
    ByteArrayOutputStream errors = new ByteArrayOutputStream();
    try (PrintStream captured = new PrintStream(errors, true, "UTF-8")) {
      System.setErr(captured);
      TestExtractor testExtractor = new TestExtractor(original.toString(), "Sample", 
                                false, false, true);


      testExtractor.extractBlockTests();
      testExtractor.extractTestsToSameClass(output.toString(), "Sample", 0);
    } finally {
      System.setErr(originalError);
    }
    assertFalse(Files.exists(output));
    assertTrue(errors.toString("UTF-8").contains("Exception"));
  }
}
