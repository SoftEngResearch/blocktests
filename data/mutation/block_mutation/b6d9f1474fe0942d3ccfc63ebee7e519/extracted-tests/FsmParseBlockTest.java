package MorphologicalAnalysis;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import Dictionary.TxtWord;
import Dictionary.Word;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static MorphologicalAnalysis.FsmParse.*;

public class FsmParseBlockTest {

    @Test
    public void testLine659() throws Exception {
        ArrayList<State> suffixList = new ArrayList<>(Arrays.asList(new State("OrdinalRoot", true, true)));
        ArrayList<String> formList = new ArrayList<>(Arrays.asList("foo"));
        String result = null;
        if (suffixList.get(0).getName().equalsIgnoreCase("OrdinalRoot")) {
            result = formList.get(0) + "+NUM+ORD";
        } else {
            if (suffixList.get(0).getName().startsWith("Adjective")) {
            }
        }
        assertEquals("foo+NUM+ORD", result);
    }

    @Test
    public void testLine663() throws Exception {
        ArrayList<State> suffixList = new ArrayList<>(Arrays.asList(new State("bar", true, true)));
        ArrayList<String> formList = new ArrayList<>(Arrays.asList("bar"));
        String result = null;
        if (suffixList.get(0).getName().equalsIgnoreCase("OrdinalRoot")) {
            result = formList.get(0) + "+NUM+ORD";
        } else {
            if (suffixList.get(0).getName().startsWith("Adjective")) {
            }
        }
        assertEquals(null, result);
    }

    @Test
    public void testLine667() throws Exception {
        ArrayList<State> suffixList = new ArrayList<>(Arrays.asList(new State("AdjectiveBlahblah", true, true)));
        ArrayList<String> formList = new ArrayList<>(Arrays.asList("baz"));
        String result = null;
        if (suffixList.get(0).getName().equalsIgnoreCase("OrdinalRoot")) {
            result = formList.get(0) + "+NUM+ORD";
        } else {
            if (suffixList.get(0).getName().startsWith("Adjective")) {
            }
        }
        assertEquals("baz+ADJ", result);
    }

    @Test
    public void testLine634() throws Exception {
        ArrayList<String> formList = new ArrayList<>(Collections.singletonList("birlikte"));
        String result = null;
        if (formList.get(0).equalsIgnoreCase("birlikte") || formList.get(0).equalsIgnoreCase("beraber")) {
            result = formList.get(0) + "+POSTP+PCINS";
        } else {
            if (formList.get(0).equalsIgnoreCase("akn") || formList.get(0).equalsIgnoreCase("takiben")) {
                result = formList.get(0) + "+POSTP+PCACC";
            } else {
                result = formList.get(0) + "+POSTP+PCNOM";
            }
        }
        assertEquals("birlikte+POSTP+PCINS", result);
    }

    @Test
    public void testLine636() throws Exception {
        ArrayList<String> formList = new ArrayList<>(Collections.singletonList("beraber"));
        String result = null;
        if (formList.get(0).equalsIgnoreCase("birlikte") || formList.get(0).equalsIgnoreCase("beraber")) {
            result = formList.get(0) + "+POSTP+PCINS";
        } else {
            if (formList.get(0).equalsIgnoreCase("akn") || formList.get(0).equalsIgnoreCase("takiben")) {
                result = formList.get(0) + "+POSTP+PCACC";
            } else {
                result = formList.get(0) + "+POSTP+PCNOM";
            }
        }
        assertEquals("beraber+POSTP+PCINS", result);
    }

    @Test
    public void testLine638() throws Exception {
        ArrayList<String> formList = new ArrayList<>(Collections.singletonList("akn"));
        String result = null;
        if (formList.get(0).equalsIgnoreCase("birlikte") || formList.get(0).equalsIgnoreCase("beraber")) {
            result = formList.get(0) + "+POSTP+PCINS";
        } else {
            if (formList.get(0).equalsIgnoreCase("akn") || formList.get(0).equalsIgnoreCase("takiben")) {
                result = formList.get(0) + "+POSTP+PCACC";
            } else {
                result = formList.get(0) + "+POSTP+PCNOM";
            }
        }
        assertEquals("akn+POSTP+PCACC", result);
    }

    @Test
    public void testLine640() throws Exception {
        ArrayList<String> formList = new ArrayList<>(Collections.singletonList("takiben"));
        String result = null;
        if (formList.get(0).equalsIgnoreCase("birlikte") || formList.get(0).equalsIgnoreCase("beraber")) {
            result = formList.get(0) + "+POSTP+PCINS";
        } else {
            if (formList.get(0).equalsIgnoreCase("akn") || formList.get(0).equalsIgnoreCase("takiben")) {
                result = formList.get(0) + "+POSTP+PCACC";
            } else {
                result = formList.get(0) + "+POSTP+PCNOM";
            }
        }
        assertEquals("takiben+POSTP+PCACC", result);
    }

    @Test
    public void testLine642() throws Exception {
        ArrayList<String> formList = new ArrayList<>(Collections.singletonList("foo"));
        String result = null;
        if (formList.get(0).equalsIgnoreCase("birlikte") || formList.get(0).equalsIgnoreCase("beraber")) {
            result = formList.get(0) + "+POSTP+PCINS";
        } else {
            if (formList.get(0).equalsIgnoreCase("akn") || formList.get(0).equalsIgnoreCase("takiben")) {
                result = formList.get(0) + "+POSTP+PCACC";
            } else {
                result = formList.get(0) + "+POSTP+PCNOM";
            }
        }
        assertEquals("foo+POSTP+PCNOM", result);
    }
}
