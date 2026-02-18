package org.apache.xmlgraphics.image.codec.tiff;

import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferShort;
import java.awt.image.DataBufferUShort;
import java.awt.image.IndexColorModel;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import org.apache.xmlgraphics.image.codec.util.PropertyUtil;
import org.apache.xmlgraphics.image.codec.util.SeekableStream;
import org.apache.xmlgraphics.image.rendered.AbstractRed;
import org.apache.xmlgraphics.image.rendered.CachableRed;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.apache.xmlgraphics.image.codec.tiff.TIFFImage.*;

public class TIFFImageBlockTest {

    @Test
    public void testLine229() throws Exception {
        TIFFDirectory dir = null;
        int sampleSize;
        TIFFField bitsField = new TIFFField(1, 2, 3, new char[] { 100, 101, 102 });
        char[] bitsPerSample = null;
        if (bitsField != null) {
            bitsPerSample = bitsField.getAsChars();
        } else {
            bitsPerSample = new char[] { 1 };
            // Ensure that all samples have the same bit depth.
            for (int i = 1; i < bitsPerSample.length; i++) {
                if (bitsPerSample[i] != bitsPerSample[0]) {
                    throw new RuntimeException(PropertyUtil.getString("TIFFImage1"));
                }
            }
        }
        sampleSize = bitsPerSample[0];
        assertEquals(100, sampleSize);
    }

    @Test
    public void testLine231() throws Exception {
        TIFFDirectory dir = null;
        int sampleSize;
        TIFFField bitsField = null;
        char[] bitsPerSample = null;
        if (bitsField != null) {
            bitsPerSample = bitsField.getAsChars();
        } else {
            bitsPerSample = new char[] { 1 };
            // Ensure that all samples have the same bit depth.
            for (int i = 1; i < bitsPerSample.length; i++) {
                if (bitsPerSample[i] != bitsPerSample[0]) {
                    throw new RuntimeException(PropertyUtil.getString("TIFFImage1"));
                }
            }
        }
        sampleSize = bitsPerSample[0];
        assertEquals(1, sampleSize);
    }

    @Test
    public void testLine973() throws Exception {
        int newRect__height = 2;
        int newRect__width = 4;
        byte[] data = new byte[100];
        byte[] tempData = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
        int padding = 1;
        int dstCount = 0;
        int srcCount = 0;
        for (int j = 0; j < newRect__height; j++) {
            for (int i = 0; i < newRect__width / 2; i++) {
                data[dstCount++] = (byte) ((tempData[srcCount] & 0xf0) >> 4);
                data[dstCount++] = (byte) (tempData[srcCount++] & 0x0f);
            }
            if (padding == 1) {
                data[dstCount++] = (byte) ((tempData[srcCount++] & 0xf0) >> 4);
            }
        }
        assertTrue(data[0] == 0 && data[1] == 1 && data[2] == 0 && data[3] == 2 && data[4] == 0 && data[5] == 0 && data[6] == 4 && data[7] == 0 && data[8] == 5);
    }

    @Test
    public void testLine810() throws Exception {
        short[] sdata = new short[500];
        char[] colormap = new char[100000];
        int unitsBeforeLookup = 5;
        int len2 = 10;
        short[] tempData = new short[] { 1, 2, 3, 4, 5 };
        int len = 3;
        int lookup;
        int cmapValue;
        int count = 0;
        for (int i = 0; i < 100000; i++) {
            colormap[i] = (char) (i % 50);
        }
        for (int i = 0; i < unitsBeforeLookup; i++) {
            // Get the index into the colormap
            lookup = tempData[i] & 0xffff;
            // Get the blue value
            cmapValue = colormap[lookup + len2];
            sdata[count++] = (short) cmapValue;
            // Get the green value
            cmapValue = colormap[lookup + len];
            sdata[count++] = (short) cmapValue;
            // Get the red value
            cmapValue = colormap[lookup];
            sdata[count++] = (short) cmapValue;
        }
        assertTrue(sdata[0] == 11 && sdata[1] == 4 && sdata[2] == 1 && sdata[3] == 12);
    }

    @Test
    public void testLine783() throws Exception {
        short[] sdata = new short[500];
        char[] colormap = new char[100000];
        int unitsBeforeLookup = 5;
        int len2 = 10;
        short[] tempData = new short[] { 1, 2, 3, 4, 5 };
        int len = 3;
        int lookup;
        int cmapValue;
        int count = 0;
        for (int i = 0; i < 100000; i++) {
            colormap[i] = (char) (i % 50);
        }
        for (int i = 0; i < unitsBeforeLookup; i++) {
            // Get the index into the colormap
            lookup = tempData[i] & 0xffff;
            // Get the blue value
            cmapValue = colormap[lookup + len2];
            sdata[count++] = (short) (cmapValue & 0xffff);
            // Get the green value
            cmapValue = colormap[lookup + len];
            sdata[count++] = (short) (cmapValue & 0xffff);
            // Get the red value
            cmapValue = colormap[lookup];
        }
        assertTrue(sdata[0] == 11 && sdata[1] == 4 && sdata[2] == 1 && sdata[3] == 12);
    }
}
