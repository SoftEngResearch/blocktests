package org.apdplat.word.dictionary;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.apdplat.word.dictionary.impl.DictionaryTrie;
import org.apdplat.word.recognition.PersonName;
import org.apdplat.word.util.AutoDetector;
import org.apdplat.word.util.ResourceLoader;
import org.apdplat.word.util.WordConfTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.apdplat.word.dictionary.DictionaryFactory.*;

public class DictionaryFactoryBlockTest {

    @Test
    public void testLine217() throws Exception {
        java.lang.String word = "";
        Dictionary dictionary = DictionaryFactory.getDictionary();
        AtomicInteger e = new AtomicInteger();
        AtomicInteger h = new AtomicInteger();
        for (int j = 0; j < word.length(); j++) {
            String sw = word.substring(0, j + 1);
            for (int k = 0; k < sw.length(); k++) {
                if (dictionary.contains(sw, k, sw.length() - k)) {
                    h.incrementAndGet();
                } else {
                    e.incrementAndGet();
                }
            }
        }
        assertEquals(6, e.get());
        assertEquals(4, h.get());
    }

    @Test
    public void testLine218() throws Exception {
        java.lang.String word = "";
        Dictionary dictionary = DictionaryFactory.getDictionary();
        AtomicInteger e = new AtomicInteger();
        AtomicInteger h = new AtomicInteger();
        for (int j = 0; j < word.length(); j++) {
            String sw = word.substring(0, j + 1);
            for (int k = 0; k < sw.length(); k++) {
                if (dictionary.contains(sw, k, sw.length() - k)) {
                    h.incrementAndGet();
                } else {
                    e.incrementAndGet();
                }
            }
        }
        assertEquals(2, e.get());
        assertEquals(1, h.get());
    }
}
