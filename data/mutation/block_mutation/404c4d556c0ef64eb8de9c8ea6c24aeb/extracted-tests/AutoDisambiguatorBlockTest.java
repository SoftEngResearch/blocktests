package MorphologicalDisambiguation;

import Corpus.Sentence;
import DataStructure.CounterHashMap;
import MorphologicalAnalysis.FsmMorphologicalAnalyzer;
import MorphologicalAnalysis.FsmParse;
import MorphologicalAnalysis.FsmParseList;
import MorphologicalAnalysis.MorphologicalTag;
import java.util.ArrayList;
import org.blocktest.BTest;
import org.blocktest.types.Flow;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static MorphologicalDisambiguation.AutoDisambiguator.*;

public class AutoDisambiguatorBlockTest {

    @Test
    public void test1() throws Exception {
        int index = 1;
        FsmParseList[] fsmParses;
        Sentence sentence = new Sentence("Yarn doktora gidecekler");
        FsmMorphologicalAnalyzer fsm = new FsmMorphologicalAnalyzer();
        fsmParses = fsm.robustMorphologicalAnalysis(sentence);
        try {
            if (index > 0) {
                if (fsmParses[index].getFsmParse(0).isCapitalWord()) {
                    {
                        assertEquals("A3PL+PNON+NOM", ("PROP+A3PL+PNON+NOM"));
                        return;
                    }
                }
                {
                    assertEquals("A3PL+PNON+NOM", ("A3PL+PNON+NOM"));
                    return;
                }
            }
        } finally {
        }
    }

    @Test
    public void test2() throws Exception {
        int index = 1;
        FsmParseList[] fsmParses;
        Sentence sentence = new Sentence("Yarn Telerate gidecekler");
        FsmMorphologicalAnalyzer fsm = new FsmMorphologicalAnalyzer();
        fsmParses = fsm.robustMorphologicalAnalysis(sentence);
        try {
            if (index > 0) {
                if (fsmParses[index].getFsmParse(0).isCapitalWord()) {
                    {
                        assertEquals("PROP+A3PL+PNON+NOM", ("PROP+A3PL+PNON+NOM"));
                        return;
                    }
                }
                {
                    assertEquals("PROP+A3PL+PNON+NOM", ("A3PL+PNON+NOM"));
                    return;
                }
            }
        } finally {
        }
    }

    @Test
    public void test3() throws Exception {
        int index = 0;
        FsmParseList[] fsmParses;
        Sentence sentence = new Sentence("Yarn Doktora gidecekler");
        FsmMorphologicalAnalyzer fsm = new FsmMorphologicalAnalyzer();
        fsmParses = fsm.robustMorphologicalAnalysis(sentence);
        boolean[] _blocktest_flow_test3 = new boolean[0];
        try {
            if (index > 0) {
                if (fsmParses[index].getFsmParse(0).isCapitalWord()) {
                    return;
                }
                return;
            }
        } finally {
            for (int _blocktest_test3_i = 0; _blocktest_test3_i < 0; _blocktest_test3_i += 1) {
                assertTrue(_blocktest_flow_test3[_blocktest_test3_i]);
            }
        }
    }
}
