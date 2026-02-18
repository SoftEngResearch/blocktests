package neo.ui;

import static neo.Renderer.Material.MF_DEFAULTED;
import static neo.Renderer.Material.SS_GUI;
import neo.Renderer.Material.idMaterial;
import static neo.Renderer.RenderSystem_init.r_skipGuiShaders;
import static neo.TempDump.NOT;
import static neo.TempDump.atof;
import static neo.TempDump.atoi;
import static neo.TempDump.btoi;
import static neo.TempDump.etoi;
import static neo.TempDump.isNotNullOrEmpty;
import static neo.TempDump.itob;
import static neo.TempDump.sizeof;
import static neo.framework.CVarSystem.CVAR_BOOL;
import static neo.framework.CVarSystem.CVAR_GUI;
import neo.framework.CVarSystem.idCVar;
import static neo.framework.Common.EDITOR_GUI;
import static neo.framework.Common.com_editors;
import static neo.framework.Common.common;
import static neo.framework.DeclManager.declManager;
import static neo.framework.DeclManager.declType_t.DECL_TABLE;
import neo.framework.DeclTable.idDeclTable;
import neo.framework.DemoFile.idDemoFile;
import neo.framework.File_h.idFile;
import static neo.framework.KeyInput.K_ENTER;
import static neo.framework.KeyInput.K_ESCAPE;
import static neo.framework.KeyInput.K_MOUSE1;
import static neo.framework.KeyInput.K_MOUSE2;
import static neo.framework.KeyInput.K_MOUSE3;
import static neo.framework.KeyInput.K_SHIFT;
import static neo.framework.KeyInput.K_TAB;
import neo.framework.KeyInput.idKeyInput;
import static neo.framework.Session.session;
import static neo.framework.UsercmdGen.USERCMD_MSEC;
import neo.idlib.Dict_h.idDict;
import neo.idlib.Dict_h.idKeyValue;
import static neo.idlib.Lib.colorBlack;
import static neo.idlib.Text.Lexer.LEXFL_ALLOWBACKSLASHSTRINGCONCAT;
import static neo.idlib.Text.Lexer.LEXFL_ALLOWMULTICHARLITERALS;
import static neo.idlib.Text.Lexer.LEXFL_NOFATALERRORS;
import static neo.idlib.Text.Lexer.LEXFL_NOSTRINGCONCAT;
import neo.idlib.Text.Parser.idParser;
import neo.idlib.Text.Str.idStr;
import static neo.idlib.Text.Str.va;
import static neo.idlib.Text.Token.TT_FLOAT;
import static neo.idlib.Text.Token.TT_INTEGER;
import static neo.idlib.Text.Token.TT_NAME;
import static neo.idlib.Text.Token.TT_NUMBER;
import neo.idlib.Text.Token.idToken;
import neo.idlib.containers.List.idList;
import neo.idlib.math.Interpolate.idInterpolateAccelDecelLinear;
import neo.idlib.math.Matrix.idMat3;
import static neo.idlib.math.Matrix.idMat3.getMat3_identity;
import neo.idlib.math.Rotation.idRotation;
import static neo.idlib.math.Vector.getVec3_origin;
import neo.idlib.math.Vector.idVec2;
import neo.idlib.math.Vector.idVec3;
import neo.idlib.math.Vector.idVec4;
import static neo.idlib.precompiled.MAX_EXPRESSION_OPS;
import static neo.idlib.precompiled.MAX_EXPRESSION_REGISTERS;
import static neo.sys.sys_public.sysEventType_t.SE_CHAR;
import static neo.sys.sys_public.sysEventType_t.SE_KEY;
import static neo.sys.sys_public.sysEventType_t.SE_MOUSE;
import static neo.sys.sys_public.sysEventType_t.SE_NONE;
import neo.sys.sys_public.sysEvent_s;
import neo.ui.BindWindow.idBindWindow;
import neo.ui.ChoiceWindow.idChoiceWindow;
import neo.ui.DeviceContext.idDeviceContext;
import static neo.ui.DeviceContext.idDeviceContext.CURSOR.CURSOR_ARROW;
import static neo.ui.DeviceContext.idDeviceContext.CURSOR.CURSOR_HAND;
import neo.ui.EditWindow.idEditWindow;
import neo.ui.FieldWindow.idFieldWindow;
import neo.ui.GameBearShootWindow.idGameBearShootWindow;
import neo.ui.GameBustOutWindow.idGameBustOutWindow;
import neo.ui.GameSSDWindow.idGameSSDWindow;
import neo.ui.GuiScript.idGuiScript;
import neo.ui.GuiScript.idGuiScriptList;
import neo.ui.ListWindow.idListWindow;
import neo.ui.MarkerWindow.idMarkerWindow;
import neo.ui.Rectangle.idRectangle;
import neo.ui.RegExp.idRegister;
import static neo.ui.RegExp.idRegister.REGTYPE.BOOL;
import static neo.ui.RegExp.idRegister.REGTYPE.FLOAT;
import static neo.ui.RegExp.idRegister.REGTYPE.RECTANGLE;
import static neo.ui.RegExp.idRegister.REGTYPE.STRING;
import static neo.ui.RegExp.idRegister.REGTYPE.VEC2;
import static neo.ui.RegExp.idRegister.REGTYPE.VEC4;
import neo.ui.RegExp.idRegisterList;
import neo.ui.RenderWindow.idRenderWindow;
import neo.ui.SimpleWindow.drawWin_t;
import neo.ui.SimpleWindow.idSimpleWindow;
import neo.ui.SliderWindow.idSliderWindow;
import neo.ui.UserInterfaceLocal.idUserInterfaceLocal;
import static neo.ui.Window.idWindow.ON.ON_ACTION;
import static neo.ui.Window.idWindow.ON.ON_ACTIONRELEASE;
import static neo.ui.Window.idWindow.ON.ON_ACTIVATE;
import static neo.ui.Window.idWindow.ON.ON_DEACTIVATE;
import static neo.ui.Window.idWindow.ON.ON_ESC;
import static neo.ui.Window.idWindow.ON.ON_FRAME;
import static neo.ui.Window.idWindow.ON.ON_MOUSEENTER;
import static neo.ui.Window.idWindow.ON.ON_MOUSEEXIT;
import static neo.ui.Window.idWindow.ON.ON_TRIGGER;
import static neo.ui.Window.idWindow.ON.SCRIPT_COUNT;
import static neo.ui.Window.wexpOpType_t.WOP_TYPE_ADD;
import static neo.ui.Window.wexpOpType_t.WOP_TYPE_AND;
import static neo.ui.Window.wexpOpType_t.WOP_TYPE_COND;
import static neo.ui.Window.wexpOpType_t.WOP_TYPE_DIVIDE;
import static neo.ui.Window.wexpOpType_t.WOP_TYPE_EQ;
import static neo.ui.Window.wexpOpType_t.WOP_TYPE_GE;
import static neo.ui.Window.wexpOpType_t.WOP_TYPE_GT;
import static neo.ui.Window.wexpOpType_t.WOP_TYPE_LE;
import static neo.ui.Window.wexpOpType_t.WOP_TYPE_LT;
import static neo.ui.Window.wexpOpType_t.WOP_TYPE_MOD;
import static neo.ui.Window.wexpOpType_t.WOP_TYPE_MULTIPLY;
import static neo.ui.Window.wexpOpType_t.WOP_TYPE_NE;
import static neo.ui.Window.wexpOpType_t.WOP_TYPE_OR;
import static neo.ui.Window.wexpOpType_t.WOP_TYPE_SUBTRACT;
import static neo.ui.Window.wexpOpType_t.WOP_TYPE_TABLE;
import static neo.ui.Window.wexpOpType_t.WOP_TYPE_VAR;
import static neo.ui.Window.wexpOpType_t.WOP_TYPE_VARB;
import static neo.ui.Window.wexpOpType_t.WOP_TYPE_VARF;
import static neo.ui.Window.wexpOpType_t.WOP_TYPE_VARI;
import static neo.ui.Window.wexpOpType_t.WOP_TYPE_VARS;
import static neo.ui.Window.wexpRegister_t.WEXP_REG_NUM_PREDEFINED;
import static neo.ui.Window.wexpRegister_t.WEXP_REG_TIME;
import static neo.ui.Winvar.VAR_GUIPREFIX;
import neo.ui.Winvar.idWinBackground;
import neo.ui.Winvar.idWinBool;
import neo.ui.Winvar.idWinFloat;
import neo.ui.Winvar.idWinInt;
import neo.ui.Winvar.idWinRectangle;
import neo.ui.Winvar.idWinStr;
import neo.ui.Winvar.idWinVar;
import neo.ui.Winvar.idWinVec4;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static neo.ui.Window.*;

