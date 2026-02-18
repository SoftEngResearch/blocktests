package org.blocktest;

import org.blocktest.types.EndAt;
import org.blocktest.types.Flow;

public class BTest {

    public static BTest btest = new BTest();

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


    public BTest checkFlow(Flow... value) {
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

