package com.github.jaiimageio.impl.plugins.tiff;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.imageio.IIOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.ImageOutputStream;
import org.w3c.dom.Node;
import com.github.jaiimageio.impl.common.ImageUtil;
import com.github.jaiimageio.impl.common.SimpleRenderedImage;
import com.github.jaiimageio.impl.common.SingleTileRenderedImage;
import com.github.jaiimageio.plugins.tiff.BaselineTIFFTagSet;
import com.github.jaiimageio.plugins.tiff.EXIFParentTIFFTagSet;
import com.github.jaiimageio.plugins.tiff.EXIFTIFFTagSet;
import com.github.jaiimageio.plugins.tiff.TIFFColorConverter;
import com.github.jaiimageio.plugins.tiff.TIFFCompressor;
import com.github.jaiimageio.plugins.tiff.TIFFField;
import com.github.jaiimageio.plugins.tiff.TIFFImageWriteParam;
import com.github.jaiimageio.plugins.tiff.TIFFTag;
import com.github.jaiimageio.plugins.tiff.TIFFTagSet;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.github.jaiimageio.impl.plugins.tiff.TIFFImageWriter.*;

public class TIFFImageWriterBlockTest {

    @Test
    public void testLine751() throws Exception {
        int compression = 0;
        String compressionType = "CCITT T.6";
        int len = TIFFImageWriter.compressionTypes.length;
        for (int i = 0; i < len; i++) {
            if (compressionType.equals(TIFFImageWriter.compressionTypes[i])) {
                compression = TIFFImageWriter.compressionNumbers[i];
            }
        }
        assertEquals(BaselineTIFFTagSet.COMPRESSION_CCITT_T_6, compression);
    }

    @Test
    public void testLine752() throws Exception {
        int compression = 0;
        String compressionType = "CCITT";
        int len = TIFFImageWriter.compressionTypes.length;
        for (int i = 0; i < len; i++) {
            if (compressionType.equals(TIFFImageWriter.compressionTypes[i])) {
                compression = TIFFImageWriter.compressionNumbers[i];
            }
        }
        assertEquals(0, compression);
    }

    @Test
    public void testLine1777() throws Exception {
        byte[] buf = new byte[] { 0, 1, 2, 3, 127 };
        byte[] bbuf = new byte[buf.length];
        int len = buf.length;
        for (int i = 0; i < len; i++) {
            bbuf[i] = (byte) (buf[i] ^ 0xff);
        }
        assertTrue(buf[0] == (byte) 255 && buf[1] == (byte) 254 && buf[2] == (byte) 253 && buf[3] == (byte) 252 && buf[4] == (byte) 128);
        assertEquals(5, buf.length);
    }

    @Test
    public void testLine1779() throws Exception {
        byte[] buf = new byte[] { -1, -2, -128, -127 };
        byte[] bbuf = new byte[buf.length];
        int len = buf.length;
        for (int i = 0; i < len; i++) {
            bbuf[i] = (byte) (buf[i] ^ 0xff);
        }
        assertTrue(buf[0] == 0 && buf[1] == 1 && buf[2] == 127 && buf[3] == 126);
        assertEquals(4, buf.length);
    }

    @Test
    public void testLine1781() throws Exception {
        byte[] buf = new byte[] {};
        byte[] bbuf = new byte[buf.length];
        int len = buf.length;
        for (int i = 0; i < len; i++) {
            bbuf[i] = (byte) (buf[i] ^ 0xff);
        }
        assertTrue(buf.length == 0);
        assertEquals(0, buf.length);
    }

    @Test
    public void testLine1783() throws Exception {
        byte[] buf = new byte[] { 0, 1, 2, 3, 0 };
        byte[] bbuf = new byte[buf.length];
        int len = buf.length;
        for (int i = 0; i < len; i++) {
            bbuf[i] = (byte) (buf[i] ^ 0xff);
        }
        assertTrue(buf[0] == (byte) 255 && buf[1] == (byte) 254 && buf[2] == (byte) 253 && buf[3] == (byte) 252 && buf[4] == (byte) 255);
        assertEquals(5, buf.length);
    }

    @Test
    public void testLine1785() throws Exception {
        byte[] buf = new byte[] { (byte) 0xFF };
        byte[] bbuf = new byte[buf.length];
        int len = buf.length;
        for (int i = 0; i < len; i++) {
            bbuf[i] = (byte) (buf[i] ^ 0xff);
        }
        assertTrue(buf[0] == 0);
        assertEquals(1, buf.length);
    }
}
