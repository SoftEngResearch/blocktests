package org.joni;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import static org.joni.Option.isFindLongest;
import org.jcodings.Encoding;
import org.jcodings.IntHolder;
import org.jcodings.constants.CharacterType;
import org.jcodings.specific.ASCIIEncoding;
import org.joni.constants.internal.AnchorType;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.joni.Matcher.*;

public class MatcherBlockTest {

    @Test
    public void testLine365() throws Exception {
        int range = 10;
        int start = 5;
        int str = 3;
        try {
            if (range > start) {
                // mismatch_no_msa;
                if (start != str) {
                    assertEquals(-1, (Matcher.FAILED));
                    return;
                }
                range = str + 1;
            } else {
                if (range <= str) {
                    start = str;
                    range = str;
                } else {
                    {
                        assertEquals(-1, (Matcher.FAILED));
                        return;
                    }
                }
            }
        } finally {
        }
    }

    @Test
    public void testLine366() throws Exception {
        int range = 5;
        int start = 4;
        int str = 4;
        try {
            if (range > start) {
                // mismatch_no_msa;
                if (start != str)
                    return;
                range = str + 1;
            } else {
                if (range <= str) {
                    start = str;
                    range = str;
                } else {
                    // mismatch_no_msa;
                    return;
                }
            }
        } finally {
            assertEquals(5, range);
        }
    }

    @Test
    public void testLine367() throws Exception {
        int range = 5;
        int start = 5;
        int str = 5;
        try {
            if (range > start) {
                // mismatch_no_msa;
                if (start != str)
                    return;
                range = str + 1;
            } else {
                if (range <= str) {
                    start = str;
                    range = str;
                } else {
                    // mismatch_no_msa;
                    return;
                }
            }
        } finally {
            assertEquals(5, start);
            assertEquals(5, range);
        }
    }

    @Test
    public void testLine368() throws Exception {
        int range = 5;
        int start = 5;
        int str = 0;
        try {
            if (range > start) {
                // mismatch_no_msa;
                if (start != str) {
                    assertEquals(-1, (Matcher.FAILED));
                    return;
                }
                range = str + 1;
            } else {
                if (range <= str) {
                    start = str;
                    range = str;
                } else {
                    {
                        assertEquals(-1, (Matcher.FAILED));
                        return;
                    }
                }
            }
        } finally {
        }
    }

    @Test
    public void testLine369() throws Exception {
        int range = 5;
        int start = 15;
        int str = 6;
        try {
            if (range > start) {
                // mismatch_no_msa;
                if (start != str) {
                    assertEquals(-1, (Matcher.FAILED));
                    return;
                }
                range = str + 1;
            } else {
                if (range <= str) {
                    start = str;
                    range = str;
                } else {
                    {
                        assertEquals(-1, (Matcher.FAILED));
                        return;
                    }
                }
            }
        } finally {
        }
    }

    @Test
    public void testLine370() throws Exception {
        int range = 10;
        int start = 25;
        int str = 10;
        try {
            if (range > start) {
                // mismatch_no_msa;
                if (start != str)
                    return;
                range = str + 1;
            } else {
                if (range <= str) {
                    start = str;
                    range = str;
                } else {
                    // mismatch_no_msa;
                    return;
                }
            }
        } finally {
            assertEquals(10, start);
            assertEquals(10, range);
        }
    }

    @Test
    public void testLine371() throws Exception {
        int range = 5;
        int start = 15;
        int str = 10;
        try {
            if (range > start) {
                // mismatch_no_msa;
                if (start != str)
                    return;
                range = str + 1;
            } else {
                if (range <= str) {
                    start = str;
                    range = str;
                } else {
                    // mismatch_no_msa;
                    return;
                }
            }
        } finally {
            assertEquals(10, start);
            assertEquals(10, range);
        }
    }

    @Test
    public void testLine348() throws Exception {
        int range = 10;
        int start = 5;
        int gpos = 7;
        if (range > start) {
            if (gpos > start) {
                if (gpos < range)
                    range = gpos + 1;
            } else {
                range = start + 1;
            }
        } else {
            range = start;
        }
        assertEquals(8, range);
    }

