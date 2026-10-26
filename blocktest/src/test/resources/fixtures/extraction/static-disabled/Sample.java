package sample;

public class Sample {
    static int VALUE;

    void m() {
        blocktest().given(x, 1).checkEq(x, 2);
        x = VALUE;
    }
}
