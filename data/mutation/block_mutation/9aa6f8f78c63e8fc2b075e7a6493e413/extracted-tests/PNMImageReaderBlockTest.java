package com.github.jaiimageio.impl.plugins.pnm;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferUShort;
import java.awt.image.IndexColorModel;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import com.github.jaiimageio.impl.common.ImageUtil;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.github.jaiimageio.impl.plugins.pnm.PNMImageReader.*;

public class PNMImageReaderBlockTest {

    @Test
    public void testLine479() throws Exception {
        Rectangle destinationRegion = new Rectangle(7, 0, 1, 0);
        Rectangle sourceRegion = new Rectangle(7, 0, 1, 0);
        byte[] buf = new byte[7];
        int scaleX = 1;
        byte[] data = new byte[] { 0, 1 };
        int n = 0;
        int b = 0;
        int pos = 7 - (destinationRegion.x & 7);
        for (int m = sourceRegion.x; m < sourceRegion.x + sourceRegion.width; m += scaleX) {
            b |= (data[m >> 3] >> (7 - (m & 7)) & 1) << pos;
            pos--;
            if (pos == -1) {
                buf[n++] = (byte) b;
                b = 0;
                pos = 7;
            }
        }
        assertEquals(7, pos);
    }

    @Test
    public void testLine485() throws Exception {
        Rectangle destinationRegion = new Rectangle(6, 0, 1, 0);
        Rectangle sourceRegion = new Rectangle(7, 0, 0, 0);
        byte[] buf = new byte[7];
        int scaleX = 1;
        byte[] data = new byte[] { 0, 1 };
        int n = 0;
        int b = 0;
        int pos = 7 - (destinationRegion.x & 7);
        for (int m = sourceRegion.x; m < sourceRegion.x + sourceRegion.width; m += scaleX) {
            b |= (data[m >> 3] >> (7 - (m & 7)) & 1) << pos;
            pos--;
            if (pos == -1) {
                buf[n++] = (byte) b;
                b = 0;
                pos = 7;
            }
        }
        assertEquals(1, pos);
    }

    @Test
    public void testLine434() throws Exception {
        int bitoff = 5;
        int k = 0;
        byte[] lineData = new byte[] { 1, 12, 44 };
        byte[] buf = new byte[20];
        int readLength = 3;
        int mask1 = (255 << bitoff) & 255;
        int mask2 = ~mask1 & 255;
        int shift = 8 - bitoff;
        int n = 0;
        int m = k;
        for (; n < readLength - 1; n++, m++) buf[m] = (byte) (((lineData[n] & mask2) << shift) | (lineData[n + 1] & mask1) >> bitoff);
        assertEquals(8, buf[0]);
    }

    @Test
    public void testLine438() throws Exception {
        int bitoff = 8;
        int k = 1;
        byte[] lineData = new byte[] { 1, 2, 44 };
        byte[] buf = new byte[20];
        int readLength = 1;
        int mask1 = (255 << bitoff) & 255;
        int mask2 = ~mask1 & 255;
        int shift = 8 - bitoff;
        int n = 0;
        int m = k;
        for (; n < readLength - 1; n++, m++) buf[m] = (byte) (((lineData[n] & mask2) << shift) | (lineData[n + 1] & mask1) >> bitoff);
        assertEquals(1, buf[1]);
    }
}
