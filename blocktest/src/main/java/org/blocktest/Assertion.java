package org.blocktest;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Assertion {

    public static void assertArrayEquals(Object expected, Object actual, double delta) {
        if (expected == null && actual == null) return;

        if (expected == null || actual == null)
            throw new AssertionError("one is null, other is not (expected=" + expected + ", actual=" + actual + ")");

        if (!expected.getClass().isArray())
            throw new AssertionError("expected is not an array: " + expected.getClass());

        if (!actual.getClass().isArray())
            throw new AssertionError("actual is not an array: " + actual.getClass());

        int expLen = Array.getLength(expected);
        int actLen = Array.getLength(actual);
        if (expLen != actLen)
            throw new AssertionError("length differs (expected=" + expLen + ", actual=" + actLen + ")");

        for (int i = 0; i < expLen; i++) {
            Object expElem = Array.get(expected, i);
            Object actElem = Array.get(actual, i);

            if (expElem != null && expElem.getClass().isArray()) {
                assertArrayEquals(expElem, actElem, delta);
            } else if (expElem instanceof Double || expElem instanceof Float) {
                double e = ((Number) expElem).doubleValue();
                double a = ((Number) actElem).doubleValue();
                if (Math.abs(e - a) > delta)
                    throw new AssertionError("expected=" + e + ", actual=" + a + ", delta=" + delta);
            } else {
                assertEquals(expElem, actElem);
            }
        }
    }

    public static void assertEquals(Object expected, Object actual) {
        if (expected == null && actual == null) return;

        if (expected == null || actual == null)
            throw new AssertionError("one is null, other is not (expected=" + expected + ", actual=" + actual + ")");

        if (expected instanceof Number && actual instanceof Number) {
            double e = ((Number) expected).doubleValue();
            double a = ((Number) actual).doubleValue();
            if (e != a)
                throw new AssertionError("expected=" + expected + ", actual=" + actual);
            return;
        }
        if (expected instanceof CharSequence && actual instanceof CharSequence) {
            if (!expected.toString().equals(actual.toString()))
                throw new AssertionError("expected=" + expected + ", actual=" + actual);
            return;
        }

        if (!isCompatibleType(expected, actual))
            throw new AssertionError("different classes (expected=" + expected.getClass() + ", actual=" + actual.getClass() + ")");

        if (expected.getClass().isArray()) {
            assertArrayEquals(expected, actual, 0.001);
            return;
        }

        if (hasOverriddenEquals(expected.getClass())) {
            if (!expected.equals(actual))
                throw new AssertionError("expected=" + expected + ", actual=" + actual);
        } else {
            if (!reflectivelyEqualPrimitives(expected, actual))
                throw new AssertionError("expected=" + expected + ", actual=" + actual);
        }
    }

    private static boolean isCompatibleType(Object expected, Object actual) {
        Class<?> namedA = firstNamedSuperclass(expected.getClass());
        Class<?> namedB = firstNamedSuperclass(actual.getClass());
        return namedA.equals(namedB);
    }

    private static Class<?> firstNamedSuperclass(Class<?> clazz) {
        Class<?> current = clazz;
        while (current != null && !current.equals(Object.class)) {
            if (!current.isAnonymousClass()) {
                return current;
            }
            current = current.getSuperclass();
        }
        return Object.class;
    }

    private static boolean hasOverriddenEquals(Class<?> clazz) {
        try {
            Class<?> declaringClass = clazz.getMethod("equals", Object.class).getDeclaringClass();
            return !declaringClass.equals(Object.class);
        } catch (NoSuchMethodException e) {
            // Should never happen since equals() is defined on Object
            return false;
        }
    }

    private static boolean reflectivelyEqualPrimitives(Object a, Object b) {
        List<Field> primitiveFields = collectPrimitiveFields(a.getClass());

        for (Field field : primitiveFields) {
            field.setAccessible(true);
            try {
                Object valA = field.get(a);
                Object valB = field.get(b);

                if (valA == null && valB == null) continue;
                if (valA == null || valB == null) {
                    System.out.println("Non-equal field: " + field.getName() + ", valA=" + valA + ", valB=" + valB);
                    return false;
                }
                if (!fieldsAreEqual(valA, valB, field.getType())) {
                    System.out.println("Non-equal field: " + field.getName() + ", valA=" + valA + ", valB=" + valB);
                    return false;
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException("Cannot access field: " + field.getName(), e);
            }
        }
        return true;
    }

    private static boolean fieldsAreEqual(Object valA, Object valB, Class<?> fieldType) {
        if (valA == valB) return true;
        if (valA == null || valB == null) return false;

        if (fieldType.isArray() && fieldType.getComponentType().isPrimitive()) {
            assertArrayEquals(valA, valB, 0.001);
            return true;
        }

        return valA.equals(valB);
    }

    private static List<Field> collectPrimitiveFields(Class<?> clazz) {
        List<Field> result = new ArrayList<>();
        Class<?> current = clazz;

        while (current != null && !current.equals(Object.class)) {
            for (Field field : current.getDeclaredFields()) {
                Class<?> type = field.getType();
                if (isPrimitiveOrWrapper(type) || isPrimitiveArray(type)) {
                    result.add(field);
                }
            }
            current = current.getSuperclass();
        }
        return result;
    }

    private static boolean isPrimitiveOrWrapper(Class<?> type) {
        return type.isPrimitive()
                || type == Boolean.class
                || type == Byte.class
                || type == Character.class
                || type == Short.class
                || type == Integer.class
                || type == Long.class
                || type == Float.class
                || type == Double.class
                || type == String.class;
    }

    private static boolean isPrimitiveArray(Class<?> type) {
        return type.isArray() && (type.getComponentType().isPrimitive() || type.getComponentType() == String.class);
    }

    public static void assertEquals(Object expected, Object actual, double delta) {
        if (expected == null && actual == null) return;

        if (expected == null || actual == null)
            throw new AssertionError("one is null, other is not (expected=" + expected + ", actual=" + actual + ")");

        if (expected instanceof Double || expected instanceof Float) {
            double e = ((Number) expected).doubleValue();
            double a = ((Number) actual).doubleValue();
            if (Math.abs(e - a) > delta)
                throw new AssertionError("expected=" + e + ", actual=" + a + ", delta=" + delta);
        } else {
            assertEquals(expected, actual);
        }
    }

    public static void assertTrue(boolean condition) {
        if (!condition)
            throw new AssertionError("expected condition to be true, but was false");
    }

    public static void assertFalse(boolean condition) {
        if (condition)
            throw new AssertionError("expected condition to be false, but was true");
    }
}
