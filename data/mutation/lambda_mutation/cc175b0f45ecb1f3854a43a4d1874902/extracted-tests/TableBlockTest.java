package com.github.vangj.jbayes.inf.exact.sampling;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import com.github.vangj.jbayes.inf.exact.graph.Node;
import com.github.vangj.jbayes.inf.exact.graph.Variable;
import com.github.vangj.jbayes.inf.exact.graph.util.NodeUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.github.vangj.jbayes.inf.exact.sampling.Table.*;

public class TableBlockTest {

    @Test
    public void testLine46() throws Exception {
        List<Node> parents = Arrays.asList(Node.builder().id("foo").build(), Node.builder().id("bar").build(), Node.builder().id("baz").build());
        java.util.List<java.lang.String> values = new ArrayList<>(Arrays.asList("val1", "val2", "val3"));
        try {
            int paSize = parents.size();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < paSize; i++) {
                Node n = parents.get(i);
                String v = values.get(i);
                String s = n.getId() + "=" + v;
                sb.append(s);
                if (i < paSize - 1) {
                    sb.append(",");
                }
                continue;
            }
            {
                assertEquals("foo=val1,bar=val2,baz=val3", (sb.toString()));
                return;
            }
        } finally {
        }
    }

    @Test
    public void testLine49() throws Exception {
        List<Node> parents = new ArrayList<>();
        java.util.List<java.lang.String> values = new ArrayList<>(Arrays.asList("val1", "val2", "val3"));
        try {
            int paSize = parents.size();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < paSize; i++) {
                Node n = parents.get(i);
                String v = values.get(i);
                String s = n.getId() + "=" + v;
                sb.append(s);
                if (i < paSize - 1) {
                    sb.append(",");
                }
                continue;
            }
            {
                assertEquals("", (sb.toString()));
                return;
            }
        } finally {
        }
    }
}
