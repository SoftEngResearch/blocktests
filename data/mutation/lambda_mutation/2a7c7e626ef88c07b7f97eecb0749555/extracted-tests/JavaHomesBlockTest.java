package com.fizzed.jne;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static com.fizzed.jne.JavaHomes.*;

public class JavaHomesBlockTest {

    @Test
    public void testLine218() throws Exception {
        Map<String, String> finalReleaseProperties = new HashMap<String, String>();
        java.lang.String k = "foo";
        java.lang.String v = "bar";
        if (!finalReleaseProperties.containsKey(k)) {
        }
        assertTrue(finalReleaseProperties.size() == 1);
        assertEquals(null, finalReleaseProperties.get("bar"));
        assertEquals("bar", finalReleaseProperties.get("foo"));
    }

    @Test
    public void testLine221() throws Exception {
        Map<String, String> finalReleaseProperties = new HashMap<String, String>();
        java.lang.String k = "foo";
        java.lang.String v = "bar";
        finalReleaseProperties.put("foo", "foo");
        if (!finalReleaseProperties.containsKey(k)) {
        }
        assertTrue(finalReleaseProperties.size() == 1);
        assertEquals("foo", finalReleaseProperties.get("foo"));
    }
}
