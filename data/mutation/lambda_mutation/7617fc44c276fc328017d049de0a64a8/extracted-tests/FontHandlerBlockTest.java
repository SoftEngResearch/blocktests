package edu.brandeis.llc.mae.util;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.Test;
import static org.junit.Assert.*;
import static edu.brandeis.llc.mae.util.FontHandler.*;

public class FontHandlerBlockTest {

    @Test
    public void testLine1() throws Exception {
        StyledDocument document = new DefaultStyledDocument();
        int finalLength = 1;
        String plainText = "\uD83C\uDF09";
        int finalOffset = 1;
        SimpleAttributeSet unicodeASet = new SimpleAttributeSet();
        StyleConstants.setFontFamily(unicodeASet, FontHandler.getFontToRenderSurrogateCode(plainText.codePointAt(finalOffset)).getFontName());
        assertEquals("family=Dialog.plain ", unicodeASet.toString());
    }
}
