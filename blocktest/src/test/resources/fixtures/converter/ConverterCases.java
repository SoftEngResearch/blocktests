class ConverterCases {
    void targetWithGivens() {
        int x = 99;
        x += y;
    }

    void setupWithGivens() {
        x++;
    }

    void expectedWithGivens() {
        int x = 1;
        int y = 2;
        x++;
        x += y;
        assertEquals(4, x);
    }

    void missingLocals() {
        x = 1;
        global = 2;
        missing = null;
        unknown = null;
        int declared = 3;
    }

    void topLevelDeclarations() {
        work();
        int x, y = 2;
        if (true) {
            int nested;
        }
        x = 1;
    }

    void expectedTopLevelDeclarations() {
        int x;
        work();
        int y = 2;
        if (true) {
            int nested;
        }
        x = 1;
    }

    void returnExpressionEffects() {
        if (x == 1) return ++x;
        return;
    }

    void flowBranches() {
        if (true) {
            int n = 1;
        } else {
            int n = 2;
        }
    }

    void coverageTarget() {
        work();
        return 7;
    }
}
