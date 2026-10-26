class Sample {
    int field;

    int recordsInputs(int x, boolean flag) {
        int y = 3;
        y = 4;
        blocktest("increment").given(x, 2).given(y, 3).checkEq(x + y, 6);
        x++;
        use(y);
        unrelated();
        return x;
    }

    int markersOverride(int x, boolean flag) {
        blocktest("first").given(x, 1).checkEq(x, 3);
        x++;
        blocktest("second").given(x, 2).checkEq(x, 3);
        use(x);
        blocktest("unrelated").end();
        blocktest("first").end();
        x++;
    }

    int reversedMarker(int x, boolean flag) {
        ignore();
        blocktest("r").start();
        x = 1;
        x++;
        blocktest("r").given(x, 0).checkEq(x, 2).start(LAST_REFERENCE);
    }

    int reverseReference(int x, boolean flag) {
        ignore();
        use(x);
        x++;
        unrelated();
        blocktest().given(x, 1).checkEq(x, 2).start(LAST_REFERENCE);
    }

    int firstAssignment(int x, boolean flag) {
        int y = 0;
        blocktest().given(x, 1).checkEq(x + y, 4).end(FIRST_ASSIGN);
        obj.f = 1;
        arr[0]++;
        x++;
        y = 2;
        unrelated();
    }

    int quotedFields(int x, boolean flag) {
        blocktest().given("this.field", 1).given(missing, 2, "long")
                .given(array[0], 3, "int").checkEq("this.field", 2)
                .end(FIRST_STATEMENT);
        field++;
    }

    int lambdaArguments(int x, boolean flag) {
        consume((int a, String b) -> {
            lambdatest().args(1, "two").checkReturnEq(a).argsVar(a, b);
            return a;
        });
    }

    int mockReplacement(int x, boolean flag) {
        blocktest().mock("obj.call(..)", 1, 2).mock(other(), 3)
                .mock("field", 4).mock("remove()").end(FIRST_STATEMENT, 5);
        int a = obj.call(5);
        int b = obj.call(6);
        int c = other();
        int d = field;
        remove();
    }

    int setupAndFlow(int x, boolean flag) {
        blocktest("caseName").given(x, 1)
                .setup("x = 5;").delay(7)
                .expect(IllegalArgumentException.class)
                .checkFlow(IfStmt, Then)
                .checkControlFlow("if,");
        x++;
        return x;
    }

    int serializedInputs(int x, boolean flag) {
        blocktest("caseName").given(x, 1)
                .given(text, "@value.txt")
                .given(arr, "@value.java", "int[]")
                .given(obj, "@value.xml", "Object")
                .checkEq(x, "@value.txt")
                .checkReturnEq("@value.java", "int[]")
                .checkControlFlow("@flow.txt")
                .end(FIRST_STATEMENT);
        x++;
        return x;
    }

    int serializedComparisons(int x, boolean flag) {
        blocktest("caseName").given(x, 1)
                .checkEq(x, "@value.xml", "int[]")
                .checkReturnEq("@value.txt")
                .mock(call(), "@value.txt")
                .mock(other(), "@value.java", "int[]")
                .end(FIRST_STATEMENT);
        x++;
        return x;
    }
}
