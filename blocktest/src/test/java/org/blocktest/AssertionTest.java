package org.blocktest;

import static org.junit.Assert.*;

import java.util.Arrays;
import org.junit.Test;

public class AssertionTest {
  private static void fails(String message, Runnable action) {
    AssertionError error = assertThrows(AssertionError.class, action::run);
    assertTrue(error.getMessage(), error.getMessage().contains(message));
  }

  @Test
  public void nullAndTypeMismatches() {
    Assertion.assertEquals(null, null);
    fails("one is null", () -> Assertion.assertEquals(null, 1));
    fails("one is null", () -> Assertion.assertEquals(1, null));
    fails("different classes", () -> Assertion.assertEquals(1, "1"));
    fails("different classes", () -> Assertion.assertEquals("1", 1));
    fails("different classes", () -> Assertion.assertEquals(new int[] {1}, new long[] {1}));
  }

  @Test
  public void comparesNumbersAcrossWrapperTypesAndCharacterSequencesByContent() {
    Assertion.assertEquals(7, 7L);
    Assertion.assertEquals(7.25f, 7.25d);
    fails("expected=7", () -> Assertion.assertEquals(7, 8L));
    Assertion.assertEquals(new StringBuilder("abc"), new StringBuffer("abc"));
    fails("expected=abc", () -> Assertion.assertEquals(new StringBuilder("abc"), "abd"));
  }

  @Test
  public void honorsOverriddenEquals() {
    Assertion.assertEquals(Arrays.asList(1, 2), Arrays.asList(1, 2));
    fails("expected=", () -> Assertion.assertEquals(Arrays.asList(1, 2), Arrays.asList(2, 1)));
    Assertion.assertEquals(new Object(), new Object());
  }

  @Test
  public void arrayValidationAndNestedArrays() {
    Assertion.assertArrayEquals(null, null, 0);
    fails("one is null", () -> Assertion.assertArrayEquals(null, new int[0], 0));
    fails("one is null", () -> Assertion.assertArrayEquals(new int[0], null, 0));
    fails("expected is not an array", () -> Assertion.assertArrayEquals(1, new int[0], 0));
    fails("actual is not an array", () -> Assertion.assertArrayEquals(new int[0], 1, 0));
    fails("length differs", () -> Assertion.assertArrayEquals(new int[1], new int[0], 0));
    Assertion.assertArrayEquals(new int[0], new int[0], 0);
    Assertion.assertArrayEquals(
        new Object[] {null, new int[] {1, 2}, "x"}, new Object[] {null, new int[] {1, 2}, "x"}, 0);
    fails("expected=2", () -> Assertion.assertArrayEquals(new int[] {1, 2}, new int[] {1, 3}, 0));
    Assertion.assertEquals(new int[] {1, 2}, new int[] {1, 2});
    fails(
        "expected=2",
        () ->
            Assertion.assertArrayEquals(
                new Object[] {new int[] {1, 2}}, new Object[] {new int[] {1, 3}}, 0));
    fails("expected=2", () -> Assertion.assertEquals(new int[] {1, 2}, new int[] {1, 3}));
  }

  @Test
  public void floatingPointDeltaIncludesItsBoundary() {
    Assertion.assertEquals(null, null, 0);
    fails("one is null", () -> Assertion.assertEquals(null, 1, 0));
    fails("one is null", () -> Assertion.assertEquals(1, null, 0));
    Assertion.assertEquals(1.0d, 1.25d, .25);
    Assertion.assertEquals(1.0f, .75f, .25);
    Assertion.assertEquals(1, 1L, 0);
    fails("delta=0.25", () -> Assertion.assertEquals(1.0, 1.5, .25));
    fails("delta=0.25", () -> Assertion.assertEquals(1.0f, .5f, .25));
    fails("expected=abc", () -> Assertion.assertEquals("abc", "abd", .25));
    Assertion.assertArrayEquals(new double[] {1, 2}, new double[] {1.25, 1.75}, .25);
    Assertion.assertArrayEquals(new float[] {1, 2}, new float[] {.75f, 2.25f}, .25);
    fails(
        "delta=0.25", () -> Assertion.assertArrayEquals(new float[] {1}, new float[] {1.5f}, .25));
    fails(
        "delta=0.25", () -> Assertion.assertArrayEquals(new double[] {1}, new double[] {.5}, .25));
  }

  static class Parent {
    private int inherited = 4;
  }

  static class Fields extends Parent {
    boolean z = true;
    byte b = 1;
    char c = 'x';
    short s = 2;
    int i = 3;
    long l = 4;
    float f = 5;
    double d = 6;
    Boolean wz = true;
    Byte wb = 1;
    Character wc = 'x';
    Short ws = 2;
    Integer wi = 3;
    Long wl = 4L;
    Float wf = 5f;
    Double wd = 6d;
    String text = "text";
    String nullable;
    int[] values = {1, 2};
    Object ignored = new Object();
  }

  @Test
  public void reflectiveEqualityChecksInheritedPrimitiveAndWrapperFields() throws Exception {
    Assertion.assertEquals(new Fields(), new Fields());
    for (java.lang.reflect.Field field : Fields.class.getDeclaredFields()) {
      if (field.isSynthetic()
          || field.getName().equals("ignored")
          || field.getName().equals("nullable")) continue;
      Fields changed = new Fields();
      if (field.getType().isPrimitive()) {
        if (field.getType() == boolean.class) field.setBoolean(changed, false);
        else if (field.getType() == char.class) field.setChar(changed, 'y');
        else if (field.getType() == byte.class) field.setByte(changed, (byte) 0);
        else if (field.getType() == short.class) field.setShort(changed, (short) 0);
        else if (field.getType() == int.class) field.setInt(changed, 0);
        else if (field.getType() == long.class) field.setLong(changed, 0);
        else if (field.getType() == float.class) field.setFloat(changed, 0);
        else field.setDouble(changed, 0);
      } else field.set(changed, null);
      fails("expected=", () -> Assertion.assertEquals(new Fields(), changed));
      fails("expected=", () -> Assertion.assertEquals(changed, new Fields()));
    }
    Fields changed = new Fields();
    ((Parent) changed).inherited = 9;
    fails("expected=", () -> Assertion.assertEquals(new Fields(), changed));
    changed.values = new int[] {1, 3};
    fails("expected=2", () -> Assertion.assertEquals(new Fields(), changed));
  }

  @Test
  public void anonymousSubclassesUseTheFirstNamedSuperclass() {
    Assertion.assertEquals(new Fields(), new Fields() {});
    Assertion.assertEquals(new Object() {}, new Object() {});
    fails("different classes", () -> Assertion.assertEquals(new Fields() {}, new Parent()));
  }

  @Test
  public void truthAssertionsRejectOppositeValues() {
    Assertion.assertTrue(true);
    Assertion.assertFalse(false);
    fails("expected condition to be true", () -> Assertion.assertTrue(false));
    fails("expected condition to be false", () -> Assertion.assertFalse(true));
  }
}
