package com.lowagie.text.pdf.codec;

import java.awt.color.ICC_Profile;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Image;
import com.lowagie.text.Jpeg;
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfNumber;
import com.lowagie.text.pdf.PdfString;
import com.lowagie.text.pdf.RandomAccessFileOrArray;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.lowagie.text.pdf.codec.TiffImage.*;

public class TiffImageBlockTest {

    @Test
    public void testLine509() throws Exception {
        byte[] palette = new byte[6];
        int gColor = 2;
        int bColor = 4;
        TIFFDirectory dir;
        TIFFField fd = null;
        char[] rgb = new char[] { 0x1A2B, 0x3C4D, 0x5E6F, 0x7081, 0x9293, 0xA4B5 };
        for (int k = 0; k < gColor; ++k) {
            palette[k * 3] = (byte) (rgb[k] >>> 8);
            palette[k * 3 + 1] = (byte) (rgb[k + gColor] >>> 8);
        }
        assertEquals((byte) -92, palette[5]);
        assertEquals((byte) 112, palette[4]);
        assertEquals((byte) 60, palette[3]);
        assertEquals((byte) -110, palette[2]);
        assertEquals((byte) 94, palette[1]);
        assertEquals((byte) 26, palette[0]);
    }

    @Test
    public void testLine512() throws Exception {
        byte[] palette = new byte[3];
        int gColor = 1;
        int bColor = 2;
        TIFFDirectory dir;
        TIFFField fd = null;
        char[] rgb = new char[] { 0xFF00, 0x00FF, 0x0F0F };
        for (int k = 0; k < gColor; ++k) {
            palette[k * 3] = (byte) (rgb[k] >>> 8);
            palette[k * 3 + 1] = (byte) (rgb[k + gColor] >>> 8);
        }
        assertEquals((byte) 0x0F, palette[2]);
        assertEquals((byte) 0x00, palette[1]);
        assertEquals((byte) 0xFF, palette[0]);
    }

    @Test
    public void testLine514() throws Exception {
        byte[] palette = new byte[18];
        int gColor = 6;
        int bColor = 12;
        TIFFDirectory dir;
        TIFFField fd = null;
        char[] rgb = new char[] { 0x1122, 0x3344, 0x5566, 0x7788, 0x99AA, 0xBBCC, 0xDDEE, 0x1122, 0x3344, 0x5566, 0x7788, 0x99AA, 0xBBCC, 0xDDEE, 0x1122, 0x3344, 0x5566, 0x7788 };
        for (int k = 0; k < gColor; ++k) {
            palette[k * 3] = (byte) (rgb[k] >>> 8);
            palette[k * 3 + 1] = (byte) (rgb[k + gColor] >>> 8);
        }
        assertEquals((byte) 119, palette[17]);
        assertEquals((byte) -103, palette[16]);
        assertEquals((byte) -69, palette[15]);
        assertEquals((byte) -35, palette[5]);
        assertEquals((byte) 17, palette[4]);
        assertEquals((byte) 51, palette[3]);
        assertEquals((byte) -69, palette[2]);
        assertEquals((byte) -35, palette[1]);
        assertEquals((byte) 17, palette[0]);
    }
}
