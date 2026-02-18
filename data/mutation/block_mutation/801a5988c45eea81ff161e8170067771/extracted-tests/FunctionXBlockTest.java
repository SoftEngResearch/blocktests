package com.mathlibrary.function;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import com.mathlibrary.exception.CalculatorException;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.mathlibrary.function.FunctionX.*;

public class FunctionXBlockTest {

    @Test(expected = CalculatorException.class)
    public void testLine328() throws Exception {
        boolean hasNumber = true;
        String number = "123";
        int i = 5;
        String f_x = "123456";
        char character = '4';
        try {
            if (i == (f_x.length() - 1)) {
                throw new CalculatorException("The function is not well-formed");
            }
            if (hasNumber && (number.length() > 0)) {
            }
            return;
        } finally {
        }
    }

    @Test
    public void testLine329() throws Exception {
        boolean hasNumber = true;
        String number = "123";
        int i = 5;
        String f_x = "1234567";
        char character = '4';
        if (i == (f_x.length() - 1)) {
            throw new CalculatorException("The function is not well-formed");
        }
        if (hasNumber && (number.length() > 0)) {
        }
        assertEquals("1234", number);
    }

    @Test
    public void testLine330() throws Exception {
        boolean hasNumber = false;
        String number = "123";
        int i = 5;
        String f_x = "1234567";
        char character = 'x';
        if (i == (f_x.length() - 1)) {
            throw new CalculatorException("The function is not well-formed");
        }
        if (hasNumber && (number.length() > 0)) {
        }
        assertEquals("123", number);
    }

    @Test
    public void testLine331() throws Exception {
        boolean hasNumber = true;
        String number = "";
        int i = 5;
        String f_x = "1234567";
        char character = 'x';
        if (i == (f_x.length() - 1)) {
            throw new CalculatorException("The function is not well-formed");
        }
        if (hasNumber && (number.length() > 0)) {
        }
        assertEquals("", number);
    }
}
