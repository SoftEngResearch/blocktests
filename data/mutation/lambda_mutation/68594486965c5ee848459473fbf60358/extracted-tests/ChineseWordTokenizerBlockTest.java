package org.apdplat.word.lucene;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apdplat.word.segmentation.Segmentation;
import org.apdplat.word.segmentation.SegmentationAlgorithm;
import org.apdplat.word.segmentation.SegmentationFactory;
import org.apdplat.word.recognition.StopWord;
import org.apdplat.word.segmentation.Word;
import org.apdplat.word.tagging.AntonymTagging;
import org.apdplat.word.tagging.PinyinTagging;
import org.apdplat.word.tagging.SynonymTagging;
import org.apdplat.word.util.WordConfTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.apdplat.word.lucene.ChineseWordTokenizer.*;

public class ChineseWordTokenizerBlockTest {

    @Test
    public void testLine145() throws Exception {
        Word w = new Word("foo");
        Queue<String> tokens = new LinkedTransferQueue<>();
        if (1 == 1) {
            tokens.offer(w.getText());
        }
        assertEquals("foo", tokens.poll());
    }

    @Test
    public void testLine146() throws Exception {
        Word w = new Word("");
        Queue<String> tokens = new LinkedTransferQueue<>();
        if (1 == 1) {
            tokens.offer(w.getText());
        }
        assertTrue(tokens.isEmpty());
    }

    @Test
    public void testLine157() throws Exception {
        Word w = new Word("foo");
        Queue<String> tokens = new LinkedTransferQueue<>();
        if (!"".equals(w.getText())) {
            tokens.offer(w.getText());
        }
        assertEquals("foo", tokens.poll());
    }

    @Test
    public void testLine158() throws Exception {
        Word w = new Word("");
        Queue<String> tokens = new LinkedTransferQueue<>();
        if (!"".equals(w.getText())) {
            tokens.offer(w.getText());
        }
        assertTrue(tokens.isEmpty());
    }
}
