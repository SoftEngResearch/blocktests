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
        boolean isWhiteZero;
        long tiffT4Options;
        long[] tileByteCounts;
        int sampleSize;
        long[] tileOffsets;
        int fillOrder;
        TIFFDecodeParam param;
        long tiffT6Options;
        int tilesY;
        boolean isBigEndian;
        int predictor;
        char[] colormap;
        int imageType;
        int tilesX;
        boolean tiled;
        int dataType;
        TIFFFaxDecoder decoder;
        int samplesPerPixel = sfield == null ? 1 : (int) sfield.getAsLong(0);
        int numBands;
        int tileSize;
        TIFFLZWDecoder lzwDecoder;
        boolean decodePaletteAsShorts = param.getDecodePaletteAsShorts();
        int compression;
        Inflater inflater;
        int photometricType;
        int tileWidth;
        int tileHeight;
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
        TIFFField sampleFormatField = new TIFFField(1, 2, 3, new char[] { 100, 101, 102 });
        char[] sampleFormat = null;
        if (sampleFormatField != null) {
            sampleFormat = sampleFormatField.getAsChars();
            // Check that all the samples have the same format
            for (int l = 1; l < sampleFormat.length; l++) {
                if (sampleFormat[l] != sampleFormat[0]) {
                    throw new RuntimeException(PropertyUtil.getString("TIFFImage2"));
                }
            }
        } else {
            sampleFormat = new char[] { 1 };
        }
        boolean isValidDataFormat = false;
        switch(sampleSize) {
            case 1:
            case 4:
            case 8:
                if (sampleFormat[0] != 3) {
                    // Ignore whether signed or unsigned: treat all as unsigned.
                    dataType = DataBuffer.TYPE_BYTE;
                    isValidDataFormat = true;
                }
                break;
            case 16:
                if (sampleFormat[0] != 3) {
                    dataType = sampleFormat[0] == 2 ? DataBuffer.TYPE_SHORT : DataBuffer.TYPE_USHORT;
                    isValidDataFormat = true;
                }
                break;
            case 32:
                if (sampleFormat[0] == 3) {
                    isValidDataFormat = false;
                } else {
                    dataType = DataBuffer.TYPE_INT;
                    isValidDataFormat = true;
                }
                break;
        }
        if (!isValidDataFormat) {
            throw new RuntimeException(PropertyUtil.getString("TIFFImage3"));
        }
        TIFFField compField = new TIFFField(1, 2, 3, new char[] { 100, 101, 102 });
        compression = compField == null ? TIFFImage.COMP_NONE : compField.getAsInt(0);
        TIFFField photometricTypeField = new TIFFField(1, 2, 3, new char[] { 100, 101, 102 });
        if (photometricTypeField == null) {
            // White is zero
            photometricType = 0;
        } else {
            photometricType = photometricTypeField.getAsInt(0);
        }
        imageType = TIFFImage.TYPE_UNSUPPORTED;
        switch(photometricType) {
            case // WhiteIsZero
            0:
                isWhiteZero = true;
            case // BlackIsZero
            1:
                if (sampleSize == 1 && samplesPerPixel == 1) {
                    imageType = TIFFImage.TYPE_BILEVEL;
                } else if (sampleSize == 4 && samplesPerPixel == 1) {
                    imageType = TIFFImage.TYPE_GRAY_4BIT;
                } else if (sampleSize % 8 == 0) {
                    if (samplesPerPixel == 1) {
                        imageType = TIFFImage.TYPE_GRAY;
                    } else if (samplesPerPixel == 2) {
                        imageType = TIFFImage.TYPE_GRAY_ALPHA;
                    } else {
                        imageType = TIFFImage.TYPE_GENERIC;
                    }
                }
                break;
            case // RGB
            2:
                if (sampleSize % 8 == 0) {
                    if (samplesPerPixel == 3) {
                        imageType = TIFFImage.TYPE_RGB;
                    } else if (samplesPerPixel == 4) {
                        imageType = TIFFImage.TYPE_RGB_ALPHA;
                    } else {
                        imageType = TIFFImage.TYPE_GENERIC;
                    }
                }
                break;
            case // RGB Palette
            3:
                if (samplesPerPixel == 1 && (sampleSize == 4 || sampleSize == 8 || sampleSize == 16)) {
                    imageType = TIFFImage.TYPE_PALETTE;
                }
                break;
            case // Transparency mask
            4:
                if (sampleSize == 1 && samplesPerPixel == 1) {
                    imageType = TIFFImage.TYPE_BILEVEL;
                }
                break;
            default:
                // Other including CMYK, CIE L*a*b*, unknown.
                if (sampleSize % 8 == 0) {
                    imageType = TIFFImage.TYPE_GENERIC;
                }
        }
        if (imageType == TIFFImage.TYPE_UNSUPPORTED) {
            throw new RuntimeException(PropertyUtil.getString("TIFFImage4") + ": " + imageType);
        }
        Rectangle bounds = new Rectangle(0, 0, (int) dir.getFieldAsLong(TIFFImageDecoder.TIFF_IMAGE_WIDTH), (int) dir.getFieldAsLong(TIFFImageDecoder.TIFF_IMAGE_LENGTH));
        numBands = samplesPerPixel;
        TIFFField efield = new TIFFField(1, 2, 3, new char[] { 100, 101, 102 });
        int extraSamples = efield == null ? 0 : (int) efield.getAsLong(0);
        if (new TIFFField(1, 2, 3, new char[] { 100, 101, 102 }) != null) {
            tiled = true;
            // Image is in tiled format
            tileWidth = (int) dir.getFieldAsLong(TIFFImageDecoder.TIFF_TILE_WIDTH);
            tileHeight = (int) dir.getFieldAsLong(TIFFImageDecoder.TIFF_TILE_LENGTH);
            tileOffsets = (new TIFFField(1, 2, 3, new char[] { 100, 101, 102 })).getAsLongs();
            tileByteCounts = getFieldAsLongs(new TIFFField(1, 2, 3, new char[] { 100, 101, 102 }));
        } else {
            tiled = false;
            // we default here to the tile dimensions if they are written.
            tileWidth = new TIFFField(1, 2, 3, new char[] { 100, 101, 102 }) != null ? (int) dir.getFieldAsLong(TIFFImageDecoder.TIFF_TILE_WIDTH) : bounds.width;
            TIFFField field = new TIFFField(1, 2, 3, new char[] { 100, 101, 102 });
            if (field == null) {
                // Default is infinity (2^32 -1), basically the entire image
                tileHeight = new TIFFField(1, 2, 3, new char[] { 100, 101, 102 }) != null ? (int) dir.getFieldAsLong(TIFFImageDecoder.TIFF_TILE_LENGTH) : bounds.height;
            } else {
                long l = field.getAsLong(0);
                long infinity = 1;
                infinity = (infinity << 32) - 1;
                if (l == infinity) {
                    // 2^32 - 1 (effectively infinity, entire image is 1 strip)
                    tileHeight = bounds.height;
                } else {
                    tileHeight = (int) l;
                }
            }
            TIFFField tileOffsetsField = new TIFFField(1, 2, 3, new char[] { 100, 101, 102 });
            if (tileOffsetsField == null) {
                throw new RuntimeException(PropertyUtil.getString("TIFFImage5"));
            } else {
                tileOffsets = getFieldAsLongs(tileOffsetsField);
            }
            TIFFField tileByteCountsField = new TIFFField(1, 2, 3, new char[] { 100, 101, 102 });
            if (tileByteCountsField == null) {
                throw new RuntimeException(PropertyUtil.getString("TIFFImage6"));
            } else {
                tileByteCounts = getFieldAsLongs(tileByteCountsField);
            }
        }
        tilesX = (bounds.width + tileWidth - 1) / tileWidth;
        tilesY = (bounds.height + tileHeight - 1) / tileHeight;
        tileSize = tileWidth * tileHeight * numBands;
        isBigEndian = dir.isBigEndian();
        TIFFField fillOrderField = new TIFFField(1, 2, 3, new char[] { 100, 101, 102 });
        if (fillOrderField != null) {
            fillOrder = fillOrderField.getAsInt(0);
        } else {
            // Default Fill Order
            fillOrder = 1;
        }
        switch(compression) {
            case TIFFImage.COMP_NONE:
            case TIFFImage.COMP_PACKBITS:
                // Do nothing.
                break;
            case TIFFImage.COMP_DEFLATE:
                inflater = new Inflater();
                break;
            case TIFFImage.COMP_FAX_G3_1D:
            case TIFFImage.COMP_FAX_G3_2D:
            case TIFFImage.COMP_FAX_G4_2D:
                if (sampleSize != 1) {
                    throw new RuntimeException(PropertyUtil.getString("TIFFImage7"));
                }
                // Fax T.4 compression options
                if (compression == 3) {
                    TIFFField t4OptionsField = new TIFFField(1, 2, 3, new char[] { 100, 101, 102 });
                    if (t4OptionsField != null) {
                        tiffT4Options = t4OptionsField.getAsLong(0);
                    } else {
                        // Use default value
                        tiffT4Options = 0;
                    }
                }
                // Fax T.6 compression options
                if (compression == 4) {
                    TIFFField t6OptionsField = new TIFFField(1, 2, 3, new char[] { 100, 101, 102 });
                    if (t6OptionsField != null) {
                        tiffT6Options = t6OptionsField.getAsLong(0);
                    } else {
                        // Use default value
                        tiffT6Options = 0;
                    }
                }
                // Fax encoding, need to create the Fax decoder.
                decoder = new TIFFFaxDecoder(fillOrder, tileWidth, tileHeight);
                break;
            case TIFFImage.COMP_LZW:
                TIFFField predictorField = new TIFFField(1, 2, 3, new char[] { 100, 101, 102 });
                if (predictorField == null) {
                    predictor = 1;
                } else {
                    predictor = predictorField.getAsInt(0);
                    if (predictor != 1 && predictor != 2) {
                        throw new RuntimeException(PropertyUtil.getString("TIFFImage8"));
                    }
                    if (predictor == 2 && sampleSize != 8) {
                        throw new RuntimeException(PropertyUtil.getString("TIFFImage9"));
                    }
                }
                lzwDecoder = new TIFFLZWDecoder(tileWidth, predictor, samplesPerPixel);
                break;
            case TIFFImage.COMP_JPEG_OLD:
                throw new RuntimeException(PropertyUtil.getString("TIFFImage15"));
            default:
                throw new RuntimeException(PropertyUtil.getString("TIFFImage10") + ": " + compression);
        }
        ColorModel colorModel = null;
        SampleModel sampleModel = null;
        switch(imageType) {
            case TIFFImage.TYPE_BILEVEL:
            case TIFFImage.TYPE_GRAY_4BIT:
                sampleModel = new MultiPixelPackedSampleModel(dataType, tileWidth, tileHeight, sampleSize);
                if (imageType == TIFFImage.TYPE_BILEVEL) {
                    byte[] map = new byte[] { (byte) (isWhiteZero ? 255 : 0), (byte) (isWhiteZero ? 0 : 255) };
                    colorModel = new IndexColorModel(1, 2, map, map, map);
                } else {
                    byte[] map = new byte[16];
                    if (isWhiteZero) {
                        for (int i = 0; i < map.length; i++) {
                            map[i] = (byte) (255 - (16 * i));
                        }
                    } else {
                        for (int i = 0; i < map.length; i++) {
                            map[i] = (byte) (16 * i);
                        }
                    }
                    colorModel = new IndexColorModel(4, 16, map, map, map);
                }
                break;
            case TIFFImage.TYPE_GRAY:
            case TIFFImage.TYPE_GRAY_ALPHA:
            case TIFFImage.TYPE_RGB:
            case TIFFImage.TYPE_RGB_ALPHA:
                int[] reverseOffsets = new int[numBands];
                // Create a pixel interleaved SampleModel with decreasing
                for (int i = 0; i < numBands; i++) {
                    reverseOffsets[i] = numBands - 1 - i;
                }
                sampleModel = new PixelInterleavedSampleModel(dataType, tileWidth, tileHeight, numBands, numBands * tileWidth, reverseOffsets);
                if (imageType == TIFFImage.TYPE_GRAY) {
                    colorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_GRAY), new int[] { sampleSize }, false, false, Transparency.OPAQUE, dataType);
                } else if (imageType == TIFFImage.TYPE_RGB) {
                    colorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] { sampleSize, sampleSize, sampleSize }, false, false, Transparency.OPAQUE, dataType);
                } else {
                    int transparency = Transparency.OPAQUE;
                    if (extraSamples == 1) {
                        // associated (premultiplied) alpha
                        transparency = Transparency.TRANSLUCENT;
                    } else if (extraSamples == 2) {
                        // unassociated alpha
                        transparency = Transparency.BITMASK;
                    }
                    colorModel = createAlphaComponentColorModel(dataType, numBands, extraSamples == 1, transparency);
                }
                break;
            case TIFFImage.TYPE_GENERIC:
            case TIFFImage.TYPE_YCBCR_SUB:
                int[] bandOffsets = new int[numBands];
                // For this case we can't display the image, so we create a
                // SampleModel with increasing bandOffsets, and keep the
                for (int i = 0; i < numBands; i++) {
                    bandOffsets[i] = i;
                }
                sampleModel = new PixelInterleavedSampleModel(dataType, tileWidth, tileHeight, numBands, numBands * tileWidth, bandOffsets);
                colorModel = null;
                break;
            case TIFFImage.TYPE_PALETTE:
                TIFFField cfield = new TIFFField(1, 2, 3, new char[] { 100, 101, 102 });
                if (cfield == null) {
                    throw new RuntimeException(PropertyUtil.getString("TIFFImage11"));
                } else {
                    colormap = cfield.getAsChars();
                }
                // Could be either 1 or 3 bands depending on whether we use
                // IndexColorModel or not.
                if (decodePaletteAsShorts) {
                    numBands = 3;
                    // be ushort.
                    if (dataType == DataBuffer.TYPE_BYTE) {
                        dataType = DataBuffer.TYPE_USHORT;
                    }
                    // a colormodel whose entries are of short data type.
                    sampleModel = TIFFImage.createPixelInterleavedSampleModel(dataType, tileWidth, tileHeight, numBands);
                    colorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] { 16, 16, 16 }, false, false, Transparency.OPAQUE, dataType);
                } else {
                    numBands = 1;
                    if (sampleSize == 4) {
                        // IndexColorModel to do the unpacking.
                        sampleModel = new MultiPixelPackedSampleModel(DataBuffer.TYPE_BYTE, tileWidth, tileHeight, sampleSize);
                    } else if (sampleSize == 8) {
                        sampleModel = TIFFImage.createPixelInterleavedSampleModel(DataBuffer.TYPE_BYTE, tileWidth, tileHeight, numBands);
                    } else if (sampleSize == 16) {
                        // negative.
                        dataType = DataBuffer.TYPE_USHORT;
                        sampleModel = TIFFImage.createPixelInterleavedSampleModel(DataBuffer.TYPE_USHORT, tileWidth, tileHeight, numBands);
                    }
                    int bandLength = colormap.length / 3;
                    byte[] r = new byte[bandLength];
                    byte[] g = new byte[bandLength];
                    byte[] b = new byte[bandLength];
                    int gIndex = bandLength;
                    int bIndex = bandLength * 2;
                    if (dataType == DataBuffer.TYPE_SHORT) {
                        for (int i = 0; i < bandLength; i++) {
                            r[i] = param.decodeSigned16BitsTo8Bits((short) colormap[i]);
                            g[i] = param.decodeSigned16BitsTo8Bits((short) colormap[gIndex + i]);
                            b[i] = param.decodeSigned16BitsTo8Bits((short) colormap[bIndex + i]);
                        }
                    } else {
                        for (int i = 0; i < bandLength; i++) {
                            r[i] = param.decode16BitsTo8Bits(colormap[i] & 0xffff);
                            g[i] = param.decode16BitsTo8Bits(colormap[gIndex + i] & 0xffff);
                            b[i] = param.decode16BitsTo8Bits(colormap[bIndex + i] & 0xffff);
                        }
                    }
                    colorModel = new IndexColorModel(sampleSize, bandLength, r, g, b);
                }
                break;
            default:
                throw new RuntimeException(PropertyUtil.getString("TIFFImage4") + ": " + imageType);
        }
        Map properties = new HashMap();
        properties.put("tiff_directory", dir);
        init((CachableRed) null, bounds, colorModel, sampleModel, 0, 0, properties);
        assertEquals(100, sampleSize);
    }

    @Test
    public void testLine231() throws Exception {
        TIFFDirectory dir = null;
        boolean isWhiteZero;
        long tiffT4Options;
        long[] tileByteCounts;
        int sampleSize;
        long[] tileOffsets;
        int fillOrder;
        TIFFDecodeParam param;
        long tiffT6Options;
        int tilesY;
        boolean isBigEndian;
        int predictor;
        char[] colormap;
        int imageType;
        int tilesX;
        boolean tiled;
        int dataType;
        TIFFFaxDecoder decoder;
        int samplesPerPixel = sfield == null ? 1 : (int) sfield.getAsLong(0);
        int numBands;
        int tileSize;
        TIFFLZWDecoder lzwDecoder;
        boolean decodePaletteAsShorts = param.getDecodePaletteAsShorts();
        int compression;
        Inflater inflater;
        int photometricType;
        int tileWidth;
        int tileHeight;
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
        TIFFField sampleFormatField = null;
        char[] sampleFormat = null;
        if (sampleFormatField != null) {
            sampleFormat = sampleFormatField.getAsChars();
            // Check that all the samples have the same format
            for (int l = 1; l < sampleFormat.length; l++) {
                if (sampleFormat[l] != sampleFormat[0]) {
                    throw new RuntimeException(PropertyUtil.getString("TIFFImage2"));
                }
            }
        } else {
            sampleFormat = new char[] { 1 };
        }
        boolean isValidDataFormat = false;
        switch(sampleSize) {
            case 1:
            case 4:
            case 8:
                if (sampleFormat[0] != 3) {
                    // Ignore whether signed or unsigned: treat all as unsigned.
                    dataType = DataBuffer.TYPE_BYTE;
                    isValidDataFormat = true;
                }
                break;
            case 16:
                if (sampleFormat[0] != 3) {
                    dataType = sampleFormat[0] == 2 ? DataBuffer.TYPE_SHORT : DataBuffer.TYPE_USHORT;
                    isValidDataFormat = true;
                }
                break;
            case 32:
                if (sampleFormat[0] == 3) {
                    isValidDataFormat = false;
                } else {
                    dataType = DataBuffer.TYPE_INT;
                    isValidDataFormat = true;
                }
                break;
        }
        if (!isValidDataFormat) {
            throw new RuntimeException(PropertyUtil.getString("TIFFImage3"));
        }
        TIFFField compField = null;
        compression = compField == null ? TIFFImage.COMP_NONE : compField.getAsInt(0);
        TIFFField photometricTypeField = null;
        if (photometricTypeField == null) {
            // White is zero
            photometricType = 0;
        } else {
            photometricType = photometricTypeField.getAsInt(0);
        }
        imageType = TIFFImage.TYPE_UNSUPPORTED;
        switch(photometricType) {
            case // WhiteIsZero
            0:
                isWhiteZero = true;
            case // BlackIsZero
            1:
                if (sampleSize == 1 && samplesPerPixel == 1) {
                    imageType = TIFFImage.TYPE_BILEVEL;
                } else if (sampleSize == 4 && samplesPerPixel == 1) {
                    imageType = TIFFImage.TYPE_GRAY_4BIT;
                } else if (sampleSize % 8 == 0) {
                    if (samplesPerPixel == 1) {
                        imageType = TIFFImage.TYPE_GRAY;
                    } else if (samplesPerPixel == 2) {
                        imageType = TIFFImage.TYPE_GRAY_ALPHA;
                    } else {
                        imageType = TIFFImage.TYPE_GENERIC;
                    }
                }
                break;
            case // RGB
            2:
                if (sampleSize % 8 == 0) {
                    if (samplesPerPixel == 3) {
                        imageType = TIFFImage.TYPE_RGB;
                    } else if (samplesPerPixel == 4) {
                        imageType = TIFFImage.TYPE_RGB_ALPHA;
                    } else {
                        imageType = TIFFImage.TYPE_GENERIC;
                    }
                }
                break;
            case // RGB Palette
            3:
                if (samplesPerPixel == 1 && (sampleSize == 4 || sampleSize == 8 || sampleSize == 16)) {
                    imageType = TIFFImage.TYPE_PALETTE;
                }
                break;
            case // Transparency mask
            4:
                if (sampleSize == 1 && samplesPerPixel == 1) {
                    imageType = TIFFImage.TYPE_BILEVEL;
                }
                break;
            default:
                // Other including CMYK, CIE L*a*b*, unknown.
                if (sampleSize % 8 == 0) {
                    imageType = TIFFImage.TYPE_GENERIC;
                }
        }
        if (imageType == TIFFImage.TYPE_UNSUPPORTED) {
            throw new RuntimeException(PropertyUtil.getString("TIFFImage4") + ": " + imageType);
        }
        Rectangle bounds = new Rectangle(0, 0, (int) dir.getFieldAsLong(TIFFImageDecoder.TIFF_IMAGE_WIDTH), (int) dir.getFieldAsLong(TIFFImageDecoder.TIFF_IMAGE_LENGTH));
        numBands = samplesPerPixel;
        TIFFField efield = null;
        int extraSamples = efield == null ? 0 : (int) efield.getAsLong(0);
        if (null != null) {
            tiled = true;
            // Image is in tiled format
            tileWidth = (int) dir.getFieldAsLong(TIFFImageDecoder.TIFF_TILE_WIDTH);
            tileHeight = (int) dir.getFieldAsLong(TIFFImageDecoder.TIFF_TILE_LENGTH);
            tileOffsets = (null).getAsLongs();
            tileByteCounts = getFieldAsLongs(null);
        } else {
            tiled = false;
            // we default here to the tile dimensions if they are written.
            tileWidth = null != null ? (int) dir.getFieldAsLong(TIFFImageDecoder.TIFF_TILE_WIDTH) : bounds.width;
            TIFFField field = null;
            if (field == null) {
                // Default is infinity (2^32 -1), basically the entire image
                tileHeight = null != null ? (int) dir.getFieldAsLong(TIFFImageDecoder.TIFF_TILE_LENGTH) : bounds.height;
            } else {
                long l = field.getAsLong(0);
                long infinity = 1;
                infinity = (infinity << 32) - 1;
                if (l == infinity) {
                    // 2^32 - 1 (effectively infinity, entire image is 1 strip)
                    tileHeight = bounds.height;
                } else {
                    tileHeight = (int) l;
                }
            }
            TIFFField tileOffsetsField = null;
            if (tileOffsetsField == null) {
                throw new RuntimeException(PropertyUtil.getString("TIFFImage5"));
            } else {
                tileOffsets = getFieldAsLongs(tileOffsetsField);
            }
            TIFFField tileByteCountsField = null;
            if (tileByteCountsField == null) {
                throw new RuntimeException(PropertyUtil.getString("TIFFImage6"));
            } else {
                tileByteCounts = getFieldAsLongs(tileByteCountsField);
            }
        }
        tilesX = (bounds.width + tileWidth - 1) / tileWidth;
        tilesY = (bounds.height + tileHeight - 1) / tileHeight;
        tileSize = tileWidth * tileHeight * numBands;
        isBigEndian = dir.isBigEndian();
        TIFFField fillOrderField = null;
        if (fillOrderField != null) {
            fillOrder = fillOrderField.getAsInt(0);
        } else {
            // Default Fill Order
            fillOrder = 1;
        }
        switch(compression) {
            case TIFFImage.COMP_NONE:
            case TIFFImage.COMP_PACKBITS:
                // Do nothing.
                break;
            case TIFFImage.COMP_DEFLATE:
                inflater = new Inflater();
                break;
            case TIFFImage.COMP_FAX_G3_1D:
            case TIFFImage.COMP_FAX_G3_2D:
            case TIFFImage.COMP_FAX_G4_2D:
                if (sampleSize != 1) {
                    throw new RuntimeException(PropertyUtil.getString("TIFFImage7"));
                }
                // Fax T.4 compression options
                if (compression == 3) {
                    TIFFField t4OptionsField = null;
                    if (t4OptionsField != null) {
                        tiffT4Options = t4OptionsField.getAsLong(0);
                    } else {
                        // Use default value
                        tiffT4Options = 0;
                    }
                }
                // Fax T.6 compression options
                if (compression == 4) {
                    TIFFField t6OptionsField = null;
                    if (t6OptionsField != null) {
                        tiffT6Options = t6OptionsField.getAsLong(0);
                    } else {
                        // Use default value
                        tiffT6Options = 0;
                    }
                }
                // Fax encoding, need to create the Fax decoder.
                decoder = new TIFFFaxDecoder(fillOrder, tileWidth, tileHeight);
                break;
            case TIFFImage.COMP_LZW:
                TIFFField predictorField = null;
                if (predictorField == null) {
                    predictor = 1;
                } else {
                    predictor = predictorField.getAsInt(0);
                    if (predictor != 1 && predictor != 2) {
                        throw new RuntimeException(PropertyUtil.getString("TIFFImage8"));
                    }
                    if (predictor == 2 && sampleSize != 8) {
                        throw new RuntimeException(PropertyUtil.getString("TIFFImage9"));
                    }
                }
                lzwDecoder = new TIFFLZWDecoder(tileWidth, predictor, samplesPerPixel);
                break;
            case TIFFImage.COMP_JPEG_OLD:
                throw new RuntimeException(PropertyUtil.getString("TIFFImage15"));
            default:
                throw new RuntimeException(PropertyUtil.getString("TIFFImage10") + ": " + compression);
        }
        ColorModel colorModel = null;
        SampleModel sampleModel = null;
        switch(imageType) {
            case TIFFImage.TYPE_BILEVEL:
            case TIFFImage.TYPE_GRAY_4BIT:
                sampleModel = new MultiPixelPackedSampleModel(dataType, tileWidth, tileHeight, sampleSize);
                if (imageType == TIFFImage.TYPE_BILEVEL) {
                    byte[] map = new byte[] { (byte) (isWhiteZero ? 255 : 0), (byte) (isWhiteZero ? 0 : 255) };
                    colorModel = new IndexColorModel(1, 2, map, map, map);
                } else {
                    byte[] map = new byte[16];
                    if (isWhiteZero) {
                        for (int i = 0; i < map.length; i++) {
                            map[i] = (byte) (255 - (16 * i));
                        }
                    } else {
                        for (int i = 0; i < map.length; i++) {
                            map[i] = (byte) (16 * i);
                        }
                    }
                    colorModel = new IndexColorModel(4, 16, map, map, map);
                }
                break;
            case TIFFImage.TYPE_GRAY:
            case TIFFImage.TYPE_GRAY_ALPHA:
            case TIFFImage.TYPE_RGB:
            case TIFFImage.TYPE_RGB_ALPHA:
                int[] reverseOffsets = new int[numBands];
                // Create a pixel interleaved SampleModel with decreasing
                for (int i = 0; i < numBands; i++) {
                    reverseOffsets[i] = numBands - 1 - i;
                }
                sampleModel = new PixelInterleavedSampleModel(dataType, tileWidth, tileHeight, numBands, numBands * tileWidth, reverseOffsets);
                if (imageType == TIFFImage.TYPE_GRAY) {
                    colorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_GRAY), new int[] { sampleSize }, false, false, Transparency.OPAQUE, dataType);
                } else if (imageType == TIFFImage.TYPE_RGB) {
                    colorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] { sampleSize, sampleSize, sampleSize }, false, false, Transparency.OPAQUE, dataType);
                } else {
                    int transparency = Transparency.OPAQUE;
                    if (extraSamples == 1) {
                        // associated (premultiplied) alpha
                        transparency = Transparency.TRANSLUCENT;
                    } else if (extraSamples == 2) {
                        // unassociated alpha
                        transparency = Transparency.BITMASK;
                    }
                    colorModel = createAlphaComponentColorModel(dataType, numBands, extraSamples == 1, transparency);
                }
                break;
            case TIFFImage.TYPE_GENERIC:
            case TIFFImage.TYPE_YCBCR_SUB:
                int[] bandOffsets = new int[numBands];
                // For this case we can't display the image, so we create a
                // SampleModel with increasing bandOffsets, and keep the
                for (int i = 0; i < numBands; i++) {
                    bandOffsets[i] = i;
                }
                sampleModel = new PixelInterleavedSampleModel(dataType, tileWidth, tileHeight, numBands, numBands * tileWidth, bandOffsets);
                colorModel = null;
                break;
            case TIFFImage.TYPE_PALETTE:
                TIFFField cfield = null;
                if (cfield == null) {
                    throw new RuntimeException(PropertyUtil.getString("TIFFImage11"));
                } else {
                    colormap = cfield.getAsChars();
                }
                // Could be either 1 or 3 bands depending on whether we use
                // IndexColorModel or not.
                if (decodePaletteAsShorts) {
                    numBands = 3;
                    // be ushort.
                    if (dataType == DataBuffer.TYPE_BYTE) {
                        dataType = DataBuffer.TYPE_USHORT;
                    }
                    // a colormodel whose entries are of short data type.
                    sampleModel = TIFFImage.createPixelInterleavedSampleModel(dataType, tileWidth, tileHeight, numBands);
                    colorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] { 16, 16, 16 }, false, false, Transparency.OPAQUE, dataType);
                } else {
                    numBands = 1;
                    if (sampleSize == 4) {
                        // IndexColorModel to do the unpacking.
                        sampleModel = new MultiPixelPackedSampleModel(DataBuffer.TYPE_BYTE, tileWidth, tileHeight, sampleSize);
                    } else if (sampleSize == 8) {
                        sampleModel = TIFFImage.createPixelInterleavedSampleModel(DataBuffer.TYPE_BYTE, tileWidth, tileHeight, numBands);
                    } else if (sampleSize == 16) {
                        // negative.
                        dataType = DataBuffer.TYPE_USHORT;
                        sampleModel = TIFFImage.createPixelInterleavedSampleModel(DataBuffer.TYPE_USHORT, tileWidth, tileHeight, numBands);
                    }
                    int bandLength = colormap.length / 3;
                    byte[] r = new byte[bandLength];
                    byte[] g = new byte[bandLength];
                    byte[] b = new byte[bandLength];
                    int gIndex = bandLength;
                    int bIndex = bandLength * 2;
                    if (dataType == DataBuffer.TYPE_SHORT) {
                        for (int i = 0; i < bandLength; i++) {
                            r[i] = param.decodeSigned16BitsTo8Bits((short) colormap[i]);
                            g[i] = param.decodeSigned16BitsTo8Bits((short) colormap[gIndex + i]);
                            b[i] = param.decodeSigned16BitsTo8Bits((short) colormap[bIndex + i]);
                        }
                    } else {
                        for (int i = 0; i < bandLength; i++) {
                            r[i] = param.decode16BitsTo8Bits(colormap[i] & 0xffff);
                            g[i] = param.decode16BitsTo8Bits(colormap[gIndex + i] & 0xffff);
                            b[i] = param.decode16BitsTo8Bits(colormap[bIndex + i] & 0xffff);
                        }
                    }
                    colorModel = new IndexColorModel(sampleSize, bandLength, r, g, b);
                }
                break;
            default:
                throw new RuntimeException(PropertyUtil.getString("TIFFImage4") + ": " + imageType);
        }
        Map properties = new HashMap();
        properties.put("tiff_directory", dir);
        init((CachableRed) null, bounds, colorModel, sampleModel, 0, 0, properties);
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
            sdata[count++] = (short) (cmapValue & 0xffff);
        }
        assertTrue(sdata[0] == 11 && sdata[1] == 4 && sdata[2] == 1 && sdata[3] == 12);
    }
}
