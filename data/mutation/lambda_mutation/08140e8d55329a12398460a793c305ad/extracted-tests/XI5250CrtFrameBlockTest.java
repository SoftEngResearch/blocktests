package net.infordata.em.crt5250;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.util.ResourceBundle;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import net.infordata.em.tn5250.XI5250EmulatorCtrl;
import net.infordata.em.util.XICommand;
import net.infordata.em.util.XICommandMgr;
import net.infordata.em.util.XIUtil;
import org.junit.Test;
import static org.junit.Assert.*;
import static net.infordata.em.crt5250.XI5250CrtFrame.*;

public class XI5250CrtFrameBlockTest {

    @Test
    public void testLine164() throws Exception {
        boolean ivFirstTime = true;
        boolean ivSizeControlledFrame = false;
        try {
            if (ivFirstTime || ivSizeControlledFrame) {
                ivFirstTime = false;
            }
        } finally {
        }
        assertFalse(ivPending);
        assertFalse(ivSizeControlledFrame);
        assertFalse(ivFirstTime);
    }

    @Test
    public void testLine165() throws Exception {
        boolean ivFirstTime = false;
        boolean ivSizeControlledFrame = true;
        try {
            if (ivFirstTime || ivSizeControlledFrame) {
                ivFirstTime = false;
            }
        } finally {
        }
        assertFalse(ivPending);
        assertTrue(ivSizeControlledFrame);
        assertFalse(ivFirstTime);
    }

    @Test
    public void testLine166() throws Exception {
        boolean ivFirstTime = true;
        boolean ivSizeControlledFrame = true;
        try {
            if (ivFirstTime || ivSizeControlledFrame) {
                ivFirstTime = false;
            }
        } finally {
        }
        assertFalse(ivPending);
        assertTrue(ivSizeControlledFrame);
        assertFalse(ivFirstTime);
    }

    @Test
    public void testLine167() throws Exception {
        boolean ivFirstTime = false;
        boolean ivSizeControlledFrame = false;
        try {
            if (ivFirstTime || ivSizeControlledFrame) {
                ivFirstTime = false;
            }
        } finally {
        }
        assertFalse(ivPending);
        assertFalse(ivSizeControlledFrame);
        assertFalse(ivFirstTime);
    }
}
