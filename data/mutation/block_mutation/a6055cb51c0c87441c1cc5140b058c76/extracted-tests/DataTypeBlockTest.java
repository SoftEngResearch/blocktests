package com.alibaba.compileflow.engine.common.util;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.io.StringReader;
import java.lang.reflect.Modifier;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.alibaba.compileflow.engine.common.util.DataType.*;

public class DataTypeBlockTest {

    @Test(expected = RuntimeException.class)
    public void testLine913() throws Exception {
        Object value = null;
        try {
            try {
                SimpleDateFormat a = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String tmpstr = value.toString();
                if (tmpstr.trim().length() <= 10) {
                    tmpstr = tmpstr + " 00:00:00";
                }
                return;
            } catch (Exception e) {
                String msg = "Date type convert failed:" + value;
                throw new RuntimeException(msg);
            }
        } finally {
        }
    }

    @Test
    public void testLine1() throws Exception {
        Object value = "2025-12-01 19:27:04";
        try {
            try {
                SimpleDateFormat a = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String tmpstr = value.toString();
                if (tmpstr.trim().length() <= 10) {
                    tmpstr = tmpstr + " 00:00:00";
                }
                {
                    assertEquals(new java.sql.Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2025-12-01 19:27:04").getTime()), (new java.sql.Timestamp(a.parse(tmpstr).getTime())));
                    return;
                }
            } catch (Exception e) {
                String msg = "Date type convert failed:" + value;
                throw new RuntimeException(msg);
            }
        } finally {
        }
    }

    @Test
    public void hms() throws Exception {
        Object value = "19:27:04";
        try {
            try {
                SimpleDateFormat dataFormatHhmmss = new SimpleDateFormat("HH:mm:ss");
                {
                    assertEquals(new java.sql.Time(new SimpleDateFormat("HH:mm:ss").parse("19:27:04").getTime()), (new java.sql.Time(dataFormatHhmmss.parse(value.toString()).getTime())));
                    return;
                }
            } catch (Exception e) {
                String msg = "Date type convert failed:" + value;
                throw new RuntimeException(msg);
            }
        } finally {
        }
    }

    @Test(expected = RuntimeException.class)
    public void testLine1035() throws Exception {
        Object value = null;
        try {
            try {
                String tmpstr = value.toString().replace('/', '-');
                SimpleDateFormat dataFormatYyyymmdd = new SimpleDateFormat("yyyy-MM-dd");
                return;
            } catch (Exception ex) {
                if (ex instanceof RuntimeException) {
                    throw (RuntimeException) ex;
                }
                String msg = "" + value;
                throw new RuntimeException(msg);
            }
        } finally {
        }
    }

    @Test
    public void FormatConversion() throws Exception {
        Object value = "2025/12/01";
        try {
            try {
                String tmpstr = value.toString().replace('/', '-');
                SimpleDateFormat dataFormatYyyymmdd = new SimpleDateFormat("yyyy-MM-dd");
                {
                    assertEquals(new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse("2025-12-01").getTime()), (new java.sql.Date(dataFormatYyyymmdd.parse(tmpstr).getTime())));
                    return;
                }
            } catch (Exception ex) {
                if (ex instanceof RuntimeException) {
                    throw (RuntimeException) ex;
                }
                String msg = "" + value;
                throw new RuntimeException(msg);
            }
        } finally {
        }
    }
}
