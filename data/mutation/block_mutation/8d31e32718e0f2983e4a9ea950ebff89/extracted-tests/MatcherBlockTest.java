package regexodus;

import regexodus.ds.IntBitSet;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import static regexodus.Replacer.wrap;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static regexodus.Matcher.*;

public class MatcherBlockTest {

    @Test
    public void testLine1434() throws Exception {
        boolean ch1Meets = false;
        IntBitSet bitset = new IntBitSet();
        char[] data = new char[] { 'A', 'B', 'C', 'D', 'E' };
        int i = 3;
        int offset = 5;
        char c = ' ';
        try {
            int j = i - 1;
            if (j < offset)
                return;
            c = true ? Category.caseFold(data[j]) : data[j];
            ch1Meets = ((1 == 1) && bitset.get(c));
        } finally {
            assertEquals(' ', c);
        }
    }

    @Test
    public void testLine1435() throws Exception {
        boolean ch1Meets = false;
        IntBitSet bitset = new IntBitSet();
        char[] data = new char[] { 'A', 'B', 'C', 'D', 'E' };
        int i = 3;
        int offset = 0;
        char c = ' ';
        try {
            int j = i - 1;
            if (j < offset)
                return;
            c = true ? Category.caseFold(data[j]) : data[j];
            ch1Meets = ((1 == 1) && bitset.get(c));
        } finally {
            assertFalse(ch1Meets);
            assertEquals('c', c);
        }
    }
}
