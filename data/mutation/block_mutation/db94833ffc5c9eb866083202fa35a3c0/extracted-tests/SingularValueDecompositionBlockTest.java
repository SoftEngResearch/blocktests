package com.harium.keel.catalano.math.decomposition;

import com.harium.keel.catalano.math.Constants;
import com.harium.keel.catalano.math.Matrix;
import com.harium.keel.catalano.math.Tools;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.harium.keel.catalano.math.decomposition.SingularValueDecomposition.*;

public class SingularValueDecompositionBlockTest {

    @Test
    public void testLine187() throws Exception {
        double[] s = new double[] { 0.0, 5.0, 0.0 };
        double[][] A = new double[][] { { 1.0, 2.0 }, { 4.0, 8.0 }, { 6.0, 12.0 } };
        int k = 1;
        int m = 3;
        if (s[k] != 0.0) {
            if (A[k][k] < 0.0) {
                s[k] = -s[k];
            }
            for (int i = k; i < m; i++) {
                A[i][k] /= s[k];
            }
        }
        s[k] = -s[k];
        assertEquals(-5.0, s[1], 0.01);
        assertEquals(2.4, A[2][1], 0.01);
        assertEquals(2.6, A[1][1], 0.01);
    }

    @Test
    public void testLine189() throws Exception {
        double[] s = new double[] { 0.0, 4.0, 0.0 };
        double[][] A = new double[][] { { 1.0, 2.0 }, { 3.0, -8.0 }, { 5.0, 12.0 } };
        int k = 1;
        int m = 3;
        if (s[k] != 0.0) {
            if (A[k][k] < 0.0) {
                s[k] = -s[k];
            }
            for (int i = k; i < m; i++) {
                A[i][k] /= s[k];
            }
        }
        s[k] = -s[k];
        assertEquals(4.0, s[1], 0.01);
        assertEquals(-3.0, A[2][1], 0.01);
        assertEquals(3.0, A[1][1], 0.01);
    }

