package org.blocktest;

import static org.junit.Assert.*;

import com.github.javaparser.StaticJavaParser;
import java.nio.file.*;
import org.blocktest.support.AstTestSupport;
import org.blocktest.utils.Util;
import org.junit.Test;

public class RunnerTest extends AstTestSupport {
  @Test
  public void validatesRequiredInputAndDependencyFile() throws Exception {
    assertEquals(
        "input_file parameter is required",
        assertThrows(RuntimeException.class, () -> BlockTestRunnerSourceCode.main(new String[0]))
            .getMessage());
    assertEquals(
        "input file does not exist",
        assertThrows(
                RuntimeException.class,
                () ->
                    BlockTestRunnerSourceCode.main(
                        new String[] {"--input_file=" + temporary.getRoot() + "/missing.java"}))
            .getMessage());
    Path input = sourceFixture("runner/minimal/Input.java", "Input.java");
    assertTrue(
        assertThrows(
                RuntimeException.class,
                () ->
                    BlockTestRunnerSourceCode.main(
                        new String[] {
                          "--input_file=" + input, "--dep_file_path=" + input + ".missing"
                        }))
            .getMessage()
            .contains("dep file does not exist"));
  }

  @Test
  public void parsesOptionsWritesOutputAndPropagatesFlags() throws Exception {
    Path input = sourceFixture("runner/options/Input.java", "Input.java");
    Path deps = source("deps.txt", "");
    Path output = temporary.getRoot().toPath().resolve("generated/Chosen.java");
    BlockTestRunnerSourceCode.main(
        new String[] {
          "--input_file=" + input,
          "--output_file=" + output,
          "--dep_file_path=" + deps,
          "--app_src_path=" + temporary.getRoot(),
          "--junit_version= JUnit4 ",
          "--duplicated_test_count=2",
          "--coverage=false",
          "--with_assertion=true",
          "--rewrite=true",
          "--compile=false",
          "--predict=true",
          "--loadXml=true",
          "--rewriteStaticVar=false",
          "--public_var=true",
          "--generated_tests=true"
        });
    assertTrue(Files.exists(output));
    assertTrue(Util.predictValue);
    assertTrue(Util.loadXml);
    assertTrue(Util.withAssertion);
    assertTrue(Util.allPublic);
    assertTrue(Util.generatedTests);
    assertFalse(Util.rewriteStaticVar);
    assertFalse(Util.coverage);
    assertEquals("junit4", Util.junitVersion);
    assertEquals("", Util.depClassPaths);
    assertEquals(
        3,
        StaticJavaParser.parse(output)
            .findAll(com.github.javaparser.ast.body.MethodDeclaration.class)
            .size());
  }

  @Test
  public void defaultOptionsSupportOutputDirectoryAndConsoleOnly() throws Exception {
    Path input = sourceFixture("runner/empty/Input.java", "Input.java");
    BlockTestRunnerSourceCode.main(
        new String[] {"--input_file=" + input, "--output_dir=" + temporary.getRoot() + "/out"});
    assertTrue(Files.exists(temporary.getRoot().toPath().resolve("out/InputBlockTest.java")));
    BlockTestRunnerSourceCode.main(new String[] {"--input_file=" + input});
  }

  @Test
  public void dependencyReadFailuresAreReported() throws Exception {
    Path input = sourceFixture("runner/minimal/Input.java", "Input.java");
    assertThrows(
        RuntimeException.class,
        () ->
            BlockTestRunnerSourceCode.main(
                new String[] {"--input_file=" + input, "--dep_file_path=" + temporary.getRoot()}));
  }
}
