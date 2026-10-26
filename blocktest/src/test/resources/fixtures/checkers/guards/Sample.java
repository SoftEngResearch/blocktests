class Sample {
    public void m(int a, int[] values) {
        if (a > 0) {
            while (a > 0) {
                for (int i = 0; i < 1; i++) {
                    for (int v : values) {
                        do {
                            switch (a) {
                                case 1:
                                    try {
                                        work();
                                    } catch (Exception e) {
                                        synchronized (this) {
                                            work();
                                        }
                                    }
                            }
                        } while (false);
                    }
                }
            }
        }
    }
}
