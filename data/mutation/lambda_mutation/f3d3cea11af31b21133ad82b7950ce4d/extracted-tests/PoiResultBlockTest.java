package com.github.stupdit1t.excel.common;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.github.stupdit1t.excel.common.PoiResult.*;

public class PoiResultBlockTest {

    @Test
    public void testLine86() throws Exception {
        ErrorMessage e = new ErrorMessage("a", 1, 2, new FileNotFoundException("b"));
        try {
            int col = e.getCol();
            int row = e.getRow();
            String location = e.getLocation();
            Exception exception = e.getException();
            if (col != -1 && row != -1) {
                {
                    assertEquals("a-b", (String.format("%s-%s", location, exception.getMessage())));
                    return;
                }
            }
            {
                {
                    assertEquals("a-b", (exception.getMessage()));
                    return;
                }
            }
        } finally {
        }
    }

    @Test
    public void testLine88() throws Exception {
        ErrorMessage e = new ErrorMessage("a", 2, 0, new FileNotFoundException("b!"));
        try {
            int col = e.getCol();
            int row = e.getRow();
            String location = e.getLocation();
            Exception exception = e.getException();
            if (col != -1 && row != -1) {
                {
                    assertEquals("a-b!", (String.format("%s-%s", location, exception.getMessage())));
                    return;
                }
            }
            {
                {
                    assertEquals("a-b!", (exception.getMessage()));
                    return;
                }
            }
        } finally {
        }
    }

    @Test
    public void testLine90() throws Exception {
        ErrorMessage e = new ErrorMessage("a", -1, 2, new FileNotFoundException("bX"));
        try {
            int col = e.getCol();
            int row = e.getRow();
            String location = e.getLocation();
            Exception exception = e.getException();
            if (col != -1 && row != -1) {
                {
                    assertEquals("bX", (String.format("%s-%s", location, exception.getMessage())));
                    return;
                }
            }
            {
                {
                    assertEquals("bX", (exception.getMessage()));
                    return;
                }
            }
        } finally {
        }
    }

    @Test
    public void testLine92() throws Exception {
        ErrorMessage e = new ErrorMessage("a", 1, -1, new FileNotFoundException("b"));
        try {
            int col = e.getCol();
            int row = e.getRow();
            String location = e.getLocation();
            Exception exception = e.getException();
            if (col != -1 && row != -1) {
                {
                    assertEquals("b", (String.format("%s-%s", location, exception.getMessage())));
                    return;
                }
            }
            {
                {
                    assertEquals("b", (exception.getMessage()));
                    return;
                }
            }
        } finally {
        }
    }

    @Test
    public void testLine94() throws Exception {
        ErrorMessage e = new ErrorMessage("a", -1, -1, new FileNotFoundException("bW"));
        try {
            int col = e.getCol();
            int row = e.getRow();
            String location = e.getLocation();
            Exception exception = e.getException();
            if (col != -1 && row != -1) {
                {
                    assertEquals("bW", (String.format("%s-%s", location, exception.getMessage())));
                    return;
                }
            }
            {
                {
                    assertEquals("bW", (exception.getMessage()));
                    return;
                }
            }
        } finally {
        }
    }
}
