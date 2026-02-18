package com.hummeling.if97;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import static com.hummeling.if97.IF97.*;
import static java.lang.Double.NaN;
import static java.lang.StrictMath.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.hummeling.if97.Region3.*;

public class Region3BlockTest {

    @Test
    public void testLine1207() throws Exception {
        double pi = Math.PI;
        double theta = 0.5;
        double omega = 0;
        SubRegion subRegion = SubRegion.x;
        double[] x;
        try {
            x = new double[] { pi - subRegion.A, theta - subRegion.B };
            for (double[] ijn : subRegion.IJn) {
                omega += ijn[2] * pow(x[0], ijn[0]) * pow(x[1], ijn[1]);
            }
            {
                assertEquals(Double.POSITIVE_INFINITY, (exp(omega) * subRegion.nuRed), Double.MIN_VALUE);
                return;
            }
        } finally {
        }
    }

    @Test
    public void testLine1218() throws Exception {
        double pi = Math.PI;
        double theta = 0.5;
        double omega = 0;
        SubRegion subRegion = SubRegion.x;
        double[] x = new double[] { pi - subRegion.A, theta - subRegion.B };
        try {
            x = new double[] { pow(pi - subRegion.A, subRegion.C), pow(theta - subRegion.B, subRegion.D) };
            for (double[] ijn : subRegion.IJn) {
                omega += ijn[2] * pow(x[0], ijn[0]) * pow(x[1], ijn[1]);
            }
            {
                assertEquals(2.5436585071070736E22, (pow(subRegion.E, omega) * subRegion.nuRed), Double.MIN_VALUE);
                return;
            }
        } finally {
        }
    }
}
