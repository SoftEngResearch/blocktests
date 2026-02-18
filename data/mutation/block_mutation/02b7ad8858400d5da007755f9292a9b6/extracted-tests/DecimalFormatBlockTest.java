package tec.units.ri.internal.format.l10n;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.util.Vector;
import org.junit.Test;
import static org.junit.Assert.*;
import static tec.units.ri.internal.format.l10n.DecimalFormat.*;

public class DecimalFormatBlockTest {

    @Test
    public void testLine1960() throws Exception {
        boolean inQuote = false;
        int pos = 0;
        String pattern = "do";
        StringBuffer affix = new StringBuffer();
        if ((pos + 1) < pattern.length() && pattern.charAt(pos + 1) == DecimalFormat.QUOTE) {
            ++pos;
            // o''clock
            affix.append("''");
        } else {
            // 'do'
            inQuote = true;
        }
        assertTrue(affix.toString().isEmpty());
        assertTrue(inQuote);
        assertEquals(0, pos);
    }

    @Test
    public void testLine1961() throws Exception {
        boolean inQuote = false;
        int pos = 10;
        String pattern = "do";
        StringBuffer affix = new StringBuffer();
        if ((pos + 1) < pattern.length() && pattern.charAt(pos + 1) == DecimalFormat.QUOTE) {
            ++pos;
            // o''clock
            affix.append("''");
        } else {
            // 'do'
            inQuote = true;
        }
        assertTrue(affix.toString().isEmpty());
        assertTrue(inQuote);
        assertEquals(10, pos);
    }

    @Test
    public void testLine1962() throws Exception {
        boolean inQuote = false;
        int pos = 2;
        String pattern = "don't";
        StringBuffer affix = new StringBuffer();
        if ((pos + 1) < pattern.length() && pattern.charAt(pos + 1) == DecimalFormat.QUOTE) {
            ++pos;
            // o''clock
            affix.append("''");
        } else {
            // 'do'
            inQuote = true;
        }
        assertTrue(affix.toString().equals("''"));
        assertFalse(inQuote);
        assertEquals(3, pos);
    }

    @Test
    public void testLine1937() throws Exception {
        StringBuffer affix = new StringBuffer();
        int pos = 0;
        String pattern = " \u00A4";
        boolean isCurrencyFormat;
        boolean doubled = (pos + 1) < pattern.length() && pattern.charAt(pos + 1) == DecimalFormat.CURRENCY_SIGN;
        if (doubled)
            // Skip over the doubled character
            ++pos;
        isCurrencyFormat = true;
        assertTrue(affix.toString().equals("'\u00A4\u00A4"));
        assertTrue(isCurrencyFormat);
        assertEquals(1, pos);
    }

    @Test
    public void testLine1939() throws Exception {
        StringBuffer affix = new StringBuffer();
        int pos = 10;
        String pattern = " \u00A4";
        boolean isCurrencyFormat;
        boolean doubled = (pos + 1) < pattern.length() && pattern.charAt(pos + 1) == DecimalFormat.CURRENCY_SIGN;
        if (doubled)
            // Skip over the doubled character
            ++pos;
        isCurrencyFormat = true;
        assertTrue(affix.toString().equals("'\u00A4"));
        assertTrue(isCurrencyFormat);
        assertEquals(10, pos);
    }

    @Test
    public void testLine1941() throws Exception {
        StringBuffer affix = new StringBuffer();
        int pos = 1;
        String pattern = "abc\u00A4";
        boolean isCurrencyFormat;
        boolean doubled = (pos + 1) < pattern.length() && pattern.charAt(pos + 1) == DecimalFormat.CURRENCY_SIGN;
        if (doubled)
            // Skip over the doubled character
            ++pos;
        isCurrencyFormat = true;
        assertTrue(affix.toString().equals("'\u00A4"));
        assertTrue(isCurrencyFormat);
        assertEquals(1, pos);
    }

    @Test
    public void testLine1943() throws Exception {
        StringBuffer affix = new StringBuffer();
        int pos = 0;
        String pattern = " ";
        boolean isCurrencyFormat;
        boolean doubled = (pos + 1) < pattern.length() && pattern.charAt(pos + 1) == DecimalFormat.CURRENCY_SIGN;
        if (doubled)
            // Skip over the doubled character
            ++pos;
        isCurrencyFormat = true;
        assertTrue(affix.toString().equals("'\u00A4"));
        assertTrue(isCurrencyFormat);
        assertEquals(0, pos);
    }

    @Test
    public void testLine1901() throws Exception {
        boolean inQuote = true;
        int pos = 0;
        String pattern = "do";
        StringBuffer affix = new StringBuffer();
        if ((pos + 1) < pattern.length() && pattern.charAt(pos + 1) == DecimalFormat.QUOTE) {
            ++pos;
            // 'don''t'
            affix.append("''");
        } else {
            // 'do'
            inQuote = false;
        }
        assertTrue(affix.toString().isEmpty());
        assertFalse(inQuote);
        assertEquals(0, pos);
    }

    @Test
    public void testLine1902() throws Exception {
        boolean inQuote = true;
        int pos = 10;
        String pattern = "do";
        StringBuffer affix = new StringBuffer();
        if ((pos + 1) < pattern.length() && pattern.charAt(pos + 1) == DecimalFormat.QUOTE) {
            ++pos;
            // 'don''t'
            affix.append("''");
        } else {
            // 'do'
            inQuote = false;
        }
        assertTrue(affix.toString().isEmpty());
        assertFalse(inQuote);
        assertEquals(10, pos);
    }

    @Test
    public void testLine1903() throws Exception {
        boolean inQuote = true;
        int pos = 2;
        String pattern = "don't";
        StringBuffer affix = new StringBuffer();
        if ((pos + 1) < pattern.length() && pattern.charAt(pos + 1) == DecimalFormat.QUOTE) {
            ++pos;
            // 'don''t'
            affix.append("''");
        } else {
            // 'do'
            inQuote = false;
        }
        assertTrue(affix.toString().equals("''"));
        assertTrue(inQuote);
        assertEquals(3, pos);
    }
}
