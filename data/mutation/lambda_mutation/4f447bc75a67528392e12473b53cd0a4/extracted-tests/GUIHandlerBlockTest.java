package clsvis.logging;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import org.junit.Test;
import static org.junit.Assert.*;
import static clsvis.logging.GUIHandler.*;

public class GUIHandlerBlockTest {

    @Test
    public void testLine43() throws Exception {
        JTextArea GUIHandler__textArea = new JTextArea();
        ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream(0x2000);
        for (int i = 0; i < 512; i++) {
            GUIHandler__textArea.append("Line " + i + "\n");
        }
        try {
            outputBuffer.write("hello?".getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
        }
        int lineCount = GUIHandler__textArea.getLineCount();
        if (lineCount > GUIHandler.MAX_LINES) {
            try {
                GUIHandler__textArea.replaceRange("...\n", 0, GUIHandler__textArea.getLineEndOffset(lineCount - GUIHandler.MAX_LINES - 1));
            } catch (BadLocationException ignored) {
            }
        }
        String logMsg = outputBuffer.toString();
        outputBuffer.reset();
        assertTrue(GUIHandler__textArea.getText().endsWith("hello?"));
        assertTrue(GUIHandler__textArea.getText().startsWith("..."));
    }

    @Test
    public void testLine50() throws Exception {
        JTextArea GUIHandler__textArea = new JTextArea();
        ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream(0x2000);
        for (int i = 0; i < 512; i++) {
            GUIHandler__textArea.append("Line " + i + "\n");
        }
        int lineCount = GUIHandler__textArea.getLineCount();
        if (lineCount > GUIHandler.MAX_LINES) {
            try {
                GUIHandler__textArea.replaceRange("...\n", 0, GUIHandler__textArea.getLineEndOffset(lineCount - GUIHandler.MAX_LINES - 1));
            } catch (BadLocationException ignored) {
            }
        }
        String logMsg = outputBuffer.toString();
        outputBuffer.reset();
        assertTrue(GUIHandler__textArea.getText().endsWith("Line 511\n"));
        assertTrue(GUIHandler__textArea.getText().startsWith("..."));
    }

    @Test
    public void testLine56() throws Exception {
        JTextArea GUIHandler__textArea = new JTextArea();
        ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream(0x2000);
        for (int i = 0; i < 511; i++) {
            GUIHandler__textArea.append("Line " + i + "\n");
        }
        try {
            outputBuffer.write("hello?".getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
        }
        int lineCount = GUIHandler__textArea.getLineCount();
        if (lineCount > GUIHandler.MAX_LINES) {
            try {
                GUIHandler__textArea.replaceRange("...\n", 0, GUIHandler__textArea.getLineEndOffset(lineCount - GUIHandler.MAX_LINES - 1));
            } catch (BadLocationException ignored) {
            }
        }
        String logMsg = outputBuffer.toString();
        outputBuffer.reset();
        assertTrue(GUIHandler__textArea.getText().endsWith("hello?"));
        assertTrue(GUIHandler__textArea.getText().startsWith("Line 0"));
    }

    @Test
    public void testLine63() throws Exception {
        JTextArea GUIHandler__textArea = new JTextArea();
        ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream(0x2000);
        for (int i = 0; i < 511; i++) {
            GUIHandler__textArea.append("Line " + i + "\n");
        }
        int lineCount = GUIHandler__textArea.getLineCount();
        if (lineCount > GUIHandler.MAX_LINES) {
            try {
                GUIHandler__textArea.replaceRange("...\n", 0, GUIHandler__textArea.getLineEndOffset(lineCount - GUIHandler.MAX_LINES - 1));
            } catch (BadLocationException ignored) {
            }
        }
        String logMsg = outputBuffer.toString();
        outputBuffer.reset();
        assertTrue(GUIHandler__textArea.getText().endsWith("Line 510\n"));
        assertTrue(GUIHandler__textArea.getText().startsWith("Line 0"));
    }

    @Test
    public void testLine69() throws Exception {
        JTextArea GUIHandler__textArea = new JTextArea();
        ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream(0x2000);
        for (int i = 0; i < 10; i++) {
            GUIHandler__textArea.append("Line " + i + "\n");
        }
        try {
            outputBuffer.write("hello?".getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
        }
        int lineCount = GUIHandler__textArea.getLineCount();
        if (lineCount > GUIHandler.MAX_LINES) {
            try {
                GUIHandler__textArea.replaceRange("...\n", 0, GUIHandler__textArea.getLineEndOffset(lineCount - GUIHandler.MAX_LINES - 1));
            } catch (BadLocationException ignored) {
            }
        }
        String logMsg = outputBuffer.toString();
        outputBuffer.reset();
        assertTrue(GUIHandler__textArea.getText().endsWith("hello?"));
        assertTrue(GUIHandler__textArea.getText().startsWith("Line 0"));
    }

    @Test
    public void testLine76() throws Exception {
        JTextArea GUIHandler__textArea = new JTextArea();
        ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream(0x2000);
        for (int i = 0; i < 10; i++) {
            GUIHandler__textArea.append("Line " + i + "\n");
        }
        int lineCount = GUIHandler__textArea.getLineCount();
        if (lineCount > GUIHandler.MAX_LINES) {
            try {
                GUIHandler__textArea.replaceRange("...\n", 0, GUIHandler__textArea.getLineEndOffset(lineCount - GUIHandler.MAX_LINES - 1));
            } catch (BadLocationException ignored) {
            }
        }
        String logMsg = outputBuffer.toString();
        outputBuffer.reset();
        assertTrue(GUIHandler__textArea.getText().endsWith("Line 9\n"));
        assertTrue(GUIHandler__textArea.getText().startsWith("Line 0"));
    }
}
