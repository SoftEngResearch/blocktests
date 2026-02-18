package org.attoparser.select;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.attoparser.select.MarkupSelectorItems.*;

public class MarkupSelectorItemsBlockTest {

    @Test
    public void testLine157() throws Exception {
        List<IMarkupSelectorItem> result = null;
        boolean html = false;
        IMarkupSelectorReferenceResolver referenceResolver = null;
        String selectorSpecStr = "/A/B/C";
        int firstNonSlash = 1;
        int selEnd = selectorSpecStr.substring(firstNonSlash).indexOf('/');
        String tail = selectorSpecStr.substring(firstNonSlash).substring(selEnd);
        result = MarkupSelectorItems.parseSelector(html, tail, referenceResolver);
        assertEquals("[/B, /C]", result.toString());
        assertEquals("/B/C", tail);
        assertEquals(1, selEnd);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLine258() throws Exception {
        String path = "hi.";
        int classModifierPos = 2;
        String selector = "foo";
        String selectorPathClassModifier = path.substring(classModifierPos + MarkupSelectorItem.CLASS_MODIFIER_SEPARATOR.length());
        path = path.substring(0, classModifierPos);
        if (MarkupSelectorItems.isEmptyOrWhitespace(selectorPathClassModifier)) {
            throw new IllegalArgumentException("Empty id modifier in selector expression " + "\"" + selector + "\", which is forbidden.");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLine259() throws Exception {
        String path = "hi. ";
        int classModifierPos = 2;
        String selector = "foo";
        String selectorPathClassModifier = path.substring(classModifierPos + MarkupSelectorItem.CLASS_MODIFIER_SEPARATOR.length());
        path = path.substring(0, classModifierPos);
        if (MarkupSelectorItems.isEmptyOrWhitespace(selectorPathClassModifier)) {
            throw new IllegalArgumentException("Empty id modifier in selector expression " + "\"" + selector + "\", which is forbidden.");
        }
    }

    @Test
    public void testLine260() throws Exception {
        String path = "h.i ";
        int classModifierPos = 1;
        String selector = "foo";
        String selectorPathClassModifier = path.substring(classModifierPos + MarkupSelectorItem.CLASS_MODIFIER_SEPARATOR.length());
        path = path.substring(0, classModifierPos);
        if (MarkupSelectorItems.isEmptyOrWhitespace(selectorPathClassModifier)) {
            throw new IllegalArgumentException("Empty id modifier in selector expression " + "\"" + selector + "\", which is forbidden.");
        }
        assertEquals("i ", selectorPathClassModifier);
    }

    @Test
    public void testLine261() throws Exception {
        String path = "hi.      foo";
        int classModifierPos = 2;
        String selector = "foo";
        String selectorPathClassModifier = path.substring(classModifierPos + MarkupSelectorItem.CLASS_MODIFIER_SEPARATOR.length());
        path = path.substring(0, classModifierPos);
        if (MarkupSelectorItems.isEmptyOrWhitespace(selectorPathClassModifier)) {
            throw new IllegalArgumentException("Empty id modifier in selector expression " + "\"" + selector + "\", which is forbidden.");
        }
        assertEquals("hi", path);
    }

    @Test
    public void testLine420() throws Exception {
        String resolvedSelector = "//foo";
        boolean anyLevel = true;
        if (resolvedSelector.startsWith("//")) {
            if (!anyLevel) {
                // We remove one slash to make it match
                resolvedSelector = resolvedSelector.substring(1);
            }
        } else if (resolvedSelector.startsWith("/")) {
            if (anyLevel) {
                // We add a slash to make it match
                resolvedSelector = "/" + resolvedSelector;
            }
        } else if (!anyLevel) {
            // We add a slash to make it match
            resolvedSelector = "/" + resolvedSelector;
        }
        assertEquals("//foo", resolvedSelector);
    }

    @Test
    public void testLine421() throws Exception {
        String resolvedSelector = "//foo";
        boolean anyLevel = false;
        if (resolvedSelector.startsWith("//")) {
            if (!anyLevel) {
                // We remove one slash to make it match
                resolvedSelector = resolvedSelector.substring(1);
            }
        } else if (resolvedSelector.startsWith("/")) {
            if (anyLevel) {
                // We add a slash to make it match
                resolvedSelector = "/" + resolvedSelector;
            }
        } else if (!anyLevel) {
            // We add a slash to make it match
            resolvedSelector = "/" + resolvedSelector;
        }
        assertEquals("/foo", resolvedSelector);
    }

    @Test
    public void testLine422() throws Exception {
        String resolvedSelector = "/foo";
        boolean anyLevel = true;
        if (resolvedSelector.startsWith("//")) {
            if (!anyLevel) {
                // We remove one slash to make it match
                resolvedSelector = resolvedSelector.substring(1);
            }
        } else if (resolvedSelector.startsWith("/")) {
            if (anyLevel) {
                // We add a slash to make it match
                resolvedSelector = "/" + resolvedSelector;
            }
        } else if (!anyLevel) {
            // We add a slash to make it match
            resolvedSelector = "/" + resolvedSelector;
        }
        assertEquals("//foo", resolvedSelector);
    }

    @Test
    public void testLine423() throws Exception {
        String resolvedSelector = "/foo";
        boolean anyLevel = false;
        if (resolvedSelector.startsWith("//")) {
            if (!anyLevel) {
                // We remove one slash to make it match
                resolvedSelector = resolvedSelector.substring(1);
            }
        } else if (resolvedSelector.startsWith("/")) {
            if (anyLevel) {
                // We add a slash to make it match
                resolvedSelector = "/" + resolvedSelector;
            }
        } else if (!anyLevel) {
            // We add a slash to make it match
            resolvedSelector = "/" + resolvedSelector;
        }
        assertEquals("/foo", resolvedSelector);
    }

    @Test
    public void testLine424() throws Exception {
        String resolvedSelector = "foo";
        boolean anyLevel = true;
        if (resolvedSelector.startsWith("//")) {
            if (!anyLevel) {
                // We remove one slash to make it match
                resolvedSelector = resolvedSelector.substring(1);
            }
        } else if (resolvedSelector.startsWith("/")) {
            if (anyLevel) {
                // We add a slash to make it match
                resolvedSelector = "/" + resolvedSelector;
            }
        } else if (!anyLevel) {
            // We add a slash to make it match
            resolvedSelector = "/" + resolvedSelector;
        }
        assertEquals("foo", resolvedSelector);
    }

    @Test
    public void testLine425() throws Exception {
        String resolvedSelector = "foo";
        boolean anyLevel = false;
        if (resolvedSelector.startsWith("//")) {
            if (!anyLevel) {
                // We remove one slash to make it match
                resolvedSelector = resolvedSelector.substring(1);
            }
        } else if (resolvedSelector.startsWith("/")) {
            if (anyLevel) {
                // We add a slash to make it match
                resolvedSelector = "/" + resolvedSelector;
            }
        } else if (!anyLevel) {
            // We add a slash to make it match
            resolvedSelector = "/" + resolvedSelector;
        }
        assertEquals("/foo", resolvedSelector);
    }
}
