package com.github.vangj.jbayes.inf.exact.graph.pptc;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import com.github.vangj.jbayes.inf.exact.graph.Node;
import com.github.vangj.jbayes.inf.exact.graph.lpd.Potential;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.github.vangj.jbayes.inf.exact.graph.pptc.Evidence.*;

public class EvidenceBlockTest {

    @Test
    public void testLine276() throws Exception {
        Map<String, Double> values = new LinkedHashMap<String, Double>();
        java.lang.String k = "Foo";
        values.put(k, 1.0d);
        assertEquals(1, values.get("Foo"), 0.001);
    }

    @Test
    public void testLine265() throws Exception {
        Map<String, Double> values = new LinkedHashMap<String, Double>();
        java.lang.String k = "Foo";
        String key = "Foo";
        if (key.equals(k)) {
            values.put(k, 1.0d);
        } else {
        }
        assertEquals(1, values.get("Foo"), 0.001);
    }

    @Test
    public void testLine266() throws Exception {
        Map<String, Double> values = new LinkedHashMap<String, Double>();
        java.lang.String k = "Foo";
        String key = "Bar";
        if (key.equals(k)) {
            values.put(k, 1.0d);
        } else {
        }
        assertEquals(0, values.get("Foo"), 0.001);
    }
}
