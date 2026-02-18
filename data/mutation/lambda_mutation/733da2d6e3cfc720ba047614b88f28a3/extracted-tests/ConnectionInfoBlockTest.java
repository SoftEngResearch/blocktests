package com.googlecode.paradox;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.BTest.lambdatest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import com.googlecode.paradox.data.filefilters.DirectoryFilter;
import com.googlecode.paradox.exceptions.DataError;
import com.googlecode.paradox.exceptions.ParadoxDataException;
import com.googlecode.paradox.exceptions.ParadoxException;
import com.googlecode.paradox.exceptions.ParadoxNotSupportedException;
import com.googlecode.paradox.metadata.Schema;
import com.googlecode.paradox.metadata.schema.DirectorySchema;
import com.googlecode.paradox.metadata.schema.SystemSchema;
import com.googlecode.paradox.utils.Expressions;
import java.io.File;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.googlecode.paradox.ConnectionInfo.*;

public class ConnectionInfoBlockTest {

    @Test
    public void testLine511() throws Exception {
        File catalog = new File(System.getProperty("user.dir") + "/target/generated-sources/annotations");
        Locale locale = ConnectionInfo.DEFAULT_LOCALE;
        try {
            File[] schemas = catalog.listFiles(new DirectoryFilter(locale));
            {
                assertFalse(((1 == 1) && schemas.length > 0));
                return;
            }
        } finally {
        }
    }

    @Test
    public void testLine510() throws Exception {
        File catalog = new File(System.getProperty("user.dir"));
        Locale locale = ConnectionInfo.DEFAULT_LOCALE;
        try {
            File[] schemas = catalog.listFiles(new DirectoryFilter(locale));
            {
                assertTrue(((1 == 1) && schemas.length > 0));
                return;
            }
        } finally {
        }
    }
}
