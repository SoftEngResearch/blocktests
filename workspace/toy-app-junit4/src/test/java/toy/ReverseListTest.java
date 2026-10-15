package toy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;

public class ReverseListTest {
    @Test
    public void reversesAList() {
        assertEquals(Arrays.asList("c", "b", "a"), ReverseList.reverseList(Arrays.asList("a", "b", "c")));
    }

    @Test
    public void emptyListStaysEmpty() {
        assertEquals(Collections.emptyList(), ReverseList.reverseList(Collections.emptyList()));
    }

    @Test
    public void nullStaysNull() {
        assertNull(ReverseList.reverseList(null));
    }
}
