package com.junichi11.netbeans.modules.color.codes.preview.impl;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import com.junichi11.netbeans.modules.color.codes.preview.impl.colors.HexCssColorCodeFormatter;
import com.junichi11.netbeans.modules.color.codes.preview.impl.ui.options.HexCssOptionsPanel;
import com.junichi11.netbeans.modules.color.codes.preview.impl.utils.ColorsUtils;
import com.junichi11.netbeans.modules.color.codes.preview.impl.utils.HexCssColorType;
import com.junichi11.netbeans.modules.color.codes.preview.options.ColorCodesPreviewOptions;
import com.junichi11.netbeans.modules.color.codes.preview.spi.ColorCodeFormatter;
import com.junichi11.netbeans.modules.color.codes.preview.spi.ColorCodeGeneratorItem;
import com.junichi11.netbeans.modules.color.codes.preview.spi.ColorCodesPreviewOptionsPanel;
import com.junichi11.netbeans.modules.color.codes.preview.spi.ColorCodesProvider;
import com.junichi11.netbeans.modules.color.codes.preview.spi.ColorValue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.Document;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.junichi11.netbeans.modules.color.codes.preview.impl.HexCssColorCodesProvider.*;

public class HexCssColorCodesProviderBlockTest {

    @Test
    public void testLine127() throws Exception {
        String l = "I am such as a foo ";
        String var = "foo";
        Map<Integer, String> map = new HashMap<>();
        if (l.contains(var)) {
            int indexOfVar = l.indexOf(var);
            int offsetBehindVariableName = indexOfVar + var.length();
            if (offsetBehindVariableName < l.length()) {
                char c = l.charAt(offsetBehindVariableName);
                if ((0 == 1) || c == ';') {
                    map.put(indexOfVar, var);
                }
            }
        }
        assertEquals("foo", map.get(15));
        assertEquals(1, map.size());
    }

    @Test
    public void testLine128() throws Exception {
        String l = "I am such as foo ";
        String var = "foo";
        Map<Integer, String> map = new HashMap<>();
        if (l.contains(var)) {
            int indexOfVar = l.indexOf(var);
            int offsetBehindVariableName = indexOfVar + var.length();
            if (offsetBehindVariableName < l.length()) {
                char c = l.charAt(offsetBehindVariableName);
                if ((0 == 1) || c == ';') {
                    map.put(indexOfVar, var);
                }
            }
        }
        assertEquals("foo", map.get(13));
        assertEquals(1, map.size());
    }

    @Test
    public void testLine129() throws Exception {
        String l = "I am such as a foo foo foo";
        String var = "foo";
        Map<Integer, String> map = new HashMap<>();
        if (l.contains(var)) {
            int indexOfVar = l.indexOf(var);
            int offsetBehindVariableName = indexOfVar + var.length();
            if (offsetBehindVariableName < l.length()) {
                char c = l.charAt(offsetBehindVariableName);
                if ((0 == 1) || c == ';') {
                    map.put(indexOfVar, var);
                }
            }
        }
        assertEquals("foo", map.get(15));
        assertEquals(1, map.size());
    }

    @Test
    public void testLine130() throws Exception {
        String l = "I am such as a foo;";
        String var = "foo";
        Map<Integer, String> map = new HashMap<>();
        if (l.contains(var)) {
            int indexOfVar = l.indexOf(var);
            int offsetBehindVariableName = indexOfVar + var.length();
            if (offsetBehindVariableName < l.length()) {
                char c = l.charAt(offsetBehindVariableName);
                if ((0 == 1) || c == ';') {
                    map.put(indexOfVar, var);
                }
            }
        }
        assertEquals("foo", map.get(15));
        assertEquals(1, map.size());
    }

    @Test
    public void testLine131() throws Exception {
        String l = "I am such as a Foo ";
        String var = "foo";
        Map<Integer, String> map = new HashMap<>();
        if (l.contains(var)) {
            int indexOfVar = l.indexOf(var);
            int offsetBehindVariableName = indexOfVar + var.length();
            if (offsetBehindVariableName < l.length()) {
                char c = l.charAt(offsetBehindVariableName);
                if ((0 == 1) || c == ';') {
                    map.put(indexOfVar, var);
                }
            }
        }
        assertEquals(0, map.size());
    }

    @Test
    public void testLine132() throws Exception {
        String l = "I am such as a fool";
        String var = "foo";
        Map<Integer, String> map = new HashMap<>();
        if (l.contains(var)) {
            int indexOfVar = l.indexOf(var);
            int offsetBehindVariableName = indexOfVar + var.length();
            if (offsetBehindVariableName < l.length()) {
                char c = l.charAt(offsetBehindVariableName);
                if ((0 == 1) || c == ';') {
                    map.put(indexOfVar, var);
                }
            }
        }
        assertEquals(0, map.size());
    }
}
