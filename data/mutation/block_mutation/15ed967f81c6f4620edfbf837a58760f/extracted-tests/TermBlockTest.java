package regexodus;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import regexodus.ds.IntBitSet;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.junit.Test;
import static org.junit.Assert.*;
import static regexodus.Term.*;

public class TermBlockTest {

    @Test
    public void testLine527() throws Exception {
        char cp = '@';
        char[] data = new char[] { '\\', 'k', '<', '@', 'a', 'm', 'e', '>' };
        int p = 3;
        int end = 8;
        boolean mr = false;
        boolean mb = false;
        boolean mu = false;
        boolean mi = false;
        while (Category.Space.contains(cp) || Category.Po.contains(cp)) {
            p++;
            if (p == end)
                throw new PatternSyntaxException("'group_id' expected");
            switch(cp) {
                case '@':
                    mi = !mi;
                    break;
                case '/':
                    mr = !mr;
                    break;
                case ':':
                    mb = !mb;
                    break;
                case '!':
                    mu = !mu;
                    break;
            }
            cp = data[p];
        }
        assertEquals(true, mi);
    }

    @Test
    public void testLine528() throws Exception {
        char cp = '@';
        char[] data = new char[] { '\\', 'k', '<', '@', '@', 'm', 'e', '>' };
        int p = 3;
        int end = 8;
        boolean mr = false;
        boolean mb = false;
        boolean mu = false;
        boolean mi = false;
        while (Category.Space.contains(cp) || Category.Po.contains(cp)) {
            p++;
            if (p == end)
                throw new PatternSyntaxException("'group_id' expected");
            switch(cp) {
                case '@':
                    mi = !mi;
                    break;
                case '/':
                    mr = !mr;
                    break;
                case ':':
                    mb = !mb;
                    break;
                case '!':
                    mu = !mu;
                    break;
            }
            cp = data[p];
        }
        assertEquals(false, mi);
    }

    @Test
    public void testLine529() throws Exception {
        char cp = 'n';
        char[] data = new char[] { '\\', 'k', '<', 'n', '@', 'm', 'e', '>' };
        int p = 3;
        int end = 8;
        boolean mr = false;
        boolean mb = false;
        boolean mu = false;
        boolean mi = false;
        while (Category.Space.contains(cp) || Category.Po.contains(cp)) {
            p++;
            if (p == end)
                throw new PatternSyntaxException("'group_id' expected");
            switch(cp) {
                case '@':
                    mi = !mi;
                    break;
                case '/':
                    mr = !mr;
                    break;
                case ':':
                    mb = !mb;
                    break;
                case '!':
                    mu = !mu;
                    break;
            }
            cp = data[p];
        }
        assertEquals(false, mi);
    }

    @Test
    public void testLine530() throws Exception {
        char cp = '/';
        char[] data = new char[] { '\\', 'k', '<', '/', '@', 'm', 'e', '>' };
        int p = 3;
        int end = 8;
        boolean mr = true;
        boolean mb = false;
        boolean mu = false;
        boolean mi = false;
        while (Category.Space.contains(cp) || Category.Po.contains(cp)) {
            p++;
            if (p == end)
                throw new PatternSyntaxException("'group_id' expected");
            switch(cp) {
                case '@':
                    mi = !mi;
                    break;
                case '/':
                    mr = !mr;
                    break;
                case ':':
                    mb = !mb;
                    break;
                case '!':
                    mu = !mu;
                    break;
            }
            cp = data[p];
        }
        assertEquals(true, mi);
    }

    @Test
    public void testLine531() throws Exception {
        char cp = '/';
        char[] data = new char[] { '\\', 'k', '<', '/', '@', ':', 'e', '>' };
        int p = 3;
        int end = 8;
        boolean mr = true;
        boolean mb = true;
        boolean mu = false;
        boolean mi = false;
        while (Category.Space.contains(cp) || Category.Po.contains(cp)) {
            p++;
            if (p == end)
                throw new PatternSyntaxException("'group_id' expected");
            switch(cp) {
                case '@':
                    mi = !mi;
                    break;
                case '/':
                    mr = !mr;
                    break;
                case ':':
                    mb = !mb;
                    break;
                case '!':
                    mu = !mu;
                    break;
            }
            cp = data[p];
        }
        assertEquals(true, mi);
    }

