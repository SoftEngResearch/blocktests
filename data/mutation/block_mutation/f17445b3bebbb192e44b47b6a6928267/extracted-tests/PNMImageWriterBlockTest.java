package com.github.jaiimageio.impl.plugins.pnm;

import java.awt.Rectangle;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.IIOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.ImageOutputStream;
import com.github.jaiimageio.impl.common.ImageUtil;
import com.github.jaiimageio.plugins.pnm.PNMImageWriteParam;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.github.jaiimageio.impl.plugins.pnm.PNMImageWriter.*;

public class PNMImageWriterBlockTest {

    @Test
    public void testLine449() throws Exception {
        int numBands = 5;
        int variant = PNMImageWriter.PPM_ASCII;
        ComponentSampleModel csm = null;
        boolean writeOptimal = false;
        writeOptimal = true;
        if (variant == PNMImageWriter.PPM_RAW) {
            int[] bandOffsets = csm.getBandOffsets();
            for (int b = 0; b < numBands; b++) {
                if (bandOffsets[b] != b) {
                    writeOptimal = false;
                    break;
                }
                continue;
            }
        }
        assertTrue(writeOptimal);
    }

    @Test
    public void testLine450() throws Exception {
        int numBands = 4;
        int variant = PNMImageWriter.PPM_RAW;
        ComponentSampleModel csm = new ComponentSampleModel(DataBuffer.TYPE_BYTE, 100, 200, 300, 400, new int[] { 0, 1, 2, 3 });
        boolean writeOptimal = false;
        writeOptimal = true;
        if (variant == PNMImageWriter.PPM_RAW) {
            int[] bandOffsets = csm.getBandOffsets();
            for (int b = 0; b < numBands; b++) {
                if (bandOffsets[b] != b) {
                    writeOptimal = false;
                    break;
                }
                continue;
            }
        }
        assertTrue(writeOptimal);
    }

    @Test
    public void testLine453() throws Exception {
        int numBands = 4;
        int variant = PNMImageWriter.PPM_RAW;
        ComponentSampleModel csm = new ComponentSampleModel(DataBuffer.TYPE_BYTE, 100, 200, 300, 400, new int[] { 1, 2, 3, 4 });
        boolean writeOptimal = false;
        writeOptimal = true;
        if (variant == PNMImageWriter.PPM_RAW) {
            int[] bandOffsets = csm.getBandOffsets();
            for (int b = 0; b < numBands; b++) {
                if (bandOffsets[b] != b) {
                    writeOptimal = false;
                    break;
                }
                continue;
            }
        }
        assertFalse(writeOptimal);
    }

    @Test
    public void testLine456() throws Exception {
        int numBands = 4;
        int variant = PNMImageWriter.PPM_RAW;
        ComponentSampleModel csm = new ComponentSampleModel(DataBuffer.TYPE_BYTE, 100, 200, 300, 400, new int[] { 0, 1, 2, 4 });
        boolean writeOptimal = false;
        writeOptimal = true;
        if (variant == PNMImageWriter.PPM_RAW) {
            int[] bandOffsets = csm.getBandOffsets();
            for (int b = 0; b < numBands; b++) {
                if (bandOffsets[b] != b) {
                    writeOptimal = false;
                    break;
                }
                continue;
            }
        }
        assertFalse(writeOptimal);
    }

    @Test
    public void testLine459() throws Exception {
        int numBands = 4;
        int variant = PNMImageWriter.PPM_RAW;
        ComponentSampleModel csm = new ComponentSampleModel(DataBuffer.TYPE_BYTE, 100, 200, 300, 400, new int[] { 0, 1, 2, 3, 5 });
        boolean writeOptimal = false;
        writeOptimal = true;
        if (variant == PNMImageWriter.PPM_RAW) {
            int[] bandOffsets = csm.getBandOffsets();
            for (int b = 0; b < numBands; b++) {
                if (bandOffsets[b] != b) {
                    writeOptimal = false;
                    break;
                }
                continue;
            }
        }
        assertTrue(writeOptimal);
    }
}
