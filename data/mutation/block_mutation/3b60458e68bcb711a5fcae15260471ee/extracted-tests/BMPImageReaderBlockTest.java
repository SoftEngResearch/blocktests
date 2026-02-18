package com.github.jaiimageio.impl.plugins.bmp;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferUShort;
import java.awt.image.DirectColorModel;
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
import java.util.ArrayList;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.event.IIOReadProgressListener;
import javax.imageio.event.IIOReadUpdateListener;
import javax.imageio.event.IIOReadWarningListener;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import com.github.jaiimageio.impl.common.ImageUtil;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.github.jaiimageio.impl.plugins.bmp.BMPImageReader.*;

public class BMPImageReaderBlockTest {

    @Test
    public void testLine476() throws Exception {
        int imageType = 0;
        long compression = 6;
        int redMask = 0;
        int greenMask = 0;
        int blueMask = 0;
        int bitsPerPixel = 1;
        if (bitsPerPixel == 1) {
            imageType = BMPImageReader.VERSION_4_1_BIT;
        } else if (bitsPerPixel == 4) {
            imageType = BMPImageReader.VERSION_4_4_BIT;
        } else if (bitsPerPixel == 8) {
            imageType = BMPImageReader.VERSION_4_8_BIT;
        } else if (bitsPerPixel == 16) {
            imageType = BMPImageReader.VERSION_4_16_BIT;
            if ((int) compression == BI_RGB) {
                redMask = 0x7C00;
                greenMask = 0x3E0;
                blueMask = 0x1F;
            }
        } else if (bitsPerPixel == 24) {
            imageType = BMPImageReader.VERSION_4_24_BIT;
        } else if (bitsPerPixel == 32) {
            imageType = BMPImageReader.VERSION_4_32_BIT;
            if ((int) compression == BI_RGB) {
                redMask = 0x00FF0000;
                greenMask = 0x0000FF00;
                blueMask = 0x000000FF;
            }
        }
        assertEquals(10, imageType);
    }

    @Test
    public void testLine477() throws Exception {
        int imageType = 0;
        long compression = 6;
        int redMask = 0;
        int greenMask = 0;
        int blueMask = 0;
        int bitsPerPixel = 4;
        if (bitsPerPixel == 1) {
            imageType = BMPImageReader.VERSION_4_1_BIT;
        } else if (bitsPerPixel == 4) {
            imageType = BMPImageReader.VERSION_4_4_BIT;
        } else if (bitsPerPixel == 8) {
            imageType = BMPImageReader.VERSION_4_8_BIT;
        } else if (bitsPerPixel == 16) {
            imageType = BMPImageReader.VERSION_4_16_BIT;
            if ((int) compression == BI_RGB) {
                redMask = 0x7C00;
                greenMask = 0x3E0;
                blueMask = 0x1F;
            }
        } else if (bitsPerPixel == 24) {
            imageType = BMPImageReader.VERSION_4_24_BIT;
        } else if (bitsPerPixel == 32) {
            imageType = BMPImageReader.VERSION_4_32_BIT;
            if ((int) compression == BI_RGB) {
                redMask = 0x00FF0000;
                greenMask = 0x0000FF00;
                blueMask = 0x000000FF;
            }
        }
        assertEquals(11, imageType);
    }

    @Test
    public void testLine478() throws Exception {
        int imageType = 0;
        long compression = 6;
        int redMask = 0;
        int greenMask = 0;
        int blueMask = 0;
        int bitsPerPixel = 8;
        if (bitsPerPixel == 1) {
            imageType = BMPImageReader.VERSION_4_1_BIT;
        } else if (bitsPerPixel == 4) {
            imageType = BMPImageReader.VERSION_4_4_BIT;
        } else if (bitsPerPixel == 8) {
            imageType = BMPImageReader.VERSION_4_8_BIT;
        } else if (bitsPerPixel == 16) {
            imageType = BMPImageReader.VERSION_4_16_BIT;
            if ((int) compression == BI_RGB) {
                redMask = 0x7C00;
                greenMask = 0x3E0;
                blueMask = 0x1F;
            }
        } else if (bitsPerPixel == 24) {
            imageType = BMPImageReader.VERSION_4_24_BIT;
        } else if (bitsPerPixel == 32) {
            imageType = BMPImageReader.VERSION_4_32_BIT;
            if ((int) compression == BI_RGB) {
                redMask = 0x00FF0000;
                greenMask = 0x0000FF00;
                blueMask = 0x000000FF;
            }
        }
        assertEquals(12, imageType);
    }

    @Test
    public void testLine479() throws Exception {
        int imageType = 0;
        long compression = 6;
        int redMask = 0;
        int greenMask = 0;
        int blueMask = 0;
        int bitsPerPixel = 16;
        if (bitsPerPixel == 1) {
            imageType = BMPImageReader.VERSION_4_1_BIT;
        } else if (bitsPerPixel == 4) {
            imageType = BMPImageReader.VERSION_4_4_BIT;
        } else if (bitsPerPixel == 8) {
            imageType = BMPImageReader.VERSION_4_8_BIT;
        } else if (bitsPerPixel == 16) {
            imageType = BMPImageReader.VERSION_4_16_BIT;
            if ((int) compression == BI_RGB) {
                redMask = 0x7C00;
                greenMask = 0x3E0;
                blueMask = 0x1F;
            }
        } else if (bitsPerPixel == 24) {
            imageType = BMPImageReader.VERSION_4_24_BIT;
        } else if (bitsPerPixel == 32) {
            imageType = BMPImageReader.VERSION_4_32_BIT;
            if ((int) compression == BI_RGB) {
                redMask = 0x00FF0000;
                greenMask = 0x0000FF00;
                blueMask = 0x000000FF;
            }
        }
        assertEquals(0, blueMask);
        assertEquals(0, greenMask);
        assertEquals(0, redMask);
        assertEquals(13, imageType);
    }

