package com.github.jaiimageio.impl.plugins.bmp;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.SampleModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import javax.imageio.ImageWriteParam;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.metadata.IIOMetadataNode;
import org.w3c.dom.Node;
import com.github.jaiimageio.impl.common.ImageUtil;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.github.jaiimageio.impl.plugins.bmp.BMPMetadata.*;

public class BMPMetadataBlockTest {

    @Test
    public void testLine1016() throws Exception {
        boolean gotWidth = false;
        boolean gotHeight = false;
        boolean gotSpaceX = true;
        boolean gotSpaceY = true;
        boolean gotAspectRatio = true;
        double aspectRatio = 0.0;
        int xPixelsPerMeter = 0;
        int yPixelsPerMeter = 0;
        double spaceY = -1.0F;
        double spaceX = -1.0F;
        double width = -1.0F;
        double height = -1.0F;
        if (!(gotWidth || gotHeight) && (gotSpaceX || gotSpaceY)) {
            width = spaceX;
            gotWidth = gotSpaceX;
            height = spaceY;
            gotHeight = gotSpaceY;
        }
        if (gotWidth && gotHeight) {
            xPixelsPerMeter = (int) (1000.0 / width + 0.5);
            yPixelsPerMeter = (int) (1000.0 / height + 0.5);
        } else if (gotAspectRatio && aspectRatio != 0.0) {
            if (gotWidth) {
                xPixelsPerMeter = (int) (1000.0 / width + 0.5);
                yPixelsPerMeter = (int) (aspectRatio * (1000.0 / width) + 0.5);
            } else if (gotHeight) {
                xPixelsPerMeter = (int) (1000.0 / height / aspectRatio + 0.5);
                yPixelsPerMeter = (int) (1000.0 / height + 0.5);
            }
        }
        assertEquals(-999.0, yPixelsPerMeter, Double.MIN_VALUE);
        assertEquals(-999.0, xPixelsPerMeter, Double.MIN_VALUE);
    }

    @Test
    public void testLine1023() throws Exception {
        boolean gotWidth = true;
        boolean gotHeight = false;
        boolean gotSpaceX = true;
        boolean gotSpaceY = true;
        boolean gotAspectRatio = true;
        double aspectRatio = -1.0;
        int xPixelsPerMeter = 0;
        int yPixelsPerMeter = 0;
        double spaceY = -1.0F;
        double spaceX = -1.0F;
        double width = -1.0F;
        double height = -1.0F;
        if (!(gotWidth || gotHeight) && (gotSpaceX || gotSpaceY)) {
            width = spaceX;
            gotWidth = gotSpaceX;
            height = spaceY;
            gotHeight = gotSpaceY;
        }
        if (gotWidth && gotHeight) {
            xPixelsPerMeter = (int) (1000.0 / width + 0.5);
            yPixelsPerMeter = (int) (1000.0 / height + 0.5);
        } else if (gotAspectRatio && aspectRatio != 0.0) {
            if (gotWidth) {
                xPixelsPerMeter = (int) (1000.0 / width + 0.5);
                yPixelsPerMeter = (int) (aspectRatio * (1000.0 / width) + 0.5);
            } else if (gotHeight) {
                xPixelsPerMeter = (int) (1000.0 / height / aspectRatio + 0.5);
                yPixelsPerMeter = (int) (1000.0 / height + 0.5);
            }
        }
        assertEquals(1000.0, yPixelsPerMeter, Double.MIN_VALUE);
        assertEquals(-999.0, xPixelsPerMeter, Double.MIN_VALUE);
    }

    @Test
    public void testLine958() throws Exception {
        String s = "1\n2";
        List bps = new ArrayList(4);
        int[] bitsPerSample = null;
        StringTokenizer t = new StringTokenizer(s);
        while (t.hasMoreTokens()) {
            bps.add(Integer.valueOf(t.nextToken()));
        }
        bitsPerSample = new int[bps.size()];
        for (int i = 0; i < bitsPerSample.length; i++) {
            bitsPerSample[i] = ((Integer) bps.get(i)).intValue();
            continue;
        }
        assertEquals(1, bitsPerSample[0]);
    }

    @Test
    public void testLine959() throws Exception {
        String s = "";
        List bps = new ArrayList(4);
        int[] bitsPerSample = null;
        StringTokenizer t = new StringTokenizer(s);
        while (t.hasMoreTokens()) {
            bps.add(Integer.valueOf(t.nextToken()));
        }
        bitsPerSample = new int[bps.size()];
        for (int i = 0; i < bitsPerSample.length; i++) {
            bitsPerSample[i] = ((Integer) bps.get(i)).intValue();
            continue;
        }
        assertEquals(0, bitsPerSample.length);
    }
}
