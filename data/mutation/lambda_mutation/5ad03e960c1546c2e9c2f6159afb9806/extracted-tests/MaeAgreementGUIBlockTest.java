package edu.brandeis.llc.mae.agreement.view;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import edu.brandeis.llc.mae.MaeException;
import edu.brandeis.llc.mae.MaeStrings;
import edu.brandeis.llc.mae.agreement.MaeAgreementMain;
import edu.brandeis.llc.mae.agreement.MaeAgreementStrings;
import edu.brandeis.llc.mae.database.LocalSqliteDriverImpl;
import edu.brandeis.llc.mae.database.MaeDBException;
import edu.brandeis.llc.mae.database.MaeDriverI;
import edu.brandeis.llc.mae.io.MaeIOException;
import edu.brandeis.llc.mae.util.MappedSet;
import org.xml.sax.SAXException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import static edu.brandeis.llc.mae.agreement.MaeAgreementStrings.ALL_METRIC_TYPE_STRINGS;
import org.junit.Test;
import static org.junit.Assert.*;
import static edu.brandeis.llc.mae.agreement.view.MaeAgreementGUI.*;

public class MaeAgreementGUIBlockTest {

    @Test(expected = NullPointerException.class)
    public void testLine1() throws Exception {
        String annotatorID = "foo";
        MaeAgreementMain calc = new MaeAgreementMain(new LocalSqliteDriverImpl(MaeStrings.newTempTestDBFile()));
        java.awt.event.ItemEvent e = new ItemEvent(new java.awt.List(), 5, "String", 2);
        if (e.getStateChange() == ItemEvent.DESELECTED) {
            calc.ignoreAnnotator(annotatorID);
        } else {
        }
    }
}
