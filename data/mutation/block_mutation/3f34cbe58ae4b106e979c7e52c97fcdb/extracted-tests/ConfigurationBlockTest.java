package com.logpresso.scanner;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import com.logpresso.scanner.utils.IoUtils;
import com.logpresso.scanner.utils.ZipUtils;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.logpresso.scanner.Configuration.*;

public class ConfigurationBlockTest {

    @Test(expected = IllegalArgumentException.class)
    public void testLine354() throws Exception {
        java.lang.String c__reportDir = "";
        String[] args = new String[] { "--report-dir", "--reports" };
        int i = 0;
        try {
            if (args.length > i + 1) {
                String pattern = args[i + 1];
                if (pattern.startsWith("--"))
                    throw new IllegalArgumentException("Report dir should not starts with `--`.");
                c__reportDir = args[i + 1];
                File reportFile = new File(c__reportDir);
                if (!reportFile.exists())
                    throw new IllegalArgumentException("Directory not existent - " + reportFile.getAbsolutePath());
                else if (!reportFile.isDirectory())
                    throw new IllegalArgumentException("Not a directory - " + reportFile.getAbsolutePath());
                i++;
            } else {
                throw new IllegalArgumentException("Specify report output path.");
            }
            return;
        } finally {
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLine355() throws Exception {
        java.lang.String c__reportDir = "";
        String[] args = new String[] { "--report-dir" };
        int i = 0;
        try {
            if (args.length > i + 1) {
                String pattern = args[i + 1];
                if (pattern.startsWith("--"))
                    throw new IllegalArgumentException("Report dir should not starts with `--`.");
                c__reportDir = args[i + 1];
                File reportFile = new File(c__reportDir);
                if (!reportFile.exists())
                    throw new IllegalArgumentException("Directory not existent - " + reportFile.getAbsolutePath());
                else if (!reportFile.isDirectory())
                    throw new IllegalArgumentException("Not a directory - " + reportFile.getAbsolutePath());
                i++;
            } else {
                throw new IllegalArgumentException("Specify report output path.");
            }
            return;
        } finally {
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLine356() throws Exception {
        java.lang.String c__reportDir = "";
        String[] args = new String[] { "--report-dir", "foo" };
        int i = 0;
        try {
            if (args.length > i + 1) {
                String pattern = args[i + 1];
                if (pattern.startsWith("--"))
                    throw new IllegalArgumentException("Report dir should not starts with `--`.");
                c__reportDir = args[i + 1];
                File reportFile = new File(c__reportDir);
                if (!reportFile.exists())
                    throw new IllegalArgumentException("Directory not existent - " + reportFile.getAbsolutePath());
                else if (!reportFile.isDirectory())
                    throw new IllegalArgumentException("Not a directory - " + reportFile.getAbsolutePath());
                i++;
            } else {
                throw new IllegalArgumentException("Specify report output path.");
            }
            return;
        } finally {
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLine357() throws Exception {
        java.lang.String c__reportDir = "";
        String[] args = new String[] { "--report-dir", System.getProperty("user.dir") + "/__a.txt" };
        int i = 0;
        try {
            if (args.length > i + 1) {
                String pattern = args[i + 1];
                if (pattern.startsWith("--"))
                    throw new IllegalArgumentException("Report dir should not starts with `--`.");
                c__reportDir = args[i + 1];
                File reportFile = new File(c__reportDir);
                if (!reportFile.exists())
                    throw new IllegalArgumentException("Directory not existent - " + reportFile.getAbsolutePath());
                else if (!reportFile.isDirectory())
                    throw new IllegalArgumentException("Not a directory - " + reportFile.getAbsolutePath());
                i++;
            } else {
                throw new IllegalArgumentException("Specify report output path.");
            }
            return;
        } finally {
        }
    }

    @Test
    public void testLine358() throws Exception {
        java.lang.String c__reportDir = "";
        String[] args = new String[] { "--report-dir", System.getProperty("user.dir") };
        int i = 0;
        if (args.length > i + 1) {
            String pattern = args[i + 1];
            if (pattern.startsWith("--"))
                throw new IllegalArgumentException("Report dir should not starts with `--`.");
            c__reportDir = args[i + 1];
            File reportFile = new File(c__reportDir);
            if (!reportFile.exists())
                throw new IllegalArgumentException("Directory not existent - " + reportFile.getAbsolutePath());
            else if (!reportFile.isDirectory())
                throw new IllegalArgumentException("Not a directory - " + reportFile.getAbsolutePath());
            i++;
        } else {
            throw new IllegalArgumentException("Specify report output path.");
        }
        assertEquals(1, i);
    }
}
