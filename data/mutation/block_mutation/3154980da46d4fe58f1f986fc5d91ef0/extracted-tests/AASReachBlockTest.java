package neo.Tools.Compilers.AAS;

import neo.idlib.math.Vector;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import static java.lang.Math.abs;
import static neo.TempDump.SNOT;
import static neo.TempDump.btoi;
import static neo.TempDump.itob;
import static neo.Tools.Compilers.AAS.AASFile.AREACONTENTS_CLUSTERPORTAL;
import static neo.Tools.Compilers.AAS.AASFile.AREACONTENTS_WATER;
import static neo.Tools.Compilers.AAS.AASFile.AREA_CROUCH;
import static neo.Tools.Compilers.AAS.AASFile.AREA_FLOOR;
import static neo.Tools.Compilers.AAS.AASFile.AREA_LADDER;
import static neo.Tools.Compilers.AAS.AASFile.AREA_LIQUID;
import static neo.Tools.Compilers.AAS.AASFile.AREA_REACHABLE_FLY;
import static neo.Tools.Compilers.AAS.AASFile.AREA_REACHABLE_WALK;
import static neo.Tools.Compilers.AAS.AASFile.FACE_FLOOR;
import static neo.Tools.Compilers.AAS.AASFile.TFL_BARRIERJUMP;
import static neo.Tools.Compilers.AAS.AASFile.TFL_FLY;
import static neo.Tools.Compilers.AAS.AASFile.TFL_SWIM;
import static neo.Tools.Compilers.AAS.AASFile.TFL_WALK;
import static neo.Tools.Compilers.AAS.AASFile.TFL_WALKOFFLEDGE;
import static neo.Tools.Compilers.AAS.AASFile.TFL_WATERJUMP;
import neo.Tools.Compilers.AAS.AASFile.aasArea_s;
import neo.Tools.Compilers.AAS.AASFile.aasEdge_s;
import neo.Tools.Compilers.AAS.AASFile.aasFace_s;
import neo.Tools.Compilers.AAS.AASFile.aasTrace_s;
import neo.Tools.Compilers.AAS.AASFile.idReachability;
import neo.Tools.Compilers.AAS.AASFile.idReachability_BarrierJump;
import neo.Tools.Compilers.AAS.AASFile.idReachability_Fly;
import neo.Tools.Compilers.AAS.AASFile.idReachability_Swim;
import neo.Tools.Compilers.AAS.AASFile.idReachability_Walk;
import neo.Tools.Compilers.AAS.AASFile.idReachability_WalkOffLedge;
import neo.Tools.Compilers.AAS.AASFile.idReachability_WaterJump;
import neo.Tools.Compilers.AAS.AASFile_local.idAASFileLocal;
import static neo.framework.Common.common;
import neo.idlib.MapFile.idMapFile;
import static neo.idlib.math.Math_h.INTSIGNBITNOTSET;
import static neo.idlib.math.Math_h.INTSIGNBITSET;
import neo.idlib.math.Math_h.idMath;
import neo.idlib.math.Plane.idPlane;
import neo.idlib.math.Vector.idVec3;
import org.junit.Test;
import static org.junit.Assert.*;
import static neo.Tools.Compilers.AAS.AASReach.*;

public class AASReachBlockTest {

    @Test
    public void testLine554() throws Exception {
        float y1 = 1;
        float x3 = 3;
        float x1 = 1;
        float y2 = 2;
        float x2 = 2;
        idVec3 v3 = new idVec3();
        float y3 = 3;
        idVec3 p1area1 = new idVec3();
        idVec3 p1area2 = new idVec3();
        float dist1 = 0;
        float y = y1 + (x3 - x1) * (y2 - y1) / (x2 - x1);
        y = y1 + (x3 - x1) * (y2 - y1) / (x2 - x1);
        dist1 = y3 - y;
        p1area1 = v3;
        p1area1.oSet(2, y);
        p1area2 = v3;
        assertEquals(0.0, dist1, Double.MIN_VALUE);
    }

    @Test
    public void testLine601() throws Exception {
        float dist1 = 3;
        float dist2 = 3;
        idVec3 p1area1 = new idVec3();
        idVec3 p1area2 = new idVec3();
        idVec3 p2area1 = new idVec3();
        idVec3 p2area2 = new idVec3();
        float dist = 0;
        idVec3 start;
        idVec3 end;
        try {
            if (dist1 > dist2 - 1.0f && dist1 < dist2 + 1.0f) {
                dist = dist1;
                start = (p1area1.oPlus(p2area1)).oMultiply(0.5f);
                end = (p1area2.oPlus(p2area2)).oMultiply(0.5f);
            } else if (dist1 < dist2) {
                dist = dist1;
                start = p1area1;
                end = p1area2;
            } else {
                dist = dist2;
                start = p2area1;
                end = p2area2;
                return;
            }
        } finally {
            assertEquals(new idVec3(), end);
            assertEquals(new idVec3(), start);
            assertEquals(3.0f, dist, Float.MIN_VALUE);
        }
    }

    @Test
    public void testLine606() throws Exception {
        float dist1 = 1;
        float dist2 = 2;
        idVec3 p1area1 = new idVec3();
        idVec3 p2area1 = new idVec3();
        idVec3 p1area2 = new idVec3();
        idVec3 p2area2 = new idVec3();
        float dist = 0;
        idVec3 start = new idVec3();
        idVec3 end = new idVec3();
        try {
            if (dist1 > dist2 - 1.0f && dist1 < dist2 + 1.0f) {
                dist = dist1;
                start = (p1area1.oPlus(p2area1)).oMultiply(0.5f);
                end = (p1area2.oPlus(p2area2)).oMultiply(0.5f);
            } else if (dist1 < dist2) {
                dist = dist1;
                start = p1area1;
                end = p1area2;
            } else {
                dist = dist2;
                start = p2area1;
                end = p2area2;
                return;
            }
        } finally {
            assertEquals(new idVec3(), end);
            assertEquals(new idVec3(), start);
            assertEquals(1.0f, dist, Float.MIN_VALUE);
        }
    }

    @Test
    public void testLine612() throws Exception {
        float dist1 = 5;
        float dist2 = 4;
        idVec3 p1area1 = new idVec3();
        idVec3 p2area1 = new idVec3();
        idVec3 p1area2 = new idVec3();
        idVec3 p2area2 = new idVec3();
        float dist = 0;
        idVec3 start = new idVec3();
        idVec3 end = new idVec3();
        try {
            if (dist1 > dist2 - 1.0f && dist1 < dist2 + 1.0f) {
                dist = dist1;
                start = (p1area1.oPlus(p2area1)).oMultiply(0.5f);
                end = (p1area2.oPlus(p2area2)).oMultiply(0.5f);
            } else if (dist1 < dist2) {
                dist = dist1;
                start = p1area1;
                end = p1area2;
            } else {
                dist = dist2;
                start = p2area1;
                end = p2area2;
                return;
            }
        } finally {
            assertEquals(new idVec3(), end);
            assertEquals(new idVec3(), start);
            assertEquals(4.0f, dist, Float.MIN_VALUE);
        }
    }
}
