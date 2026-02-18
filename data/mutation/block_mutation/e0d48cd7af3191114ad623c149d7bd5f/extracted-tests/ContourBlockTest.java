package wcontour;

import wcontour.global.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static wcontour.Contour.*;

public class ContourBlockTest {

    @Test
    public void testLine124() throws Exception {
        int i = 1;
        int j = 2;
        int[][] S1 = new int[][] { { 0, 0, 0, 0, 0 }, { 0, 0, 1, 0, 0 }, { 0, 0, 0, 0, 0 } };
        int rb;
        int r;
        int b;
        int rt;
        int t;
        int lb;
        int lt;
        boolean isContinue = false;
        int l;
        if (// data point
        S1[i][j] == 1) {
            l = S1[i][j - 1];
            r = S1[i][j + 1];
            b = S1[i - 1][j];
            t = S1[i + 1][j];
            lb = S1[i - 1][j - 1];
            rb = S1[i - 1][j + 1];
            lt = S1[i + 1][j - 1];
            rt = S1[i + 1][j + 1];
            if (// Up, down, left and right points are all undefine data
            (l == 0 && r == 0) || (b == 0 && t == 0)) {
                S1[i][j] = 0;
                isContinue = true;
            }
            if ((lt == 0 && r == 0 && b == 0) || (rt == 0 && l == 0 && b == 0) || (lb == 0 && r == 0 && t == 0) || (rb == 0 && l == 0 && t == 0)) {
                S1[i][j] = 0;
                isContinue = true;
            }
        }
        assertEquals(0, S1[1][2]);
        assertTrue(isContinue);
    }

    @Test
    public void testLine129() throws Exception {
        int i = 1;
        int j = 2;
        int[][] S1 = new int[][] { { 0, 0, 0, 0, 0 }, // center point isolated vertically
        { 0, 2, 1, 3, 0 }, { 0, 0, 0, 0, 0 } };
        int rb;
        int r;
        int b;
        int rt;
        int t;
        int lb;
        int lt;
        boolean isContinue = false;
        int l;
        if (// data point
        S1[i][j] == 1) {
            l = S1[i][j - 1];
            r = S1[i][j + 1];
            b = S1[i - 1][j];
            t = S1[i + 1][j];
            lb = S1[i - 1][j - 1];
            rb = S1[i - 1][j + 1];
            lt = S1[i + 1][j - 1];
            rt = S1[i + 1][j + 1];
            if (// Up, down, left and right points are all undefine data
            (l == 0 && r == 0) || (b == 0 && t == 0)) {
                S1[i][j] = 0;
                isContinue = true;
            }
            if ((lt == 0 && r == 0 && b == 0) || (rt == 0 && l == 0 && b == 0) || (lb == 0 && r == 0 && t == 0) || (rb == 0 && l == 0 && t == 0)) {
                S1[i][j] = 0;
                isContinue = true;
            }
        }
        assertEquals(0, S1[1][2]);
        assertTrue(isContinue);
    }

    @Test
    public void testLine134() throws Exception {
        int i = 1;
        int j = 2;
        int[][] S1 = new int[][] { { 0, 0, 0, 0, 0 }, // r == 0, b == 0
        { 0, 2, 1, 0, 0 }, // lt == 0
        { 0, 0, 0, 0, 0 } };
        int rb;
        int r;
        int b;
        int rt;
        int t;
        int lb;
        int lt;
        boolean isContinue = false;
        int l;
        if (// data point
        S1[i][j] == 1) {
            l = S1[i][j - 1];
            r = S1[i][j + 1];
            b = S1[i - 1][j];
            t = S1[i + 1][j];
            lb = S1[i - 1][j - 1];
            rb = S1[i - 1][j + 1];
            lt = S1[i + 1][j - 1];
            rt = S1[i + 1][j + 1];
            if (// Up, down, left and right points are all undefine data
            (l == 0 && r == 0) || (b == 0 && t == 0)) {
                S1[i][j] = 0;
                isContinue = true;
            }
            if ((lt == 0 && r == 0 && b == 0) || (rt == 0 && l == 0 && b == 0) || (lb == 0 && r == 0 && t == 0) || (rb == 0 && l == 0 && t == 0)) {
                S1[i][j] = 0;
                isContinue = true;
            }
        }
        assertEquals(0, S1[1][2]);
        assertTrue(isContinue);
    }

