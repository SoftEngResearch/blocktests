package org.threeten.bp.format;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import static org.threeten.bp.temporal.ChronoField.DAY_OF_MONTH;
import static org.threeten.bp.temporal.ChronoField.HOUR_OF_DAY;
import static org.threeten.bp.temporal.ChronoField.INSTANT_SECONDS;
import static org.threeten.bp.temporal.ChronoField.MINUTE_OF_HOUR;
import static org.threeten.bp.temporal.ChronoField.MONTH_OF_YEAR;
import static org.threeten.bp.temporal.ChronoField.NANO_OF_SECOND;
import static org.threeten.bp.temporal.ChronoField.OFFSET_SECONDS;
import static org.threeten.bp.temporal.ChronoField.SECOND_OF_MINUTE;
import static org.threeten.bp.temporal.ChronoField.YEAR;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import org.threeten.bp.DateTimeException;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.chrono.ChronoLocalDate;
import org.threeten.bp.chrono.Chronology;
import org.threeten.bp.format.SimpleDateTimeTextProvider.LocaleStore;
import org.threeten.bp.jdk8.Jdk8Methods;
import org.threeten.bp.temporal.ChronoField;
import org.threeten.bp.temporal.IsoFields;
import org.threeten.bp.temporal.TemporalAccessor;
import org.threeten.bp.temporal.TemporalField;
import org.threeten.bp.temporal.TemporalQueries;
import org.threeten.bp.temporal.TemporalQuery;
import org.threeten.bp.temporal.ValueRange;
import org.threeten.bp.temporal.WeekFields;
import org.threeten.bp.zone.ZoneRulesProvider;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
import static org.threeten.bp.format.DateTimeFormatterBuilder.*;

public class DateTimeFormatterBuilderBlockTest {

    @Test
    public void testLine1498() throws Exception {
        int pos = 0;
        String pattern = "\'\'\'";
        int start = pos++;
        for (; pos < pattern.length(); pos++) {
            if (pattern.charAt(pos) == '\'') {
                if (pos + 1 < pattern.length() && pattern.charAt(pos + 1) == '\'') {
                    pos++;
                } else {
                    // end of literal
                    break;
                }
            }
        }
        assertEquals(3, pos);
    }

    @Test
    public void testLine1405() throws Exception {
        String pattern = "aabbb";
        int pos = 2;
        int pad = 0;
        int count = 2;
        int start = 0;
        char cur = pattern.charAt(pos);
        cur = pattern.charAt(pos);
        if ((cur >= 'A' && cur <= 'Z') || (cur >= 'a' && cur <= 'z')) {
            pad = count;
            start = pos++;
            // short loop
            for (; pos < pattern.length() && pattern.charAt(pos) == cur; pos++) ;
            count = pos - start;
        }
        assertEquals(3, count);
    }
}
