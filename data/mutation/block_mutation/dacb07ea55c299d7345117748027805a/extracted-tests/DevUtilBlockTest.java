package io.openliberty.tools.common.plugins.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.Watchable;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import com.sun.nio.file.SensitivityWatchEventModifier;
import io.openliberty.tools.ant.ServerTask;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.input.CloseShieldInputStream;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static io.openliberty.tools.common.plugins.util.DevUtil.*;

public class DevUtilBlockTest {

    @Test
    public void testLine4318() throws Exception {
        Collection<File> modifiedClasses = new HashSet<File>();
        File fileChanged = new File("foo.class");
        ChangeType changeType = ChangeType.MODIFY;
        if (true && fileChanged.getName().endsWith(".class") && (changeType == ChangeType.MODIFY || changeType == ChangeType.CREATE)) {
            modifiedClasses.add(fileChanged);
        } else if (changeType == ChangeType.DELETE) {
        }
        assertEquals(new File("foo.class"), modifiedClasses.iterator().next());
        assertFalse(modifiedClasses.isEmpty());
    }

    @Test
    public void testLine4319() throws Exception {
        Collection<File> modifiedClasses = new HashSet<File>();
        File fileChanged = new File("foo.class");
        ChangeType changeType = ChangeType.CREATE;
        if (true && fileChanged.getName().endsWith(".class") && (changeType == ChangeType.MODIFY || changeType == ChangeType.CREATE)) {
            modifiedClasses.add(fileChanged);
        } else if (changeType == ChangeType.DELETE) {
        }
        assertEquals(new File("foo.class"), modifiedClasses.iterator().next());
        assertFalse(modifiedClasses.isEmpty());
    }

    @Test
    public void testLine4320() throws Exception {
        Collection<File> modifiedClasses = new HashSet<File>();
        File fileChanged = new File("foo.java");
        ChangeType changeType = ChangeType.MODIFY;
        if (true && fileChanged.getName().endsWith(".class") && (changeType == ChangeType.MODIFY || changeType == ChangeType.CREATE)) {
            modifiedClasses.add(fileChanged);
        } else if (changeType == ChangeType.DELETE) {
        }
        assertTrue(modifiedClasses.isEmpty());
    }

    @Test
    public void testLine4321() throws Exception {
        Collection<File> modifiedClasses = new HashSet<>(Collections.singleton(new File("foo.class")));
        File fileChanged = new File("foo.class");
        ChangeType changeType = ChangeType.DELETE;
        if (false && fileChanged.getName().endsWith(".class") && (changeType == ChangeType.MODIFY || changeType == ChangeType.CREATE)) {
            modifiedClasses.add(fileChanged);
        } else if (changeType == ChangeType.DELETE) {
        }
        assertTrue(modifiedClasses.isEmpty());
    }

    @Test
    public void testLine4322() throws Exception {
        Collection<File> modifiedClasses = new HashSet<>(Collections.singleton(new File("foo2.class")));
        File fileChanged = new File("foo.class");
        ChangeType changeType = ChangeType.MODIFY;
        if (false && fileChanged.getName().endsWith(".class") && (changeType == ChangeType.MODIFY || changeType == ChangeType.CREATE)) {
            modifiedClasses.add(fileChanged);
        } else if (changeType == ChangeType.DELETE) {
        }
        assertEquals(new File("foo2.class"), modifiedClasses.iterator().next());
        assertFalse(modifiedClasses.isEmpty());
    }
}
