class SimpleVisitorsFixtures {
    void declarationsBefore() {
        int x = 1, y = 2;
        int x2;
        if (true) {
            int x;
            int z;
        }
        use(y);
    }

    void declarationsAfter() {
        int y = 2;
        if (true) {
            int z;
        }
        use(y);
    }

    void variablesToCollect() {
        int x = 1, y;
        x = 2;
        obj.field = 3;
        arr[0] = 4;
        try {
            y += x;
        } catch (Exception e) {
            int z = 1;
        }
    }

    void thisBefore() {
        this.x = obj.x;
        use(this, Outer.this);
    }

    void thisFieldsAfter() {
        x = obj.x;
        use(this, Outer.this);
    }

    void thisVariablesAfter() {
        x = obj.x;
        use(new Owner(), Outer.this);
    }

    void quotedArgumentsBefore() {
        blocktest().given("a[0]", 1).checkEq("a[0]", 2);
        blocktest().given(x, 1);
        blocktest().given();
        obj.given("x", 1);
        given("x", 1);
        other().given("x", 1);
        blocktest().checkTrue("x");
    }

    void quotedArgumentsAfter() {
        blocktest().given(a[0], 1).checkEq(a[0], 2);
        blocktest().given(x, 1);
        blocktest().given();
        obj.given("x", 1);
        given("x", 1);
        other().given("x", 1);
        blocktest().checkTrue("x");
    }

    void breaksBefore() {
        if (x) {
            break;
        }
        continue;
        for (;;) {
            break;
            continue;
        }
        for (int n : a) {
            break;
            continue;
        }
        while (x) {
            break;
            continue;
        }
        do {
            break;
            continue;
        } while (x);
        switch (x) {
            case 1:
                break;
            default:
                continue;
        }
    }

    void breaksAfter() {
        if (x) {
            return;
        }
        return;
        for (;;) {
            break;
            continue;
        }
        for (int n : a) {
            break;
            continue;
        }
        while (x) {
            break;
            continue;
        }
        do {
            break;
            continue;
        } while (x);
        switch (x) {
            case 1:
                break;
            default:
                continue;
        }
    }

    void lambdaBefore() {
        blocktest().given(x, 1).end(LAMBDA);
        int skip = 0;
        consume((int x) -> {
            return x + 1;
        });
    }

    void lambdaAfter() {
        int skip = 0;
        consume((int x) -> {
            blocktest().given(x, 1).argsVar(x);
            return x + 1;
        });
    }

    void lambdaArgumentsBefore() {
        lambdatest().args(1, 2);
        consume((x, y) -> {
            return x + y;
        });
    }

    void lambdaArgumentsAfter() {
        consume((x, y) -> {
            lambdatest().args(1, 2).argsVar(x, y);
            return x + y;
        });
    }

    void ordinaryEnd() {
        blocktest().given(x, 1).end(FIRST_BLOCK);
        x++;
    }

    void invalidLambdaEnd() {
        blocktest().given(x, 1).end();
    }

    void expressionLambdaBefore() {
        blocktest().given(x, 1).end(LAMBDA);
        consume(x -> x + 1);
        consume(x -> {
            return x;
        });
    }

    void expressionLambdaAfter() {
        consume(x -> x + 1);
        consume(x -> {
            blocktest().given(x, 1).argsVar(x);
            return x;
        });
    }

    void noLambda() {
        blocktest().given(x, 1).end(LAMBDA);
        work();
    }

    void noLambdaAfter() {
        work();
    }

    void nestedLambda() {
        if (true) blocktest().given(x, 1).end(LAMBDA);
        consume(x -> {
            return x;
        });
    }
}
