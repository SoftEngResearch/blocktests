package com.github.jaiimageio.impl.plugins.bmp;

import java.awt.Rectangle;
import java.awt.image.BandedSampleModel;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferShort;
import java.awt.image.DataBufferUShort;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Iterator;
import javax.imageio.IIOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.event.IIOWriteProgressListener;
import javax.imageio.event.IIOWriteWarningListener;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.ImageOutputStream;
import com.github.jaiimageio.impl.common.ImageUtil;
import com.github.jaiimageio.plugins.bmp.BMPImageWriteParam;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.github.jaiimageio.impl.plugins.bmp.BMPImageWriter.*;

public class BMPImageWriterBlockTest {

    @Test
    public void testLine685() throws Exception {
        ComponentSampleModel csm = new ComponentSampleModel(DataBuffer.TYPE_BYTE, 5, 10, 2, 5, new int[] { 5, 12, 15 });
        int startX = 3;
        int startY = 5;
        int pos = 0;
        pos = csm.getOffset(startX, startY, 0);
        assertEquals(5 * 5 + 3 * 2 + 5, pos);
    }

    @Test
    public void testLine687() throws Exception {
        ComponentSampleModel csm = new ComponentSampleModel(DataBuffer.TYPE_BYTE, 5, 10, 2, 5, new int[] { 5, 12, 15 });
        int startX = 3;
        int startY = 5;
        int pos = 0;
        pos = csm.getOffset(startX, startY, 0);
        for (int nb = 1; nb < csm.getNumBands(); nb++) {
            if (pos > csm.getOffset(startX, startY, nb)) {
                pos = csm.getOffset(startX, startY, nb);
            }
        }
        assertEquals(5 * 5 + 3 * 2 + 5, pos);
    }

    @Test
    public void testLine689() throws Exception {
        ComponentSampleModel csm = new ComponentSampleModel(DataBuffer.TYPE_BYTE, 5, 10, 2, 5, new int[] { 5, 3, 6 });
        int startX = 3;
        int startY = 5;
        int pos = 0;
        pos = csm.getOffset(startX, startY, 0);
        for (int nb = 1; nb < csm.getNumBands(); nb++) {
            if (pos > csm.getOffset(startX, startY, nb)) {
                pos = csm.getOffset(startX, startY, nb);
            }
        }
        assertEquals(5 * 5 + 3 * 2 + 3, pos);
    }

    @Test
    public void testLine691() throws Exception {
        ComponentSampleModel csm = new ComponentSampleModel(DataBuffer.TYPE_BYTE, 5, 10, 2, 5, new int[] { 5, 3, 1 });
        int startX = 3;
        int startY = 5;
        int pos = 0;
        pos = csm.getOffset(startX, startY, 0);
        for (int nb = 1; nb < csm.getNumBands(); nb++) {
            if (pos > csm.getOffset(startX, startY, nb)) {
                pos = csm.getOffset(startX, startY, nb);
            }
        }
        assertEquals(5 * 5 + 3 * 2 + 1, pos);
    }
}
