package com.wildbitsfoundry.etk4j.signals.filters;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import com.wildbitsfoundry.etk4j.control.TransferFunction;
import com.wildbitsfoundry.etk4j.control.ZeroPoleGain;
import com.wildbitsfoundry.etk4j.math.complex.Complex;
import com.wildbitsfoundry.etk4j.math.functions.UnivariateFunction;
import com.wildbitsfoundry.etk4j.math.optimize.solvers.NewtonRaphson;
import com.wildbitsfoundry.etk4j.math.optimize.solvers.NewtonRaphsonComplex;
import com.wildbitsfoundry.etk4j.math.optimize.solvers.SolverResults;
import com.wildbitsfoundry.etk4j.math.polynomials.Polynomial;
import com.wildbitsfoundry.etk4j.util.ComplexArrays;
import com.wildbitsfoundry.etk4j.util.DoubleArrays;
import static com.wildbitsfoundry.etk4j.signals.filters.Filters.*;
import java.util.Arrays;
import java.util.function.Function;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.wildbitsfoundry.etk4j.signals.filters.Bessel.*;

public class BesselBlockTest {

    @Test
    public void testLine303() throws Exception {
        Complex z = new Complex(5, 10);
        int n = 10;
        try {
            Complex[] result = new Complex[1];
            com.wildbitsfoundry.etk4j.math.specialfunctions.Bessel.nonexpbesska01(n + 0.5, z.invert(), result, new Complex[1]);
            {
                assertEquals("(45532726963875710000.0000 - 74323182754758330000.0000j)", (result[(0 - 1)]).toString());
                return;
            }
        } finally {
        }
    }

    @Test
    public void testLine304() throws Exception {
        Complex z = new Complex(2, 0);
        int n = 0;
        try {
            Complex[] result = new Complex[1];
            com.wildbitsfoundry.etk4j.math.specialfunctions.Bessel.nonexpbesska01(n + 0.5, z.invert(), result, new Complex[1]);
            {
                assertEquals("(1.7725 + 0.0000j)", (result[(0 - 1)]).toString());
                return;
            }
        } finally {
        }
    }
}
