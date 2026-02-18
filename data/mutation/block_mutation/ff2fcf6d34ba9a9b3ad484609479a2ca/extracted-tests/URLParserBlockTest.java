package io.mola.galimatias;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import static io.mola.galimatias.URLUtils.*;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static io.mola.galimatias.URLParser.*;

public class URLParserBlockTest {

    @Test
    public void testLine486() throws Exception {
        ParseURLState state = null;
        int c = '\\';
        String scheme = "file";
        if (c == '\\') {
        }
        if ("file".equals(scheme)) {
            state = ParseURLState.FILE_HOST;
        } else {
            state = ParseURLState.AUTHORITY_IGNORE_SLASHES;
        }
        assertEquals(ParseURLState.FILE_HOST, state);
    }

    @Test
    public void testLine487() throws Exception {
        ParseURLState state = null;
        int c = '\\';
        String scheme = "https";
        if (c == '\\') {
        }
        if ("file".equals(scheme)) {
            state = ParseURLState.FILE_HOST;
        } else {
            state = ParseURLState.AUTHORITY_IGNORE_SLASHES;
        }
        assertEquals(ParseURLState.AUTHORITY_IGNORE_SLASHES, state);
    }
}
