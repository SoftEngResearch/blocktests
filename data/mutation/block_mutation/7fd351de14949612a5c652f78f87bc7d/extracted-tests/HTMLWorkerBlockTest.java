package com.lowagie.text.html.simpleparser;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.StringTokenizer;
import com.lowagie.text.Chunk;
import com.lowagie.text.DocListener;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ElementTags;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.FontFactoryImp;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.List;
import com.lowagie.text.ListItem;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.TextElementArray;
import com.lowagie.text.html.HtmlTags;
import com.lowagie.text.html.Markup;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.lowagie.text.xml.simpleparser.SimpleXMLDocHandler;
import com.lowagie.text.xml.simpleparser.SimpleXMLParser;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.lowagie.text.html.simpleparser.HTMLWorker.*;

public class HTMLWorkerBlockTest {

    @Test(expected = java.io.FileNotFoundException.class)
    public void testLine291() throws Exception {
        Image img = null;
        String src = "testing";
        HashMap interfaceProps = new HashMap() {

            {
                put("img_baseurl", "/foo");
            }
        };
        String baseurl = (String) interfaceProps.get("img_baseurl");
        if (baseurl != null) {
            src = baseurl + src;
        }
        assertEquals("/footesting", src);
    }

    @Test
    public void testLine293() throws Exception {
        Image img = null;
        String src = "testing";
        HashMap interfaceProps = new HashMap() {

            {
                put("img_baseurls", "/foo");
            }
        };
        String baseurl = (String) interfaceProps.get("img_baseurl");
        if (baseurl != null) {
            src = baseurl + src;
        }
        assertEquals("testing", src);
    }
}
