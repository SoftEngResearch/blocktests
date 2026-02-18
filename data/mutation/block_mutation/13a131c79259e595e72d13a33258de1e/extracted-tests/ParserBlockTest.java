package org.konte.parse;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import org.konte.model.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.blocktest.types.Flow.IfStmt;
import static org.konte.lang.Tokens.*;
import org.konte.lang.Tokenizer;
import org.konte.model.Untransformable;
import org.konte.misc.Readers;
import org.konte.expression.BooleanExpression;
import org.konte.expression.Expression;
import org.konte.expression.ExpressionFunction;
import org.konte.expression.Name;
import org.konte.expression.Value;
import org.konte.generate.Runtime;
import org.konte.generate.StreamingShapeReader;
import org.konte.image.Camera;
import org.konte.model.systems.DistanceMetric;
import org.konte.image.CanvasEffect;
import org.konte.imprt.SvgImport;
import org.konte.lang.CameraProperties;
import org.konte.lang.Language;
import org.konte.lang.Language.LanguageFunctor;
import org.konte.lang.LightModifier;
import org.konte.lang.ShapeReaders;
import org.konte.lang.Tokenizer.TokenizerString;
import org.konte.misc.PrefixStringMap;
import org.konte.model.PathRule.Placeholder;
import org.konte.plugin.KontePluginScript;
import org.konte.plugin.KonteScriptExtension;
import org.konte.ui.Ui;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.konte.parse.Parser.*;

public class ParserBlockTest {

    @Test(expected = ParseException.class)
    public void testLine653() throws Exception {
        Matcher imgMatcher = Pattern.compile("(.*(jpg|jpeg|png|gif|JPG|PNG|GIF))\\s*(\\w*)$").matcher("hello.png");
        int Parser__lineNr = 0;
        int Parser__caretPos = 0;
        imgMatcher.find();
        String refName = imgMatcher.group(3);
        if (refName.length() == 0)
            throw new ParseException("Syntax: include [bitmap-name] [ref-name]", Parser__lineNr, Parser__caretPos);
    }

    @Test
    public void testLine656() throws Exception {
        Matcher imgMatcher = Pattern.compile("(.*(jpg|jpeg|png|gif|JPG|PNG|GIF))\\s*(\\w*)$").matcher("img/README/2015-02-23-21-57-cubes.png funny");
        int Parser__lineNr = 0;
        int Parser__caretPos = 0;
        String workdir = System.getProperty("konte.workdir");
        imgMatcher.find();
        String refName = imgMatcher.group(3);
        if (refName.length() == 0)
            throw new ParseException("Syntax: include [bitmap-name] [ref-name]", Parser__lineNr, Parser__caretPos);
        File fl = new File("img/README/2015-02-23-21-57-cubes.png");
        Image img = null;
        if (fl.exists()) {
            img = Model.bitmapCache.add(fl, refName);
        } else {
            img = Model.bitmapCache.add(new URL(imgMatcher.group(1)), refName);
        }
        if (img == null)
            throw new ParseException("Image not found");
        Runtime.sysoutln(String.format("%s [%dx%d] included as %s", fl.getAbsolutePath(), img.getWidth(null), img.getHeight(null), refName), 5);
        Model.bitmapCache.init();
        assertTrue(Model.bitmapCache.getImage("funny") != null);
    }

    @Test(expected = ParseException.class)
    public void testLine659() throws Exception {
        Matcher imgMatcher = Pattern.compile("(.*(jpg|jpeg|png|gif|JPG|PNG|GIF))\\s*(\\w*)$").matcher("img/README/2015-02-23-21-57-cubes.png funny");
        int Parser__lineNr = 0;
        int Parser__caretPos = 0;
        String workdir = System.getProperty("konte.workdir");
        imgMatcher.find();
        String refName = imgMatcher.group(3);
        if (refName.length() == 0)
            throw new ParseException("Syntax: include [bitmap-name] [ref-name]", Parser__lineNr, Parser__caretPos);
        File fl = new File("img/README/2015-02-23-21-57-cubes.png");
        Image img = null;
        if (fl.exists()) {
            img = null;
        } else {
            img = Model.bitmapCache.add(new URL(imgMatcher.group(1)), refName);
        }
        if (img == null)
            throw new ParseException("Image not found");
        Runtime.sysoutln(String.format("%s [%dx%d] included as %s", fl.getAbsolutePath(), img.getWidth(null), img.getHeight(null), refName), 5);
        Model.bitmapCache.init();
    }
}
