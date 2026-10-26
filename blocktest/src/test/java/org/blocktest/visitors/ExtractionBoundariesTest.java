package org.blocktest.visitors;

import static org.junit.Assert.*;

import com.github.javaparser.StaticJavaParser;
import java.util.*;
import org.blocktest.BlockTest;
import org.blocktest.support.AstTestSupport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ExtractionBoundariesTest extends AstTestSupport {
  @Parameterized.Parameters(name = "{0}, reverse={1}, count={2}, include={3}")
  public static Collection<Object[]> boundaries() {
    List<Object[]> cases = new ArrayList<>();
    for (String mode :
        Arrays.asList("FIRST_STATEMENT", "FIRST_BLOCK", "FIRST_RETURN", "FIRST_THROW")) {
      for (boolean reverse : new boolean[] {false, true}) {
        for (int count : new int[] {1, 2, 3}) {
          for (boolean include : new boolean[] {false, true}) {
            cases.add(new Object[] {mode, reverse, count, include});
          }
        }
      }
    }
    return cases;
  }

  private final String mode;
  private final boolean reverse;
  private final int count;
  private final boolean include;

  public ExtractionBoundariesTest(String mode, boolean reverse, int count, boolean include) {
    this.mode = mode;
    this.reverse = reverse;
    this.count = count;
    this.include = include;
  }

  @Test
  public void selectsExactlyTheRequestedBoundaryInEitherDirection() throws Exception {
    String first, second;
    if (mode.equals("FIRST_BLOCK")) {
      first = "{ x = 1; }";
      second = "{ x = 2; }";
    } else if (mode.equals("FIRST_RETURN")) {
      first = "if (x == 1) return 1;";
      second = "if (x == 2) return 2;";
    } else if (mode.equals("FIRST_THROW")) {
      first = "if (x == 1) throw new Exception();";
      second = "if (x == 2) throw new Exception();";
    } else {
      first = "x = 1;";
      second = "x = 2;";
    }
    String[] statements = {"before();", first, "between();", second, "after();"};
    String marker =
        "blocktest(\"boundary\").given(x,0)."
            + (reverse ? "start" : "end")
            + "("
            + mode
            + ", "
            + count
            + ", "
            + include
            + ");";
    String body = String.join(" ", statements);
    String source =
        fixtureText("extraction-boundaries/Demo.java")
            .replace("// TEST_BODY", reverse ? body + marker : marker + body);
    List<BlockTest> tests = new ArrayList<>();
    new ExtractionVisitor(tests)
        .visit(StaticJavaParser.parse(source), new ExtractionVisitor.Context());
    assertEquals(1, tests.size());
    BlockTest b = tests.get(0);
    assertEquals("boundary", b.testName);
    assertEquals("Demo", b.className);
    assertEquals(reverse, b.endReversed);
    assertEquals(count, b.endAtInstances);
    assertEquals(include, b.endAfter);
    int boundary;
    if (mode.equals("FIRST_STATEMENT")) {
      boundary = reverse ? 5 - count : count - 1;
    } else if (count == 3) {
      boundary = reverse ? -1 : 5;
    } else if (reverse) {
      boundary = count == 1 ? 3 : 1;
    } else {
      boundary = count == 1 ? 1 : 3;
    }
    int from = reverse ? Math.max(0, boundary + (include ? 0 : 1)) : 0;
    int to = reverse ? 5 : Math.min(5, boundary + (include ? 1 : 0));
    String expected = "{" + String.join(" ", Arrays.copyOfRange(statements, from, to)) + "}";
    assertBlock(expected, b.statements);
  }
}
