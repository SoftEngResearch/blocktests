package com.esotericsoftware.yamlbeans;

import static com.esotericsoftware.yamlbeans.parser.EventType.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import com.esotericsoftware.yamlbeans.Beans.Property;
import com.esotericsoftware.yamlbeans.parser.AliasEvent;
import com.esotericsoftware.yamlbeans.parser.CollectionStartEvent;
import com.esotericsoftware.yamlbeans.parser.Event;
import com.esotericsoftware.yamlbeans.parser.EventType;
import com.esotericsoftware.yamlbeans.parser.Parser;
import com.esotericsoftware.yamlbeans.parser.Parser.ParserException;
import com.esotericsoftware.yamlbeans.parser.ScalarEvent;
import com.esotericsoftware.yamlbeans.scalar.ScalarSerializer;
import com.esotericsoftware.yamlbeans.tokenizer.Tokenizer.TokenizerException;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.esotericsoftware.yamlbeans.YamlReader.*;

public class YamlReaderBlockTest {

    @Test
    public void testLine358() throws Exception {
        Event nextEvent = new com.esotericsoftware.yamlbeans.parser.SequenceStartEvent("anchor", "myTag", true, false);
        Object object = new HashMap<String, String>();
        Object key = "foo";
        java.lang.String config__tagSuffix = ".conf";
        switch(nextEvent.type) {
            case MAPPING_START:
            case SEQUENCE_START:
                ((Map) object).put(key + config__tagSuffix, ((CollectionStartEvent) nextEvent).tag);
                break;
            case SCALAR:
                ((Map) object).put(key + config__tagSuffix, ((ScalarEvent) nextEvent).tag);
                break;
        }
        assertEquals("myTag", ((Map<String, String>) object).get("foo.conf"));
    }

    @Test
    public void testLine361() throws Exception {
        Event nextEvent = new com.esotericsoftware.yamlbeans.parser.MappingStartEvent("anchor", "myTag2", true, false);
        Object object = new HashMap<String, String>();
        Object key = "foo2";
        java.lang.String config__tagSuffix = ".conf2";
        switch(nextEvent.type) {
            case MAPPING_START:
            case SEQUENCE_START:
                ((Map) object).put(key + config__tagSuffix, ((CollectionStartEvent) nextEvent).tag);
                break;
            case SCALAR:
                ((Map) object).put(key + config__tagSuffix, ((ScalarEvent) nextEvent).tag);
                break;
        }
        assertEquals("myTag2", ((Map<String, String>) object).get("foo2.conf2"));
    }

    @Test
    public void testLine364() throws Exception {
        Event nextEvent = new com.esotericsoftware.yamlbeans.parser.ScalarEvent("anchor", "myTag3", new boolean[] { true, false }, "test", 'c');
        Object object = new HashMap<String, String>();
        Object key = "foo3";
        java.lang.String config__tagSuffix = ".conf3";
        switch(nextEvent.type) {
            case MAPPING_START:
            case SEQUENCE_START:
                ((Map) object).put(key + config__tagSuffix, ((CollectionStartEvent) nextEvent).tag);
                break;
            case SCALAR:
                ((Map) object).put(key + config__tagSuffix, ((ScalarEvent) nextEvent).tag);
                break;
        }
        assertEquals("myTag3", ((Map<String, String>) object).get("foo3.conf3"));
    }

    @Test
    public void testLine367() throws Exception {
        Event nextEvent = new com.esotericsoftware.yamlbeans.parser.DocumentStartEvent(false, null, null);
        Object object = new HashMap<String, String>();
        Object key = "foo4";
        java.lang.String config__tagSuffix = ".conf4";
        switch(nextEvent.type) {
            case MAPPING_START:
            case SEQUENCE_START:
                ((Map) object).put(key + config__tagSuffix, ((CollectionStartEvent) nextEvent).tag);
                break;
            case SCALAR:
                ((Map) object).put(key + config__tagSuffix, ((ScalarEvent) nextEvent).tag);
                break;
        }
        assertEquals(true, ((Map<String, String>) object).isEmpty());
    }
}
