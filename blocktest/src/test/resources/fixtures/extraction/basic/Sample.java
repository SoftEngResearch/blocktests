package sample;

import static org.blocktest.BTest.blocktest;

public class Sample {
    public void m() {
        int x = 0;
        blocktest("increment").given(x, 1).checkEq(x, 2);
        x++;
    }
}
