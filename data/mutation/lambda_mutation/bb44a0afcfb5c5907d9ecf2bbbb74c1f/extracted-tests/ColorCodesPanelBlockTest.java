package com.junichi11.netbeans.modules.color.codes.preview.ui;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import com.junichi11.netbeans.modules.color.codes.preview.spi.ColorValue;
import com.junichi11.netbeans.modules.color.codes.preview.utils.Utils;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.netbeans.api.editor.EditorRegistry;
import org.netbeans.api.editor.document.LineDocumentUtils;
import org.netbeans.editor.BaseDocument;
import org.openide.text.NbDocument;
import org.openide.util.NbBundle;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.junichi11.netbeans.modules.color.codes.preview.ui.ColorCodesPanel.*;

public class ColorCodesPanelBlockTest {

    @Test
    public void testLine1() throws Exception {
        StyledDocument document = new javax.swing.text.DefaultStyledDocument();
        int lineOffset = 0;
        int startOffset = 0;
        int endOffset = 2;
        ColorValue colorValue = null;
        Color selectedColor = null;
        try {
            document.insertString(0, "test", null);
        } catch (BadLocationException error) {
            System.exit(1);
        }
        try {
            int removeStart = lineOffset + startOffset;
            document.remove(removeStart, endOffset - startOffset);
            document.insertString(removeStart, "green", null);
        } catch (BadLocationException ex2) {
            ColorCodesPanel.LOGGER.log(Level.WARNING, ex2.getMessage());
        }
        assertEquals("greenst", document.getText(0, document.getLength()));
    }
}
