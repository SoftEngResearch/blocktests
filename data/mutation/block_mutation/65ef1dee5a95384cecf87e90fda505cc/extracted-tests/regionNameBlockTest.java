package com.maxmind.geoip;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.maxmind.geoip.regionName.*;

public class regionNameBlockTest {

    @Test
    public void testLine18() throws Exception {
        String regionCode = "CA";
        int region_code2 = -1;
        if (((regionCode.charAt(0) >= 48) && (regionCode.charAt(0) < (48 + 10))) && ((regionCode.charAt(1) >= 48) && (regionCode.charAt(1) < (48 + 10)))) {
            // only numbers, that shorten the large switch statements
            region_code2 = (regionCode.charAt(0) - 48) * 10 + regionCode.charAt(1) - 48;
        } else if ((((regionCode.charAt(0) >= 65) && (regionCode.charAt(0) < (65 + 26))) || ((regionCode.charAt(0) >= 48) && (regionCode.charAt(0) < (48 + 10)))) && (((regionCode.charAt(1) >= 65) && (regionCode.charAt(1) < (65 + 26))) || ((regionCode.charAt(1) >= 48) && (regionCode.charAt(1) < (48 + 10))))) {
        }
        assertEquals(934, region_code2);
    }

    @Test
    public void testLine19() throws Exception {
        String regionCode = "93";
        int region_code2 = -1;
        if (((regionCode.charAt(0) >= 48) && (regionCode.charAt(0) < (48 + 10))) && ((regionCode.charAt(1) >= 48) && (regionCode.charAt(1) < (48 + 10)))) {
            // only numbers, that shorten the large switch statements
            region_code2 = (regionCode.charAt(0) - 48) * 10 + regionCode.charAt(1) - 48;
        } else if ((((regionCode.charAt(0) >= 65) && (regionCode.charAt(0) < (65 + 26))) || ((regionCode.charAt(0) >= 48) && (regionCode.charAt(0) < (48 + 10)))) && (((regionCode.charAt(1) >= 65) && (regionCode.charAt(1) < (65 + 26))) || ((regionCode.charAt(1) >= 48) && (regionCode.charAt(1) < (48 + 10))))) {
        }
        assertEquals(93, region_code2);
    }
}
