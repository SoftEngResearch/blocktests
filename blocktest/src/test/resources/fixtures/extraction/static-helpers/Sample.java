package sample;

import static org.blocktest.BTest.blocktest;

public class Sample {
    private static int VALUE = 3;

    private static int helper(int n) {
        return n + 1;
    }

    private static int helper() {
        return 0;
    }

    private static int unused() {
        return 9;
    }

    void m() {
        int x = 0;
        blocktest("helperTest").given(x, 1).checkEq(x, 2);
        x = helper(x);
        use(VALUE);
    }
}
