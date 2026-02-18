package com.github.vincentrussell.ini;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.commons.text.StringSubstitutor;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import static org.apache.commons.lang3.ObjectUtils.firstNonNull;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.github.vincentrussell.ini.Ini.*;

public class IniBlockTest {

    @Test
    public void testLine1() throws Exception {
        Map<String, Object> variables = new HashMap<>();
        MutableObject<String> section = new MutableObject<>(NO_SECTION);
        variables.put("a", "b");
        try {
            StringSubstitutor stringSubstitutor = new StringSubstitutor(new DelegateMapWrapper(variables, new HashMap<String, Object>()));
            {
                assertTrue((stringSubstitutor).toString().contains("org.apache.commons.text.StringSubstitutor"));
                return;
            }
        } finally {
        }
    }
}
