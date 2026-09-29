class Demo {
    String text;

    void m() {
        blocktest().mock(call()).noInit(this.text).end(FIRST_STATEMENT);
        work();
    }
}
