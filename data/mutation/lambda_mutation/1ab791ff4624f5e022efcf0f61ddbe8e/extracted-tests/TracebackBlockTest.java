package sorra.tracesonar.core;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import sorra.tracesonar.model.Query;
import org.junit.Test;
import static org.junit.Assert.*;
import static sorra.tracesonar.core.Traceback.*;

public class TracebackBlockTest {

    @Test
    public void testLine55() throws Exception {
        StringBuilder output = new StringBuilder();
        TreeNode node = new TreeNode(new sorra.tracesonar.model.Method("a", "b", "c"), false, null);
        char[] indents = new char[node.depth];
        Arrays.fill(indents, '\t');
        output.append(String.valueOf(indents)).append(node.self);
        if (node.hasError()) {
            output.append(" {").append(node.getError()).append('}');
        }
        assertEquals("<- a #b c\n", output.toString());
    }

    @Test
    public void testLine56() throws Exception {
        StringBuilder output = new StringBuilder();
        TreeNode node = new TreeNode(new sorra.tracesonar.model.Method("a", "b", "c"), false, null);
        node.setError("error");
        char[] indents = new char[node.depth];
        Arrays.fill(indents, '\t');
        output.append(String.valueOf(indents)).append(node.self);
        if (node.hasError()) {
            output.append(" {").append(node.getError()).append('}');
        }
        assertEquals("<- a #b c {error}\n", output.toString());
    }
}
