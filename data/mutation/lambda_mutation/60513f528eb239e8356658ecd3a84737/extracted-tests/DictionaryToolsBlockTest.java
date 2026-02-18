package org.apdplat.word.dictionary;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.apdplat.word.recognition.RecognitionTool;
import org.apdplat.word.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.apdplat.word.dictionary.DictionaryTools.*;

public class DictionaryToolsBlockTest {

    @Test
    public void testLine107() throws Exception {
        AtomicInteger i = new AtomicInteger(0);
        java.lang.String line = "a   ";
        Set<String> set = new HashSet<String>();
        try {
            i.incrementAndGet();
            line = line.trim();
            if (line.length() > 4 || line.length() < 2 || !Utils.isChineseCharAndLengthAtLeastTwo(line) || RecognitionTool.recog(line)) {
                DictionaryTools.LOGGER.debug("" + line);
                return;
            }
            set.add(line);
        } finally {
            assertTrue(set.size() == 0);
        }
    }

    @Test
    public void testLine108() throws Exception {
        AtomicInteger i = new AtomicInteger(0);
        java.lang.String line = "   abcde";
        Set<String> set = new HashSet<String>();
        try {
            i.incrementAndGet();
            line = line.trim();
            if (line.length() > 4 || line.length() < 2 || !Utils.isChineseCharAndLengthAtLeastTwo(line) || RecognitionTool.recog(line)) {
                DictionaryTools.LOGGER.debug("" + line);
                return;
            }
            set.add(line);
        } finally {
            assertTrue(set.size() == 0);
        }
    }

    @Test
    public void testLine109() throws Exception {
        AtomicInteger i = new AtomicInteger(0);
        java.lang.String line = "  ";
        Set<String> set = new HashSet<String>();
        try {
            i.incrementAndGet();
            line = line.trim();
            if (line.length() > 4 || line.length() < 2 || !Utils.isChineseCharAndLengthAtLeastTwo(line) || RecognitionTool.recog(line)) {
                DictionaryTools.LOGGER.debug("" + line);
                return;
            }
            set.add(line);
        } finally {
            assertTrue(set.iterator().next().equals(""));
        }
    }

    @Test
    public void testLine110() throws Exception {
        AtomicInteger i = new AtomicInteger(0);
        java.lang.String line = "";
        Set<String> set = new HashSet<String>();
        try {
            i.incrementAndGet();
            line = line.trim();
            if (line.length() > 4 || line.length() < 2 || !Utils.isChineseCharAndLengthAtLeastTwo(line) || RecognitionTool.recog(line)) {
                DictionaryTools.LOGGER.debug("" + line);
                return;
            }
            set.add(line);
        } finally {
            assertTrue(set.iterator().next().equals(""));
        }
    }

    @Test
    public void testLine111() throws Exception {
        AtomicInteger i = new AtomicInteger(0);
        java.lang.String line = "  ";
        Set<String> set = new HashSet<String>();
        try {
            i.incrementAndGet();
            line = line.trim();
            if (line.length() > 4 || line.length() < 2 || !Utils.isChineseCharAndLengthAtLeastTwo(line) || RecognitionTool.recog(line)) {
                DictionaryTools.LOGGER.debug("" + line);
                return;
            }
            set.add(line);
        } finally {
            assertTrue(set.size() == 0);
        }
    }

    @Test
    public void testLine112() throws Exception {
        AtomicInteger i = new AtomicInteger(0);
        java.lang.String line = "a";
        Set<String> set = new HashSet<String>();
        try {
            i.incrementAndGet();
            line = line.trim();
            if (line.length() > 4 || line.length() < 2 || !Utils.isChineseCharAndLengthAtLeastTwo(line) || RecognitionTool.recog(line)) {
                DictionaryTools.LOGGER.debug("" + line);
                return;
            }
            set.add(line);
        } finally {
            assertTrue(set.size() == 0);
        }
    }
}
