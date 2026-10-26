class StackedComments {
    void blocktestFirst() {
        // @blocktest().given(x,1)
        // @blocktest().given(x,2);
        x++;
    }

    void lambdatestFirst() {
        // @lambdatest().args(1);
        // @blocktest().given(x,2);
        x++;
    }

    void ordinaryThenBlocktest() {
        // ordinary explanation
        // @blocktest().given(x,1)
        x++;
    }

    void ordinaryOnly() {
        // ordinary explanation
        x++;
    }
}
