package com.github.packageurl;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.github.packageurl.PackageURL.*;

public class PackageURLBlockTest {

    @Test
    public void testLine425() throws Exception {
        java.util.Map.Entry<java.lang.String, java.lang.String> entry = new TreeMap<String, String>() {

            {
                put("a", "%b");
            }
        }.entrySet().iterator().next();
        StringBuilder purl = new StringBuilder();
        purl.append(entry.getKey().toLowerCase());
        purl.append("=");
        purl.append(PackageURL.uriEncode(entry.getValue(), StandardCharsets.UTF_8));
        assertEquals("a=%25b&", purl.toString());
    }
}