    @Test
    public void testLine139() throws Exception {
        int i = 1;
        int j = 2;
        int[][] S1 = new int[][] { { 2, 3, 4, 5, 6 }, // center point well connected
        { 7, 8, 1, 9, 10 }, { 11, 12, 13, 14, 15 } };
        int rb;
        int r;
        int b;
        int rt;
        int t;
        int lb;
        int lt;
        boolean isContinue = false;
        int l;
        if (// data point
        S1[i][j] == 1) {
            l = S1[i][j - 1];
            r = S1[i][j + 1];
            b = S1[i - 1][j];
            t = S1[i + 1][j];
            lb = S1[i - 1][j - 1];
            rb = S1[i - 1][j + 1];
            lt = S1[i + 1][j - 1];
            rt = S1[i + 1][j + 1];
            if (// Up, down, left and right points are all undefine data
            (l == 0 && r == 0) || (b == 0 && t == 0)) {
                S1[i][j] = 0;
                isContinue = true;
            }
            if ((lt == 0 && r == 0 && b == 0) || (rt == 0 && l == 0 && b == 0) || (lb == 0 && r == 0 && t == 0) || (rb == 0 && l == 0 && t == 0)) {
                S1[i][j] = 0;
                isContinue = true;
            }
        }
        assertEquals(1, S1[1][2]);
        assertFalse(isContinue);
    }

    @Test
    public void testLine144() throws Exception {
        int i = 1;
        int j = 2;
        int[][] S1 = new int[][] { { 0, 0, 0, 0, 0 }, // center point is not 1
        { 0, 0, 2, 0, 0 }, { 0, 0, 0, 0, 0 } };
        int rb;
        int r;
        int b;
        int rt;
        int t;
        int lb;
        int lt;
        boolean isContinue = false;
        int l;
        if (// data point
        S1[i][j] == 1) {
            l = S1[i][j - 1];
            r = S1[i][j + 1];
            b = S1[i - 1][j];
            t = S1[i + 1][j];
            lb = S1[i - 1][j - 1];
            rb = S1[i - 1][j + 1];
            lt = S1[i + 1][j - 1];
            rt = S1[i + 1][j + 1];
            if (// Up, down, left and right points are all undefine data
            (l == 0 && r == 0) || (b == 0 && t == 0)) {
                S1[i][j] = 0;
                isContinue = true;
            }
            if ((lt == 0 && r == 0 && b == 0) || (rt == 0 && l == 0 && b == 0) || (lb == 0 && r == 0 && t == 0) || (rb == 0 && l == 0 && t == 0)) {
                S1[i][j] = 0;
                isContinue = true;
            }
        }
        assertEquals(2, S1[1][2]);
        assertFalse(isContinue);
    }

    @Test
    public void testLine271() throws Exception {
        int i = 1;
        int j = 1;
        int[][] UNum = new int[3][3];
        int[][] S2 = new int[][] { { 0, 1, 0 }, // center point with cross pattern
        { 1, 1, 1 }, { 0, 1, 1 } };
        int rb;
        int r;
        int b;
        int rt;
        int t;
        int lb;
        int lt;
        int l;
        if (S2[i][j] == 1) {
            l = S2[i][j - 1];
            r = S2[i][j + 1];
            b = S2[i - 1][j];
            t = S2[i + 1][j];
            lb = S2[i - 1][j - 1];
            rb = S2[i - 1][j + 1];
            lt = S2[i + 1][j - 1];
            rt = S2[i + 1][j + 1];
            // ---- Cross point with two boder lines, will be used twice.
            if (l == 1 && r == 1 && b == 1 && t == 1 && ((lb == 0 && rt == 0) || (rb == 0 && lt == 0))) {
                UNum[i][j] = 2;
            } else {
                UNum[i][j] = 1;
            }
        } else {
            UNum[i][j] = 0;
        }
        assertEquals(2, UNum[1][1]);
    }

