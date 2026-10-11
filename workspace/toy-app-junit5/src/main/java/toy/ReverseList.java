// FRAMEWORK: junit5
package toy;

import static org.blocktest.BTest.blocktest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ReverseList {
    private ReverseList() {}

    public static <T> List<T> reverseList(List<T> items) {
        // Column name for the parameterized expectation. The generated test supplies the value.
        List<String> expected = null;
        blocktest("reverses")
                .parameterized(new Object[][] {
                        {null, null},
                        {new ArrayList<String>(), new ArrayList<String>()},
                        {Arrays.asList("x"), Arrays.asList("x")},
                        {Arrays.asList("a", "b", "c"), Arrays.asList("c", "b", "a")}
                    }, "List<String> List<String>", items, expected)
                .checkReturnEq(expected);
        if (items == null) {
            return null;
        } else if (items.isEmpty()) {
            return new ArrayList<>();
        }

        List reversed = new ArrayList(items.size());
        for (int index = items.size() - 1; index >= 0; index--) {
            reversed.add(items.get(index));
        }
        return reversed;
    }
}
