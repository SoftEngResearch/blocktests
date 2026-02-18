package biweekly.util.com.google.ical.iter;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;
import biweekly.util.ByDay;
import biweekly.util.DayOfWeek;
import biweekly.util.Frequency;
import biweekly.util.Google2445Utils;
import biweekly.util.ICalDate;
import biweekly.util.Recurrence;
import biweekly.util.com.google.ical.util.Predicate;
import biweekly.util.com.google.ical.util.Predicates;
import biweekly.util.com.google.ical.util.TimeUtils;
import biweekly.util.com.google.ical.values.DateTimeValue;
import biweekly.util.com.google.ical.values.DateTimeValueImpl;
import biweekly.util.com.google.ical.values.DateValue;
import biweekly.util.com.google.ical.values.DateValueImpl;
import biweekly.util.com.google.ical.values.TimeValue;
import org.junit.Test;
import static org.junit.Assert.*;
import static biweekly.util.com.google.ical.iter.RecurrenceIteratorFactory.*;

public class RecurrenceIteratorFactoryBlockTest {

    @Test
    public void testLine238() throws Exception {
        DateValue dtStart = new DateValueImpl(0, 0, 0);
        DayOfWeek wkst = DayOfWeek.SUNDAY;
        try {
            int d = (7 + wkst.ordinal() - TimeUtils.dayOfWeek(dtStart).getCalendarConstant()) % 7;
            return;
        } finally {
            assertEquals(new DateValueImpl(-1, 11, 28), start);
        }
    }
}
