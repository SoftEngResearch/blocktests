package regexodus;

import regexodus.ds.IntBitSet;
import java.util.ArrayList;
import java.util.HashMap;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static regexodus.CharacterClass.*;

public class CharacterClassBlockTest {

    @Test
    public void testLine513() throws Exception {
        char[] data = new char[] { '7', 'x' };
        int i = 0;
        int oct = 0;
        char c = 8;
        oct = 0;
        for (; ; ) {
            char d = data[i++];
            if (d >= '0' && d <= '7') {
                oct *= 8;
                oct += d - '0';
                if (oct > 0xffff) {
                    oct -= d - '0';
                    oct /= 8;
                    break;
                }
            }
            break;
        }
        c = (char) oct;
        assertEquals(2, i);
        assertEquals(7, (int) c);
        assertEquals(7, oct);
    }

    @Test
    public void testLine514() throws Exception {
        char[] data = new char[] { '1', '2', '3', 'x' };
        int i = 0;
        int oct = 0;
        char c = 8;
        oct = 0;
        for (; ; ) {
            char d = data[i++];
            if (d >= '0' && d <= '7') {
                oct *= 8;
                oct += d - '0';
                if (oct > 0xffff) {
                    oct -= d - '0';
                    oct /= 8;
                    break;
                }
            }
            break;
        }
        c = (char) oct;
        assertEquals(4, i);
        assertEquals(83, (int) c);
        assertEquals(83, oct);
    }

    @Test
    public void testLine515() throws Exception {
        char[] data = new char[] { '1', '2', '8', '3' };
        int i = 0;
        int oct = 0;
        char c = 8;
        oct = 0;
        for (; ; ) {
            char d = data[i++];
            if (d >= '0' && d <= '7') {
                oct *= 8;
                oct += d - '0';
                if (oct > 0xffff) {
                    oct -= d - '0';
                    oct /= 8;
                    break;
                }
            }
            break;
        }
        c = (char) oct;
        assertEquals(3, i);
        assertEquals(10, (int) c);
        assertEquals(10, oct);
    }

    @Test
    public void testLine516() throws Exception {
        char[] data = new char[] { '7', '7', '7', '7', '7', '7' };
        int i = 0;
        int oct = 0;
        char c = 8;
        oct = 0;
        for (; ; ) {
            char d = data[i++];
            if (d >= '0' && d <= '7') {
                oct *= 8;
                oct += d - '0';
                if (oct > 0xffff) {
                    oct -= d - '0';
                    oct /= 8;
                    break;
                }
            }
            break;
        }
        c = (char) oct;
        assertTrue(oct < 0xffff);
        assertEquals(32767, oct);
    }

    @Test
    public void testLine517() throws Exception {
        char[] data = new char[] { '0', '0', '0', 'x' };
        int i = 0;
        int oct = 0;
        char c = 8;
        oct = 0;
        for (; ; ) {
            char d = data[i++];
            if (d >= '0' && d <= '7') {
                oct *= 8;
                oct += d - '0';
                if (oct > 0xffff) {
                    oct -= d - '0';
                    oct /= 8;
                    break;
                }
            }
            break;
        }
        c = (char) oct;
        assertEquals(4, i);
        assertEquals(0, (int) c);
        assertEquals(0, oct);
    }

    @Test
    public void testLine518() throws Exception {
        char[] data = new char[] { '1', '7', '7', '7', '7', '7', 'x' };
        int i = 0;
        int oct = 0;
        char c = 8;
        oct = 0;
        for (; ; ) {
            char d = data[i++];
            if (d >= '0' && d <= '7') {
                oct *= 8;
                oct += d - '0';
                if (oct > 0xffff) {
                    oct -= d - '0';
                    oct /= 8;
                    break;
                }
            }
            break;
        }
        c = (char) oct;
        assertEquals(65535, (int) c);
        assertEquals(65535, oct);
    }

    @Test
    public void testLine519() throws Exception {
        char[] data = new char[] { 'x', '2', '3' };
        int i = 0;
        int oct = 0;
        char c = 8;
        oct = 0;
        for (; ; ) {
            char d = data[i++];
            if (d >= '0' && d <= '7') {
                oct *= 8;
                oct += d - '0';
                if (oct > 0xffff) {
                    oct -= d - '0';
                    oct /= 8;
                    break;
                }
            }
            break;
        }
        c = (char) oct;
        assertEquals(1, i);
        assertEquals(0, (int) c);
        assertEquals(0, oct);
    }

    @Test
    public void testLine544() throws Exception {
        char[] data = new char[] { '7', 'x' };
        int i = 1;
        int oct = 0;
        char c = (char) oct;
        oct = 0;
        for (; ; ) {
            char d = data[i - 1];
            if (d >= '0' && d <= '7') {
                i++;
                oct *= 8;
                oct += d - '0';
                if (oct > 0xffff) {
                    oct -= d - '0';
                    oct /= 8;
                    break;
                }
            } else {
                i--;
                break;
            }
        }
        c = (char) oct;
        assertEquals(1, i);
        assertEquals(7, (int) c);
        assertEquals(7, oct);
    }