    @Test
    public void testLine276() throws Exception {
        int i = 1;
        int j = 1;
        int[][] UNum = new int[3][3];
        int[][] S2 = new int[][] { { 1, 1, 0 }, // center point with cross pattern
        { 1, 1, 1 }, { 0, 1, 0 } };
        int rb;
        int r;
        int b;
        int rt;
        int t;
        int lb;
        int lt;
        int l;
        if (S2[i][j] == 1) {
            l = S2[i][j - 1];
            r = S2[i][j + 1];
            b = S2[i - 1][j];
            t = S2[i + 1][j];
            lb = S2[i - 1][j - 1];
            rb = S2[i - 1][j + 1];
            lt = S2[i + 1][j - 1];
            rt = S2[i + 1][j + 1];
            // ---- Cross point with two boder lines, will be used twice.
            if (l == 1 && r == 1 && b == 1 && t == 1 && ((lb == 0 && rt == 0) || (rb == 0 && lt == 0))) {
                UNum[i][j] = 2;
            } else {
                UNum[i][j] = 1;
            }
        } else {
            UNum[i][j] = 0;
        }
        assertEquals(2, UNum[1][1]);
    }

    @Test
    public void testLine281() throws Exception {
        int i = 1;
        int j = 1;
        int[][] UNum = new int[3][3];
        int[][] S2 = new int[][] { { 1, 1, 1 }, // all neighbors are 1, no diagonal zeros
        { 1, 1, 1 }, { 1, 1, 1 } };
        int rb;
        int r;
        int b;
        int rt;
        int t;
        int lb;
        int lt;
        int l;
        if (S2[i][j] == 1) {
            l = S2[i][j - 1];
            r = S2[i][j + 1];
            b = S2[i - 1][j];
            t = S2[i + 1][j];
            lb = S2[i - 1][j - 1];
            rb = S2[i - 1][j + 1];
            lt = S2[i + 1][j - 1];
            rt = S2[i + 1][j + 1];
            // ---- Cross point with two boder lines, will be used twice.
            if (l == 1 && r == 1 && b == 1 && t == 1 && ((lb == 0 && rt == 0) || (rb == 0 && lt == 0))) {
                UNum[i][j] = 2;
            } else {
                UNum[i][j] = 1;
            }
        } else {
            UNum[i][j] = 0;
        }
        assertEquals(1, UNum[1][1]);
    }

    @Test
    public void testLine286() throws Exception {
        int i = 1;
        int j = 1;
        int[][] UNum = new int[3][3];
        int[][] S2 = new int[][] { { 0, 1, 0 }, // missing right neighbor
        { 1, 1, 0 }, { 0, 1, 0 } };
        int rb;
        int r;
        int b;
        int rt;
        int t;
        int lb;
        int lt;
        int l;
        if (S2[i][j] == 1) {
            l = S2[i][j - 1];
            r = S2[i][j + 1];
            b = S2[i - 1][j];
            t = S2[i + 1][j];
            lb = S2[i - 1][j - 1];
            rb = S2[i - 1][j + 1];
            lt = S2[i + 1][j - 1];
            rt = S2[i + 1][j + 1];
            // ---- Cross point with two boder lines, will be used twice.
            if (l == 1 && r == 1 && b == 1 && t == 1 && ((lb == 0 && rt == 0) || (rb == 0 && lt == 0))) {
                UNum[i][j] = 2;
            } else {
                UNum[i][j] = 1;
            }
        } else {
            UNum[i][j] = 0;
        }
        assertEquals(1, UNum[1][1]);
    }