    @Test
    public void testLine480() throws Exception {
        int imageType = 0;
        long compression = 0;
        int redMask = 0;
        int greenMask = 0;
        int blueMask = 0;
        int bitsPerPixel = 16;
        if (bitsPerPixel == 1) {
            imageType = BMPImageReader.VERSION_4_1_BIT;
        } else if (bitsPerPixel == 4) {
            imageType = BMPImageReader.VERSION_4_4_BIT;
        } else if (bitsPerPixel == 8) {
            imageType = BMPImageReader.VERSION_4_8_BIT;
        } else if (bitsPerPixel == 16) {
            imageType = BMPImageReader.VERSION_4_16_BIT;
            if ((int) compression == BI_RGB) {
                redMask = 0x7C00;
                greenMask = 0x3E0;
                blueMask = 0x1F;
            }
        } else if (bitsPerPixel == 24) {
            imageType = BMPImageReader.VERSION_4_24_BIT;
        } else if (bitsPerPixel == 32) {
            imageType = BMPImageReader.VERSION_4_32_BIT;
            if ((int) compression == BI_RGB) {
                redMask = 0x00FF0000;
                greenMask = 0x0000FF00;
                blueMask = 0x000000FF;
            }
        }
        assertEquals(0x1F, blueMask);
        assertEquals(0x3E0, greenMask);
        assertEquals(0x7C00, redMask);
        assertEquals(13, imageType);
    }

    @Test
    public void testLine481() throws Exception {
        int imageType = 0;
        long compression = 6;
        int redMask = 0;
        int greenMask = 0;
        int blueMask = 0;
        int bitsPerPixel = 24;
        if (bitsPerPixel == 1) {
            imageType = BMPImageReader.VERSION_4_1_BIT;
        } else if (bitsPerPixel == 4) {
            imageType = BMPImageReader.VERSION_4_4_BIT;
        } else if (bitsPerPixel == 8) {
            imageType = BMPImageReader.VERSION_4_8_BIT;
        } else if (bitsPerPixel == 16) {
            imageType = BMPImageReader.VERSION_4_16_BIT;
            if ((int) compression == BI_RGB) {
                redMask = 0x7C00;
                greenMask = 0x3E0;
                blueMask = 0x1F;
            }
        } else if (bitsPerPixel == 24) {
            imageType = BMPImageReader.VERSION_4_24_BIT;
        } else if (bitsPerPixel == 32) {
            imageType = BMPImageReader.VERSION_4_32_BIT;
            if ((int) compression == BI_RGB) {
                redMask = 0x00FF0000;
                greenMask = 0x0000FF00;
                blueMask = 0x000000FF;
            }
        }
        assertEquals(14, imageType);
    }

    @Test
    public void testLine482() throws Exception {
        int imageType = 0;
        long compression = 6;
        int redMask = 0;
        int greenMask = 0;
        int blueMask = 0;
        int bitsPerPixel = 32;
        if (bitsPerPixel == 1) {
            imageType = BMPImageReader.VERSION_4_1_BIT;
        } else if (bitsPerPixel == 4) {
            imageType = BMPImageReader.VERSION_4_4_BIT;
        } else if (bitsPerPixel == 8) {
            imageType = BMPImageReader.VERSION_4_8_BIT;
        } else if (bitsPerPixel == 16) {
            imageType = BMPImageReader.VERSION_4_16_BIT;
            if ((int) compression == BI_RGB) {
                redMask = 0x7C00;
                greenMask = 0x3E0;
                blueMask = 0x1F;
            }
        } else if (bitsPerPixel == 24) {
            imageType = BMPImageReader.VERSION_4_24_BIT;
        } else if (bitsPerPixel == 32) {
            imageType = BMPImageReader.VERSION_4_32_BIT;
            if ((int) compression == BI_RGB) {
                redMask = 0x00FF0000;
                greenMask = 0x0000FF00;
                blueMask = 0x000000FF;
            }
        }
        assertEquals(0, blueMask);
        assertEquals(0, greenMask);
        assertEquals(0, redMask);
        assertEquals(15, imageType);
    }

    @Test
    public void testLine483() throws Exception {
        int imageType = 0;
        long compression = 0;
        int redMask = 0;
        int greenMask = 0;
        int blueMask = 0;
        int bitsPerPixel = 32;
        if (bitsPerPixel == 1) {
            imageType = BMPImageReader.VERSION_4_1_BIT;
        } else if (bitsPerPixel == 4) {
            imageType = BMPImageReader.VERSION_4_4_BIT;
        } else if (bitsPerPixel == 8) {
            imageType = BMPImageReader.VERSION_4_8_BIT;
        } else if (bitsPerPixel == 16) {
            imageType = BMPImageReader.VERSION_4_16_BIT;
            if ((int) compression == BI_RGB) {
                redMask = 0x7C00;
                greenMask = 0x3E0;
                blueMask = 0x1F;
            }
        } else if (bitsPerPixel == 24) {
            imageType = BMPImageReader.VERSION_4_24_BIT;
        } else if (bitsPerPixel == 32) {
            imageType = BMPImageReader.VERSION_4_32_BIT;
            if ((int) compression == BI_RGB) {
                redMask = 0x00FF0000;
                greenMask = 0x0000FF00;
                blueMask = 0x000000FF;
            }
        }
        assertEquals(0x000000FF, blueMask);
        assertEquals(0x0000FF00, greenMask);
        assertEquals(0x00FF0000, redMask);
        assertEquals(15, imageType);
    }
}
