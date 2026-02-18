package com.lowagie.text.xml.simpleparser;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Stack;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.lowagie.text.xml.simpleparser.SimpleXMLParser.*;

public class SimpleXMLParserBlockTest {

    @Test
    public void testLine383() throws Exception {
        int state = 0;
        StringBuilder entity = new StringBuilder().append("#x400");
        StringBuilder text = new StringBuilder();
        String cent = entity.toString();
        entity.setLength(0);
        char ce = EntitiesToUnicode.decodeEntity(cent);
        if (ce == '\0')
            text.append('&').append(cent).append(';');
        else
            text.append(ce);
        assertTrue(entity.toString().isEmpty());
        assertEquals("", text.toString());
    }

    @Test
    public void testLine385() throws Exception {
        int state = 0;
        StringBuilder entity = new StringBuilder().append("#100");
        StringBuilder text = new StringBuilder();
        String cent = entity.toString();
        entity.setLength(0);
        char ce = EntitiesToUnicode.decodeEntity(cent);
        if (ce == '\0')
            text.append('&').append(cent).append(';');
        else
            text.append(ce);
        assertTrue(entity.toString().isEmpty());
        assertEquals("d", text.toString());
    }

    @Test
    public void testLine387() throws Exception {
        int state = 0;
        StringBuilder entity = new StringBuilder().append("nbsp");
        StringBuilder text = new StringBuilder();
        String cent = entity.toString();
        entity.setLength(0);
        char ce = EntitiesToUnicode.decodeEntity(cent);
        if (ce == '\0')
            text.append('&').append(cent).append(';');
        else
            text.append(ce);
        assertTrue(entity.toString().isEmpty());
        assertEquals("" + '\u00a0', text.toString());
    }

    @Test
    public void testLine389() throws Exception {
        int state = 0;
        StringBuilder entity = new StringBuilder().append("#x-853749375934987595");
        StringBuilder text = new StringBuilder();
        String cent = entity.toString();
        entity.setLength(0);
        char ce = EntitiesToUnicode.decodeEntity(cent);
        if (ce == '\0')
            text.append('&').append(cent).append(';');
        else
            text.append(ce);
        assertTrue(entity.toString().isEmpty());
        assertEquals("&#x-853749375934987595;", text.toString());
    }

    @Test
    public void testLine391() throws Exception {
        int state = 0;
        StringBuilder entity = new StringBuilder().append("#abc");
        StringBuilder text = new StringBuilder();
        String cent = entity.toString();
        entity.setLength(0);
        char ce = EntitiesToUnicode.decodeEntity(cent);
        if (ce == '\0')
            text.append('&').append(cent).append(';');
        else
            text.append(ce);
        assertTrue(entity.toString().isEmpty());
        assertEquals("&#abc;", text.toString());
    }

    @Test
    public void testLine393() throws Exception {
        int state = 0;
        StringBuilder entity = new StringBuilder().append("lol");
        StringBuilder text = new StringBuilder();
        String cent = entity.toString();
        entity.setLength(0);
        char ce = EntitiesToUnicode.decodeEntity(cent);
        if (ce == '\0')
            text.append('&').append(cent).append(';');
        else
            text.append(ce);
        assertTrue(entity.toString().isEmpty());
        assertEquals("&lol;", text.toString());
    }
}
