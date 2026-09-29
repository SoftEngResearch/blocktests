class ControlFlowFixtures {
    void ifElseIfBefore() {
        if (a) yes();
        else if (b) maybe();
        else no();
        if (c) {
            done();
        }
    }

    void ifElseIfAfter() {
        if (a) {
            trace.append("if,");
            yes();
        } else if (b) {
            trace.append("else if,");
            maybe();
        } else {
            trace.append("else,");
            no();
        }
        if (c) {
            trace.append("if,");
            done();
        } else {
            trace.append("else,");
        }
    }

    void loopsBefore() {
        for (;;) work();
        for (int x : xs) {
            work();
        }
        while (x) work();
        do work(); while (x);
        switch (x) {
            case 1:
                work();
                break;
            default:
                other();
        }
    }

    void loopsAfter() {
        for (;;) {
            trace.append("for,");
            work();
        }
        for (int x : xs) {
            trace.append("enhanced for,");
            work();
        }
        while (x) {
            trace.append("while,");
            work();
        }
        do {
            trace.append("do,");
            work();
        } while (x);
        switch (x) {
            case 1:
                trace.append("case_1,");
                work();
                break;
            default:
                trace.append("default,");
                other();
        }
    }

    void nestedTypesBefore() {
        class Local {
            void m() {
                if (a) work();
            }
        }
        new Object() {
            void m() {
                if (b) work();
            }
        };
        new Task(() -> {
            while (c) work();
        });
        consume(() -> {
            if (d) work();
        });
    }

    void selectedThenBefore() {
        if (a) {
            if (b) {
                yes();
            }
        } else {
            if (c) {
                no();
            }
        }
    }

    void selectedThenAfter() {
        if (a) {
            _blocktest_flow_path[0] = true;
            if (b) {
                _blocktest_flow_path[1] = true;
                yes();
            }
        } else {
            if (c) {
                no();
            }
        }
    }

    void elseIfPathBefore() {
        if (a) {
            if (x) {
                skip();
            }
        } else if (b) {
            if (y) {
                skip();
            }
        } else if (c) {
            hit();
        } else {
            miss();
        }
    }

    void elsePathBefore() {
        if (a) {
            if (x) {
                skip();
            }
        } else {
            hit();
        }
    }

    void elsePathAfter() {
        if (a) {
            if (x) {
                skip();
            }
        } else {
            _blocktest_flow_p[0] = true;
            hit();
        }
    }

    void unselectedBefore() {
        if (a) {
            hit();
        }
    }

    void unselectedElseIfBefore() {
        if (a) {
            hit();
        } else if (b) {
            miss();
        }
    }

    void elseIfWithoutElseBefore() {
        if (a) {
            skip();
        } else if (b) {
            hit();
        }
    }

    void elseIfWithoutElseAfter() {
        if (a) {
            skip();
        } else if (b) {
            _blocktest_flow_p[0] = true;
            hit();
        }
    }

    void elseIfWithElseBefore() {
        if (a) {
            skip();
        } else {
            hit();
        }
    }
}
