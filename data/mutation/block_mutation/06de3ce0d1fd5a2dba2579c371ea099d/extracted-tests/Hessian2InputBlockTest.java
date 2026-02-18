package com.alibaba.com.caucho.hessian.io;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.alibaba.com.caucho.hessian.io.Hessian2Input.*;

public class Hessian2InputBlockTest {

    @Test
    public void testLine731() throws Exception {
        int a = 0;
        try {
            {
                assertFalse(((0x1000000L * 0 + 0x10000L * 0 + 0x100 * 0 + 0) != 0));
                return;
            }
        } finally {
        }
    }

    @Test
    public void testLine732() throws Exception {
        int a = 0;
        try {
            {
                assertTrue(((0x1000000L * 5 + 0x10000L * 10 + 0x100 * 15 + 20) != 0));
                return;
            }
        } finally {
        }
    }

    @Test
    public void testLine908() throws Exception {
        try {
            {
                assertEquals(336860180, (((20 << 24) + (20 << 16) + (20 << 8) + 20)));
                return;
            }
        } finally {
        }
    }

    @Test
    public void testLine1125() throws Exception {
        int tag = 10;
        try {
            {
                assertEquals(-13233132, (((tag - BC_INT_SHORT_ZERO) << 16) + 256 * 20 + 20));
                return;
            }
        } finally {
        }
    }

    @Test
    public void testLine1417() throws Exception {
        int tag = 10;
        try {
            {
                assertEquals(-3271660, (((tag - BC_LONG_SHORT_ZERO) << 16) + 256 * 20 + 20));
                return;
            }
        } finally {
        }
    }

    @Test
    public void testLine1810() throws Exception {
        int _offset = 1;
        int _length = 2;
        byte[] _buffer = new byte[] { 'a', 'b', 'c' };
        try {
            {
                assertEquals("98", (String.valueOf((byte) (_offset < _length ? _buffer[_offset--] : 'f'))));
                return;
            }
        } finally {
        }
    }
}
