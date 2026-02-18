package com.lowagie.text.pdf.codec;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import com.lowagie.text.BadElementException;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Image;
import com.lowagie.text.ImgRaw;
import com.lowagie.text.Utilities;
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfNumber;
import com.lowagie.text.pdf.PdfString;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.lowagie.text.pdf.codec.BmpImage.*;

public class BmpImageBlockTest {

    @Test
    public void testA() throws Exception {
        int imageType = BmpImage.VERSION_3_1_BIT;
        int sizeOfPalette = -1;
        long bitmapOffset = 0;
        long size = 10;
        long colorsUsed = 0;
        switch(imageType) {
            case BmpImage.VERSION_3_1_BIT:
                sizeOfPalette = (int) (colorsUsed == 0 ? 2 : colorsUsed) * 4;
                break;
            case BmpImage.VERSION_3_4_BIT:
                sizeOfPalette = (int) (colorsUsed == 0 ? 16 : colorsUsed) * 4;
                break;
            case BmpImage.VERSION_3_8_BIT:
                sizeOfPalette = (int) (colorsUsed == 0 ? 256 : colorsUsed) * 4;
                break;
            default:
                sizeOfPalette = 0;
                break;
        }
        assertEquals(18, bitmapOffset);
        assertEquals(8, sizeOfPalette);
    }

    @Test
    public void testB() throws Exception {
        int imageType = BmpImage.VERSION_3_1_BIT;
        int sizeOfPalette = -1;
        long bitmapOffset = 0;
        long size = 10;
        long colorsUsed = 1;
        switch(imageType) {
            case BmpImage.VERSION_3_1_BIT:
                sizeOfPalette = (int) (colorsUsed == 0 ? 2 : colorsUsed) * 4;
                break;
            case BmpImage.VERSION_3_4_BIT:
                sizeOfPalette = (int) (colorsUsed == 0 ? 16 : colorsUsed) * 4;
                break;
            case BmpImage.VERSION_3_8_BIT:
                sizeOfPalette = (int) (colorsUsed == 0 ? 256 : colorsUsed) * 4;
                break;
            default:
                sizeOfPalette = 0;
                break;
        }
        assertEquals(14, bitmapOffset);
        assertEquals(4, sizeOfPalette);
    }

    @Test
    public void testC() throws Exception {
        int imageType = BmpImage.VERSION_3_4_BIT;
        int sizeOfPalette = -1;
        long bitmapOffset = 0;
        long size = 10;
        long colorsUsed = 0;
        switch(imageType) {
            case BmpImage.VERSION_3_1_BIT:
                sizeOfPalette = (int) (colorsUsed == 0 ? 2 : colorsUsed) * 4;
                break;
            case BmpImage.VERSION_3_4_BIT:
                sizeOfPalette = (int) (colorsUsed == 0 ? 16 : colorsUsed) * 4;
                break;
            case BmpImage.VERSION_3_8_BIT:
                sizeOfPalette = (int) (colorsUsed == 0 ? 256 : colorsUsed) * 4;
                break;
            default:
                sizeOfPalette = 0;
                break;
        }
        assertEquals(74, bitmapOffset);
        assertEquals(64, sizeOfPalette);
    }

    @Test
    public void testD() throws Exception {
        int imageType = BmpImage.VERSION_3_4_BIT;
        int sizeOfPalette = -1;
        long bitmapOffset = 0;
        long size = 10;
        long colorsUsed = 2;
        switch(imageType) {
            case BmpImage.VERSION_3_1_BIT:
                sizeOfPalette = (int) (colorsUsed == 0 ? 2 : colorsUsed) * 4;
                break;
            case BmpImage.VERSION_3_4_BIT:
                sizeOfPalette = (int) (colorsUsed == 0 ? 16 : colorsUsed) * 4;
                break;
            case BmpImage.VERSION_3_8_BIT:
                sizeOfPalette = (int) (colorsUsed == 0 ? 256 : colorsUsed) * 4;
                break;
            default:
                sizeOfPalette = 0;
                break;
        }
        assertEquals(18, bitmapOffset);
        assertEquals(8, sizeOfPalette);
    }

    @Test
    public void testE() throws Exception {
        int imageType = BmpImage.VERSION_3_8_BIT;
        int sizeOfPalette = -1;
        long bitmapOffset = 0;
        long size = 10;
        long colorsUsed = 0;
        switch(imageType) {
            case BmpImage.VERSION_3_1_BIT:
                sizeOfPalette = (int) (colorsUsed == 0 ? 2 : colorsUsed) * 4;
                break;
            case BmpImage.VERSION_3_4_BIT:
                sizeOfPalette = (int) (colorsUsed == 0 ? 16 : colorsUsed) * 4;
                break;
            case BmpImage.VERSION_3_8_BIT:
                sizeOfPalette = (int) (colorsUsed == 0 ? 256 : colorsUsed) * 4;
                break;
            default:
                sizeOfPalette = 0;
                break;
        }
        assertEquals(1034, bitmapOffset);
        assertEquals(1024, sizeOfPalette);
    }

    @Test
    public void testF() throws Exception {
        int imageType = BmpImage.VERSION_3_8_BIT;
        int sizeOfPalette = -1;
        long bitmapOffset = 0;
        long size = 10;
        long colorsUsed = 3;
        switch(imageType) {
            case BmpImage.VERSION_3_1_BIT:
                sizeOfPalette = (int) (colorsUsed == 0 ? 2 : colorsUsed) * 4;
                break;
            case BmpImage.VERSION_3_4_BIT:
                sizeOfPalette = (int) (colorsUsed == 0 ? 16 : colorsUsed) * 4;
                break;
            case BmpImage.VERSION_3_8_BIT:
                sizeOfPalette = (int) (colorsUsed == 0 ? 256 : colorsUsed) * 4;
                break;
            default:
                sizeOfPalette = 0;
                break;
        }
        assertEquals(22, bitmapOffset);
        assertEquals(12, sizeOfPalette);
    }

    @Test
    public void testLine422() throws Exception {
        int imageType = -100;
        int sizeOfPalette = -1;
        long bitmapOffset = 0;
        long size = 10;
        long colorsUsed = 3;
        switch(imageType) {
            case BmpImage.VERSION_3_1_BIT:
                sizeOfPalette = (int) (colorsUsed == 0 ? 2 : colorsUsed) * 4;
                break;
            case BmpImage.VERSION_3_4_BIT:
                sizeOfPalette = (int) (colorsUsed == 0 ? 16 : colorsUsed) * 4;
                break;
            case BmpImage.VERSION_3_8_BIT:
                sizeOfPalette = (int) (colorsUsed == 0 ? 256 : colorsUsed) * 4;
                break;
            default:
                sizeOfPalette = 0;
                break;
        }
        assertEquals(10, bitmapOffset);
        assertEquals(0, sizeOfPalette);
    }
}
