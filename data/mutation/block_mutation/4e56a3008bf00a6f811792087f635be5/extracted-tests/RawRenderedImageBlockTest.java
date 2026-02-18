package com.github.jaiimageio.impl.plugins.raw;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BandedSampleModel;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferDouble;
import java.awt.image.DataBufferFloat;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferShort;
import java.awt.image.DataBufferUShort;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageTypeSpecifier;
import com.github.jaiimageio.impl.common.ImageUtil;
import com.github.jaiimageio.impl.common.SimpleRenderedImage;
import com.github.jaiimageio.stream.RawImageInputStream;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.github.jaiimageio.impl.plugins.raw.RawRenderedImage.*;

public class RawRenderedImageBlockTest {

    @Test
    public void foo() throws Exception {
        Rectangle destRect = new Rectangle(1, 2, 3, 4);
        int offx = 5;
        int offy = 6;
        int scaleX = 7;
        int scaleY = 8;
        Point sourceOrigin = new Point(9, 10);
        int tileWidth = 2;
        int tileHeight = 3;
        int sourceSX = (destRect.x - offx) * scaleX + sourceOrigin.x;
        int sourceSY = (destRect.y - offy) * scaleY + sourceOrigin.y;
        int sourceEX = (destRect.width - 1) * scaleX + sourceSX;
        int sourceEY = (destRect.height - 1) * scaleY + sourceSY;
        int startXTile = sourceSX / tileWidth;
        int startYTile = sourceSY / tileHeight;
        int endXTile = sourceEX / tileWidth;
        int endYTile = sourceEY / tileHeight;
        assertEquals(0, endYTile);
        assertEquals(-2, endXTile);
        assertEquals(-7, startYTile);
        assertEquals(-9, startXTile);
    }

    @Test
    public void test1() throws Exception {
        int x = 1;
        int y = 2;
        int tileWidth = 3;
        int tileHeight = 4;
        Dimension originalDimension = new Dimension(1, 1);
        int startX = x * tileWidth;
        int startY = y * tileHeight;
        int cTileHeight = tileHeight;
        int cTileWidth = tileWidth;
        if (startY + cTileHeight >= originalDimension.height)
            cTileHeight = originalDimension.height - startY;
        if (startX + cTileWidth >= originalDimension.width)
            cTileWidth = originalDimension.width - startX;
        int tx = startX;
        int ty = startY;
        assertEquals(-2, cTileWidth);
        assertEquals(-7, cTileHeight);
    }

    @Test
    public void test2() throws Exception {
        int x = 1;
        int y = 2;
        int tileWidth = 3;
        int tileHeight = 4;
        Dimension originalDimension = new Dimension(100, 100);
        int startX = x * tileWidth;
        int startY = y * tileHeight;
        int cTileHeight = tileHeight;
        int cTileWidth = tileWidth;
        if (startY + cTileHeight >= originalDimension.height)
            cTileHeight = originalDimension.height - startY;
        if (startX + cTileWidth >= originalDimension.width)
            cTileWidth = originalDimension.width - startX;
        int tx = startX;
        int ty = startY;
        assertEquals(3, cTileWidth);
        assertEquals(4, cTileHeight);
    }

    @Test
    public void testLine552() throws Exception {
        byte[] pixbuf = new byte[] { (byte) 0b10101010, (byte) 0b11001101, (byte) 0b11110000, (byte) 0b00001110 };
        int bitoff = 1;
        int readBytes = 4;
        int mask1 = (255 << bitoff) & 255;
        int mask2 = ~mask1 & 255;
        int shift = 8 - bitoff;
        int n = 0;
        for (; n < readBytes - 1; n++) pixbuf[n] = (byte) (((pixbuf[n] & mask2) << shift) | (pixbuf[n + 1] & mask1) >> bitoff);
        assertEquals(0, pixbuf[3]);
        assertEquals(7, pixbuf[2]);
        assertEquals(-8, pixbuf[1]);
        assertEquals(102, pixbuf[0]);
    }
}
