package spullara.util.concurrent;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;
import static org.junit.Assert.*;
import static spullara.util.concurrent.CompletableFutureTest.*;

public class CompletableFutureTestBlockTest {

    @Test
    public void testLine362() throws Exception {
        CompletableFuture<List<String>> result = new CompletableFuture<>();
        java.lang.Throwable t = new Throwable();
        List<String> list = new ArrayList<>();
        java.lang.String s = "bar";
        int size = 1;
        if (t == null) {
            list.add(s);
            if (list.size() == size) {
                result.complete(list);
            }
        } else {
            result.completeExceptionally(t);
        }
        assertTrue(result.toString().contains("Completed exceptionally"));
        assertEquals(0, list.size());
    }

    @Test
    public void testLine366() throws Exception {
        CompletableFuture<List<String>> result = new CompletableFuture<>();
        java.lang.Throwable t = null;
        List<String> list = new ArrayList<>();
        java.lang.String s = "foo";
        int size = 1;
        if (t == null) {
            list.add(s);
            if (list.size() == size) {
                result.complete(list);
            }
        } else {
            result.completeExceptionally(t);
        }
        assertTrue(result.toString().contains("Completed normally"));
        assertEquals("foo", list.iterator().next());
        assertEquals(1, list.size());
    }

    @Test
    public void testLine371() throws Exception {
        CompletableFuture<List<String>> result = new CompletableFuture<>();
        java.lang.Throwable t = null;
        List<String> list = new ArrayList<>();
        java.lang.String s = "foo";
        int size = 2;
        if (t == null) {
            list.add(s);
            if (list.size() == size) {
                result.complete(list);
            }
        } else {
            result.completeExceptionally(t);
        }
        assertTrue(result.toString().contains("Not completed"));
        assertEquals("foo", list.iterator().next());
        assertEquals(1, list.size());
    }
}
