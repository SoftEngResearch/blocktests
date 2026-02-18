package com.github.pfmiles.org.apache.velocity.runtime.parser;

import java.util.ArrayList;
import java.util.List;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.github.pfmiles.org.apache.velocity.runtime.parser.ParserTokenManager.*;

public class ParserTokenManagerBlockTest {

    @Test
    public void testLine4467() throws Exception {
        char curChar = 'D';
        int hiByte = (int) (curChar >> 8);
        int i1 = hiByte >> 6;
        long l1 = 1L << (hiByte & 077);
        int i2 = (curChar & 0xff) >> 6;
        long l2 = 1L << (curChar | 077);
        assertEquals(16, (int) l2);
        assertEquals(1, i2);
        assertEquals(1, (int) l1);
        assertEquals(0, i1);
    }
}
