package org.v8LogScanner.logsCfg;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.v8LogScanner.commonly.Constants;
import org.v8LogScanner.commonly.ExcpReporting;
import org.v8LogScanner.commonly.fsys;
import org.v8LogScanner.logs.LogsOperations;
import org.v8LogScanner.rgx.RegExp;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.Provider;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.v8LogScanner.logsCfg.LogBuilder.*;

public class LogBuilderBlockTest {

    @Test
    public void testLine1() throws Exception {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element logEl = null;
        LogEvent event = new LogEvent();
        logEl = doc.createElement(LogConfig.LOC_TAG_NAME);
        Element eventEl = doc.createElement(LogConfig.EVENT_TAG_NAME);
        for (LogEvent.EventRow eventRow : event) {
            Element eventPropEl = doc.createElement(eventRow.getComparison());
            eventPropEl.setAttribute(LogConfig.PROP_NAME, eventRow.getKey());
            eventPropEl.setAttribute(LogConfig.VAL_NAME, eventRow.getVal());
            eventEl.appendChild(eventPropEl);
        }
        logEl.appendChild(eventEl);
        assertEquals(1, logEl.getChildNodes().getLength());
    }

    @Test
    public void testB() throws Exception {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element logEl = null;
        LogProperty prop = new LogProperty("bar");
        String DUMMY = "dummy";
        logEl = doc.createElement(LogConfig.LOC_TAG_NAME);
        Element propEl = doc.createElement(LogConfig.PROP_NAME);
        propEl.setAttribute(LogConfig.PROPERTY_PROP_NAME, prop.getName());
        Element dummyEl = doc.createElement(DUMMY);
        logEl.appendChild(propEl);
        assertEquals(1, logEl.getChildNodes().getLength());
    }
}