    @Test
    public void testLine532() throws Exception {
        char cp = '/';
        char[] data = new char[] { '\\', 'k', '<', '/', '@', ':', '!', '>' };
        int p = 3;
        int end = 8;
        boolean mr = true;
        boolean mb = true;
        boolean mu = true;
        boolean mi = false;
        while (Category.Space.contains(cp) || Category.Po.contains(cp)) {
            p++;
            if (p == end)
                throw new PatternSyntaxException("'group_id' expected");
            switch(cp) {
                case '@':
                    mi = !mi;
                    break;
                case '/':
                    mr = !mr;
                    break;
                case ':':
                    mb = !mb;
                    break;
                case '!':
                    mu = !mu;
                    break;
            }
            cp = data[p];
        }
        assertEquals(true, mi);
    }

    @Test
    public void testLine533() throws Exception {
        char cp = '/';
        char[] data = new char[] { '\\', 'k', '<', '/', '@', ':', '/', '>' };
        int p = 3;
        int end = 8;
        boolean mr = false;
        boolean mb = true;
        boolean mu = false;
        boolean mi = false;
        while (Category.Space.contains(cp) || Category.Po.contains(cp)) {
            p++;
            if (p == end)
                throw new PatternSyntaxException("'group_id' expected");
            switch(cp) {
                case '@':
                    mi = !mi;
                    break;
                case '/':
                    mr = !mr;
                    break;
                case ':':
                    mb = !mb;
                    break;
                case '!':
                    mu = !mu;
                    break;
            }
            cp = data[p];
        }
        assertEquals(true, mi);
    }

    @Test
    public void testLine534() throws Exception {
        char cp = '/';
        char[] data = new char[] { '\\', 'k', '<', '/', '@', ':', ':', '>' };
        int p = 3;
        int end = 8;
        boolean mr = true;
        boolean mb = false;
        boolean mu = false;
        boolean mi = false;
        while (Category.Space.contains(cp) || Category.Po.contains(cp)) {
            p++;
            if (p == end)
                throw new PatternSyntaxException("'group_id' expected");
            switch(cp) {
                case '@':
                    mi = !mi;
                    break;
                case '/':
                    mr = !mr;
                    break;
                case ':':
                    mb = !mb;
                    break;
                case '!':
                    mu = !mu;
                    break;
            }
            cp = data[p];
        }
        assertEquals(true, mi);
    }

    @Test
    public void testLine535() throws Exception {
        char cp = '/';
        char[] data = new char[] { '\\', 'k', '<', '/', '@', '!', '!', '>' };
        int p = 3;
        int end = 8;
        boolean mr = true;
        boolean mb = false;
        boolean mu = true;
        boolean mi = false;
        while (Category.Space.contains(cp) || Category.Po.contains(cp)) {
            p++;
            if (p == end)
                throw new PatternSyntaxException("'group_id' expected");
            switch(cp) {
                case '@':
                    mi = !mi;
                    break;
                case '/':
                    mr = !mr;
                    break;
                case ':':
                    mb = !mb;
                    break;
                case '!':
                    mu = !mu;
                    break;
            }
            cp = data[p];
        }
        assertEquals(true, mi);
    }

    @Test(expected = PatternSyntaxException.class)
    public void testLine536() throws Exception {
        char cp = '/';
        char[] data = new char[] { '\\', 'k', '<', '/', '@', '!', '!', '>' };
        int p = 3;
        int end = 5;
        boolean mr = false;
        boolean mb = false;
        boolean mu = false;
        boolean mi = false;
        while (Category.Space.contains(cp) || Category.Po.contains(cp)) {
            p++;
            if (p == end)
                throw new PatternSyntaxException("'group_id' expected");
            switch(cp) {
                case '@':
                    mi = !mi;
                    break;
                case '/':
                    mr = !mr;
                    break;
                case ':':
                    mb = !mb;
                    break;
                case '!':
                    mu = !mu;
                    break;
            }
            cp = data[p];
        }
    }

