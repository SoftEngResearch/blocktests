package com.google.json;

import java.math.BigInteger;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.google.json.JsonSanitizer.*;

public class JsonSanitizerBlockTest {

    @Test
    public void testLine348() throws Exception {
        int i = 1;
        String jsonish = "abcdef";
        int end = i + 1;
        int n = jsonish.length();
        end = n;
        for (int j = i + 2; j < n; ++j) {
            char cch = jsonish.charAt(j);
            if (cch == '\n' || cch == '\r' || cch == '\u2028' || cch == '\u2029') {
                end = j + 1;
                break;
            }
        }
        assertEquals(6, end);
    }

    @Test
    public void testLine349() throws Exception {
        int i = 1;
        String jsonish = "abcdef\n";
        int end = i + 1;
        int n = jsonish.length();
        end = n;
        for (int j = i + 2; j < n; ++j) {
            char cch = jsonish.charAt(j);
            if (cch == '\n' || cch == '\r' || cch == '\u2028' || cch == '\u2029') {
                end = j + 1;
                break;
            }
        }
        assertEquals(7, end);
    }

    @Test
    public void testLine350() throws Exception {
        int i = 1;
        String jsonish = "abcd\nf";
        int end = i + 1;
        int n = jsonish.length();
        end = n;
        for (int j = i + 2; j < n; ++j) {
            char cch = jsonish.charAt(j);
            if (cch == '\n' || cch == '\r' || cch == '\u2028' || cch == '\u2029') {
                end = j + 1;
                break;
            }
        }
        assertEquals(5, end);
    }

    @Test
    public void testLine351() throws Exception {
        int i = 1;
        String jsonish = "\nbcd\nf";
        int end = i + 1;
        int n = jsonish.length();
        end = n;
        for (int j = i + 2; j < n; ++j) {
            char cch = jsonish.charAt(j);
            if (cch == '\n' || cch == '\r' || cch == '\u2028' || cch == '\u2029') {
                end = j + 1;
                break;
            }
        }
        assertEquals(5, end);
    }

    @Test
    public void testLine352() throws Exception {
        int i = 1;
        String jsonish = "\nbcd\rf";
        int end = i + 1;
        int n = jsonish.length();
        end = n;
        for (int j = i + 2; j < n; ++j) {
            char cch = jsonish.charAt(j);
            if (cch == '\n' || cch == '\r' || cch == '\u2028' || cch == '\u2029') {
                end = j + 1;
                break;
            }
        }
        assertEquals(5, end);
    }

    @Test
    public void testLine353() throws Exception {
        int i = 1;
        String jsonish = "\nbcd\u2028f";
        int end = i + 1;
        int n = jsonish.length();
        end = n;
        for (int j = i + 2; j < n; ++j) {
            char cch = jsonish.charAt(j);
            if (cch == '\n' || cch == '\r' || cch == '\u2028' || cch == '\u2029') {
                end = j + 1;
                break;
            }
        }
        assertEquals(5, end);
    }

    @Test
    public void testLine354() throws Exception {
        int i = 1;
        String jsonish = "\nbcd\u2029f";
        int end = i + 1;
        int n = jsonish.length();
        end = n;
        for (int j = i + 2; j < n; ++j) {
            char cch = jsonish.charAt(j);
            if (cch == '\n' || cch == '\r' || cch == '\u2028' || cch == '\u2029') {
                end = j + 1;
                break;
            }
        }
        assertEquals(5, end);
    }

    @Test
    public void testLine355() throws Exception {
        int i = 1;
        String jsonish = "\nbcd\tf";
        int end = i + 1;
        int n = jsonish.length();
        end = n;
        for (int j = i + 2; j < n; ++j) {
            char cch = jsonish.charAt(j);
            if (cch == '\n' || cch == '\r' || cch == '\u2028' || cch == '\u2029') {
                end = j + 1;
                break;
            }
        }
        assertEquals(6, end);
    }

    @Test
    public void testLine356() throws Exception {
        int i = 0;
        String jsonish = "ab\nd\nf";
        int end = i + 1;
        int n = jsonish.length();
        end = n;
        for (int j = i + 2; j < n; ++j) {
            char cch = jsonish.charAt(j);
            if (cch == '\n' || cch == '\r' || cch == '\u2028' || cch == '\u2029') {
                end = j + 1;
                break;
            }
        }
        assertEquals(3, end);
    }

    @Test
    public void testLine369() throws Exception {
        int end = 0;
        String jsonish = "/*comment*/rest";
        int i = 0;
        int n = jsonish.length();
        end = n;
        if (i + 3 < n) {
            for (int j = i + 2; (j = jsonish.indexOf('/', j + 1)) >= 0; ) {
                if (jsonish.charAt(j - 1) == '*') {
                    end = j + 1;
                    break;
                }
            }
        }
        assertEquals(11, end);
    }

    @Test
    public void testLine371() throws Exception {
        int end = 0;
        String jsonish = "/*text/more*/data";
        int i = 0;
        int n = jsonish.length();
        end = n;
        if (i + 3 < n) {
            for (int j = i + 2; (j = jsonish.indexOf('/', j + 1)) >= 0; ) {
                if (jsonish.charAt(j - 1) == '*') {
                    end = j + 1;
                    break;
                }
            }
        }
        assertEquals(13, end);
    }

    @Test
    public void testLine373() throws Exception {
        int end = 0;
        String jsonish = "/*comment/no/end";
        int i = 0;
        int n = jsonish.length();
        end = n;
        if (i + 3 < n) {
            for (int j = i + 2; (j = jsonish.indexOf('/', j + 1)) >= 0; ) {
                if (jsonish.charAt(j - 1) == '*') {
                    end = j + 1;
                    break;
                }
            }
        }
        assertEquals(16, end);
    }

    @Test
    public void testLine375() throws Exception {
        int end = 0;
        String jsonish = "/*";
        int i = 0;
        int n = jsonish.length();
        end = n;
        if (i + 3 < n) {
            for (int j = i + 2; (j = jsonish.indexOf('/', j + 1)) >= 0; ) {
                if (jsonish.charAt(j - 1) == '*') {
                    end = j + 1;
                    break;
                }
            }
        }
        assertEquals(2, end);
    }

    @Test
    public void testLine377() throws Exception {
        int end = 0;
        String jsonish = "//*text/end/here*/";
        int i = 1;
        int n = jsonish.length();
        end = n;
        if (i + 3 < n) {
            for (int j = i + 2; (j = jsonish.indexOf('/', j + 1)) >= 0; ) {
                if (jsonish.charAt(j - 1) == '*') {
                    end = j + 1;
                    break;
                }
            }
        }
        assertEquals(18, end);
    }
}
