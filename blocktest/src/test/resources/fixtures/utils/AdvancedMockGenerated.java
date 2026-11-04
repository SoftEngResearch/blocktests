import static org.junit.Assert.*;

import org.junit.Test;

public class AdvancedMockGenerated {
  static String foo(int value) {
    throw new AssertionError("The original method should not run");
  }

  static String word(String value) {
    throw new AssertionError("The original method should not run");
  }

  static String flag(boolean value) {
    throw new AssertionError("The original method should not run");
  }

  static String combine(int first, int second) {
    throw new AssertionError("The original method should not run");
  }

  static int inner() {
    throw new AssertionError("The original method should not run");
  }

  static String outer(int value) {
    throw new AssertionError("The original method should not run");
  }

  static class Chain {
    Chain a(int value) { throw new AssertionError("The original method should not run"); }
    String b(int value) { throw new AssertionError("The original method should not run"); }
  }

  static Chain obj = new Chain();

  @Test
  public void sequential() {
    int j = 1;
    assertEquals(1, foo(j).length());
    assertEquals(2, foo(j).length());
    assertEquals(2, foo(j).length());
    j = 2;
    assertNull(foo(j));
    assertEquals(3, sequential_mock0_count[0]);
  }

  @Test
  public void wildcard() {
    int j = 1;
    assertEquals("exact", foo(j));
    j = 2;
    assertEquals("any", foo(j));
    assertEquals(1, wildcard_mock0_count[0]);
    assertEquals(1, wildcard_mock0_count[1]);
  }

  @Test
  public void separateArgumentCounts() {
    assertEquals("A", word("foo"));
    assertEquals("B", word("bar"));
    assertEquals("C", word("baz"));
    assertEquals("A", word("foo"));
    assertNull(word("other"));
    assertArrayEquals(new int[] {1, 1, 2}, separateArgumentCounts_mock0_count);
  }

  @Test
  public void booleanPriority() {
    assertEquals("exact", flag(true));
    assertEquals("any", flag(false));
  }

  @Test
  public void nullValue() {
    if (false) {
      foo(1).length();
    }
    assertNull(foo(1));
  }

  @Test
  public void multiArgumentWildcard() {
    assertEquals("any", combine(1, 2));
  }

  @Test
  public void wholeCallChain() {
    int j = 1;
    assertEquals("mapped", obj.a(j).b(2));
    j = 2;
    assertNull(obj.a(j).b(2));
  }

  @Test
  public void nestedArgument() {
    assertEquals("yes", outer(inner()));
  }
}
