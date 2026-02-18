package com.sforce.ws.parser;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.sforce.ws.parser.MXParser.*;

public class MXParserBlockTest {

    @Test
    public void testLine1935() throws Exception {
        int xmlnsPos = 1;
        char ch = 'm';
        boolean startsWithXmlns = true;
        if (xmlnsPos == 1) {
            if (ch != 'm')
                startsWithXmlns = false;
        } else if (xmlnsPos == 2) {
            if (ch != 'l')
                startsWithXmlns = false;
        } else if (xmlnsPos == 3) {
            if (ch != 'n')
                startsWithXmlns = false;
        } else if (xmlnsPos == 4) {
            if (ch != 's')
                startsWithXmlns = false;
        } else if (xmlnsPos == 5) {
            if (ch != ':')
                throw new XmlPullParserException("after xmlns in attribute name must be colon" + "when namespaces are enabled", new MXParser(), null);
        }
        assertTrue(startsWithXmlns);
    }

    @Test
    public void testLine1936() throws Exception {
        int xmlnsPos = 1;
        char ch = 'a';
        boolean startsWithXmlns = true;
        if (xmlnsPos == 1) {
            if (ch != 'm')
                startsWithXmlns = false;
        } else if (xmlnsPos == 2) {
            if (ch != 'l')
                startsWithXmlns = false;
        } else if (xmlnsPos == 3) {
            if (ch != 'n')
                startsWithXmlns = false;
        } else if (xmlnsPos == 4) {
            if (ch != 's')
                startsWithXmlns = false;
        } else if (xmlnsPos == 5) {
            if (ch != ':')
                throw new XmlPullParserException("after xmlns in attribute name must be colon" + "when namespaces are enabled", new MXParser(), null);
        }
        assertFalse(startsWithXmlns);
    }

    @Test
    public void testLine1938() throws Exception {
        int xmlnsPos = 2;
        char ch = 'l';
        boolean startsWithXmlns = true;
        if (xmlnsPos == 1) {
            if (ch != 'm')
                startsWithXmlns = false;
        } else if (xmlnsPos == 2) {
            if (ch != 'l')
                startsWithXmlns = false;
        } else if (xmlnsPos == 3) {
            if (ch != 'n')
                startsWithXmlns = false;
        } else if (xmlnsPos == 4) {
            if (ch != 's')
                startsWithXmlns = false;
        } else if (xmlnsPos == 5) {
            if (ch != ':')
                throw new XmlPullParserException("after xmlns in attribute name must be colon" + "when namespaces are enabled", new MXParser(), null);
        }
        assertTrue(startsWithXmlns);
    }

    @Test
    public void testLine1939() throws Exception {
        int xmlnsPos = 2;
        char ch = 'a';
        boolean startsWithXmlns = true;
        if (xmlnsPos == 1) {
            if (ch != 'm')
                startsWithXmlns = false;
        } else if (xmlnsPos == 2) {
            if (ch != 'l')
                startsWithXmlns = false;
        } else if (xmlnsPos == 3) {
            if (ch != 'n')
                startsWithXmlns = false;
        } else if (xmlnsPos == 4) {
            if (ch != 's')
                startsWithXmlns = false;
        } else if (xmlnsPos == 5) {
            if (ch != ':')
                throw new XmlPullParserException("after xmlns in attribute name must be colon" + "when namespaces are enabled", new MXParser(), null);
        }
        assertFalse(startsWithXmlns);
    }

    @Test
    public void testLine1941() throws Exception {
        int xmlnsPos = 3;
        char ch = 'n';
        boolean startsWithXmlns = true;
        if (xmlnsPos == 1) {
            if (ch != 'm')
                startsWithXmlns = false;
        } else if (xmlnsPos == 2) {
            if (ch != 'l')
                startsWithXmlns = false;
        } else if (xmlnsPos == 3) {
            if (ch != 'n')
                startsWithXmlns = false;
        } else if (xmlnsPos == 4) {
            if (ch != 's')
                startsWithXmlns = false;
        } else if (xmlnsPos == 5) {
            if (ch != ':')
                throw new XmlPullParserException("after xmlns in attribute name must be colon" + "when namespaces are enabled", new MXParser(), null);
        }
        assertTrue(startsWithXmlns);
    }