    @Test
    public void testLine537() throws Exception {
        char cp = '$';
        int p = 0;
        int end = 3;
        boolean mi = false;
        boolean mr = false;
        boolean mb = false;
        boolean mu = false;
        char[] data = new char[] { '4', ':' };
        while (Category.Space.contains(cp) || Category.Po.contains(cp)) {
            p++;
            if (p == end)
                throw new PatternSyntaxException("'group_id' expected");
            switch(cp) {
                case '@':
                    mi = !mi;
                    break;
                case '/':
                    mr = !mr;
                    break;
                case ':':
                    mb = !mb;
                    break;
                case '!':
                    mu = !mu;
                    break;
            }
            cp = data[p];
        }
        assertFalse(mb);
    }

    @Test
    public void testLine1152() throws Exception {
        int i = 0;
        int out = 4;
        char[] data = new char[] { '0', '1', '2', '3' };
        char c = 0;
        int oct = 0;
        for (; i < out; ) {
            char d = data[i++];
            if (d >= '0' && d <= '7') {
                oct *= 8;
                oct += d - '0';
                if (oct > 0xffff) {
                    oct -= d - '0';
                    oct /= 8;
                    break;
                }
            } else
                break;
        }
        c = (char) oct;
        assertEquals(83, oct);
    }

    @Test
    public void testLine1171() throws Exception {
        int i = 0;
        int out = 3;
        char[] data = new char[] { '1', '2', '3' };
        char c = 0;
        int dec = 0;
        for (; i < out; ) {
            char d = data[i++];
            if (d >= '0' && d <= '9') {
                dec *= 10;
                dec += d - '0';
                if (dec > 0xffff) {
                    dec -= d - '0';
                    dec /= 10;
                    break;
                }
            } else
                break;
        }
        c = (char) dec;
        assertEquals(123, dec);
    }

    @Test
    public void testLine1274() throws Exception {
        int i = 0;
        int out = 2;
        char[] data = new char[] { '1', '2' };
        char c = '2';
        int n = c - '0';
        while ((i < out) && (c = data[i]) >= '0' && c <= '9') {
            n = (n * 10) + c - '0';
        }
        assertEquals(212, n);
    }

    @Test
    public void testLine1492() throws Exception {
        StringBuilder b = new StringBuilder();
        int failNext__instanceNum = 5;
        b.append(", =>");
        b.append(failNext__instanceNum);
        b.append(", ");
        assertEquals(", =>5, ", b.toString());
    }

    @Test
    public void testLine1493() throws Exception {
        StringBuilder b = new StringBuilder();
        int failNext__instanceNum = 15;
        b.append(", =>");
        b.append(failNext__instanceNum);
        b.append(", ");
        assertEquals(", =>15, ", b.toString());
    }

    @Test
    public void testLine1507() throws Exception {
        StringBuilder b = new StringBuilder();
        int failNext__instanceNum = 5;
        b.append(", =>");
        b.append(failNext__instanceNum);
        b.append(", ");
        assertEquals(", =>5, ", b.toString());
    }

    @Test
    public void testLine1508() throws Exception {
        StringBuilder b = new StringBuilder();
        int failNext__instanceNum = 15;
        b.append(", =>");
        b.append(failNext__instanceNum);
        b.append(", ");
        assertEquals(", =>15, ", b.toString());
    }

    @Test
    public void testLine1654() throws Exception {
        int lookaheadId = 1;
        Term failNext = new Term();
        int failNext__instanceNum = 1;
        StringBuilder b = new StringBuilder(100);
        b.append("(cond");
        b.append(lookaheadId);
        b.append(true ? '=' : '!');
        b.append(" , ");
        if (failNext != null) {
            b.append(", =>");
            b.append(failNext__instanceNum);
            b.append(", ");
        }
        assertEquals("(cond1= , , =>1, ", b.toString());
    }

    @Test
    public void testLine1657() throws Exception {
        int lookaheadId = 3;
        Term failNext = new Term();
        int failNext__instanceNum = 2;
        StringBuilder b = new StringBuilder(100);
        b.append("(cond");
        b.append(lookaheadId);
        b.append(false ? '=' : '!');
        b.append(" , ");
        if (failNext != null) {
            b.append(", =>");
            b.append(failNext__instanceNum);
            b.append(", ");
        }
        assertEquals("(cond3! , , =>2, ", b.toString());
    }