    @Test
    public void testLine349() throws Exception {
        int range = 10;
        int start = 5;
        int gpos = 17;
        if (range > start) {
            if (gpos > start) {
                if (gpos < range)
                    range = gpos + 1;
            } else {
                range = start + 1;
            }
        } else {
            range = start;
        }
        assertEquals(10, range);
    }

    @Test
    public void testLine350() throws Exception {
        int range = 10;
        int start = 5;
        int gpos = 0;
        if (range > start) {
            if (gpos > start) {
                if (gpos < range)
                    range = gpos + 1;
            } else {
                range = start + 1;
            }
        } else {
            range = start;
        }
        assertEquals(6, range);
    }

    @Test
    public void testLine351() throws Exception {
        int range = 1;
        int start = 5;
        int gpos = 7;
        if (range > start) {
            if (gpos > start) {
                if (gpos < range)
                    range = gpos + 1;
            } else {
                range = start + 1;
            }
        } else {
            range = start;
        }
        assertEquals(5, range);
    }

    @Test
    public void testLine352() throws Exception {
        int range = 1;
        int start = 5;
        int gpos = 0;
        if (range > start) {
            if (gpos > start) {
                if (gpos < range)
                    range = gpos + 1;
            } else {
                range = start + 1;
            }
        } else {
            range = start;
        }
        assertEquals(5, range);
    }

    @Test
    public void testLine464() throws Exception {
        int MinMaxLen__INFINITE_DISTANCE = 0x7FFFFFFF;
        int range = 5;
        int end = 0;
        int regex__dMax = 2;
        int schRange = range;
        if (regex__dMax != 0) {
            if (regex__dMax == MinMaxLen__INFINITE_DISTANCE) {
                schRange = end;
            } else {
                schRange += regex__dMax;
                if (1 == 1)
                    schRange = end;
            }
        }
        assertEquals(0, schRange);
    }

    @Test
    public void testLine465() throws Exception {
        int MinMaxLen__INFINITE_DISTANCE = 0x7FFFFFFF;
        int range = 5;
        int end = 0;
        int regex__dMax = 0;
        int schRange = range;
        if (regex__dMax != 0) {
            if (regex__dMax == MinMaxLen__INFINITE_DISTANCE) {
                schRange = end;
            } else {
                schRange += regex__dMax;
                if (1 == 1)
                    schRange = end;
            }
        }
        assertEquals(5, schRange);
    }

    @Test
    public void testLine466() throws Exception {
        int MinMaxLen__INFINITE_DISTANCE = 0x7FFFFFFF;
        int range = 5;
        int end = 100;
        int regex__dMax = 2;
        int schRange = range;
        if (regex__dMax != 0) {
            if (regex__dMax == MinMaxLen__INFINITE_DISTANCE) {
                schRange = end;
            } else {
                schRange += regex__dMax;
                if (1 == 1)
                    schRange = end;
            }
        }
        assertEquals(7, schRange);
    }

    @Test
    public void testLine467() throws Exception {
        int MinMaxLen__INFINITE_DISTANCE = 0x7FFFFFFF;
        int range = 5;
        int end = 100;
        int regex__dMax = 96;
        int schRange = range;
        if (regex__dMax != 0) {
            if (regex__dMax == MinMaxLen__INFINITE_DISTANCE) {
                schRange = end;
            } else {
                schRange += regex__dMax;
                if (1 == 1)
                    schRange = end;
            }
        }
        assertEquals(100, schRange);
    }

    @Test
    public void testLine468() throws Exception {
        int MinMaxLen__INFINITE_DISTANCE = 0x7FFFFFFF;
        int range = 5;
        int end = 0xFFFFFFFF;
        int regex__dMax = 0x7FFFFFFF;
        int schRange = range;
        if (regex__dMax != 0) {
            if (regex__dMax == MinMaxLen__INFINITE_DISTANCE) {
                schRange = end;
            } else {
                schRange += regex__dMax;
                if (1 == 1)
                    schRange = end;
            }
        }
        assertEquals(0xFFFFFFFF, schRange);
    }
}
