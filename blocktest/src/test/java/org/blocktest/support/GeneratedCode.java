package org.blocktest.support;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import javax.tools.*;
import org.blocktest.Assertion;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

/** Compiles generated code with a real JDK and runs its JUnit 4 tests. */
public final class GeneratedCode {
  private GeneratedCode() {}

  public static Result run(Path directory, String className, Path... sources) throws Exception {
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    assertNotNull("Run Maven with a JDK, not a JRE", compiler);
    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
    List<File> files = new ArrayList<>();
    for (Path source : sources) files.add(source.toFile());
    StringBuilder cp = new StringBuilder(System.getProperty("java.class.path"));
    for (Class<?> type :
        Arrays.asList(org.junit.Test.class, org.hamcrest.Matcher.class, Assertion.class))
      cp.append(File.pathSeparator)
          .append(new File(type.getProtectionDomain().getCodeSource().getLocation().toURI()));
    try (StandardJavaFileManager manager =
        compiler.getStandardFileManager(diagnostics, null, StandardCharsets.UTF_8)) {
      boolean ok =
          compiler
              .getTask(
                  null,
                  manager,
                  diagnostics,
                  Arrays.asList(
                      "-classpath",
                      cp.toString(),
                      "-d",
                      directory.toString(),
                      "-source",
                      "8",
                      "-target",
                      "8"),
                  null,
                  manager.getJavaFileObjectsFromFiles(files))
              .call();
      assertTrue("Generated code must compile: " + diagnostics.getDiagnostics(), ok);
    }
    try (URLClassLoader loader =
        new URLClassLoader(
            new URL[] {directory.toUri().toURL()}, GeneratedCode.class.getClassLoader())) {
      return JUnitCore.runClasses(loader.loadClass(className));
    }
  }
}
