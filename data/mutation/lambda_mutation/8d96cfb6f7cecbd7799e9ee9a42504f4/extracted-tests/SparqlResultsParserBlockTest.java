package world.data.jdbc.internal.transport;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.BTest.lambdatest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import world.data.jdbc.model.Node;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static world.data.jdbc.internal.transport.ParserUtil.expect;
import org.junit.Test;
import static org.junit.Assert.*;
import static world.data.jdbc.internal.transport.SparqlResultsParser.*;

public class SparqlResultsParserBlockTest {

    @Test
    public void testLine78() throws Exception {
        JsonParser parser = null;
        boolean[] foundVariables = new boolean[] { false };
        JsonToken headToken = JsonToken.END_OBJECT;
        String headField = "vars";
        List<String> variables = new ArrayList<>();
        if ("vars".equals(headField) && headToken == JsonToken.START_ARRAY) {
            foundVariables[0] = true;
        }
        assertFalse(foundVariables[0]);
    }

    @Test
    public void testLine76() throws Exception {
        JsonParser parser = null;
        boolean[] foundVariables = new boolean[] { false };
        JsonToken headToken = JsonToken.START_ARRAY;
        String headField = "vars2";
        List<String> variables = new ArrayList<>();
        if ("vars".equals(headField) && headToken == JsonToken.START_ARRAY) {
            foundVariables[0] = true;
        }
        assertFalse(foundVariables[0]);
    }

    @Test
    public void testLine74() throws Exception {
        JsonParser parser = null;
        boolean[] foundVariables = new boolean[] { false };
        JsonToken headToken = JsonToken.START_ARRAY;
        String headField = "vars";
        List<String> variables = new ArrayList<>();
        if ("vars".equals(headField) && headToken == JsonToken.START_ARRAY) {
            foundVariables[0] = true;
        }
        assertTrue(foundVariables[0]);
    }

    @Test
    public void testLine84() throws Exception {
        JsonToken token = JsonToken.VALUE_TRUE;
        java.lang.String parser__getText__ = "fun";
        List<String> variables = new ArrayList<>();
        if (token.isScalarValue()) {
            variables.add(parser__getText__);
        }
        assertEquals("fun", variables.get(0));
    }

    @Test
    public void testLine85() throws Exception {
        JsonToken token = JsonToken.START_OBJECT;
        java.lang.String parser__getText__ = "fun";
        List<String> variables = new ArrayList<>();
        if (token.isScalarValue()) {
            variables.add(parser__getText__);
        }
        assertTrue(variables.isEmpty());
    }
}
