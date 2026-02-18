package org.xcsp.modeler.implementation;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import org.xcsp.common.Utilities;
import org.xcsp.modeler.api.ProblemAPI;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.xcsp.modeler.implementation.ProblemDataHandler.*;

public class ProblemDataHandlerBlockTest {

    @Test
    public void testLine200() throws Exception {
        Object object = new ProblemDataHandler("test");
        Field f = ProblemDataHandler.class.getDeclaredFields()[0];
        try {
            try {
                f.setAccessible(true);
                {
                    assertEquals("null", (f.get(object) == null ? "null" : f.get(object)));
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                {
                    assertEquals("null", (null));
                    return;
                }
            }
        } finally {
        }
    }

    @Test
    public void testLine201() throws Exception {
        Object object = null;
        Field f = org.xcsp.parser.loaders.CtrLoaderSymbolic.class.getDeclaredFields()[0];
        try {
            try {
                f.setAccessible(true);
                {
                    assertEquals(null, (f.get(object) == null ? "null" : f.get(object)));
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                {
                    assertEquals(null, (null));
                    return;
                }
            }
        } finally {
        }
    }

    @Test
    public void testLine1() throws Exception {
        Object object = new org.xcsp.parser.loaders.CtrLoaderSymbolic(new org.xcsp.parser.callbacks.CompetitionValidator(Boolean.TRUE, false, "foo"));
        Field f = org.xcsp.parser.loaders.CtrLoaderSymbolic.class.getDeclaredFields()[0];
        try {
            try {
                f.setAccessible(true);
                {
                    assertTrue((f.get(object) == null ? "null" : f.get(object)) != null);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                {
                    assertTrue((null) != null);
                    return;
                }
            }
        } finally {
        }
    }

    @Test
    public void testLine186() throws Exception {
        Object object = new ProblemDataHandler();
        Field f = ProblemDataHandler.class.getDeclaredFields()[0];
        try {
            try {
                f.setAccessible(true);
                {
                    assertEquals("testing", (f.get(object) == null ? "null" : f.get(object)));
                    return;
                }
            } catch (Exception e) {
                {
                    assertEquals("testing", ("null"));
                    return;
                }
            }
        } finally {
        }
    }

    @Test
    public void testLine187() throws Exception {
        Object object = new ProblemDataHandler("test");
        Field f = ProblemDataHandler.class.getDeclaredFields()[0];
        try {
            try {
                f.setAccessible(true);
                {
                    assertEquals("null", (f.get(object) == null ? "null" : f.get(object)));
                    return;
                }
            } catch (Exception e) {
                {
                    assertEquals("null", ("null"));
                    return;
                }
            }
        } finally {
        }
    }

    @Test
    public void test2() throws Exception {
        Object object = null;
        Field f = org.xcsp.parser.loaders.CtrLoaderSymbolic.class.getDeclaredFields()[0];
        try {
            try {
                f.setAccessible(true);
                {
                    assertEquals("null", (f.get(object) == null ? "null" : f.get(object)));
                    return;
                }
            } catch (Exception e) {
                {
                    assertEquals("null", ("null"));
                    return;
                }
            }
        } finally {
        }
    }
}