    @Test
    public void testLine545() throws Exception {
        char[] data = new char[] { '1', '2', '3', 'x' };
        int i = 1;
        int oct = 0;
        char c = (char) oct;
        oct = 0;
        for (; ; ) {
            char d = data[i - 1];
            if (d >= '0' && d <= '7') {
                i++;
                oct *= 8;
                oct += d - '0';
                if (oct > 0xffff) {
                    oct -= d - '0';
                    oct /= 8;
                    break;
                }
            } else {
                i--;
                break;
            }
        }
        c = (char) oct;
        assertEquals(3, i);
        assertEquals(83, (int) c);
        assertEquals(83, oct);
    }

    @Test
    public void testLine546() throws Exception {
        char[] data = new char[] { '1', '2', '8', '3' };
        int i = 1;
        int oct = 0;
        char c = (char) oct;
        oct = 0;
        for (; ; ) {
            char d = data[i - 1];
            if (d >= '0' && d <= '7') {
                i++;
                oct *= 8;
                oct += d - '0';
                if (oct > 0xffff) {
                    oct -= d - '0';
                    oct /= 8;
                    break;
                }
            } else {
                i--;
                break;
            }
        }
        c = (char) oct;
        assertEquals(2, i);
        assertEquals(10, (int) c);
        assertEquals(10, oct);
    }

    @Test
    public void testLine547() throws Exception {
        char[] data = new char[] { '7', '7', '7', '7', '7', '7' };
        int i = 1;
        int oct = 0;
        char c = (char) oct;
        oct = 0;
        for (; ; ) {
            char d = data[i - 1];
            if (d >= '0' && d <= '7') {
                i++;
                oct *= 8;
                oct += d - '0';
                if (oct > 0xffff) {
                    oct -= d - '0';
                    oct /= 8;
                    break;
                }
            } else {
                i--;
                break;
            }
        }
        c = (char) oct;
        assertEquals(7, i);
        assertTrue(oct <= 0xffff);
        assertEquals(32767, oct);
    }

    @Test
    public void testLine548() throws Exception {
        char[] data = new char[] { '0', '0', '0', 'x' };
        int i = 1;
        int oct = 0;
        char c = (char) oct;
        oct = 0;
        for (; ; ) {
            char d = data[i - 1];
            if (d >= '0' && d <= '7') {
                i++;
                oct *= 8;
                oct += d - '0';
                if (oct > 0xffff) {
                    oct -= d - '0';
                    oct /= 8;
                    break;
                }
            } else {
                i--;
                break;
            }
        }
        c = (char) oct;
        assertEquals(3, i);
        assertEquals(0, (int) c);
        assertEquals(0, oct);
    }

    @Test
    public void testLine549() throws Exception {
        char[] data = new char[] { '1', '7', '7', '7', '7', '7', 'x' };
        int i = 1;
        int oct = 0;
        char c = (char) oct;
        oct = 0;
        for (; ; ) {
            char d = data[i - 1];
            if (d >= '0' && d <= '7') {
                i++;
                oct *= 8;
                oct += d - '0';
                if (oct > 0xffff) {
                    oct -= d - '0';
                    oct /= 8;
                    break;
                }
            } else {
                i--;
                break;
            }
        }
        c = (char) oct;
        assertEquals(6, i);
        assertEquals(65535, (int) c);
        assertEquals(65535, oct);
    }

    @Test
    public void testLine550() throws Exception {
        char[] data = new char[] { 'x', '2', '3' };
        int i = 1;
        int oct = 0;
        char c = (char) oct;
        oct = 0;
        for (; ; ) {
            char d = data[i - 1];
            if (d >= '0' && d <= '7') {
                i++;
                oct *= 8;
                oct += d - '0';
                if (oct > 0xffff) {
                    oct -= d - '0';
                    oct /= 8;
                    break;
                }
            } else {
                i--;
                break;
            }
        }
        c = (char) oct;
        assertEquals(0, i);
        assertEquals(0, (int) c);
        assertEquals(0, oct);
    }

    @Test
    public void testLine551() throws Exception {
        char[] data = new char[] { '5', '9', '1' };
        int i = 1;
        int oct = 0;
        char c = (char) oct;
        oct = 0;
        for (; ; ) {
            char d = data[i - 1];
            if (d >= '0' && d <= '7') {
                i++;
                oct *= 8;
                oct += d - '0';
                if (oct > 0xffff) {
                    oct -= d - '0';
                    oct /= 8;
                    break;
                }
            } else {
                i--;
                break;
            }
        }
        c = (char) oct;
        assertEquals(1, i);
        assertEquals(5, (int) c);
        assertEquals(5, oct);
    }
}
