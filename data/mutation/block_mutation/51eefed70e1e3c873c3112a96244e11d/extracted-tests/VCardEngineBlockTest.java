package net.sourceforge.cardme.engine;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.types.Flow.Else;
import static org.blocktest.types.Flow.IfStmt;
import static org.blocktest.types.Flow.Then;
import static org.blocktest.utils.Constant.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;
import net.sourceforge.cardme.io.CompatibilityMode;
import net.sourceforge.cardme.util.Base64Wrapper;
import net.sourceforge.cardme.util.ISOUtils;
import net.sourceforge.cardme.util.VCardUtils;
import net.sourceforge.cardme.vcard.VCard;
import net.sourceforge.cardme.vcard.VCardImpl;
import net.sourceforge.cardme.vcard.arch.EncodingType;
import net.sourceforge.cardme.vcard.arch.VCardTypeName;
import net.sourceforge.cardme.vcard.arch.VCardVersion;
import net.sourceforge.cardme.vcard.errors.ErrorSeverity;
import net.sourceforge.cardme.vcard.errors.VCardError;
import net.sourceforge.cardme.vcard.exceptions.VCardParseException;
import net.sourceforge.cardme.vcard.features.AdrFeature;
import net.sourceforge.cardme.vcard.features.BDayFeature;
import net.sourceforge.cardme.vcard.features.EmailFeature;
import net.sourceforge.cardme.vcard.features.ImppFeature;
import net.sourceforge.cardme.vcard.features.KeyFeature;
import net.sourceforge.cardme.vcard.features.LabelFeature;
import net.sourceforge.cardme.vcard.features.LogoFeature;
import net.sourceforge.cardme.vcard.features.PhotoFeature;
import net.sourceforge.cardme.vcard.features.SoundFeature;
import net.sourceforge.cardme.vcard.features.TelFeature;
import net.sourceforge.cardme.vcard.features.TzFeature;
import net.sourceforge.cardme.vcard.features.UrlFeature;
import net.sourceforge.cardme.vcard.types.AbstractVCardType;
import net.sourceforge.cardme.vcard.types.AdrType;
import net.sourceforge.cardme.vcard.types.BDayType;
import net.sourceforge.cardme.vcard.types.BeginType;
import net.sourceforge.cardme.vcard.types.CategoriesType;
import net.sourceforge.cardme.vcard.types.ClassType;
import net.sourceforge.cardme.vcard.types.EmailType;
import net.sourceforge.cardme.vcard.types.EndType;
import net.sourceforge.cardme.vcard.types.ExtendedType;
import net.sourceforge.cardme.vcard.types.FNType;
import net.sourceforge.cardme.vcard.types.GeoType;
import net.sourceforge.cardme.vcard.types.ImppType;
import net.sourceforge.cardme.vcard.types.KeyType;
import net.sourceforge.cardme.vcard.types.LabelType;
import net.sourceforge.cardme.vcard.types.LogoType;
import net.sourceforge.cardme.vcard.types.MailerType;
import net.sourceforge.cardme.vcard.types.NType;
import net.sourceforge.cardme.vcard.types.NameType;
import net.sourceforge.cardme.vcard.types.NicknameType;
import net.sourceforge.cardme.vcard.types.NoteType;
import net.sourceforge.cardme.vcard.types.OrgType;
import net.sourceforge.cardme.vcard.types.PhotoType;
import net.sourceforge.cardme.vcard.types.ProdIdType;
import net.sourceforge.cardme.vcard.types.ProfileType;
import net.sourceforge.cardme.vcard.types.RevType;
import net.sourceforge.cardme.vcard.types.RoleType;
import net.sourceforge.cardme.vcard.types.SortStringType;
import net.sourceforge.cardme.vcard.types.SoundType;
import net.sourceforge.cardme.vcard.types.SourceType;
import net.sourceforge.cardme.vcard.types.TelType;
import net.sourceforge.cardme.vcard.types.TitleType;
import net.sourceforge.cardme.vcard.types.TzType;
import net.sourceforge.cardme.vcard.types.UidType;
import net.sourceforge.cardme.vcard.types.UrlType;
import net.sourceforge.cardme.vcard.types.VersionType;
import net.sourceforge.cardme.vcard.types.media.AudioMediaType;
import net.sourceforge.cardme.vcard.types.media.ImageMediaType;
import net.sourceforge.cardme.vcard.types.media.KeyTextType;
import net.sourceforge.cardme.vcard.types.params.AdrParamType;
import net.sourceforge.cardme.vcard.types.params.BDayParamType;
import net.sourceforge.cardme.vcard.types.params.EmailParamType;
import net.sourceforge.cardme.vcard.types.params.ExtendedParamType;
import net.sourceforge.cardme.vcard.types.params.ImppParamType;
import net.sourceforge.cardme.vcard.types.params.LabelParamType;
import net.sourceforge.cardme.vcard.types.params.TelParamType;
import net.sourceforge.cardme.vcard.types.params.TzParamType;
import net.sourceforge.cardme.vcard.types.params.UrlParamType;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Test;
import static org.junit.Assert.*;
import static net.sourceforge.cardme.engine.VCardEngine.*;

public class VCardEngineBlockTest {

    @Test
    public void testLine2355() throws Exception {
        String val = "foo,bar";
        String nam = null;
        boolean[] _blocktest_flow_testLine2355 = new boolean[1];
        try {
            if (val.indexOf(',') != -1) {
                _blocktest_flow_testLine2355[0] = true;
                String[] typeValueList = "foo,bar".split(",");
                for (String typeValue : typeValueList) {
                }
            } else {
            }
            return;
        } finally {
            for (int _blocktest_testLine2355_i = 0; _blocktest_testLine2355_i < 1; _blocktest_testLine2355_i += 1) {
                assertTrue(_blocktest_flow_testLine2355[_blocktest_testLine2355_i]);
            }
        }
    }

    @Test
    public void testLine2360() throws Exception {
        String val = "foobar";
        String nam = null;
        boolean[] _blocktest_flow_testLine2360 = new boolean[1];
        try {
            if (val.indexOf(',') != -1) {
                String[] typeValueList = "foo,bar".split(",");
                for (String typeValue : typeValueList) {
                }
            } else {
                _blocktest_flow_testLine2360[0] = true;
            }
            return;
        } finally {
            for (int _blocktest_testLine2360_i = 0; _blocktest_testLine2360_i < 1; _blocktest_testLine2360_i += 1) {
                assertTrue(_blocktest_flow_testLine2360[_blocktest_testLine2360_i]);
            }
        }
    }
}
