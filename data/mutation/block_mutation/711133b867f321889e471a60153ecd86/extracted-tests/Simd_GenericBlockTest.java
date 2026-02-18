package neo.idlib.math;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;
import neo.Renderer.Model.dominantTri_s;
import neo.TempDump;
import neo.idlib.containers.List.idList;
import neo.idlib.geometry.DrawVert.idDrawVert;
import neo.idlib.geometry.JointTransform.idJointMat;
import neo.idlib.geometry.JointTransform.idJointQuat;
import static neo.idlib.math.Math_h.FLOATSIGNBITSET;
import neo.idlib.math.Math_h.idMath;
import neo.idlib.math.Matrix.idMatX;
import neo.idlib.math.Plane.idPlane;
import neo.idlib.math.Simd.idSIMDProcessor;
import neo.idlib.math.Vector.idVec2;
import neo.idlib.math.Vector.idVec3;
import neo.idlib.math.Vector.idVec4;
import neo.idlib.math.Vector.idVecX;
import org.junit.Test;
import static org.junit.Assert.*;
import static neo.idlib.math.Simd_Generic.*;

public class Simd_GenericBlockTest {

    @Test
    public void testLine1292() throws Exception {
        int k = 6;
        int dIndex = 0;
        int m1Index = 0;
        int m2Index = 0;
        float[] dstPtr = new float[] { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f };
        float[] m1Ptr = new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f };
        float[] m2Ptr = new float[] { 10.0f, 20.0f, 30.0f, 40.0f, 50.0f, 60.0f };
        int i;
        for (i = 0; i < k; i++) {
            // Nx1 * 1x6
            dstPtr[dIndex++] = m1Ptr[m1Index + i] * m2Ptr[m2Index + 0];
            dstPtr[dIndex++] = m1Ptr[m1Index + i] * m2Ptr[m2Index + 1];
            dstPtr[dIndex++] = m1Ptr[m1Index + i] * m2Ptr[m2Index + 2];
            dstPtr[dIndex++] = m1Ptr[m1Index + i] * m2Ptr[m2Index + 3];
            dstPtr[dIndex++] = m1Ptr[m1Index + i] * m2Ptr[m2Index + 4];
            dstPtr[dIndex++] = m1Ptr[m1Index + i] * m2Ptr[m2Index + 5];
        }
        assertEquals(360.0f, dstPtr[35], Float.MIN_VALUE);
        assertEquals(250.0f, dstPtr[28], Float.MIN_VALUE);
        assertEquals(160.0f, dstPtr[21], Float.MIN_VALUE);
        assertEquals(90.0f, dstPtr[14], Float.MIN_VALUE);
        assertEquals(40.0f, dstPtr[7], Float.MIN_VALUE);
        assertEquals(10.0f, dstPtr[0], Float.MIN_VALUE);
    }

    @Test
    public void testLine1332() throws Exception {
        int k = 6;
        int dIndex = 0;
        int m1Index = 0;
        int m2Index = 0;
        float[] dstPtr = new float[] { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f };
        float[] m1Ptr = new float[] { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f };
        float[] m2Ptr = new float[] { 10.0f, 20.0f, 30.0f, 40.0f, 50.0f, 60.0f, 70.0f, 80.0f, 90.0f, 100.0f, 110.0f, 120.0f };
        int i;
        try {
            for (i = 0; i < k; i++) {
                // Nx2 * 2x6
                dstPtr[dIndex++] = m1Ptr[m1Index + 0] * m2Ptr[m2Index + 0] + m1Ptr[m1Index + 1] * m2Ptr[m2Index + 6];
                dstPtr[dIndex++] = m1Ptr[m1Index + 0] * m2Ptr[m2Index + 1] + m1Ptr[m1Index + 1] * m2Ptr[m2Index + 7];
                dstPtr[dIndex++] = m1Ptr[m1Index + 0] * m2Ptr[m2Index + 2] + m1Ptr[m1Index + 1] * m2Ptr[m2Index + 8];
                dstPtr[dIndex++] = m1Ptr[m1Index + 0] * m2Ptr[m2Index + 3] + m1Ptr[m1Index + 1] * m2Ptr[m2Index + 9];
                dstPtr[dIndex++] = m1Ptr[m1Index + 0] * m2Ptr[m2Index + 4] + m1Ptr[m1Index + 1] * m2Ptr[m2Index + 10];
                dstPtr[dIndex++] = m1Ptr[m1Index + 0] * m2Ptr[m2Index + 5] + m1Ptr[m1Index + 1] * m2Ptr[m2Index + 11];
                m1Index += 2;
            }
            return;
        } finally {
            assertEquals(2100.0f, dstPtr[35], Float.MIN_VALUE);
            assertEquals(1550.0f, dstPtr[28], Float.MIN_VALUE);
            assertEquals(1080.0f, dstPtr[21], Float.MIN_VALUE);
            assertEquals(690.0f, dstPtr[14], Float.MIN_VALUE);
            assertEquals(380.0f, dstPtr[7], Float.MIN_VALUE);
            assertEquals(150.0f, dstPtr[0], Float.MIN_VALUE);
        }
    }
}
