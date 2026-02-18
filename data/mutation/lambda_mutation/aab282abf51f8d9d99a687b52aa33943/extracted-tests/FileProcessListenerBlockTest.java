package io.playpen.core.utils.process;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.Test;
import static org.junit.Assert.*;
import static io.playpen.core.utils.process.FileProcessListener.*;

public class FileProcessListenerBlockTest {

    @Test
    public void testLine1() throws Exception {
        String out = "foo";
        BufferedWriter writer = null;
        StringWriter sw = new StringWriter();
        writer = new BufferedWriter(sw);
        try {
            writer.write(out);
            writer.write(System.lineSeparator());
        } catch (IOException e) {
        }
        assertEquals("foo\n", sw.toString());
    }

    @Test
    public void testB() throws Exception {
        String in = "bar";
        BufferedWriter writer = null;
        StringWriter sw = new StringWriter();
        writer = new BufferedWriter(sw);
        try {
            writer.write(in);
            writer.write(System.lineSeparator());
            writer.flush();
        } catch (IOException e) {
        }
        assertEquals("bar\n", sw.toString());
    }
}
