package org.blocktest.utils;

import static org.junit.Assert.*;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.resolution.types.*;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import org.blocktest.support.AstTestSupport;
import org.junit.Test;

public class TypeResolverTest extends AstTestSupport {
  @Test
  public void resolvesPrimitivesReferenceTypesArraysAndTypeParameters() throws Exception {
    CompilationUnit cu = parseFixture("resolver/Types.java");
    List<FieldDeclaration> fields = cu.findAll(FieldDeclaration.class);
    assertEquals("int", TypeResolver.resolveType(fields.get(0).getElementType()));
    assertEquals("java.lang.String", TypeResolver.resolveType(fields.get(1).getElementType()));
    assertEquals("java.util.List", TypeResolver.resolveType(fields.get(2).getElementType()));
    assertEquals("int[][]", TypeResolver.resolveType(fields.get(3).getVariable(0).getType()));
    assertEquals(
        "java.lang.Number",
        TypeResolver.resolveType(
            fields.get(4).getElementType(), Collections.singletonMap("T", "java.lang.Number")));
    assertEquals("Missing", TypeResolver.resolveType(fields.get(5).getElementType()));
    assertEquals(
        "java.util.Map$Entry",
        TypeResolver.normalizeType(TypeResolver.sTypeSolver.solveType("java.util.Map.Entry")));
    assertEquals(
        "int[]", TypeResolver.normalizeType(new ResolvedArrayType(ResolvedPrimitiveType.INT)));
  }

  @Test
  public void walksAncestorTypesWithoutLoopingAtObject() {
    assertTrue(
        TypeResolver.bfsSafeFunc
            .apply(TypeResolver.sTypeSolver.solveType("java.lang.Object"))
            .isEmpty());
    Set<String> names = new HashSet<>();
    for (ResolvedReferenceType type :
        TypeResolver.bfsSafeFunc.apply(TypeResolver.sTypeSolver.solveType("java.util.ArrayList")))
      names.add(type.getQualifiedName());
    assertTrue(names.contains("java.util.AbstractList"));
    assertTrue(names.contains("java.util.Collection"));
    assertTrue(names.contains("java.lang.Object"));
  }

  @Test
  public void setupAddsClasspathJarsAndSourceDirectories() throws Exception {
    TypeResolver.sTypeSolver = new CombinedTypeSolver();
    TypeResolver.sSymbolResolver = new JavaSymbolSolver(TypeResolver.sTypeSolver);
    Path source = sourceFixture("resolver/Widget.java", "sources/example/Widget.java");
    Util.appSrcPath = source.getParent().getParent().toString();
    // PIT loads JUnit through its own worker class loader, so JUnit's code
    // source is not a stable fixture path. Build the JAR being resolved.
    Path archivedSource =
        sourceFixture("resolver/Marker.java", "archive-src/archivefixture/Marker.java");
    Path compiled = temporary.newFolder("compiled").toPath();
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    assertNotNull("Run tests with a JDK", compiler);
    assertEquals(
        0, compiler.run(null, null, null, "-d", compiled.toString(), archivedSource.toString()));
    Path jar = temporary.getRoot().toPath().resolve("fixture.jar");
    try (JarOutputStream out = new JarOutputStream(Files.newOutputStream(jar))) {
      out.putNextEntry(new JarEntry("archivefixture/Marker.class"));
      Files.copy(compiled.resolve("archivefixture/Marker.class"), out);
      out.closeEntry();
    }
    Util.depClassPaths = jar + java.io.File.pathSeparator + temporary.getRoot();
    TypeResolver.setup();
    assertEquals(
        "example.Widget", TypeResolver.sTypeSolver.solveType("example.Widget").getQualifiedName());
    // PIT's Javassist interceptor cannot reflect into JarClassPath when a class
    // is resolved. Check that setup registered a solver that indexed this JAR.
    Field elements = CombinedTypeSolver.class.getDeclaredField("elements");
    elements.setAccessible(true);
    List<?> solvers = (List<?>) elements.get(TypeResolver.sTypeSolver);
    assertEquals(1, solvers.stream().filter(ReflectionTypeSolver.class::isInstance).count());
    assertEquals(2, solvers.stream().filter(JarTypeSolver.class::isInstance).count());
    assertTrue(
        "The fixture JAR should be indexed by a registered JarTypeSolver",
        solvers.stream()
            .filter(JarTypeSolver.class::isInstance)
            .map(JarTypeSolver.class::cast)
            .anyMatch(solver -> solver.getKnownClasses().contains("archivefixture.Marker")));
  }

  @Test
  public void setupAcceptsSourceFilesAndReportsBrokenJars() throws Exception {
    Path source = sourceFixture("resolver/Widget.java", "src/main/java/example/Widget.java");
    Util.appSrcPath = source.toString();
    TypeResolver.setup();
    assertEquals(
        "example.Widget", TypeResolver.sTypeSolver.solveType("example.Widget").getQualifiedName());
    Util.appSrcPath = sourceFixture("resolver/Other.java", "loose/Other.java").toString();
    TypeResolver.setup();
    assertTrue(TypeResolver.sTypeSolver.tryToSolveType("Other").isSolved());
    Util.depClassPaths = temporary.getRoot() + "/missing.jar";
    assertNotNull(assertThrows(RuntimeException.class, TypeResolver::setup).getCause());
  }
}
