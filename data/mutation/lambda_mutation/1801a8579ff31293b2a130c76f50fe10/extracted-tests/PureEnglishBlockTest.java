package org.apdplat.word.segmentation.impl;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.apdplat.word.segmentation.Segmentation;
import org.apdplat.word.segmentation.SegmentationAlgorithm;
import org.apdplat.word.segmentation.Word;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.apdplat.word.segmentation.impl.PureEnglish.*;

public class PureEnglishBlockTest {

    @Test
    public void testLine88() throws Exception {
        java.lang.String w = "foo";
        List<Word> segResult = new ArrayList<>();
        try {
            if (w.length() < 2) {
                return;
            }
            w = PureEnglish.irregularity(w);
            if (w != null) {
                segResult.add(new Word(w));
            }
        } finally {
            assertEquals("foo", segResult.iterator().next().toString());
        }
    }

    @Test
    public void testLine89() throws Exception {
        java.lang.String w = "f";
        List<Word> segResult = new ArrayList<>();
        try {
            if (w.length() < 2) {
                return;
            }
            w = PureEnglish.irregularity(w);
            if (w != null) {
                segResult.add(new Word(w));
            }
        } finally {
            assertEquals(0, segResult.size());
        }
    }

    @Test
    public void testLine90() throws Exception {
        java.lang.String w = "1foo";
        List<Word> segResult = new ArrayList<>();
        try {
            if (w.length() < 2) {
                return;
            }
            w = PureEnglish.irregularity(w);
            if (w != null) {
                segResult.add(new Word(w));
            }
        } finally {
            assertEquals(0, segResult.size());
        }
    }

    @Test
    public void testLine91() throws Exception {
        java.lang.String w = "br";
        List<Word> segResult = new ArrayList<>();
        try {
            if (w.length() < 2) {
                return;
            }
            w = PureEnglish.irregularity(w);
            if (w != null) {
                segResult.add(new Word(w));
            }
        } finally {
            assertEquals(0, segResult.size());
        }
    }

    @Test
    public void testLine92() throws Exception {
        java.lang.String w = "xr";
        List<Word> segResult = new ArrayList<>();
        try {
            if (w.length() < 2) {
                return;
            }
            w = PureEnglish.irregularity(w);
            if (w != null) {
                segResult.add(new Word(w));
            }
        } finally {
            assertEquals("xr", segResult.iterator().next().toString());
        }
    }
}
