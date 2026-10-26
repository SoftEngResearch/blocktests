package org.blocktest;

import static org.junit.Assert.*;

import org.blocktest.types.EndAt;
import org.blocktest.types.Flow;
import org.junit.Test;

public class BTestTest {
  @Test
  public void fluentDslIsInertAndReturnsTheSingletonForEveryOverload() {
    BTest b = BTest.blocktest();
    assertSame(b, BTest.blocktest("name"));
    assertSame(b, BTest.lambdatest());
    assertSame(b, BTest.lambdatest("name"));
    BTest[] results = {
      b.checkEq(1, 2),
      b.checkEq(1, 2, .1),
      b.checkReturnEq(1),
      b.checkReturnEq(1, .1),
      b.given(null, null),
      b.given(null, null, "Object"),
      b.noInit(),
      b.noInit(1, 2),
      b.args(1, 2),
      b.setup("not Java"),
      b.setup(
          () -> {
            throw new AssertionError("setup must not execute");
          }),
      b.checkTrue(false),
      b.checkFalse(true),
      b.checkReturnTrue(),
      b.checkReturnFalse(),
      b.checkNotReturn(),
      b.checkFlow(Flow.IfStmt, Flow.Then),
      b.checkControlFlow("if,"),
      b.expect(Exception.class),
      b.end(),
      b.end((Object) null),
      b.end(EndAt.FIRST_BLOCK),
      b.end(EndAt.FIRST_BLOCK, 2),
      b.end("label", false),
      b.end(EndAt.FIRST_BLOCK, false),
      b.end(EndAt.FIRST_BLOCK, 2, false),
      b.start("label"),
      b.start(EndAt.FIRST_BLOCK),
      b.start(EndAt.FIRST_BLOCK, 2),
      b.start("label", false),
      b.start(EndAt.FIRST_BLOCK, false),
      b.start(EndAt.FIRST_BLOCK, 2, false),
      b.mock("call()", 1),
      b.mockStr("call()", 1),
      b.delay(Long.MAX_VALUE)
    };
    for (BTest result : results) assertSame(b, result);
    assertTrue(BTest.group());
  }

  public static class Payload {
    public String name;
    public Integer value;
  }

  @Test
  public void xmlIgnoresUnknownElementsAndRoundTripsObjects() {
    Payload original = new Payload();
    original.name = "sample";
    original.value = 42;
    String xml =
        BTest.xstream.toXML(original).replace("</value>", "</value><unknown>ignored</unknown>");
    Payload parsed = (Payload) BTest.xstream.fromXML(xml);
    assertEquals("sample", parsed.name);
    assertEquals(Integer.valueOf(42), parsed.value);
  }

  @Test
  public void xmlToleratesUnmarshallableFields() {
    Payload p = new Payload();
    p.name = "kept";
    p.value = 42;
    Payload parsed =
        (Payload) BTest.xstream.fromXML(BTest.xstream.toXML(p).replace("42", "not-an-integer"));
    assertEquals("kept", parsed.name);
    assertNull(parsed.value);
  }

  @Test
  public void xmlMapsEvosuiteThrowableNamesToJdkTypes() {
    assertSame(
        java.io.IOException.class,
        BTest.xstream.getMapper().realClass("org.evosuite.runtime.mock.java.io.MockIOException"));
    assertSame(
        RuntimeException.class,
        BTest.xstream
            .getMapper()
            .realClass("org.evosuite.runtime.mock.java.lang.MockRuntimeException"));
    assertSame(
        Throwable.class,
        BTest.xstream.getMapper().realClass("org.evosuite.runtime.mock.java.lang.MockThrowable"));
    assertSame(String.class, BTest.xstream.getMapper().realClass("string"));
  }
}