    @Test
    public void testLine191() throws Exception {
        double[] s = new double[] { 0.0, 0.0, 0.0 };
        double[][] A = new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } };
        int k = 1;
        int m = 3;
        if (s[k] != 0.0) {
            if (A[k][k] < 0.0) {
                s[k] = -s[k];
            }
            for (int i = k; i < m; i++) {
                A[i][k] /= s[k];
            }
        }
        s[k] = -s[k];
        assertEquals(0.0, s[1], 0.01);
        assertEquals(6.0, A[2][1], 0.01);
        assertEquals(4.0, A[1][1], 0.01);
    }

    @Test
    public void testLine193() throws Exception {
        double[] s = new double[] { 0.0, 0.0, 3.0 };
        double[][] A = new double[][] { { 1.0, 2.0, 7.0 }, { 3.0, 4.0, 8.0 }, { 5.0, 6.0, 9.0 } };
        int k = 2;
        int m = 3;
        if (s[k] != 0.0) {
            if (A[k][k] < 0.0) {
                s[k] = -s[k];
            }
            for (int i = k; i < m; i++) {
                A[i][k] /= s[k];
            }
        }
        s[k] = -s[k];
        assertEquals(-3.0, s[2], 0.01);
        assertEquals(4.0, A[2][2], 0.01);
    }

    @Test
    public void testLine195() throws Exception {
        double[] s = new double[] { 0.0, -6.0, 0.0 };
        double[][] A = new double[][] { { 1.0, 2.0 }, { 3.0, 12.0 }, { 5.0, 18.0 } };
        int k = 1;
        int m = 3;
        if (s[k] != 0.0) {
            if (A[k][k] < 0.0) {
                s[k] = -s[k];
            }
            for (int i = k; i < m; i++) {
                A[i][k] /= s[k];
            }
        }
        s[k] = -s[k];
        assertEquals(6.0, s[1], 0.01);
        assertEquals(-3.0, A[2][1], 0.01);
        assertEquals(-1.0, A[1][1], 0.01);
    }

    @Test
    public void testLine197() throws Exception {
        double[] s = new double[] { 0.0, 0.001, 0.0 };
        double[][] A = new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } };
        int k = 1;
        int m = 3;
        if (s[k] != 0.0) {
            if (A[k][k] < 0.0) {
                s[k] = -s[k];
            }
            for (int i = k; i < m; i++) {
                A[i][k] /= s[k];
            }
        }
        s[k] = -s[k];
        assertEquals(-0.001, s[1], 0.01);
        assertEquals(6000.0, A[2][1], 0.01);
        assertEquals(4001.0, A[1][1], 0.01);
    }

    @Test
    public void testLine262() throws Exception {
        double[] work = new double[] { 0.0, 0.0, 0.0 };
        double[] e = new double[] { 1.0, 2.0, 3.0 };
        double[][] A = new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } };
        int k = 0;
        int m = 3;
        int n = 3;
        for (int i = k + 1; i < m; i++) {
            work[i] = 0.0;
        }
        for (int j = k + 1; j < n; j++) {
            for (int i = k + 1; i < m; i++) {
                work[i] += e[j] * A[i][j];
            }
        }
        for (int j = k + 1; j < n; j++) {
            double t = -e[j] / e[k + 1];
            for (int i = k + 1; i < m; i++) {
                A[i][j] += t * work[i];
            }
        }
        assertEquals(-55.5, A[2][2], 0.001);
        assertEquals(-35, A[2][1], 0.001);
        assertEquals(-36, A[1][2], 0.001);
        assertEquals(-23, A[1][1], 0.001);
        assertEquals(43, work[2], 0.001);
        assertEquals(28, work[1], 0.001);
    }

    @Test
    public void testLine264() throws Exception {
        double[] work = new double[] { 0.0, 0.0, 0.0, 0.0 };
        double[] e = new double[] { 1.0, 2.0, 4.0, 6.0 };
        double[][] A = new double[][] { { 1.0, 2.0, 3.0, 4.0 }, { 5.0, 6.0, 7.0, 8.0 }, { 9.0, 10.0, 11.0, 12.0 }, { 13.0, 14.0, 15.0, 16.0 } };
        int k = 1;
        int m = 4;
        int n = 4;
        for (int i = k + 1; i < m; i++) {
            work[i] = 0.0;
        }
        for (int j = k + 1; j < n; j++) {
            for (int i = k + 1; i < m; i++) {
                work[i] += e[j] * A[i][j];
            }
        }
        for (int j = k + 1; j < n; j++) {
            double t = -e[j] / e[k + 1];
            for (int i = k + 1; i < m; i++) {
                A[i][j] += t * work[i];
            }
        }
        assertEquals(-218, A[3][3], 0.001);
        assertEquals(-141.0, A[3][2], 0.001);
        assertEquals(-162, A[2][3], 0.001);
        assertEquals(-105.0, A[2][2], 0.001);
        assertEquals(156, work[3], 0.001);
        assertEquals(116, work[2], 0.001);
    }

    @Test
    public void testLine266() throws Exception {
        double[] work = new double[] { 0.0, 0.0, 0.0 };
        double[] e = new double[] { 1.0, 2.0, 3.0 };
        double[][] A = new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } };
        int k = 1;
        int m = 3;
        int n = 2;
        for (int i = k + 1; i < m; i++) {
            work[i] = 0.0;
        }
        for (int j = k + 1; j < n; j++) {
            for (int i = k + 1; i < m; i++) {
                work[i] += e[j] * A[i][j];
            }
        }
        for (int j = k + 1; j < n; j++) {
            double t = -e[j] / e[k + 1];
            for (int i = k + 1; i < m; i++) {
                A[i][j] += t * work[i];
            }
        }
        assertEquals(9.0, A[2][2], 0.001);
        assertEquals(8.0, A[2][1], 0.001);
        assertEquals(0.0, work[2], 0.001);
    }

    @Test
    public void testLine268() throws Exception {
        double[] work = new double[] { 0.0, 0.0 };
        double[] e = new double[] { 1.0, 2.0, 3.0 };
        double[][] A = new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } };
        int k = 0;
        int m = 2;
        int n = 3;
        for (int i = k + 1; i < m; i++) {
            work[i] = 0.0;
        }
        for (int j = k + 1; j < n; j++) {
            for (int i = k + 1; i < m; i++) {
                work[i] += e[j] * A[i][j];
            }
        }
        for (int j = k + 1; j < n; j++) {
            double t = -e[j] / e[k + 1];
            for (int i = k + 1; i < m; i++) {
                A[i][j] += t * work[i];
            }
        }
        assertEquals(-36.0, A[1][2], 0.001);
        assertEquals(-23, A[1][1], 0.001);
        assertEquals(28, work[1], 0.001);
    }

    @Test
    public void testLine270() throws Exception {
        double[] work = new double[] { 0.0, 0.0, 0.0 };
        double[] e = new double[] { 1.0, -2.0, 3.0 };
        double[][] A = new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, -5.0, 6.0 }, { 7.0, 8.0, -9.0 } };
        int k = 0;
        int m = 3;
        int n = 3;
        for (int i = k + 1; i < m; i++) {
            work[i] = 0.0;
        }
        for (int j = k + 1; j < n; j++) {
            for (int i = k + 1; i < m; i++) {
                work[i] += e[j] * A[i][j];
            }
        }
        for (int j = k + 1; j < n; j++) {
            double t = -e[j] / e[k + 1];
            for (int i = k + 1; i < m; i++) {
                A[i][j] += t * work[i];
            }
        }
        assertEquals(-73.5, A[2][2], 0.001);
        assertEquals(51, A[2][1], 0.001);
        assertEquals(48, A[1][2], 0.001);
        assertEquals(-33, A[1][1], 0.001);
        assertEquals(-43, work[2], 0.001);
        assertEquals(28, work[1], 0.001);
    }
}
