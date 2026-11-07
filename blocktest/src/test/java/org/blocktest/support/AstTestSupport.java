package org.blocktest.support;

import static org.junit.Assert.assertEquals;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.blocktest.TestExtractor;
import org.blocktest.utils.TypeResolver;
import org.blocktest.utils.Util;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

/** Isolates the application's mutable process-wide parser and extraction options. */
public abstract class AstTestSupport {
  @Rule public TemporaryFolder temporary = new TemporaryFolder();
  private final Map<Field, Object> options = new HashMap<>();
  private ParserConfiguration parser;
  private CombinedTypeSolver solver;
  private JavaSymbolSolver resolver;
  private Map<String, String> fields;
  private Set<String> privateFields;

  @Before
  public void isolateGlobals() throws Exception {
    for (Field field : Util.class.getFields()) {
      if (Modifier.isStatic(field.getModifiers())) options.put(field, field.get(null));
    }
    parser = StaticJavaParser.getConfiguration();
    solver = TypeResolver.sTypeSolver;
    resolver = TypeResolver.sSymbolResolver;
    fields = TestExtractor.staticFields;
    privateFields = TestExtractor.staticPrivateFields;
    TestExtractor.staticFields = new HashMap<>();
    TestExtractor.staticPrivateFields = new HashSet<>();
    TypeResolver.sTypeSolver = new CombinedTypeSolver(new ReflectionTypeSolver());
    TypeResolver.sSymbolResolver = new JavaSymbolSolver(TypeResolver.sTypeSolver);
    StaticJavaParser.setConfiguration(
        new ParserConfiguration().setSymbolResolver(TypeResolver.sSymbolResolver));
    Util.depClassPaths = null;
    Util.appSrcPath = null;
    Util.junitVersion = "junit4";
    Util.predictValue = false;
    Util.loadXml = false;
    Util.rewriteStaticVar = true;
    Util.coverage = false;
    Util.generatedTests = false;
    Util.withAssertion = false;
    Util.allPublic = false;
  }

  @After
  public void restoreGlobals() throws Exception {
    for (Map.Entry<Field, Object> entry : options.entrySet())
      entry.getKey().set(null, entry.getValue());
    StaticJavaParser.setConfiguration(parser);
    TypeResolver.sTypeSolver = solver;
    TypeResolver.sSymbolResolver = resolver;
    TestExtractor.staticFields = fields;
    TestExtractor.staticPrivateFields = privateFields;
  }

  protected Path source(String name, String text) throws Exception {
    Path file = temporary.getRoot().toPath().resolve(name);
    Files.createDirectories(file.getParent());
    Files.write(file, text.getBytes(StandardCharsets.UTF_8));
    return file;
  }

  protected String fixtureText(String name) throws IOException {
    String resource = "/fixtures/" + name;
    try (InputStream stream = getClass().getResourceAsStream(resource)) {
      if (stream == null) {
        throw new IOException("Missing test fixture: " + resource);
      }
      java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
      byte[] buffer = new byte[4096];
      int count;
      while ((count = stream.read(buffer)) != -1) {
        out.write(buffer, 0, count);
      }
      return new String(out.toByteArray(), StandardCharsets.UTF_8);
    }
  }

  protected Path sourceFixture(String fixture, String destination) throws Exception {
    return source(destination, fixtureText(fixture));
  }

  protected CompilationUnit parseFixture(String name) throws IOException {
    return StaticJavaParser.parse(fixtureText(name));
  }

  protected BlockStmt blockFixture(String name, String method) throws IOException {
    return parseFixture(name).findAll(MethodDeclaration.class).stream()
        .filter(declaration -> declaration.getNameAsString().equals(method))
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException("No fixture method " + method + " in " + name))
        .getBody()
        .orElseThrow(() -> new IllegalArgumentException("Fixture method has no body: " + method));
  }

  protected void assertBlockFixture(String name, String method, Node actual) throws IOException {
    assertEquals(blockFixture(name, method).toString(), actual.toString());
  }

  protected static void assertBlock(String expected, Node actual) {
    assertEquals(StaticJavaParser.parseBlock(expected).toString(), actual.toString());
  }
}
