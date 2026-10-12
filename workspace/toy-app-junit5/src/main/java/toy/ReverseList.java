// FRAMEWORK: junit5
package toy;

import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.FIRST_BLOCK;
import static org.blocktest.types.Flow.IfStmt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ReverseList {
    private ReverseList() {}

    public static <T> List<T> reverseList(List<T> items) {
        List<String> expected = null;
        // Generate a test for the null branch
        blocktest("nullBranch").given(items, null, "List<String>").checkFlow(IfStmt().Then()).end(FIRST_BLOCK);
        // Generate a test for the empty branch
        blocktest("emptyBranch").given(items, new ArrayList<String>(), "List<String>").checkFlow(IfStmt().ElseIf().Then()).end(FIRST_BLOCK);
        // Generate a parameterized test for the reverseList method
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
