package org.blocktest.utils;

import static org.junit.Assert.*;

import com.github.javaparser.StaticJavaParser;
import java.io.*;
import java.nio.file.*;
import org.blocktest.support.AstTestSupport;
import org.junit.Test;

public class SourceCheckersTest extends AstTestSupport {
  private interface Action {
    void run() throws Exception;
  }

  private String output(Action action) throws Exception {
    PrintStream original = System.out;
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    try (PrintStream out = new PrintStream(bytes, true, "UTF-8")) {
      System.setOut(out);
      action.run();
    } finally {
      System.setOut(original);
    }
    return bytes.toString("UTF-8");
  }

  @Test
  public void prettySignaturesEraseGenericsAndArrayDimensionsAndKeepVarargs() {
    assertEquals(
        "run(Set, byte, String...)",
        ComplexityChecker.getPrettySignature(
            StaticJavaParser.parseMethodDeclaration(
                "void run(Set<String> s, byte[][] bytes, String... values) {}")));
    assertEquals(
        "empty()",
        ComplexityChecker.getPrettySignature(
            StaticJavaParser.parseMethodDeclaration("void empty() {}")));
  }

  @Test
  public void complexityCountsAllGuardKindsButNotTryOrSynchronized() throws Exception {
    Path file = sourceFixture("checkers/guards/Sample.java", "Sample.java");
    ComplexityChecker.Result r = ComplexityChecker.analyzeStatement(14, file.toString());
    assertEquals(0, r.error);
    assertEquals(7, r.guards);
    assertEquals("public", r.mType);
    assertEquals(2, r.mArgs);
    assertEquals("m(int, int)", r.sign);
    assertEquals(22, r.lineCount);
    assertEquals(6, ComplexityChecker.analyzeStatement(11, file.toString()).guards);
  }

  @Test
  public void complexityReportsVisibilityBoundaryLinesAndMissingSources() throws Exception {
    for (String visibility : new String[] {"public", "private", "protected", ""}) {
      String fixture = visibility.isEmpty() ? "package" : visibility;
      Path file = sourceFixture("checkers/" + fixture + "/Sample.java", "Sample.java");
      ComplexityChecker.Result r = ComplexityChecker.analyzeStatement(3, file.toString());
      assertEquals(0, r.error);
      assertEquals(0, r.guards);
      assertEquals(visibility.isEmpty() ? "package-private" : visibility, r.mType);
      assertEquals(3, r.lineCount);
      assertEquals(-2, ComplexityChecker.analyzeStatement(0, file.toString()).error);
      assertEquals(-2, ComplexityChecker.analyzeStatement(99, file.toString()).error);
    }
    assertEquals(
        -1, ComplexityChecker.analyzeStatement(1, temporary.getRoot() + "/absent.java").error);
    assertEquals(
        -2,
        ComplexityChecker.analyzeStatement(
                1, sourceFixture("checkers/broken/Broken.java", "Broken.java").toString())
            .error);
    ComplexityChecker.Result field =
        ComplexityChecker.analyzeStatement(
            2, sourceFixture("checkers/fields/Fields.java", "Fields.java").toString());
    assertEquals(0, field.error);
    assertEquals("", field.mType);
    assertEquals(-1, field.mArgs);
    assertEquals(0, field.lineCount);
  }

  @Test
  public void complexityCliReadsSingleLocationsAndListsWithMalformedEntries() throws Exception {
    Path file = sourceFixture("checkers/simple/Sample.java", "Sample.java");
    assertEquals(
        file + ":3,0,0,package-private,0\n",
        output(() -> ComplexityChecker.main(new String[] {file.toString(), "3C5"})));
    assertTrue(
        output(() -> ComplexityChecker.main(new String[] {file.toString(), "3"}))
            .contains(":3,0,0,package-private,0"));
    Path list = source("locations.txt", file + ",3C2\n" + file + ",3\nbad-entry\n");
    String out = output(() -> ComplexityChecker.main(new String[] {list.toString()}));
    assertTrue(out.contains("@#$#@0@#$#@0@#$#@package-private@#$#@0@#$#@3@#$#@m()"));
    assertTrue(out.contains("bad-entry,failed"));
    assertEquals(
        "",
        output(() -> ComplexityChecker.main(new String[] {temporary.getRoot() + "/missing.txt"})));
  }

  @Test
  public void lambdaCheckerHandlesSingleFilesListsAndUnreadableInputs() throws Exception {
    Path file = sourceFixture("checkers/lambdas/Sample.java", "Sample.java");
    String expected = file + ":2-2\n" + file + ":3-5\n";
    assertEquals(expected, output(() -> LambdaChecker.main(new String[] {file.toString()})));
    Path list = source("sources.txt", file + "\n" + temporary.getRoot() + "/absent.java\n");
    assertEquals(expected, output(() -> LambdaChecker.main(new String[] {list.toString()})));
    assertEquals(
        "", output(() -> LambdaChecker.main(new String[] {temporary.getRoot() + "/absent.java"})));
    assertEquals(
        "", output(() -> LambdaChecker.main(new String[] {temporary.getRoot() + "/absent.txt"})));
  }

  private void exitsWithUsage(Action action, String expectedUsage) throws Exception {
    SecurityManager previous = System.getSecurityManager();
    PrintStream previousErr = System.err;
    ByteArrayOutputStream errors = new ByteArrayOutputStream();
    final int[] status = {-1};
    try (PrintStream captured = new PrintStream(errors, true, "UTF-8")) {
      System.setErr(captured);
      System.setSecurityManager(
          new SecurityManager() {
            @Override
            public void checkPermission(java.security.Permission permission) {}

            @Override
            public void checkExit(int code) {
              status[0] = code;
              throw new SecurityException("exit intercepted");
            }
          });
      assertThrows(SecurityException.class, action::run);
    } finally {
      System.setSecurityManager(previous);
      System.setErr(previousErr);
    }
    assertEquals(1, status[0]);
    assertEquals(expectedUsage + "\n", errors.toString("UTF-8"));
  }

  @Test
  public void commandLineToolsExplainMissingArgumentsAndExitWithFailure() throws Exception {
    exitsWithUsage(
        () -> ComplexityChecker.main(new String[0]),
        "Usage: java ComplexityChecker <source-file> <line-number>");
    exitsWithUsage(
        () -> ComplexityChecker.main(new String[] {"Sample.java"}),
        "Usage: java ComplexityChecker <source-file> <line-number>");
    exitsWithUsage(
        () -> LambdaChecker.main(new String[0]), "Usage: java LambdaChecker <source-file>");
    exitsWithUsage(
        () -> LambdaChecker.main(new String[] {"a", "b"}),
        "Usage: java LambdaChecker <source-file>");
  }

  @Test
  public void complexityReturnsParseFailureForUnreadableStream() throws Exception {
    Path directory = temporary.newFolder("directory.java").toPath();
    assertEquals(-2, ComplexityChecker.analyzeStatement(1, directory.toString()).error);
  }
}
