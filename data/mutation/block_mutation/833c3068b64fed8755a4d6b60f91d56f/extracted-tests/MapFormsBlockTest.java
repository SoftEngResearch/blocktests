package app;

import RPMap.FileMapLoader;
import RPMap.FileMapRouteLoader;
import RPMap.FileTrackSend;
import RPMap.HTTPMapRouteLoader;
import RPMap.MapCanvas;
import RPMap.MapPoint;
import RPMap.MapRoute;
import RPMap.RMSRoute;
import RPMap.MapRouteLoader;
import RPMap.ScreenSend;
import RPMap.RouteSend;
import RPMap.MapTile;
import RPMap.MapUtil;
import RPMap.RMSOption;
import RPMap.OSMTrackSend;
import RPMap.WebTrackSend;
import gpspack.LocStarter;
// #endif
import cloudmade.CloudRouteResult;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import javax.microedition.rms.RecordStore;
import kml.KMLMapPoint;
import kml.KMLMapRoute;
import kml.KMLSearchResult;
import lang.Lang;
import lang.LangHolder;
import misc.BrowseList;
import misc.DataSenderThread;
// # import misc.DebugLog;
import misc.GPSClubLoad;
// #endif
import misc.LastVersion;
import misc.LocationCanvas;
import misc.GTCanvas;
// # import misc.LogSend;
import misc.MNSInfo;
import misc.MapSound;
import misc.FileDialog;
import misc.SMSRecomend;
import netradar.NetRadar;
import gpspack.GPSReader;
import javax.microedition.lcdui.TextBox;
import misc.ProgressForm;
// #endif
import misc.RouteEditList;
import misc.SMSSender;
import misc.GeneralFeedback;
import misc.ProgressStoppable;
import misc.SportCanvas;
import misc.StringSelectList;
import misc.TR102Sender;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static app.MapForms.*;

public class MapFormsBlockTest {

    @Test
    public void testLine1029() throws Exception {
        int i = 0;
        int id = 1;
        String[] mn = new String[5];
        String[] mt = new String[5];
        String[] mp = new String[5];
        float[] mc = new float[5];
        String[] RMSOption__IMAPS_NAMES = new String[] { "1", "2", "3", "4", "5" };
        String[] RMSOption__IMAPS_RMST = new String[] { "6", "7", "8", "9", "10" };
        String[] RMSOption__IMAPS_RMSP = new String[] { "a", "b", "c", "d", "e" };
        float[] RMSOption__IMAPS_CENT = new float[] { 1.2f, 3.4f, 5.6f, 7.8f, 9.10f };
        try {
            if (i != id) {
                if (i < id) {
                    mn[i] = RMSOption__IMAPS_NAMES[i];
                    mt[i] = RMSOption__IMAPS_RMST[i];
                    mp[i] = RMSOption__IMAPS_RMSP[i];
                    mc[i * 3] = RMSOption__IMAPS_CENT[i * 3];
                    mc[i * 3 + 1] = RMSOption__IMAPS_CENT[i * 3 + 1];
                    mc[i * 3 + 2] = RMSOption__IMAPS_CENT[i * 3 + 2];
                } else {
                    mn[i - 1] = RMSOption__IMAPS_NAMES[i];
                    mt[i - 1] = RMSOption__IMAPS_RMST[i];
                    mp[i - 1] = RMSOption__IMAPS_RMSP[i];
                    mc[(i - 1) * 3] = RMSOption__IMAPS_CENT[(i * 3)];
                    mc[(i - 1) * 3 + 1] = RMSOption__IMAPS_CENT[(i * 3) + 1];
                    mc[(i - 1) * 3 + 2] = RMSOption__IMAPS_CENT[(i * 3) + 2];
                }
                System.out.println("mn");
                for (int k = 0; k < 5; k++) {
                    System.out.print(mn[k] + ",");
                }
                System.out.println("mt");
                for (int k = 0; k < 5; k++) {
                    System.out.print(mt[k] + ",");
                }
                System.out.println("mp");
                for (int k = 0; k < 5; k++) {
                    System.out.print(mp[k] + ",");
                }
                System.out.println("mc");
                for (int k = 0; k < 5; k++) {
                    System.out.print(mc[k] + ",");
                }
            } else {
                try {
                } catch (Throwable tt) {
                }
                try {
                } catch (Throwable tt) {
                    return;
                }
            }
        } finally {
            assertTrue(mc[3] == 0f);
            assertTrue(mc[2] == 5.6f);
            assertTrue(mc[1] == 3.4f);
            assertTrue(mc[0] == 1.2f);
            assertTrue(mp[1] == null);
            assertEquals("a", mp[0]);
            assertTrue(mt[1] == null);
            assertEquals("6", mt[0]);
            assertTrue(mn[1] == null);
            assertEquals("1", mn[0]);
        }
    }

