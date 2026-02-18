package org.lastaflute.web.servlet.filter;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.lastaflute.web.servlet.filter.RequestLoggingFilter.*;

public class RequestLoggingFilterBlockTest {

    @Test
    public void testLine757() throws Exception {
        String title = "foo";
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("*Cannot send error as '").append(title).append("' because of already committed:");
            {
                assertEquals("*Cannot send error as 'foo' because of already committed: path=/bar", (sb.toString()));
                return;
            }
        } finally {
        }
    }
}
