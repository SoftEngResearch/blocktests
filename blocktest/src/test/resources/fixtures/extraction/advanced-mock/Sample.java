package sample;

import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.FIRST_BLOCK;

public class Sample {
    private boolean exitMe(int x) {
        if (x > 50) return true;
        return false;
    }

    private boolean doNotExitMe(String x) {
        if (x.length() > 50) return true;
        return false;
    }

    private void tmp(int y, int z) {
        for (int i = 0; i < 100; i++) {
            int x = i * 2 + y + z;
            if (x != 1) {
                System.out.println("COOL");
            }
            if (exitMe(i)) {
                break;
            }
        }

        int hit = -1;
        blocktest().given(y, 10).given(z, 20).given(hit, -1)
                .mock("exitMe(0)", false)
                .mock("exitMe(1)", false)
                .mock("exitMe(2)", true, false, true)
                .mock("doNotExitMe(\"hi\")", false)
                .mock("doNotExitMe(\"hola\")", false)
                .mock("doNotExitMe(..)", true)
                .checkEq(hit, 2).end(FIRST_BLOCK);
        for (int i = 0; i < 3; i++) {
            int x = i * 2 + y + z;
            if (x != 0) {
                System.out.println("COOL");
            }
            if (exitMe(i)) {
                if (i == 2) {
                    boolean a = exitMe(i);
                    boolean b = exitMe(i);
                    boolean c = doNotExitMe("hi");
                    boolean d = doNotExitMe("bye");
                    if (a || !b || c || !d) {
                        throw new AssertionError("Unexpected mock sequence or wildcard value");
                    }
                    System.out.println(a + " and " + b + " and " + c + " and " + d);
                }
                hit = i;
                break;
            }
        }
    }
}