    @Test
    public void testLine291() throws Exception {
        int i = 1;
        int j = 1;
        int[][] UNum = new int[3][3];
        int[][] S2 = new int[][] { { 1, 1, 1 }, // center point is 0
        { 1, 0, 1 }, { 1, 1, 1 } };
        int rb;
        int r;
        int b;
        int rt;
        int t;
        int lb;
        int lt;
        int l;
        if (S2[i][j] == 1) {
            l = S2[i][j - 1];
            r = S2[i][j + 1];
            b = S2[i - 1][j];
            t = S2[i + 1][j];
            lb = S2[i - 1][j - 1];
            rb = S2[i - 1][j + 1];
            lt = S2[i + 1][j - 1];
            rt = S2[i + 1][j + 1];
            // ---- Cross point with two boder lines, will be used twice.
            if (l == 1 && r == 1 && b == 1 && t == 1 && ((lb == 0 && rt == 0) || (rb == 0 && lt == 0))) {
                UNum[i][j] = 2;
            } else {
                UNum[i][j] = 1;
            }
        } else {
            UNum[i][j] = 0;
        }
        assertEquals(0, UNum[1][1]);
    }

    @Test
    public void testLine296() throws Exception {
        int i = 1;
        int j = 1;
        int[][] UNum = new int[3][3];
        int[][] S2 = new int[][] { { 1, 1, 1 }, // center point is 2, not 1
        { 1, 2, 1 }, { 1, 1, 1 } };
        int rb;
        int r;
        int b;
        int rt;
        int t;
        int lb;
        int lt;
        int l;
        if (S2[i][j] == 1) {
            l = S2[i][j - 1];
            r = S2[i][j + 1];
            b = S2[i - 1][j];
            t = S2[i + 1][j];
            lb = S2[i - 1][j - 1];
            rb = S2[i - 1][j + 1];
            lt = S2[i + 1][j - 1];
            rt = S2[i + 1][j + 1];
            // ---- Cross point with two boder lines, will be used twice.
            if (l == 1 && r == 1 && b == 1 && t == 1 && ((lb == 0 && rt == 0) || (rb == 0 && lt == 0))) {
                UNum[i][j] = 2;
            } else {
                UNum[i][j] = 1;
            }
        } else {
            UNum[i][j] = 0;
        }
        assertEquals(0, UNum[1][1]);
    }

    @Test
    public void testLine1783() throws Exception {
        int i2 = 2;
        int j2 = 2;
        int[][] S1 = new int[][] { { 2, 3, 4, 5 }, { 6, 0, 8, 9 }, { 10, 11, 12, 0 }, { 14, 15, 16, 17 } };
        int[] ij3 = new int[2];
        int a;
        int b;
        int c;
        int d;
        a = S1[i2 - 1][j2 - 1];
        b = S1[i2 - 1][j2 + 1];
        c = S1[i2][j2 - 1];
        d = S1[i2][j2 + 1];
        if (a == 0 || b == 0 || c == 0 || d == 0) {
            if ((a == 0 && d == 0) || (b == 0 && c == 0)) {
                ij3[0] = i2;
                ij3[1] = j2 - 1;
            } else {
                ij3[0] = i2 - 1;
                ij3[1] = j2;
            }
        } else {
            ij3[0] = i2;
            ij3[1] = j2 - 1;
        }
        assertEquals(1, ij3[1]);
        assertEquals(2, ij3[0]);
    }

    @Test
    public void testLine1786() throws Exception {
        int i2 = 2;
        int j2 = 2;
        int[][] S1 = new int[][] { { 2, 3, 4, 0 }, { 6, 7, 8, 9 }, { 0, 11, 12, 13 }, { 14, 15, 16, 17 } };
        int[] ij3 = new int[2];
        int a;
        int b;
        int c;
        int d;
        a = S1[i2 - 1][j2 - 1];
        b = S1[i2 - 1][j2 + 1];
        c = S1[i2][j2 - 1];
        d = S1[i2][j2 + 1];
        if (a == 0 || b == 0 || c == 0 || d == 0) {
            if ((a == 0 && d == 0) || (b == 0 && c == 0)) {
                ij3[0] = i2;
                ij3[1] = j2 - 1;
            } else {
                ij3[0] = i2 - 1;
                ij3[1] = j2;
            }
        } else {
            ij3[0] = i2;
            ij3[1] = j2 - 1;
        }
        assertEquals(1, ij3[1]);
        assertEquals(2, ij3[0]);
    }

