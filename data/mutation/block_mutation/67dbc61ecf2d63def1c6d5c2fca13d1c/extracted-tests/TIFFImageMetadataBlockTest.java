package com.github.jaiimageio.impl.plugins.tiff;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.github.jaiimageio.plugins.tiff.BaselineTIFFTagSet;
import com.github.jaiimageio.plugins.tiff.EXIFParentTIFFTagSet;
import com.github.jaiimageio.plugins.tiff.TIFFField;
import com.github.jaiimageio.plugins.tiff.TIFFTag;
import com.github.jaiimageio.plugins.tiff.TIFFTagSet;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.github.jaiimageio.impl.plugins.tiff.TIFFImageMetadata.*;

public class TIFFImageMetadataBlockTest {

    @Test
    public void testLine894() throws Exception {
        Node entry = null;
        int maxIndex = 3;
        HashMap palette = new HashMap();
        String idx = "5";
        int id = Integer.parseInt(idx);
        if (id > maxIndex) {
            maxIndex = id;
        }
        char red = (char) Integer.parseInt("255");
        char green = (char) Integer.parseInt("0");
        char blue = (char) Integer.parseInt("0");
        palette.put(new Integer(id), new char[] { red, green, blue });
        assertEquals(5, maxIndex);
        assertEquals(255, ((char[]) palette.get(5))[0]);
        assertTrue(gotPalette);
    }
}
