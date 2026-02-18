package com.healthmarketscience.jackcess.impl.expr;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import com.healthmarketscience.jackcess.expr.EvalContext;
import com.healthmarketscience.jackcess.expr.EvalException;
import com.healthmarketscience.jackcess.expr.NumericConfig;
import com.healthmarketscience.jackcess.expr.TemporalConfig;
import com.healthmarketscience.jackcess.expr.Value;
import org.apache.commons.lang3.StringUtils;
import static com.healthmarketscience.jackcess.impl.expr.ExpressionTokenizer.ExprBuf;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.healthmarketscience.jackcess.impl.expr.FormatUtil.*;

public class FormatUtilBlockTest {

    @Test
    public void testLine1097() throws Exception {
        java.lang.StringBuilder sb = new StringBuilder();
        int cs__next__ = 65;
        int tmp = cs__next__;
        sb.append((tmp < FormatUtil.NO_CHAR) ? (char) tmp : ' ');
        assertEquals("A", sb.toString());
        assertTrue(sb.length() == 1);
    }

    @Test
    public void testLine1098() throws Exception {
        java.lang.StringBuilder sb = new StringBuilder();
        int cs__next__ = -1;
        int tmp = cs__next__;
        sb.append((tmp < FormatUtil.NO_CHAR) ? (char) tmp : ' ');
        assertEquals(" ", sb.toString());
        assertTrue(sb.length() == 1);
    }

    @Test
    public void testLine1108() throws Exception {
        java.lang.StringBuilder sb = new StringBuilder();
        int cs__next__ = 65;
        int tmp = cs__next__;
        if (tmp != FormatUtil.NO_CHAR) {
            sb.append((char) tmp);
        }
        assertEquals("A", sb.toString());
        assertTrue(sb.length() == 1);
    }

    @Test
    public void testLine1109() throws Exception {
        java.lang.StringBuilder sb = new StringBuilder();
        int cs__next__ = -1;
        int tmp = cs__next__;
        if (tmp != FormatUtil.NO_CHAR) {
            sb.append((char) tmp);
        }
        assertTrue(sb.length() == 0);
    }
}
