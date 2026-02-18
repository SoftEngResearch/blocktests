package com.tidb.jdbc.conf;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.tidb.jdbc.conf.StringInspector.*;

public class StringInspectorBlockTest {

    @Test
    public void testLine467() throws Exception {
        int pos = 1;
        int srcLen = 5;
        String source = "12345";
        int stopAt = 0;
        int i = 0;
        for (; i < StringInspector.NON_COMMENTS_MYSQL_VERSION_REF_LENGTH; i++) {
            if (pos + 1 + i >= srcLen || !Character.isDigit(source.charAt(pos + 1 + i))) {
                break;
            }
        }
        if (i == StringInspector.NON_COMMENTS_MYSQL_VERSION_REF_LENGTH) {
            pos += StringInspector.NON_COMMENTS_MYSQL_VERSION_REF_LENGTH;
            if (pos >= stopAt) {
                // Reached stop position. Correct position will be set by outer loop.
                pos = stopAt - 1;
            }
        }
        assertEquals(1, pos);
        assertEquals(3, i);
    }

    @Test
    public void testLine468() throws Exception {
        int pos = 1;
        int srcLen = 5;
        String source = "1234a";
        int stopAt = 0;
        int i = 0;
        for (; i < StringInspector.NON_COMMENTS_MYSQL_VERSION_REF_LENGTH; i++) {
            if (pos + 1 + i >= srcLen || !Character.isDigit(source.charAt(pos + 1 + i))) {
                break;
            }
        }
        if (i == StringInspector.NON_COMMENTS_MYSQL_VERSION_REF_LENGTH) {
            pos += StringInspector.NON_COMMENTS_MYSQL_VERSION_REF_LENGTH;
            if (pos >= stopAt) {
                // Reached stop position. Correct position will be set by outer loop.
                pos = stopAt - 1;
            }
        }
        assertEquals(1, pos);
        assertEquals(2, i);
    }

    @Test
    public void testLine469() throws Exception {
        int pos = 1;
        int srcLen = 10;
        String source = "1234567890";
        int stopAt = 100;
        int i = 0;
        for (; i < StringInspector.NON_COMMENTS_MYSQL_VERSION_REF_LENGTH; i++) {
            if (pos + 1 + i >= srcLen || !Character.isDigit(source.charAt(pos + 1 + i))) {
                break;
            }
        }
        if (i == StringInspector.NON_COMMENTS_MYSQL_VERSION_REF_LENGTH) {
            pos += StringInspector.NON_COMMENTS_MYSQL_VERSION_REF_LENGTH;
            if (pos >= stopAt) {
                // Reached stop position. Correct position will be set by outer loop.
                pos = stopAt - 1;
            }
        }
        assertEquals(6, pos);
        assertEquals(5, i);
    }

    @Test
    public void testLine470() throws Exception {
        int pos = 1;
        int srcLen = 10;
        String source = "1234567890";
        int stopAt = 1;
        int i = 0;
        for (; i < StringInspector.NON_COMMENTS_MYSQL_VERSION_REF_LENGTH; i++) {
            if (pos + 1 + i >= srcLen || !Character.isDigit(source.charAt(pos + 1 + i))) {
                break;
            }
        }
        if (i == StringInspector.NON_COMMENTS_MYSQL_VERSION_REF_LENGTH) {
            pos += StringInspector.NON_COMMENTS_MYSQL_VERSION_REF_LENGTH;
            if (pos >= stopAt) {
                // Reached stop position. Correct position will be set by outer loop.
                pos = stopAt - 1;
            }
        }
        assertEquals(0, pos);
        assertEquals(5, i);
    }

    @Test
    public void testLine471() throws Exception {
        int pos = 1;
        int srcLen = 10;
        String source = "1234567890";
        int stopAt = 2;
        int i = 0;
        for (; i < StringInspector.NON_COMMENTS_MYSQL_VERSION_REF_LENGTH; i++) {
            if (pos + 1 + i >= srcLen || !Character.isDigit(source.charAt(pos + 1 + i))) {
                break;
            }
        }
        if (i == StringInspector.NON_COMMENTS_MYSQL_VERSION_REF_LENGTH) {
            pos += StringInspector.NON_COMMENTS_MYSQL_VERSION_REF_LENGTH;
            if (pos >= stopAt) {
                // Reached stop position. Correct position will be set by outer loop.
                pos = stopAt - 1;
            }
        }
        assertEquals(1, pos);
        assertEquals(5, i);
    }

    @Test
    public void testLine472() throws Exception {
        int pos = 1;
        int srcLen = 10;
        String source = "1234567890";
        int stopAt = 0;
        int i = 0;
        for (; i < StringInspector.NON_COMMENTS_MYSQL_VERSION_REF_LENGTH; i++) {
            if (pos + 1 + i >= srcLen || !Character.isDigit(source.charAt(pos + 1 + i))) {
                break;
            }
        }
        if (i == StringInspector.NON_COMMENTS_MYSQL_VERSION_REF_LENGTH) {
            pos += StringInspector.NON_COMMENTS_MYSQL_VERSION_REF_LENGTH;
            if (pos >= stopAt) {
                // Reached stop position. Correct position will be set by outer loop.
                pos = stopAt - 1;
            }
        }
        assertEquals(-1, pos);
        assertEquals(5, i);
    }

