class UtilCases {
    void renameBefore() {
        blocktest().given(a[0], 1).checkEq(a[0], 2);
        int n = a[0];
        use(obj.field);
    }

    void renameAfter() {
        blocktest().given(a[0], 1).checkEq(a[0], 2);
        int n = a__0;
        use(obj__field);
    }

    void repeatedMock() {
        int a = TEST_EXPRESSION;
        int b = TEST_EXPRESSION;
        int c = TEST_EXPRESSION;
        untouched();
    }

    void removedMock() {
        int a = TEST_EXPRESSION;
        untouched();
    }

    void wildcardMock() {
        int n = obj.a(1).b(2);
        consume(other(7));
    }
}