    @Test
    public void testLine1942() throws Exception {
        int xmlnsPos = 3;
        char ch = 'a';
        boolean startsWithXmlns = true;
        if (xmlnsPos == 1) {
            if (ch != 'm')
                startsWithXmlns = false;
        } else if (xmlnsPos == 2) {
            if (ch != 'l')
                startsWithXmlns = false;
        } else if (xmlnsPos == 3) {
            if (ch != 'n')
                startsWithXmlns = false;
        } else if (xmlnsPos == 4) {
            if (ch != 's')
                startsWithXmlns = false;
        } else if (xmlnsPos == 5) {
            if (ch != ':')
                throw new XmlPullParserException("after xmlns in attribute name must be colon" + "when namespaces are enabled", new MXParser(), null);
        }
        assertFalse(startsWithXmlns);
    }

    @Test
    public void testLine1944() throws Exception {
        int xmlnsPos = 4;
        char ch = 's';
        boolean startsWithXmlns = true;
        if (xmlnsPos == 1) {
            if (ch != 'm')
                startsWithXmlns = false;
        } else if (xmlnsPos == 2) {
            if (ch != 'l')
                startsWithXmlns = false;
        } else if (xmlnsPos == 3) {
            if (ch != 'n')
                startsWithXmlns = false;
        } else if (xmlnsPos == 4) {
            if (ch != 's')
                startsWithXmlns = false;
        } else if (xmlnsPos == 5) {
            if (ch != ':')
                throw new XmlPullParserException("after xmlns in attribute name must be colon" + "when namespaces are enabled", new MXParser(), null);
        }
        assertTrue(startsWithXmlns);
    }

    @Test
    public void testLine1945() throws Exception {
        int xmlnsPos = 4;
        char ch = 'a';
        boolean startsWithXmlns = true;
        if (xmlnsPos == 1) {
            if (ch != 'm')
                startsWithXmlns = false;
        } else if (xmlnsPos == 2) {
            if (ch != 'l')
                startsWithXmlns = false;
        } else if (xmlnsPos == 3) {
            if (ch != 'n')
                startsWithXmlns = false;
        } else if (xmlnsPos == 4) {
            if (ch != 's')
                startsWithXmlns = false;
        } else if (xmlnsPos == 5) {
            if (ch != ':')
                throw new XmlPullParserException("after xmlns in attribute name must be colon" + "when namespaces are enabled", new MXParser(), null);
        }
        assertFalse(startsWithXmlns);
    }

    @Test(expected = XmlPullParserException.class)
    public void testLine1947() throws Exception {
        int xmlnsPos = 5;
        char ch = 'a';
        boolean startsWithXmlns = true;
        if (xmlnsPos == 1) {
            if (ch != 'm')
                startsWithXmlns = false;
        } else if (xmlnsPos == 2) {
            if (ch != 'l')
                startsWithXmlns = false;
        } else if (xmlnsPos == 3) {
            if (ch != 'n')
                startsWithXmlns = false;
        } else if (xmlnsPos == 4) {
            if (ch != 's')
                startsWithXmlns = false;
        } else if (xmlnsPos == 5) {
            if (ch != ':')
                throw new XmlPullParserException("after xmlns in attribute name must be colon" + "when namespaces are enabled", new MXParser(), null);
        }
    }

    @Test
    public void testLine1948() throws Exception {
        int xmlnsPos = 5;
        char ch = ':';
        boolean startsWithXmlns = true;
        if (xmlnsPos == 1) {
            if (ch != 'm')
                startsWithXmlns = false;
        } else if (xmlnsPos == 2) {
            if (ch != 'l')
                startsWithXmlns = false;
        } else if (xmlnsPos == 3) {
            if (ch != 'n')
                startsWithXmlns = false;
        } else if (xmlnsPos == 4) {
            if (ch != 's')
                startsWithXmlns = false;
        } else if (xmlnsPos == 5) {
            if (ch != ':')
                throw new XmlPullParserException("after xmlns in attribute name must be colon" + "when namespaces are enabled", new MXParser(), null);
        }
        assertTrue(startsWithXmlns);
    }

    @Test(expected = XmlPullParserException.class)
    public void testLine1985() throws Exception {
        int pos = 10;
        int colonPos = 9;
        int bufAbsoluteStart = 1;
        int nameLen = pos - 2 - (colonPos - bufAbsoluteStart);
        if (nameLen == 0) {
            throw new XmlPullParserException("namespace prefix is required after xmlns: " + " when namespaces are enabled", new MXParser(), null);
        }
    }

    @Test
    public void testLine1986() throws Exception {
        int pos = 10;
        int colonPos = 9;
        int bufAbsoluteStart = 2;
        int nameLen = pos - 2 - (colonPos - bufAbsoluteStart);
        if (nameLen == 0) {
            throw new XmlPullParserException("namespace prefix is required after xmlns: " + " when namespaces are enabled", new MXParser(), null);
        }
    }
}