    @Test
    public void testLine424() throws Exception {
        char c1 = ' ';
        char c0 = '\r';
        int pos = 5;
        char c2 = ' ';
        int srcLen = 8;
        String source = "Hello\rorld";
        c1 = pos + 1 < srcLen ? source.charAt(pos + 1) : Character.MIN_VALUE;
        if (c0 == '\r' && c1 == '\n') {
            // Skip next char ('\n').
            pos++;
            c1 = pos + 1 < srcLen ? source.charAt(pos + 1) : Character.MIN_VALUE;
        }
        c2 = pos + 2 < srcLen ? source.charAt(pos + 2) : Character.MIN_VALUE;
        assertEquals('r', c2);
        assertEquals('o', c1);
    }

    @Test
    public void testLine425() throws Exception {
        char c1 = ' ';
        char c0 = '\r';
        int pos = 5;
        char c2 = ' ';
        int srcLen = 8;
        String source = "Hello\r\nrld";
        c1 = pos + 1 < srcLen ? source.charAt(pos + 1) : Character.MIN_VALUE;
        if (c0 == '\r' && c1 == '\n') {
            // Skip next char ('\n').
            pos++;
            c1 = pos + 1 < srcLen ? source.charAt(pos + 1) : Character.MIN_VALUE;
        }
        c2 = pos + 2 < srcLen ? source.charAt(pos + 2) : Character.MIN_VALUE;
        assertEquals(Character.MIN_VALUE, c2);
        assertEquals('r', c1);
    }

    @Test
    public void testLine426() throws Exception {
        char c1 = ' ';
        char c0 = '\r';
        int pos = 5;
        char c2 = ' ';
        int srcLen = 9;
        String source = "Hello\r\nrld";
        c1 = pos + 1 < srcLen ? source.charAt(pos + 1) : Character.MIN_VALUE;
        if (c0 == '\r' && c1 == '\n') {
            // Skip next char ('\n').
            pos++;
            c1 = pos + 1 < srcLen ? source.charAt(pos + 1) : Character.MIN_VALUE;
        }
        c2 = pos + 2 < srcLen ? source.charAt(pos + 2) : Character.MIN_VALUE;
        assertEquals('l', c2);
        assertEquals('r', c1);
    }

    @Test
    public void testLine427() throws Exception {
        char c1 = ' ';
        char c0 = 'W';
        int pos = 5;
        char c2 = ' ';
        int srcLen = 8;
        String source = "HelloW\nrld";
        c1 = pos + 1 < srcLen ? source.charAt(pos + 1) : Character.MIN_VALUE;
        if (c0 == '\r' && c1 == '\n') {
            // Skip next char ('\n').
            pos++;
            c1 = pos + 1 < srcLen ? source.charAt(pos + 1) : Character.MIN_VALUE;
        }
        c2 = pos + 2 < srcLen ? source.charAt(pos + 2) : Character.MIN_VALUE;
        assertEquals('r', c2);
        assertEquals('\n', c1);
    }

    @Test
    public void testLine428() throws Exception {
        char c1 = ' ';
        char c0 = '\r';
        int pos = 15;
        char c2 = ' ';
        int srcLen = 8;
        String source = "Hello\rorld";
        c1 = pos + 1 < srcLen ? source.charAt(pos + 1) : Character.MIN_VALUE;
        if (c0 == '\r' && c1 == '\n') {
            // Skip next char ('\n').
            pos++;
            c1 = pos + 1 < srcLen ? source.charAt(pos + 1) : Character.MIN_VALUE;
        }
        c2 = pos + 2 < srcLen ? source.charAt(pos + 2) : Character.MIN_VALUE;
        assertEquals(Character.MIN_VALUE, c2);
        assertEquals(Character.MIN_VALUE, c1);
    }

    @Test
    public void testLine429() throws Exception {
        char c1 = ' ';
        char c0 = '\r';
        int pos = 5;
        char c2 = ' ';
        int srcLen = 7;
        String source = "Hello\rorld";
        c1 = pos + 1 < srcLen ? source.charAt(pos + 1) : Character.MIN_VALUE;
        if (c0 == '\r' && c1 == '\n') {
            // Skip next char ('\n').
            pos++;
            c1 = pos + 1 < srcLen ? source.charAt(pos + 1) : Character.MIN_VALUE;
        }
        c2 = pos + 2 < srcLen ? source.charAt(pos + 2) : Character.MIN_VALUE;
        assertEquals(Character.MIN_VALUE, c2);
        assertEquals('o', c1);
    }
}