public class WindowBlockTest {

    @Test
    public void testLine1707() throws Exception {
        int children__oGet_i___flags = 0x00000000;
        int flags = 0x11111111;
        try {
            children__oGet_i___flags ^= Window.WIN_SELECTED;
            if ((children__oGet_i___flags & Window.WIN_SELECTED) != 0) {
                flags &= ~Window.WIN_SELECTED;
                {
                    assertEquals("childsel", ("childsel"));
                    return;
                }
            }
        } finally {
            assertEquals(286327057, flags);
        }
    }

    @Test
    public void testLine1708() throws Exception {
        int children__oGet_i___flags = 0x00001000;
        int flags = 0x11111111;
        try {
            children__oGet_i___flags ^= Window.WIN_SELECTED;
            if ((children__oGet_i___flags & Window.WIN_SELECTED) != 0) {
                flags &= ~Window.WIN_SELECTED;
                {
                    assertEquals("NOTchildsel", ("childsel"));
                    return;
                }
            }
        } finally {
            assertEquals(0x11111111, flags);
        }
    }

    @Test
    public void testLine1645() throws Exception {
        String childRet = "foo";
        int child__flags = 0;
        try {
            if (childRet != null && !childRet.isEmpty()) {
                {
                    assertEquals("foo", (childRet));
                    return;
                }
            }
            if ((child__flags & Window.WIN_MODAL) != 0) {
            }
        } finally {
        }
    }

    @Test
    public void testLine1646() throws Exception {
        String childRet = "";
        int child__flags = 0x000001FF;
        try {
            if (childRet != null && !childRet.isEmpty()) {
                {
                    assertEquals("RANDOM", (childRet));
                    return;
                }
            }
            if ((child__flags & Window.WIN_MODAL) != 0) {
            }
        } finally {
        }
    }

    @Test
    public void testLine1647() throws Exception {
        String childRet = null;
        int child__flags = 0x000002FF;
        try {
            if (childRet != null && !childRet.isEmpty()) {
                {
                    assertEquals("", (childRet));
                    return;
                }
            }
            if ((child__flags & Window.WIN_MODAL) != 0) {
            }
        } finally {
        }
    }
}
