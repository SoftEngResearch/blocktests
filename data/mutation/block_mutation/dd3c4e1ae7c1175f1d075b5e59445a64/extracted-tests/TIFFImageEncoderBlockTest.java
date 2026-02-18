package org.apache.xmlgraphics.image.codec.tiff;

import java.awt.Rectangle;
import java.awt.image.ColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.zip.Deflater;
import org.apache.xmlgraphics.image.codec.util.ImageEncodeParam;
import org.apache.xmlgraphics.image.codec.util.ImageEncoderImpl;
import org.apache.xmlgraphics.image.codec.util.PropertyUtil;
import org.apache.xmlgraphics.image.codec.util.SeekableOutputStream;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.apache.xmlgraphics.image.codec.tiff.TIFFImageEncoder.*;

public class TIFFImageEncoderBlockTest {

    @Test
    public void testLine454() throws Exception {
        long totalBytesOfData = 2048;
        boolean skipByte = false;
        long[] tileOffsets = new long[] { 1000L };
        int nextIFDOffset = 0;
        nextIFDOffset = (int) (tileOffsets[0] + totalBytesOfData);
        if ((nextIFDOffset & 0x01) != 0) {
            nextIFDOffset++;
        }
        assertFalse(skipByte);
        assertEquals(3048, nextIFDOffset);
    }

    @Test
    public void testLine455() throws Exception {
        long totalBytesOfData = 2048;
        boolean skipByte = false;
        long[] tileOffsets = new long[] { 1001L };
        int nextIFDOffset = 0;
        nextIFDOffset = (int) (tileOffsets[0] + totalBytesOfData);
        if ((nextIFDOffset & 0x01) != 0) {
            nextIFDOffset++;
        }
        assertTrue(skipByte);
        assertEquals(3050, nextIFDOffset);
    }

    @Test
    public void testLine456() throws Exception {
        long totalBytesOfData = 1024;
        boolean skipByte = false;
        long[] tileOffsets = new long[] { 0L };
        int nextIFDOffset = 0;
        nextIFDOffset = (int) (tileOffsets[0] + totalBytesOfData);
        if ((nextIFDOffset & 0x01) != 0) {
            nextIFDOffset++;
        }
        assertFalse(skipByte);
        assertEquals(1024, nextIFDOffset);
    }

    @Test
    public void testLine457() throws Exception {
        long totalBytesOfData = 1023;
        boolean skipByte = false;
        long[] tileOffsets = new long[] { 0L };
        int nextIFDOffset = 0;
        nextIFDOffset = (int) (tileOffsets[0] + totalBytesOfData);
        if ((nextIFDOffset & 0x01) != 0) {
            nextIFDOffset++;
        }
        assertTrue(skipByte);
        assertEquals(1024, nextIFDOffset);
    }

    @Test
    public void testLine458() throws Exception {
        long totalBytesOfData = 25000;
        boolean skipByte = false;
        long[] tileOffsets = new long[] { 50000L };
        int nextIFDOffset = 0;
        nextIFDOffset = (int) (tileOffsets[0] + totalBytesOfData);
        if ((nextIFDOffset & 0x01) != 0) {
            nextIFDOffset++;
        }
        assertFalse(skipByte);
        assertEquals(75000, nextIFDOffset);
    }

    @Test
    public void testLine459() throws Exception {
        long totalBytesOfData = 25000;
        boolean skipByte = false;
        long[] tileOffsets = new long[] { 50001L };
        int nextIFDOffset = 0;
        nextIFDOffset = (int) (tileOffsets[0] + totalBytesOfData);
        if ((nextIFDOffset & 0x01) != 0) {
            nextIFDOffset++;
        }
        assertTrue(skipByte);
        assertEquals(75002, nextIFDOffset);
    }

    @Test
    public void testLine460() throws Exception {
        long totalBytesOfData = 1;
        boolean skipByte = false;
        long[] tileOffsets = new long[] { 0L };
        int nextIFDOffset = 0;
        nextIFDOffset = (int) (tileOffsets[0] + totalBytesOfData);
        if ((nextIFDOffset & 0x01) != 0) {
            nextIFDOffset++;
        }
        assertTrue(skipByte);
        assertEquals(2, nextIFDOffset);
    }

    @Test
    public void testLine461() throws Exception {
        long totalBytesOfData = 1001;
        boolean skipByte = false;
        long[] tileOffsets = new long[] { 1001L };
        int nextIFDOffset = 0;
        nextIFDOffset = (int) (tileOffsets[0] + totalBytesOfData);
        if ((nextIFDOffset & 0x01) != 0) {
            nextIFDOffset++;
        }
        assertFalse(skipByte);
        assertEquals(2002, nextIFDOffset);
    }
}
