class Input {
    void m() {
        int x = 0;
        blocktest("inc").given(x, 1).checkEq(x, 2);
        x++;
    }
}
