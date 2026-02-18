package fr.delthas.javamp3;

import java.io.*;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static fr.delthas.javamp3.Decoder.*;

public class DecoderBlockTest {

    @Test
    public void testLine722() throws Exception {
        int stereo = 5;
        int ch = 3;
        int[] scalefac_l = new int[stereo * 2 * 21];
        for (int i = 0; i < scalefac_l.length; i++) {
            scalefac_l[i] = i;
        }
        for (int sfb = 11; sfb < 16; sfb++) {
            scalefac_l[ch * 2 * 21 + 1 * 21 + sfb] = scalefac_l[ch * 2 * 21 + 0 * 21 + sfb];
            continue;
        }
        assertTrue(scalefac_l[162] == scalefac_l[141]);
        assertTrue(scalefac_l[161] == scalefac_l[140]);
        assertTrue(scalefac_l[160] == scalefac_l[139]);
        assertTrue(scalefac_l[159] == scalefac_l[138]);
        assertTrue(scalefac_l[158] == scalefac_l[137]);
    }
}