    @Test
    public void testLine1660() throws Exception {
        int lookaheadId = 5;
        Term failNext = null;
        int failNext__instanceNum = 6;
        StringBuilder b = new StringBuilder(100);
        b.append("(cond");
        b.append(lookaheadId);
        b.append(false ? '=' : '!');
        b.append(" , ");
        if (failNext != null) {
            b.append(", =>");
            b.append(failNext__instanceNum);
            b.append(", ");
        }
        assertEquals("(cond5! , ", b.toString());
    }

    @Test
    public void testLine1679() throws Exception {
        StringBuilder b = new StringBuilder();
        int failNext__instanceNum = 5;
        b.append(", =>");
        b.append(failNext__instanceNum);
        b.append(", ");
        assertEquals(", =>5, ", b.toString());
    }

    @Test
    public void testLine1680() throws Exception {
        StringBuilder b = new StringBuilder();
        int failNext__instanceNum = 15;
        b.append(", =>");
        b.append(failNext__instanceNum);
        b.append(", ");
        assertEquals(", =>15, ", b.toString());
    }

    @Test
    public void testLine1699() throws Exception {
        StringBuilder b = new StringBuilder();
        Term failNext = new Term();
        int failNext__instanceNum = 5;
        b.append("=>");
        if (failNext != null)
            b.append(failNext__instanceNum);
        else
            b.append("null");
        b.append(" , ");
        assertEquals("=>5 , ", b.toString());
    }

    @Test
    public void testLine1700() throws Exception {
        StringBuilder b = new StringBuilder();
        Term failNext = null;
        int failNext__instanceNum = 5;
        b.append("=>");
        if (failNext != null)
            b.append(failNext__instanceNum);
        else
            b.append("null");
        b.append(" , ");
        assertEquals("=>null , ", b.toString());
    }

    @Test
    public void testLine1893() throws Exception {
        int end = 2;
        char[] data = new char[] { '\\', 'E' };
        int i = 0;
        boolean esc = true;
        for (; i < end; i++) {
            char c1 = data[i];
            if (c1 == '\\') {
                if (i + 1 < end && data[i + 1] == 'E') {
                    i++;
                    esc = false;
                    break;
                }
            }
        }
        assertFalse(esc);
    }

    @Test
    public void testLine1896() throws Exception {
        int end = 2;
        char[] data = new char[] { '\\', 'f' };
        int i = 0;
        boolean esc = true;
        for (; i < end; i++) {
            char c1 = data[i];
            if (c1 == '\\') {
                if (i + 1 < end && data[i + 1] == 'E') {
                    i++;
                    esc = false;
                    break;
                }
            }
        }
        assertTrue(esc);
    }

    @Test
    public void testLine1899() throws Exception {
        int end = 0;
        char[] data = new char[] { '\\', 'f' };
        int i = 0;
        boolean esc = true;
        for (; i < end; i++) {
            char c1 = data[i];
            if (c1 == '\\') {
                if (i + 1 < end && data[i + 1] == 'E') {
                    i++;
                    esc = false;
                    break;
                }
            }
        }
        assertTrue(esc);
    }

    @Test(expected = PatternSyntaxException.class)
    public void testLine2036() throws Exception {
        int p = 0;
        char[] data = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8' };
        char c = ' ';
        int end = 1;
        boolean isDecl;
        int skip = 3;
        int nstart;
        int nend;
        c = data[p];
        while (Category.Space.contains(c)) {
            c = data[++p];
            skip++;
            if (p == end)
                throw new PatternSyntaxException("malformed named group");
        }
        if (c == '=') {
            isDecl = false;
            c = data[++p];
            skip++;
            if (p == end)
                throw new PatternSyntaxException("malformed named group");
        } else
            isDecl = true;
        nstart = p;
        while (Category.IdentifierPart.contains(c)) {
            c = data[++p];
            skip++;
            if (p == end)
                throw new PatternSyntaxException("malformed named group");
        }
        nend = p;
        while (Category.Space.contains(c)) {
            c = data[++p];
            skip++;
            if (p == end)
                throw new PatternSyntaxException("malformed named group");
        }
    }
}
