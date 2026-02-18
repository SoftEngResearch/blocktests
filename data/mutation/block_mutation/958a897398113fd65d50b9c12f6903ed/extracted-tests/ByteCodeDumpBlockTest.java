package org.vesalainen.bcc;

import java.io.IOException;
import java.lang.reflect.Member;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.vesalainen.bcc.ByteCodeDump.*;

public class ByteCodeDumpBlockTest {

    @Test
    public void testLine435() throws Exception {
        int high = 2;
        int low = 2;
        int pc = 5;
        Object label = null;
        CodeDataInput in = null;
        MethodCompiler mc = null;
        LineNumberPrintStream out = null;
        String tmp = "";
        int o = 0;
        for (int ii = 0; ii < high - low + 1; ii++) {
            o = 1 + pc;
            label = new Label("foo");
            if (label != null) {
                int off = low + ii;
                tmp = "\n   // " + off + " -> " + label;
            }
            continue;
        }
        assertTrue(tmp.contains("foo") && tmp.contains(" 2 "));
    }
}
