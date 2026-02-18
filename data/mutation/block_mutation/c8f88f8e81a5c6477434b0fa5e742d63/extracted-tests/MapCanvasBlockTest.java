package RPMap;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import app.MapMidlet;
import gpspack.GPSReader;
import app.MapForms;
import java.io.InputStream;
import java.util.Timer;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import kml.KMLMapRoute;
import lang.LangHolder;
import gpspack.LocStarter;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import lang.Lang;
// # import misc.DebugLog;
import misc.GPSClubLoad;
import misc.GeneralFeedback;
import misc.GraphUtils;
import misc.KeyLockTimer;
import misc.LastVersion;
import misc.LightTimer;
import misc.LocationCanvas;
import misc.MNSInfo;
import misc.MVector;
import misc.MarkList;
import netradar.NetRadar;
import netradar.NetRadarIT;
import netradar.NetRadarUser;
import misc.SMSSender;
import misc.SMSWait;
import misc.TextCanvas;
import raev.ui.menu.CanvasMenu;
import raev.ui.menu.CanvasMenuItem;
import raev.ui.menu.CanvasMenuListener;
import org.junit.Test;
import static org.junit.Assert.*;
import static RPMap.MapCanvas.*;

public class MapCanvasBlockTest {

    @Test
    public void testLine1747() throws Exception {
        int MapCanvas__dmaxx = 1;
        int MapCanvas__dmaxy = 2;
        int GPSReader__SPEED_KMH = 40;
        int GPSReader__COURSE = 20;
        int MapUtil__G2R = 10;
        int dx = 0;
        float pp = ((MapCanvas__dmaxx < MapCanvas__dmaxy) ? MapCanvas__dmaxx : MapCanvas__dmaxy) * ((GPSReader__SPEED_KMH < 40) ? ((GPSReader__SPEED_KMH > 10) ? (GPSReader__SPEED_KMH - 10) / 15 : 0) : 2) / 7;
        dx = (int) (-pp * Math.sin(GPSReader__COURSE * MapUtil__G2R));
        assertEquals(0, dy);
        assertEquals(0, dx);
    }

    @Test
    public void testLine1748() throws Exception {
        int MapCanvas__dmaxx = 200;
        int MapCanvas__dmaxy = 100;
        int GPSReader__SPEED_KMH = 300;
        int GPSReader__COURSE = 50;
        int MapUtil__G2R = 2;
        int dx = 0;
        float pp = ((MapCanvas__dmaxx < MapCanvas__dmaxy) ? MapCanvas__dmaxx : MapCanvas__dmaxy) * ((GPSReader__SPEED_KMH < 40) ? ((GPSReader__SPEED_KMH > 10) ? (GPSReader__SPEED_KMH - 10) / 15 : 0) : 2) / 7;
        dx = (int) (-pp * Math.sin(GPSReader__COURSE * MapUtil__G2R));
        assertEquals(-24, dy);
        assertEquals(14, dx);
    }

    @Test
    public void testLine5430() throws Exception {
        int[] keys = new int[] { 1, 2, 3, 4 };
        int key = 10;
        for (int i = keys.length - 1; i > 0; i--) keys[i] = keys[i - 1];
        keys[0] = key;
        assertEquals(3, keys[3]);
        assertEquals(2, keys[2]);
        assertEquals(1, keys[1]);
        assertEquals(10, keys[0]);
    }

    @Test
    public void testLine5431() throws Exception {
        int[] keys = new int[] { 1, 1, 1, 1 };
        int key = 2;
        for (int i = keys.length - 1; i > 0; i--) keys[i] = keys[i - 1];
        keys[0] = key;
        assertEquals(1, keys[3]);
        assertEquals(1, keys[2]);
        assertEquals(1, keys[1]);
        assertEquals(2, keys[0]);
    }

    @Test
    public void testLine5432() throws Exception {
        int[] keys = new int[] { 4, 3, 2, 1 };
        int key = 5;
        for (int i = keys.length - 1; i > 0; i--) keys[i] = keys[i - 1];
        keys[0] = key;
        assertEquals(2, keys[3]);
        assertEquals(3, keys[2]);
        assertEquals(4, keys[1]);
        assertEquals(5, keys[0]);
    }
}
