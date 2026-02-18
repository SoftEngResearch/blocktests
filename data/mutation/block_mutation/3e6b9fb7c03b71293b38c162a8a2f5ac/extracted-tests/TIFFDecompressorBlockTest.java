package com.github.jaiimageio.plugins.tiff;

import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferFloat;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferShort;
import java.awt.image.DataBufferUShort;
import java.awt.image.IndexColorModel;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import javax.imageio.IIOException;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import com.github.jaiimageio.impl.common.BogusColorSpace;
import com.github.jaiimageio.impl.common.ImageUtil;
import com.github.jaiimageio.impl.common.SimpleCMYKColorSpace;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.github.jaiimageio.plugins.tiff.TIFFDecompressor.*;

public class TIFFDecompressorBlockTest {

    @Test
    public void testLine1149() throws Exception {
        int[] sampleFormat = new int[] { BaselineTIFFTagSet.SAMPLE_FORMAT_SIGNED_INTEGER };
        boolean isDataTypeSet = false;
        int dataType = -1;
        if (sampleFormat[0] != BaselineTIFFTagSet.SAMPLE_FORMAT_FLOATING_POINT) {
            if (sampleFormat[0] == BaselineTIFFTagSet.SAMPLE_FORMAT_SIGNED_INTEGER) {
                dataType = DataBuffer.TYPE_SHORT;
            } else {
                dataType = DataBuffer.TYPE_USHORT;
            }
            isDataTypeSet = true;
        }
        assertTrue(isDataTypeSet);
        assertEquals(DataBuffer.TYPE_SHORT, dataType);
    }

    @Test
    public void testLine1150() throws Exception {
        int[] sampleFormat = new int[] { BaselineTIFFTagSet.SAMPLE_FORMAT_UNDEFINED };
        boolean isDataTypeSet = false;
        int dataType = -1;
        if (sampleFormat[0] != BaselineTIFFTagSet.SAMPLE_FORMAT_FLOATING_POINT) {
            if (sampleFormat[0] == BaselineTIFFTagSet.SAMPLE_FORMAT_SIGNED_INTEGER) {
                dataType = DataBuffer.TYPE_SHORT;
            } else {
                dataType = DataBuffer.TYPE_USHORT;
            }
            isDataTypeSet = true;
        }
        assertTrue(isDataTypeSet);
        assertEquals(DataBuffer.TYPE_USHORT, dataType);
    }

    @Test
    public void testLine1151() throws Exception {
        int[] sampleFormat = new int[] { BaselineTIFFTagSet.SAMPLE_FORMAT_FLOATING_POINT };
        boolean isDataTypeSet = false;
        int dataType = -1;
        if (sampleFormat[0] != BaselineTIFFTagSet.SAMPLE_FORMAT_FLOATING_POINT) {
            if (sampleFormat[0] == BaselineTIFFTagSet.SAMPLE_FORMAT_SIGNED_INTEGER) {
                dataType = DataBuffer.TYPE_SHORT;
            } else {
                dataType = DataBuffer.TYPE_USHORT;
            }
            isDataTypeSet = true;
        }
        assertFalse(isDataTypeSet);
        assertEquals(-1, dataType);
    }
}
