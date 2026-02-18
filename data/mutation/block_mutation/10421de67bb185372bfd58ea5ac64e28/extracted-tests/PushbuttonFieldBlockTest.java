package com.lowagie.text.pdf;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.io.IOException;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.lowagie.text.pdf.PushbuttonField.*;

public class PushbuttonFieldBlockTest {

    @Test
    public void testLine482() throws Exception {
        int scaleIcon = 3;
        float icx = 10;
        float icy = 5;
        switch(scaleIcon) {
            case PushbuttonField.SCALE_ICON_IS_TOO_BIG:
                icx = Math.min(icx, icy);
                icx = Math.min(icx, 1);
                break;
            case PushbuttonField.SCALE_ICON_IS_TOO_SMALL:
                icx = Math.min(icx, icy);
                icx = Math.max(icx, 1);
                break;
            case PushbuttonField.SCALE_ICON_NEVER:
                icx = 1;
                break;
            default:
                icx = Math.min(icx, icy);
                break;
        }
        assertEquals(1, icy, 0.01);
        assertEquals(1, icx, 0.01);
    }

    @Test
    public void testLine483() throws Exception {
        int scaleIcon = 3;
        float icx = 10;
        float icy = 15;
        switch(scaleIcon) {
            case PushbuttonField.SCALE_ICON_IS_TOO_BIG:
                icx = Math.min(icx, icy);
                icx = Math.min(icx, 1);
                break;
            case PushbuttonField.SCALE_ICON_IS_TOO_SMALL:
                icx = Math.min(icx, icy);
                icx = Math.max(icx, 1);
                break;
            case PushbuttonField.SCALE_ICON_NEVER:
                icx = 1;
                break;
            default:
                icx = Math.min(icx, icy);
                break;
        }
        assertEquals(1, icy, 0.01);
        assertEquals(1, icx, 0.01);
    }

    @Test
    public void testLine484() throws Exception {
        int scaleIcon = 3;
        float icx = 0;
        float icy = 5;
        switch(scaleIcon) {
            case PushbuttonField.SCALE_ICON_IS_TOO_BIG:
                icx = Math.min(icx, icy);
                icx = Math.min(icx, 1);
                break;
            case PushbuttonField.SCALE_ICON_IS_TOO_SMALL:
                icx = Math.min(icx, icy);
                icx = Math.max(icx, 1);
                break;
            case PushbuttonField.SCALE_ICON_NEVER:
                icx = 1;
                break;
            default:
                icx = Math.min(icx, icy);
                break;
        }
        assertEquals(0, icy, 0.01);
        assertEquals(0, icx, 0.01);
    }

    @Test
    public void testLine485() throws Exception {
        int scaleIcon = 4;
        float icx = 10;
        float icy = 5;
        switch(scaleIcon) {
            case PushbuttonField.SCALE_ICON_IS_TOO_BIG:
                icx = Math.min(icx, icy);
                icx = Math.min(icx, 1);
                break;
            case PushbuttonField.SCALE_ICON_IS_TOO_SMALL:
                icx = Math.min(icx, icy);
                icx = Math.max(icx, 1);
                break;
            case PushbuttonField.SCALE_ICON_NEVER:
                icx = 1;
                break;
            default:
                icx = Math.min(icx, icy);
                break;
        }
        assertEquals(5, icy, 0.01);
        assertEquals(5, icx, 0.01);
    }

    @Test
    public void testLine486() throws Exception {
        int scaleIcon = 4;
        float icx = 0;
        float icy = 5;
        switch(scaleIcon) {
            case PushbuttonField.SCALE_ICON_IS_TOO_BIG:
                icx = Math.min(icx, icy);
                icx = Math.min(icx, 1);
                break;
            case PushbuttonField.SCALE_ICON_IS_TOO_SMALL:
                icx = Math.min(icx, icy);
                icx = Math.max(icx, 1);
                break;
            case PushbuttonField.SCALE_ICON_NEVER:
                icx = 1;
                break;
            default:
                icx = Math.min(icx, icy);
                break;
        }
        assertEquals(1, icy, 0.01);
        assertEquals(1, icx, 0.01);
    }

    @Test
    public void testLine487() throws Exception {
        int scaleIcon = 2;
        float icx = 0;
        float icy = 5;
        switch(scaleIcon) {
            case PushbuttonField.SCALE_ICON_IS_TOO_BIG:
                icx = Math.min(icx, icy);
                icx = Math.min(icx, 1);
                break;
            case PushbuttonField.SCALE_ICON_IS_TOO_SMALL:
                icx = Math.min(icx, icy);
                icx = Math.max(icx, 1);
                break;
            case PushbuttonField.SCALE_ICON_NEVER:
                icx = 1;
                break;
            default:
                icx = Math.min(icx, icy);
                break;
        }
        assertEquals(1, icy, 0.01);
        assertEquals(1, icx, 0.01);
    }

    @Test
    public void testLine488() throws Exception {
        int scaleIcon = 9;
        float icx = 100;
        float icy = 105;
        switch(scaleIcon) {
            case PushbuttonField.SCALE_ICON_IS_TOO_BIG:
                icx = Math.min(icx, icy);
                icx = Math.min(icx, 1);
                break;
            case PushbuttonField.SCALE_ICON_IS_TOO_SMALL:
                icx = Math.min(icx, icy);
                icx = Math.max(icx, 1);
                break;
            case PushbuttonField.SCALE_ICON_NEVER:
                icx = 1;
                break;
            default:
                icx = Math.min(icx, icy);
                break;
        }
        assertEquals(100, icy, 0.01);
        assertEquals(100, icx, 0.01);
    }

    @Test
    public void testLine489() throws Exception {
        int scaleIcon = 9;
        float icx = 100;
        float icy = 95;
        switch(scaleIcon) {
            case PushbuttonField.SCALE_ICON_IS_TOO_BIG:
                icx = Math.min(icx, icy);
                icx = Math.min(icx, 1);
                break;
            case PushbuttonField.SCALE_ICON_IS_TOO_SMALL:
                icx = Math.min(icx, icy);
                icx = Math.max(icx, 1);
                break;
            case PushbuttonField.SCALE_ICON_NEVER:
                icx = 1;
                break;
            default:
                icx = Math.min(icx, icy);
                break;
        }
        assertEquals(95, icy, 0.01);
        assertEquals(95, icx, 0.01);
    }
}
