package com.mathlibrary.function;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.util.ArrayList;
import java.util.List;
import com.mathlibrary.exception.CalculatorException;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.mathlibrary.function.FunctionXs.*;

public class FunctionXsBlockTest {

    @Test(expected = CalculatorException.class)
    public void testLine421() throws Exception {
        String function = "fx";
        List<String> variables = java.util.Arrays.asList("x", "z", "t", "fx");
        List<Double> values = java.util.Arrays.asList(1.0, 2.0, 3.0, 4.0);
        double value = 0;
        if (function.length() == 1) {
            int n = variables.indexOf(function);
            if (n >= 0) {
                double v = values.get(n).doubleValue();
                value = v;
            } else {
                throw new CalculatorException("function is not well defined");
            }
        } else {
            throw new CalculatorException("function is not well defined");
        }
    }

    @Test
    public void testLine423() throws Exception {
        String function = "t";
        List<String> variables = java.util.Arrays.asList("x", "z", "t", "fx");
        List<Double> values = java.util.Arrays.asList(1.0, 2.0, 3.0, 4.0);
        double value = 0;
        if (function.length() == 1) {
            int n = variables.indexOf(function);
            if (n >= 0) {
                double v = values.get(n).doubleValue();
                value = v;
            } else {
                throw new CalculatorException("function is not well defined");
            }
        } else {
            throw new CalculatorException("function is not well defined");
        }
        assertEquals(3.0, value, 0.001);
    }

    @Test(expected = CalculatorException.class)
    public void testLine425() throws Exception {
        String function = "t";
        List<String> variables = java.util.Arrays.asList("x", "z", "m", "fx");
        List<Double> values = java.util.Arrays.asList(1.0, 2.0, 3.0, 4.0);
        double value = 0;
        if (function.length() == 1) {
            int n = variables.indexOf(function);
            if (n >= 0) {
                double v = values.get(n).doubleValue();
                value = v;
            } else {
                throw new CalculatorException("function is not well defined");
            }
        } else {
            throw new CalculatorException("function is not well defined");
        }
    }

    @Test(expected = CalculatorException.class)
    public void testLine427() throws Exception {
        String function = "";
        List<String> variables = java.util.Arrays.asList("x", "z", "m", "fx");
        List<Double> values = java.util.Arrays.asList(1.0, 2.0, 3.0, 4.0);
        double value = 0;
        if (function.length() == 1) {
            int n = variables.indexOf(function);
            if (n >= 0) {
                double v = values.get(n).doubleValue();
                value = v;
            } else {
                throw new CalculatorException("function is not well defined");
            }
        } else {
            throw new CalculatorException("function is not well defined");
        }
    }
}
