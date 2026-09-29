package sample;

public class Sample {
    static int VALUE;
    static int OTHER;

    void m() {
        blocktest().given("VALUE", 1, "int").end(FIRST_STATEMENT, 3);
        use(VALUE);
        use(OTHER);
        use(obj.field);
    }
}
