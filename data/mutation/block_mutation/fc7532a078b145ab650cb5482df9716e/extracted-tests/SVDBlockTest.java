package cc.mallet.util;

import org.blocktest.BTest;
import org.blocktest.types.EndAt;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.io.*;
import java.util.Formatter;
import org.junit.Test;
import static org.junit.Assert.*;
import static cc.mallet.util.SVD.*;

public class SVDBlockTest {

    @Test
    public void testLine87() throws Exception {
        int col = 0;
        int numRows = 2;
        double sum = 0.0;
        double[][] originalMatrix = new double[][] { { 0.0, 1.0 }, { 2.0, 3.0 } };
        int otherCol = 1;
        double scale = sum / originalMatrix[col][col];
        sum = 0.0;
        for (int row = col; row < numRows; row++) {
            sum += originalMatrix[row][otherCol] * originalMatrix[row][col];
        }
        scale = sum / originalMatrix[col][col];
        for (int row = col; row < numRows; row++) {
            originalMatrix[row][otherCol] -= scale * originalMatrix[row][col];
        }
        assertEquals(6.0, sum, Double.MIN_VALUE);
    }

    @Test
    public void testLine181() throws Exception {
        int col = 0;
        int numRows = 2;
        double sum = 0.0;
        double[][] U = new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } };
        int otherCol = 1;
        double scale = -sum / U[col][col];
        sum = 0.0;
        for (int row = col; row < numRows; row++) {
            sum += U[row][col] * U[row][otherCol];
        }
        scale = -sum / U[col][col];
        for (int row = col; row < numRows; row++) {
        }
        assertEquals(14.0, sum, Double.MIN_VALUE);
    }

    @Test
    public void testLine184() throws Exception {
        int col = 0;
        int numRows = 2;
        double scale = -7.0;
        double[][] U = new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } };
        int otherCol = 1;
        double sum = 0.0;
        sum = 0.0;
        for (int row = col; row < numRows; row++) {
            sum += U[row][col] * U[row][otherCol];
        }
        scale = -sum / U[col][col];
        for (int row = col; row < numRows; row++) {
        }
        assertEquals(-14.0, scale, Double.MIN_VALUE);
    }

    @Test
    public void testLine437() throws Exception {
        int col = 0;
        int numRows = 2;
        double[][] U = new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } };
        int row = 1;
        double cosine = 1.0;
        double sine = 0.0;
        double temp = cosine * U[row][col] + sine * U[row][col + 1];
        U[row][col + 1] = -sine * U[row][col] + cosine * U[row][col + 1];
        U[row][col] = temp;
        assertEquals(3.0, temp, Double.MIN_VALUE);
    }
}
