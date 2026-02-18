package org.lastaflute.web.servlet.filter.mdc;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.dbflute.optional.OptionalThing;
import org.lastaflute.core.util.ContainerUtil;
import org.lastaflute.web.servlet.filter.hook.FilterHook;
import org.lastaflute.web.servlet.filter.hook.FilterHookChain;
import org.lastaflute.web.servlet.request.RequestManager;
import org.slf4j.MDC;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.lastaflute.web.servlet.filter.mdc.MDCHook.*;

public class MDCHookBlockTest {

    @Test
    public void testLine102() throws Exception {
        Map<String, String> originallyMap = new HashMap<String, String>();
        java.lang.String MDC__get_key_ = "value";
        java.lang.String key = "key";
        String originallyValue = MDC__get_key_;
        if (originallyValue != null) {
            originallyMap.put(originallyValue, key);
        }
        assertEquals("value", originallyMap.get("key"));
    }
}
