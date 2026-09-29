class Sample {
    Sample(int p) {
        int local = 1;
    }

    void m(int p) {
        int x = 1;
        {
            String x = "s";
        }
        for (int i = 0; i < 2; i++) {
            blocktest().given(i, 0).end(FIRST_STATEMENT);
            use(i);
        }
        for (String s : values) {
            blocktest().given(s, "a").end(FIRST_STATEMENT);
            use(s);
        }
    }
}
