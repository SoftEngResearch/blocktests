class Demo {
    Demo() {
        // @blocktest().given(x,1)
        x++;
    }

    void m() {
        if (a) {
            // @blocktest().given(x,2);
            x++;
        } else {
            // @lambdatest().args(3)
            x++;
        }

        for (;;) {
            // @blocktest().given(x,4)
            x++;
        }
        for (int n : values) {
            // @blocktest().given(x,5)
            x++;
        }
        while (a) {
            // @blocktest().given(x,6)
            x++;
        }
        do {
            // @blocktest().given(x,7)
            x++;
        } while (a);

        try {
            // @blocktest().given(x,8)
            x++;
        } finally {
            // @blocktest().given(x,9)
            x++;
        }

        switch (x) {
            case 1:
                // @blocktest().given(x,10)
                x++;
                break;
        }
        // ordinary comment
        x++;
    }
}
