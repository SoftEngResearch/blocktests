package com.ibm.spectrumcomputing.cwl.parser.util;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.ibm.spectrumcomputing.cwl.model.exception.CWLException;
import com.ibm.spectrumcomputing.cwl.model.process.parameter.type.file.CWLDirectory;
import com.ibm.spectrumcomputing.cwl.model.process.parameter.type.file.CWLFile;
import com.ibm.spectrumcomputing.cwl.model.process.parameter.type.file.CWLFileBase;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.ibm.spectrumcomputing.cwl.parser.util.IOUtil.*;

public class IOUtilBlockTest {

    @Test
    public void testLine475() throws Exception {
        java.lang.String s = "";
        StringBuilder contentBuilder = new StringBuilder();
        if ((s.length() != 0) && (!s.startsWith("Sender") && !s.startsWith("Subject") && !s.startsWith("Your") && !s.startsWith("PS") && !s.startsWith("Read"))) {
            contentBuilder.append(s).append(System.getProperty(""));
        }
        assertEquals("", contentBuilder.toString());
    }

    @Test
    public void testLine476() throws Exception {
        java.lang.String s = "hey";
        StringBuilder contentBuilder = new StringBuilder();
        if ((s.length() != 0) && (!s.startsWith("Sender") && !s.startsWith("Subject") && !s.startsWith("Your") && !s.startsWith("PS") && !s.startsWith("Read"))) {
            contentBuilder.append(s).append(System.getProperty(""));
        }
        assertEquals("hey\n", contentBuilder.toString());
    }

    @Test
    public void testLine477() throws Exception {
        java.lang.String s = "Sender";
        StringBuilder contentBuilder = new StringBuilder();
        if ((s.length() != 0) && (!s.startsWith("Sender") && !s.startsWith("Subject") && !s.startsWith("Your") && !s.startsWith("PS") && !s.startsWith("Read"))) {
            contentBuilder.append(s).append(System.getProperty(""));
        }
        assertEquals("", contentBuilder.toString());
    }

    @Test
    public void testLine478() throws Exception {
        java.lang.String s = "Subject";
        StringBuilder contentBuilder = new StringBuilder();
        if ((s.length() != 0) && (!s.startsWith("Sender") && !s.startsWith("Subject") && !s.startsWith("Your") && !s.startsWith("PS") && !s.startsWith("Read"))) {
            contentBuilder.append(s).append(System.getProperty(""));
        }
        assertEquals("", contentBuilder.toString());
    }

    @Test
    public void testLine479() throws Exception {
        java.lang.String s = "Your";
        StringBuilder contentBuilder = new StringBuilder();
        if ((s.length() != 0) && (!s.startsWith("Sender") && !s.startsWith("Subject") && !s.startsWith("Your") && !s.startsWith("PS") && !s.startsWith("Read"))) {
            contentBuilder.append(s).append(System.getProperty(""));
        }
        assertEquals("", contentBuilder.toString());
    }

    @Test
    public void testLine480() throws Exception {
        java.lang.String s = "PS";
        StringBuilder contentBuilder = new StringBuilder();
        if ((s.length() != 0) && (!s.startsWith("Sender") && !s.startsWith("Subject") && !s.startsWith("Your") && !s.startsWith("PS") && !s.startsWith("Read"))) {
            contentBuilder.append(s).append(System.getProperty(""));
        }
        assertEquals("", contentBuilder.toString());
    }

    @Test
    public void testLine481() throws Exception {
        java.lang.String s = "Read";
        StringBuilder contentBuilder = new StringBuilder();
        if ((s.length() != 0) && (!s.startsWith("Sender") && !s.startsWith("Subject") && !s.startsWith("Your") && !s.startsWith("PS") && !s.startsWith("Read"))) {
            contentBuilder.append(s).append(System.getProperty(""));
        }
        assertEquals("", contentBuilder.toString());
    }
}
