package com.expression.parser.function;

import java.util.ArrayList;
import java.util.List;
import com.expression.parser.ParserManager;
import com.expression.parser.exception.CalculatorException;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.expression.parser.function.ComplexFunction.*;

public class ComplexFunctionBlockTest {

    @Test
    public void testLine108() throws Exception {
        String number = "10.5";
        int i = 5;
        String f = "abcdefghi";
        List<String> variables;
        List<Complex> values;
        boolean hasNumber = false;
        Complex value = new Complex(0, 0);
        Double numb = new Double(number);
        String new_f = f.substring(i + 1, f.length());
        value = Complex.add(new Complex(numb, 0), new Complex(5, 1));
        i += new_f.length();
        hasNumber = false;
        assertEquals("", number);
        assertFalse(hasNumber);
        assertEquals("ghi", new_f);
        assertEquals(8, i);
        assertEquals(1, value.getI(), 0.001);
        assertEquals(15.5, value.getR(), 0.001);
    }
}
