package neo.Renderer;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import static neo.framework.File_h.idFile_Memory;
import static java.lang.Math.log;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import neo.Renderer.Model_lwo.lwClip;
import neo.Renderer.Model_lwo.lwEnvelope;
import neo.Renderer.Model_lwo.lwGradKey;
import neo.Renderer.Model_lwo.lwKey;
import neo.Renderer.Model_lwo.lwLayer;
import neo.Renderer.Model_lwo.lwNode;
import neo.Renderer.Model_lwo.lwObject;
import neo.Renderer.Model_lwo.lwPlugin;
import neo.Renderer.Model_lwo.lwPoint;
import neo.Renderer.Model_lwo.lwPointList;
import neo.Renderer.Model_lwo.lwPolVert;
import neo.Renderer.Model_lwo.lwPolygon;
import neo.Renderer.Model_lwo.lwPolygonList;
import neo.Renderer.Model_lwo.lwSurface;
import neo.Renderer.Model_lwo.lwTagList;
import neo.Renderer.Model_lwo.lwTexture;
import neo.Renderer.Model_lwo.lwVMap;
import neo.Renderer.Model_lwo.lwVMapPt;
import static neo.TempDump.NOT;
import neo.TempDump.NiLLABLE;
import neo.TempDump.TODO_Exception;
import static neo.TempDump.bbtocb;
import static neo.TempDump.btoi;
import static neo.TempDump.replaceByIndex;
import static neo.TempDump.strLen;
import static neo.framework.FileSystem_h.fileSystem;
import static neo.framework.File_h.fsOrigin_t.FS_SEEK_CUR;
import static neo.framework.File_h.fsOrigin_t.FS_SEEK_SET;
import neo.framework.File_h.idFile;
import static neo.idlib.Lib.BigRevBytes;
import neo.idlib.containers.List.cmp_t;
import static neo.idlib.math.Math_h.FLOAT_IS_DENORMAL;
import neo.idlib.math.Math_h.idMath;
import org.junit.Test;
import static org.junit.Assert.*;
import static neo.Renderer.Model_lwo.*;

public class Model_lwoBlockTest {

    @Test
    public void testLine3006() throws Exception {
        int flags = 1023;
        lwSurface surf = new lwSurface();
        if ((flags & 4) == 4) {
            surf.smooth = 1.56207f;
        }
        if ((flags & 8) == 8) {
            surf.color_hilite.val = 1.0f;
        }
        if ((flags & 16) == 16) {
            surf.color_filter.val = 1.0f;
        }
        if ((flags & 128) == 128) {
            surf.dif_sharp.val = 0.5f;
        }
        if ((flags & 256) == 256) {
            surf.sideflags = 3;
        }
        if ((flags & 512) == 512) {
        }
        assertEquals(1.0f, surf.add_trans.val, Double.MIN_VALUE);
        assertEquals(3, surf.sideflags);
        assertEquals(0.5f, surf.dif_sharp.val, Double.MIN_VALUE);
        assertEquals(1.0f, surf.color_hilite.val, Double.MIN_VALUE);
        assertEquals(1.56207f, surf.smooth, Double.MIN_VALUE);
    }

    @Test
    public void testLine3013() throws Exception {
        int flags = 0;
        lwSurface surf = new lwSurface();
        if ((flags & 4) == 4) {
            surf.smooth = 1.56207f;
        }
        if ((flags & 8) == 8) {
            surf.color_hilite.val = 1.0f;
        }
        if ((flags & 16) == 16) {
            surf.color_filter.val = 1.0f;
        }
        if ((flags & 128) == 128) {
            surf.dif_sharp.val = 0.5f;
        }
        if ((flags & 256) == 256) {
            surf.sideflags = 3;
        }
        if ((flags & 512) == 512) {
        }
        assertFalse(surf.add_trans.val == 1.0f);
        assertFalse(surf.sideflags == 3);
        assertFalse(surf.dif_sharp.val == 0.5f);
        assertFalse(surf.color_hilite.val == 1.0f);
        assertFalse(surf.smooth == 1.56207f);
    }
}
