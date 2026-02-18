package clsvis.logging;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;

/**
 * Logging StreamHandler which sends output to the assigned JTextArea.
 *
 *
 * @author Jonatan Kazmierczak [Jonatan (at) Son-of-God.info]
 */
public final class GUIHandler extends StreamHandler {

    private static final int MAX_LINES = 0x200;

    private static JTextArea textArea;

    private final ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream( 0x2000 );

    public GUIHandler() {
        setOutputStream( outputBuffer );
    }

    @Override
    public synchronized void publish(LogRecord record) {
        super.publish( record );
        flush();
        if (textArea != null) {
            SwingUtilities.invokeLater( () -> {
                // Clean the overflowing part of the log
                // BLOCKTEST EVAL: https://github.com/jonatan-kazmierczak/class-visualizer/blob/71015106c3316c02d366af71d9883c909d9ac772/src/main/java/clsvis/logging/GUIHandler.java#L33-L46
                textArea = new JTextArea();
                textArea.setText("a\nb");
                blocktest().given(textArea, new JTextArea()).given(outputBuffer, new ByteArrayOutputStream(0x2000)).setup(() -> {
                    for (int i = 0; i < 512; i++) {
                        textArea.append("Line " + i + "\n");
                    }
                    try {outputBuffer.write("hello?".getBytes(StandardCharsets.UTF_8));}catch(Exception e){}
                }).checkTrue(textArea.getText().startsWith("...")).checkTrue(textArea.getText().endsWith("hello?"));

                blocktest().given(textArea, new JTextArea()).given(outputBuffer, new ByteArrayOutputStream(0x2000)).setup(() -> {
                    for (int i = 0; i < 512; i++) {
                        textArea.append("Line " + i + "\n");
                    }
                }).checkTrue(textArea.getText().startsWith("...")).checkTrue(textArea.getText().endsWith("Line 511\n"));

                blocktest().given(textArea, new JTextArea()).given(outputBuffer, new ByteArrayOutputStream(0x2000)).setup(() -> {
                    for (int i = 0; i < 511; i++) {
                        textArea.append("Line " + i + "\n");
                    }
                    try {outputBuffer.write("hello?".getBytes(StandardCharsets.UTF_8));}catch(Exception e){}
                }).checkTrue(textArea.getText().startsWith("Line 0")).checkTrue(textArea.getText().endsWith("hello?"));

                blocktest().given(textArea, new JTextArea()).given(outputBuffer, new ByteArrayOutputStream(0x2000)).setup(() -> {
                    for (int i = 0; i < 511; i++) {
                        textArea.append("Line " + i + "\n");
                    }
                }).checkTrue(textArea.getText().startsWith("Line 0")).checkTrue(textArea.getText().endsWith("Line 510\n"));

                blocktest().given(textArea, new JTextArea()).given(outputBuffer, new ByteArrayOutputStream(0x2000)).setup(() -> {
                    for (int i = 0; i < 10; i++) {
                        textArea.append("Line " + i + "\n");
                    }
                    try {outputBuffer.write("hello?".getBytes(StandardCharsets.UTF_8));}catch(Exception e){}
                }).checkTrue(textArea.getText().startsWith("Line 0")).checkTrue(textArea.getText().endsWith("hello?"));

                blocktest().given(textArea, new JTextArea()).given(outputBuffer, new ByteArrayOutputStream(0x2000)).setup(() -> {
                    for (int i = 0; i < 10; i++) {
                        textArea.append("Line " + i + "\n");
                    }
                }).checkTrue(textArea.getText().startsWith("Line 0")).checkTrue(textArea.getText().endsWith("Line 9\n"));

                int lineCount = textArea.getLineCount();
                if (lineCount > MAX_LINES) {
                    try {
                        textArea.replaceRange( "...\n", 0, textArea.getLineEndOffset( lineCount - MAX_LINES - 1 ) );
                    } catch (BadLocationException ignored) {
                    }
                }
                // Show new log entry
                String logMsg = outputBuffer.toString();
                outputBuffer.reset(); // Has to be done immediately after previous line to avoid deleting unread data
                textArea.append( logMsg );
            } );
        }
    }

    /**
     * Sets reference of the logging component to the provided one.
     */
    public static void setTextArea(JTextArea textArea) {
        GUIHandler.textArea = textArea;
    }
}