    @Test
    public void testLine1033() throws Exception {
        int i = 1;
        int id = 1;
        String[] mn = new String[5];
        String[] mt = new String[5];
        String[] mp = new String[5];
        float[] mc = new float[5];
        String[] RMSOption__IMAPS_NAMES = new String[] { "1", "2", "3", "4", "5" };
        String[] RMSOption__IMAPS_RMST = new String[] { "6", "7", "8", "9", "10" };
        String[] RMSOption__IMAPS_RMSP = new String[] { "a", "b", "c", "d", "e" };
        float[] RMSOption__IMAPS_CENT = new float[] { 1.2f, 3.4f, 5.6f, 7.8f, 9.10f };
        try {
            if (i != id) {
                if (i < id) {
                    mn[i] = RMSOption__IMAPS_NAMES[i];
                    mt[i] = RMSOption__IMAPS_RMST[i];
                    mp[i] = RMSOption__IMAPS_RMSP[i];
                    mc[i * 3] = RMSOption__IMAPS_CENT[i * 3];
                    mc[i * 3 + 1] = RMSOption__IMAPS_CENT[i * 3 + 1];
                    mc[i * 3 + 2] = RMSOption__IMAPS_CENT[i * 3 + 2];
                } else {
                    mn[i - 1] = RMSOption__IMAPS_NAMES[i];
                    mt[i - 1] = RMSOption__IMAPS_RMST[i];
                    mp[i - 1] = RMSOption__IMAPS_RMSP[i];
                    mc[(i - 1) * 3] = RMSOption__IMAPS_CENT[(i * 3)];
                    mc[(i - 1) * 3 + 1] = RMSOption__IMAPS_CENT[(i * 3) + 1];
                    mc[(i - 1) * 3 + 2] = RMSOption__IMAPS_CENT[(i * 3) + 2];
                }
                System.out.println("mn");
                for (int k = 0; k < 5; k++) {
                    System.out.print(mn[k] + ",");
                }
                System.out.println("mt");
                for (int k = 0; k < 5; k++) {
                    System.out.print(mt[k] + ",");
                }
                System.out.println("mp");
                for (int k = 0; k < 5; k++) {
                    System.out.print(mp[k] + ",");
                }
                System.out.println("mc");
                for (int k = 0; k < 5; k++) {
                    System.out.print(mc[k] + ",");
                }
            } else {
                try {
                } catch (Throwable tt) {
                }
                try {
                } catch (Throwable tt) {
                    return;
                }
            }
        } finally {
            assertTrue(mc[3] == 0f);
            assertTrue(mc[2] == 0f);
            assertTrue(mc[1] == 0f);
            assertTrue(mc[0] == 0f);
            assertTrue(mp[1] == null);
            assertEquals(null, mp[0]);
            assertTrue(mt[1] == null);
            assertEquals(null, mt[0]);
            assertTrue(mn[1] == null);
            assertEquals(null, mn[0]);
        }
    }
}
