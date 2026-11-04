package sample;

import java.util.ArrayList;
import java.util.List;

import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.FIRST_BLOCK;

public class Sample {
    public <T> void exercise(T x) {
        blocktest("generic").type("T", "String").given(x, "Foo")
                .checkEq(x, "Foo").end(FIRST_BLOCK);
        if (x != null) {
            List<T> list = new ArrayList<>();
            list.add(x);
            T y = list.iterator().next();
            List<? extends T> view = list;
            T[] unused = null;
            if (!y.equals(x)) {
                throw new AssertionError("Wrong element");
            }
            String label = "T";
            int TT = label.length();
        }
    }
}
