package org.erdtman.jcs;

import java.math.BigInteger;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.erdtman.jcs.NumberDToA.*;

public class NumberDToABlockTest {

    @Test
    public void testLine476() throws Exception {
        int j = 25;
        int j1 = 100;
        int i = 0;
        int ieps = 0;
        double ds = 1;
        double d = 1e2;
        d *= NumberDToA.tens[j1 & 0xf];
        for (j = j1 >> 4; (j != 0); j >>= 1, i++) if ((j & 1) != 0) {
            ieps++;
            d *= NumberDToA.bigtens[i];
        }
        assertEquals(2, ieps);
        assertEquals(1e102, d, 0.01);
    }

    @Test
    public void testLine467() throws Exception {
        int j = 5;
        int i = 0;
        int ieps = 0;
        double ds = 1;
        double d = 1e80;
        for (; (j != 0); j >>= 1, i++) if ((j & 1) != 0) {
            ieps++;
            ds *= NumberDToA.bigtens[i];
        }
        assertEquals(2, ieps);
        assertEquals(1.0, d, 0.01);
    }

    @Test
    public void testLine533() throws Exception {
        StringBuilder buf = new StringBuilder().append("5599");
        int k = 0;
        char lastCh;
        while (true) {
            lastCh = buf.charAt(buf.length() - 1);
            buf.setLength(buf.length() - 1);
            if (lastCh != '9')
                break;
            if (buf.length() == 0) {
                k++;
                lastCh = '0';
                break;
            }
        }
        buf.append((char) (lastCh + 1));
        assertEquals(0, k);
        assertEquals("56", buf.toString());
    }

    @Test
    public void testLine534() throws Exception {
        StringBuilder buf = new StringBuilder().append("999");
        int k = 0;
        char lastCh;
        while (true) {
            lastCh = buf.charAt(buf.length() - 1);
            buf.setLength(buf.length() - 1);
            if (lastCh != '9')
                break;
            if (buf.length() == 0) {
                k++;
                lastCh = '0';
                break;
            }
        }
        buf.append((char) (lastCh + 1));
        assertEquals(1, k);
        assertEquals("1", buf.toString());
    }

    @Test
    public void testLine841() throws Exception {
        int j1 = 0;
        BigInteger b = new BigInteger("1000");
        BigInteger S = new BigInteger("300");
        StringBuilder buf = new StringBuilder().append("999");
        boolean biasUp = true;
        char dig = '9';
        int k = 0;
        try {
            b = b.shiftLeft(1);
            j1 = b.compareTo(S);
            if (((j1 > 0) || (j1 == 0 && (((dig & 1) == 1) || biasUp))) && (dig++ == '9')) {
                buf.append('9');
                if (NumberDToA.roundOff(buf)) {
                    k++;
                    buf.append('1');
                }
                return;
            }
        } finally {
            assertEquals("1", buf.toString());
            assertEquals(1, k);
        }
    }
}
