package toy;

import static org.blocktest.BTest.propblocktest;
import static org.blocktest.types.EndAt.FIRST_BLOCK;
import static org.blocktest.types.Flow.IfStmt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ReverseList {
    private ReverseList() {}

    public static <T> List<T> reverseList(List<T> items) {
        propblocktest("nullBranch").given(items, null, "List<String>").checkFlow(IfStmt().Then()).end(FIRST_BLOCK);
        propblocktest("emptyBranch").given(items, new ArrayList<String>(), "List<String>")
                .checkFlow(IfStmt().ElseIf().Then()).end(FIRST_BLOCK);
        if (items == null) {
            return null;
        } else if (items.isEmpty()) {
            return new ArrayList<>();
        }

        // propblocktest("reversedOrder").given(items, Arrays.asList("a", "b", "c"), "List<String>")
        //         .checkEq("reversed", Arrays.asList("c", "b", "a")).end(FIRST_BLOCK);
        // propblocktest("oneElement").given(items, Arrays.asList("x"), "List<String>")
        //         .checkEq("reversed", Arrays.asList("x")).end(FIRST_BLOCK);
        propblocktest("preservesSize").givenAll(items, "List<String>")
                .checkEq("reversed.size()", items.size()).end(FIRST_BLOCK);
        propblocktest("reversesTwice").givenAll(items, "List<String>")
                .assume(items != null && !items.isEmpty())
                .checkEq("reverseList(reverseList(items))", items).end(FIRST_BLOCK);
        List reversed = new ArrayList(items.size());
        for (int index = items.size() - 1; index >= 0; index--) {
            reversed.add(items.get(index));
        }
        return reversed;
    }
}