    @Test
    public void testLine1789() throws Exception {
        int i2 = 2;
        int j2 = 2;
        int[][] S1 = new int[][] { { 0, 3, 4, 5 }, { 6, 7, 8, 9 }, { 10, 11, 12, 13 }, { 14, 15, 16, 17 } };
        int[] ij3 = new int[2];
        int a;
        int b;
        int c;
        int d;
        a = S1[i2 - 1][j2 - 1];
        b = S1[i2 - 1][j2 + 1];
        c = S1[i2][j2 - 1];
        d = S1[i2][j2 + 1];
        if (a == 0 || b == 0 || c == 0 || d == 0) {
            if ((a == 0 && d == 0) || (b == 0 && c == 0)) {
                ij3[0] = i2;
                ij3[1] = j2 - 1;
            } else {
                ij3[0] = i2 - 1;
                ij3[1] = j2;
            }
        } else {
            ij3[0] = i2;
            ij3[1] = j2 - 1;
        }
        assertEquals(1, ij3[1]);
        assertEquals(2, ij3[0]);
    }

    @Test
    public void testLine1792() throws Exception {
        int i2 = 2;
        int j2 = 2;
        int[][] S1 = new int[][] { { 2, 3, 4, 5 }, { 6, 7, 8, 0 }, { 10, 11, 12, 13 }, { 14, 15, 16, 17 } };
        int[] ij3 = new int[2];
        int a;
        int b;
        int c;
        int d;
        a = S1[i2 - 1][j2 - 1];
        b = S1[i2 - 1][j2 + 1];
        c = S1[i2][j2 - 1];
        d = S1[i2][j2 + 1];
        if (a == 0 || b == 0 || c == 0 || d == 0) {
            if ((a == 0 && d == 0) || (b == 0 && c == 0)) {
                ij3[0] = i2;
                ij3[1] = j2 - 1;
            } else {
                ij3[0] = i2 - 1;
                ij3[1] = j2;
            }
        } else {
            ij3[0] = i2;
            ij3[1] = j2 - 1;
        }
        assertEquals(2, ij3[1]);
        assertEquals(1, ij3[0]);
    }

