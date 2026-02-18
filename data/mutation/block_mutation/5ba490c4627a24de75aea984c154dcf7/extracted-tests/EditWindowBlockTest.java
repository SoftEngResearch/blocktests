package neo.ui;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.nio.ByteBuffer;
import static neo.Renderer.Material.SS_GUI;
import neo.Renderer.Material.idMaterial;
import static neo.TempDump.NOT;
import static neo.TempDump.ctos;
import static neo.TempDump.etoi;
import static neo.TempDump.isNotNullOrEmpty;
import static neo.TempDump.itob;
import static neo.framework.CVarSystem.cvarSystem;
import neo.framework.CVarSystem.idCVar;
import static neo.framework.DeclManager.declManager;
import static neo.framework.FileSystem_h.fileSystem;
import static neo.framework.KeyInput.K_BACKSPACE;
import static neo.framework.KeyInput.K_CTRL;
import static neo.framework.KeyInput.K_DEL;
import static neo.framework.KeyInput.K_DOWNARROW;
import static neo.framework.KeyInput.K_END;
import static neo.framework.KeyInput.K_ENTER;
import static neo.framework.KeyInput.K_ESCAPE;
import static neo.framework.KeyInput.K_HOME;
import static neo.framework.KeyInput.K_INS;
import static neo.framework.KeyInput.K_KP_ENTER;
import static neo.framework.KeyInput.K_LEFTARROW;
import static neo.framework.KeyInput.K_RIGHTARROW;
import static neo.framework.KeyInput.K_UPARROW;
import neo.framework.KeyInput.idKeyInput;
import static neo.idlib.Lib.colorWhite;
import static neo.idlib.Lib.idLib.common;
import neo.idlib.Text.Parser.idParser;
import neo.idlib.Text.Str.idStr;
import neo.idlib.containers.List.idList;
import neo.idlib.math.Math_h.idMath;
import neo.idlib.math.Vector.idVec4;
import static neo.sys.sys_public.sysEventType_t.SE_CHAR;
import static neo.sys.sys_public.sysEventType_t.SE_KEY;
import neo.sys.sys_public.sysEvent_s;
import static neo.sys.win_input.Sys_GetConsoleKey;
import neo.ui.DeviceContext.idDeviceContext;
import neo.ui.Rectangle.idRectangle;
import neo.ui.SimpleWindow.drawWin_t;
import neo.ui.SliderWindow.idSliderWindow;
import neo.ui.UserInterfaceLocal.idUserInterfaceLocal;
import static neo.ui.Window.WIN_CANFOCUS;
import static neo.ui.Window.WIN_FOCUS;
import neo.ui.Window.idWindow;
import static neo.ui.Window.idWindow.ON.ON_ACTION;
import static neo.ui.Window.idWindow.ON.ON_ACTIONRELEASE;
import static neo.ui.Window.idWindow.ON.ON_ENTER;
import static neo.ui.Window.idWindow.ON.ON_ENTERRELEASE;
import static neo.ui.Window.idWindow.ON.ON_ESC;
import neo.ui.Winvar.idWinBool;
import neo.ui.Winvar.idWinStr;
import neo.ui.Winvar.idWinVar;
import org.junit.Test;
import static org.junit.Assert.*;
import static neo.ui.EditWindow.*;

public class EditWindowBlockTest {

    @Test
    public void testLine294() throws Exception {
        char[] idEditWindow__buffer = new char[] { 'a', 'b', 'c', ' ', ' ', 'd', 'e' };
        int cursorPos = 1;
        int len = 7;
        idKeyInput.Init();
        idKeyInput.PreliminaryKeyEvent(K_CTRL, true);
        if (idKeyInput.IsDown(K_CTRL)) {
            // skip to next word
            while ((cursorPos < len) && (idEditWindow__buffer[cursorPos] != ' ')) {
                cursorPos++;
            }
            while ((cursorPos < len) && (idEditWindow__buffer[cursorPos] == ' ')) {
                cursorPos++;
            }
        } else {
            if (cursorPos < len) {
            }
        }
        assertEquals(5, cursorPos);
    }

    @Test
    public void testLine296() throws Exception {
        char[] idEditWindow__buffer = new char[] { 'a', 'b', 'c', ' ', ' ', 'd', 'e' };
        int cursorPos = 1;
        int len = 2;
        idKeyInput.Init();
        idKeyInput.PreliminaryKeyEvent(K_CTRL, true);
        if (idKeyInput.IsDown(K_CTRL)) {
            // skip to next word
            while ((cursorPos < len) && (idEditWindow__buffer[cursorPos] != ' ')) {
                cursorPos++;
            }
            while ((cursorPos < len) && (idEditWindow__buffer[cursorPos] == ' ')) {
                cursorPos++;
            }
        } else {
            if (cursorPos < len) {
            }
        }
        assertEquals(2, cursorPos);
    }

    @Test
    public void testLine298() throws Exception {
        char[] idEditWindow__buffer = new char[] { 'a', 'b', 'c', ' ', ' ', 'd', 'e', 'f' };
        int cursorPos = 1;
        int len = 6;
        idKeyInput.Init();
        idKeyInput.PreliminaryKeyEvent(K_CTRL, true);
        if (idKeyInput.IsDown(K_CTRL)) {
            // skip to next word
            while ((cursorPos < len) && (idEditWindow__buffer[cursorPos] != ' ')) {
                cursorPos++;
            }
            while ((cursorPos < len) && (idEditWindow__buffer[cursorPos] == ' ')) {
                cursorPos++;
            }
        } else {
            if (cursorPos < len) {
            }
        }
        assertEquals(5, cursorPos);
    }

    @Test
    public void testLine300() throws Exception {
        char[] idEditWindow__buffer = new char[] { 'a', 'b', 'c', ' ', ' ', 'd', 'e', 'f' };
        int cursorPos = 1;
        int len = 4;
        idKeyInput.Init();
        idKeyInput.PreliminaryKeyEvent(K_CTRL, true);
        if (idKeyInput.IsDown(K_CTRL)) {
            // skip to next word
            while ((cursorPos < len) && (idEditWindow__buffer[cursorPos] != ' ')) {
                cursorPos++;
            }
            while ((cursorPos < len) && (idEditWindow__buffer[cursorPos] == ' ')) {
                cursorPos++;
            }
        } else {
            if (cursorPos < len) {
            }
        }
        assertEquals(4, cursorPos);
    }
}
