package org.blocktest;

import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.mapper.MapperWrapper;
import org.blocktest.types.EndAt;
import org.blocktest.types.Flow;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;

import java.io.IOException;
import java.lang.reflect.Field;

public class BTest {

    public static BTest btest = new BTest();
    public static XStream xstream = new XStream() {
        @Override
        protected MapperWrapper wrapMapper(MapperWrapper next) {
            return new MapperWrapper(next) {
                @Override
                public Class realClass(String elementName) {
                    if (elementName.equals("org.evosuite.runtime.mock.java.io.MockIOException")) {
                        return IOException.class;
                    }
                    if (elementName.equals("org.evosuite.runtime.mock.java.lang.MockRuntimeException")) {
                        return RuntimeException.class;
                    }
                    if (elementName.equals("org.evosuite.runtime.mock.java.lang.MockThrowable")) {
                        return Throwable.class;
                    }
                    return super.realClass(elementName);
                }
            };
        }
    };

    static {
        xstream.addPermission(AnyTypePermission.ANY);
        xstream.ignoreUnknownElements();
        xstream.registerConverter(new ReflectionConverter(xstream.getMapper(), xstream.getReflectionProvider()) {
            @Override
            protected Object unmarshallField(UnmarshallingContext context, Object result, Class type, Field field) {
                try {
                    return super.unmarshallField(context, result, type, field);
                } catch (Exception e) {
                    return null;
                }
            }
        }, XStream.PRIORITY_VERY_LOW);
    }

    private BTest() {
        return;
    }

    public static BTest blocktest() {
        return btest;
    }
    public static BTest lambdatest() {
        return btest;
    }

    public static BTest blocktest(String name) {
        return btest;
    }
    public static BTest lambdatest(String name) {
        return btest;
    }

    public BTest checkEq(Object actual, Object expected) {
        return btest;
    }

    public BTest checkEq(Object actual, Object expected, Object delta) {
        return btest;
    }

    public BTest checkReturnEq(Object expected) {
        return btest;
    }

    public BTest checkReturnEq(Object expected, Object delta) {
        return btest;
    }

    public BTest given(Object variable, Object value) {
        return btest;
    }

    public BTest noInit(Object... variables) {
        return btest;
    }

    public BTest args(Object... value) {
        return btest;
    }

    public BTest given(Object variable, Object value, Object type) {
        return btest;
    }

    public BTest setup(String setupFunction) { // Sadly this cannot take Runnable otherwise code will not compile
        return btest;
    }

    public BTest setup(Runnable setupFunction) {
        return btest;
    }

    public BTest checkTrue(Object value) {
        return btest;
    }

    public BTest checkFalse(Object value) {
        return btest;
    }

    public BTest checkReturnTrue() {
        return btest;
    }

    public BTest checkReturnFalse() {
        return btest;
    }

    public BTest checkNotReturn() {
        return btest;
    }

    public BTest checkFlow(Flow... value) {
        return btest;
    }

    public BTest checkControlFlow(Object value) {
        return btest;
    }

    public BTest expect(Object value) {
        return btest;
    }

    // end()
    public BTest end() {
        return btest;
    }
    public BTest end(Object value) {
        return btest;
    }
    public BTest end(EndAt value) {
        return btest;
    }
    public BTest end(EndAt value, int at) {
        return btest;
    }
    public BTest end(Object value, boolean endAfter) {
        return btest;
    }
    public BTest end(EndAt value, boolean endAfter) {
        return btest;
    }
    public BTest end(EndAt value, int at, boolean endAfter) {
        return btest;
    }

    // start()
    public BTest start(Object value) {
        return btest;
    }
    public BTest start(EndAt value) {
        return btest;
    }
    public BTest start(EndAt value, int at) {
        return btest;
    }
    public BTest start(Object value, boolean endAfter) {
        return btest;
    }
    public BTest start(EndAt value, boolean endAfter) {
        return btest;
    }
    public BTest start(EndAt value, int at, boolean endAfter) {
        return btest;
    }

    public BTest mock(Object... value) {
        return btest;
    }
    public BTest mockStr(Object... value) {
        return btest;
    }

    public BTest delay(long time) {
        return btest;
    }

    public static boolean group() {
        return true;
    }
}