    @Test
    public void testLine1764() throws Exception {
        int i2 = 1;
        int j2 = 1;
        int[][] S1 = new int[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 5, 0 } };
        int[] ij3 = new int[2];
        int a;
        int b;
        int c;
        a = S1[i2 + 1][j2 - 1];
        b = S1[i2 - 1][j2];
        c = S1[i2 - 1][j2 + 1];
        if ((a != 0 && b == 0) || (a == 0 && b != 0 && c != 0)) {
            ij3[0] = i2;
            ij3[1] = j2 - 1;
        } else {
            ij3[0] = i2;
        }
        assertEquals(2, ij3[1]);
        assertEquals(1, ij3[0]);
    }

    @Test
    public void testLine1766() throws Exception {
        int i2 = 1;
        int j2 = 1;
        int[][] S1 = new int[][] { { 0, 3, 7 }, { 4, 0, 0 }, { 0, 0, 0 } };
        int[] ij3 = new int[2];
        int a;
        int b;
        int c;
        a = S1[i2 + 1][j2 - 1];
        b = S1[i2 - 1][j2];
        c = S1[i2 - 1][j2 + 1];
        if ((a != 0 && b == 0) || (a == 0 && b != 0 && c != 0)) {
            ij3[0] = i2;
            ij3[1] = j2 - 1;
        } else {
            ij3[0] = i2;
        }
        assertEquals(0, ij3[1]);
        assertEquals(1, ij3[0]);
    }

    @Test
    public void testLine1768() throws Exception {
        int i2 = 1;
        int j2 = 1;
        int[][] S1 = new int[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 3, 0, 0 } };
        int[] ij3 = new int[2];
        int a;
        int b;
        int c;
        a = S1[i2 + 1][j2 - 1];
        b = S1[i2 - 1][j2];
        c = S1[i2 - 1][j2 + 1];
        if ((a != 0 && b == 0) || (a == 0 && b != 0 && c != 0)) {
            ij3[0] = i2;
            ij3[1] = j2 - 1;
        } else {
            ij3[0] = i2;
        }
        assertEquals(0, ij3[1]);
        assertEquals(1, ij3[0]);
    }

    @Test
    public void atest1() throws Exception {
        int[] timesArray = new int[] { 1, 2, 3 };
        int pIdx = 1;
        int bP__Id = 0;
        int vNum = 0;
        List<PolyLine> aLineList = new ArrayList<>();
        double bValue = 0;
        PolyLine aLine;
        double aValue = 0;
        PolyLine l = new PolyLine();
        l.Value = 5;
        aLineList.add(l);
        timesArray[pIdx] += +1;
        aLine = aLineList.get(bP__Id);
        if (vNum == 0) {
            aValue = aLine.Value;
            bValue = aLine.Value;
            vNum += 1;
        } else if (aValue == bValue) {
            if (aLine.Value > aValue) {
                bValue = aLine.Value;
            } else if (aLine.Value < aValue) {
                aValue = aLine.Value;
            }
            vNum += 1;
        }
        assertEquals(5, bValue, 0.01);
        assertEquals(5, aValue, 0.01);
        assertEquals(1, vNum);
    }

    @Test
    public void atest2() throws Exception {
        int[] timesArray = new int[] { 1, 2, 3 };
        int pIdx = 1;
        int bP__Id = 1;
        int vNum = 1;
        List<PolyLine> aLineList = new ArrayList<>();
        double bValue = 0;
        PolyLine aLine;
        double aValue = 0;
        PolyLine l = new PolyLine();
        l.Value = 5;
        aLineList.add(l);
        PolyLine l2 = new PolyLine();
        l2.Value = 10;
        aLineList.add(l2);
        timesArray[pIdx] += +1;
        aLine = aLineList.get(bP__Id);
        if (vNum == 0) {
            aValue = aLine.Value;
            bValue = aLine.Value;
            vNum += 1;
        } else if (aValue == bValue) {
            if (aLine.Value > aValue) {
                bValue = aLine.Value;
            } else if (aLine.Value < aValue) {
                aValue = aLine.Value;
            }
            vNum += 1;
        }
        assertEquals(10, bValue, 0.01);
        assertEquals(0, aValue, 0.01);
        assertEquals(2, vNum);
    }

    @Test
    public void atest3() throws Exception {
        int[] timesArray = new int[] { 1, 2, 3 };
        int pIdx = 1;
        int bP__Id = 1;
        int vNum = 1;
        List<PolyLine> aLineList = new ArrayList<>();
        double bValue = 0;
        PolyLine aLine;
        double aValue = 0;
        PolyLine l = new PolyLine();
        l.Value = 5;
        aLineList.add(l);
        PolyLine l2 = new PolyLine();
        l2.Value = -10;
        aLineList.add(l2);
        timesArray[pIdx] += +1;
        aLine = aLineList.get(bP__Id);
        if (vNum == 0) {
            aValue = aLine.Value;
            bValue = aLine.Value;
            vNum += 1;
        } else if (aValue == bValue) {
            if (aLine.Value > aValue) {
                bValue = aLine.Value;
            } else if (aLine.Value < aValue) {
                aValue = aLine.Value;
            }
            vNum += 1;
        }
        assertEquals(0, bValue, 0.01);
        assertEquals(-10, aValue, 0.01);
        assertEquals(2, vNum);
    }

    @Test
    public void atest4() throws Exception {
        int[] timesArray = new int[] { 1, 2, 3 };
        int pIdx = 1;
        int bP__Id = 1;
        int vNum = 1;
        double aValue = 1;
        double bValue = 2;
        List<PolyLine> aLineList = new ArrayList<>();
        PolyLine aLine;
        PolyLine l = new PolyLine();
        l.Value = 5;
        aLineList.add(l);
        PolyLine l2 = new PolyLine();
        l2.Value = -10;
        aLineList.add(l2);
        timesArray[pIdx] += +1;
        aLine = aLineList.get(bP__Id);
        if (vNum == 0) {
            aValue = aLine.Value;
            bValue = aLine.Value;
            vNum += 1;
        } else if (aValue == bValue) {
            if (aLine.Value > aValue) {
                bValue = aLine.Value;
            } else if (aLine.Value < aValue) {
                aValue = aLine.Value;
            }
            vNum += 1;
        }
        assertEquals(2, bValue, 0.01);
        assertEquals(1, aValue, 0.01);
        assertEquals(1, vNum);
    }

    @Test
    public void testOne() throws Exception {
        int borderIdx2 = -1;
        int j = 1;
        int[] timesArray = new int[] { 1, 2, 3 };
        List<Integer> bIdxList = new ArrayList<>(Arrays.asList(2));
        boolean innerStart = true;
        BorderPoint bP = new BorderPoint();
        BorderPoint bP1 = new BorderPoint();
        boolean sameBorderIdx = false;
        int innerIdx = 1;
        int pIdx = 1;
        boolean isRepeat = false;
        bP.BInnerIdx = 4;
        bP.BorderIdx = 1;
        bP1.BInnerIdx = 3;
        bP1.BorderIdx = 1;
        pIdx = j;
        innerIdx = bP1.BInnerIdx;
        timesArray[pIdx] += 1;
        bIdxList.add(pIdx);
        borderIdx2 = bP1.BorderIdx;
        if (bP.BorderIdx > 0 && bP.BorderIdx == bP1.BorderIdx) {
            sameBorderIdx = true;
        }
        if (innerStart && bP1.BorderIdx == 0) {
            for (int bidx : bIdxList) {
                timesArray[bidx] -= 1;
            }
            isRepeat = true;
        }
        assertEquals(1, borderIdx2);
        assertEquals(3, innerIdx);
        assertEquals(1, pIdx);
        assertEquals(1, bIdxList.get(1), 0);
        assertEquals(2, bIdxList.get(0), 0);
        assertEquals(3, timesArray[2]);
        assertEquals(3, timesArray[1]);
        assertEquals(1, timesArray[0]);
        assertFalse(isRepeat);
        assertTrue(sameBorderIdx);
    }

    @Test
    public void testTwo() throws Exception {
        int borderIdx2 = -1;
        int j = 1;
        int[] timesArray = new int[] { 1, 2, 3 };
        List<Integer> bIdxList = new ArrayList<>(Arrays.asList(2));
        boolean innerStart = true;
        BorderPoint bP = new BorderPoint();
        BorderPoint bP1 = new BorderPoint();
        boolean sameBorderIdx = false;
        int innerIdx = 1;
        int pIdx = 1;
        boolean isRepeat = false;
        bP.BInnerIdx = 10;
        bP.BorderIdx = 1;
        bP1.BInnerIdx = 20;
        bP1.BorderIdx = 0;
        pIdx = j;
        innerIdx = bP1.BInnerIdx;
        timesArray[pIdx] += 1;
        bIdxList.add(pIdx);
        borderIdx2 = bP1.BorderIdx;
        if (bP.BorderIdx > 0 && bP.BorderIdx == bP1.BorderIdx) {
            sameBorderIdx = true;
        }
        if (innerStart && bP1.BorderIdx == 0) {
            for (int bidx : bIdxList) {
                timesArray[bidx] -= 1;
            }
            isRepeat = true;
        }
        assertEquals(0, borderIdx2);
        assertEquals(20, innerIdx);
        assertEquals(1, pIdx);
        assertEquals(1, bIdxList.get(1), 0);
        assertEquals(2, bIdxList.get(0), 0);
        assertEquals(2, timesArray[2]);
        assertEquals(2, timesArray[1]);
        assertEquals(1, timesArray[0]);
        assertTrue(isRepeat);
        assertFalse(sameBorderIdx);
    }
}
