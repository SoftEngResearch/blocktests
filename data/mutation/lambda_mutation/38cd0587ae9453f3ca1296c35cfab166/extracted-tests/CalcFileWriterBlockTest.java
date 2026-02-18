package org.codehaus.mojo.license.extended.spreadsheet;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.maven.model.Developer;
import org.apache.maven.model.Organization;
import org.apache.maven.model.Scm;
import org.codehaus.mojo.license.download.ProjectLicense;
import org.codehaus.mojo.license.download.ProjectLicenseInfo;
import org.codehaus.mojo.license.extended.ExtendedInfo;
import org.codehaus.mojo.license.extended.InfoFile;
import org.odftoolkit.odfdom.doc.OdfSpreadsheetDocument;
import org.odftoolkit.odfdom.doc.table.OdfTable;
import org.odftoolkit.odfdom.doc.table.OdfTableCell;
import org.odftoolkit.odfdom.doc.table.OdfTableCellRange;
import org.odftoolkit.odfdom.doc.table.OdfTableColumn;
import org.odftoolkit.odfdom.doc.table.OdfTableRow;
import org.odftoolkit.odfdom.dom.OdfContentDom;
import org.odftoolkit.odfdom.dom.OdfSettingsDom;
import org.odftoolkit.odfdom.dom.element.config.ConfigConfigItemElement;
import org.odftoolkit.odfdom.dom.element.config.ConfigConfigItemMapEntryElement;
import org.odftoolkit.odfdom.dom.element.style.StyleParagraphPropertiesElement;
import org.odftoolkit.odfdom.dom.element.style.StyleTableCellPropertiesElement;
import org.odftoolkit.odfdom.dom.element.style.StyleTextPropertiesElement;
import org.odftoolkit.odfdom.dom.element.text.TextAElement;
import org.odftoolkit.odfdom.dom.style.OdfStyleFamily;
import org.odftoolkit.odfdom.dom.style.props.OdfTableColumnProperties;
import org.odftoolkit.odfdom.incubator.doc.office.OdfOfficeStyles;
import org.odftoolkit.odfdom.incubator.doc.style.OdfStyle;
import org.odftoolkit.odfdom.type.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.COPYRIGHT_JOIN_SEPARATOR;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.CurrentRowData;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.DEVELOPERS_COLUMNS;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.DEVELOPERS_END_COLUMN;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.DEVELOPERS_START_COLUMN;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.EXTENDED_INFO_END_COLUMN;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.EXTENDED_INFO_START_COLUMN;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.GAP_WIDTH;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.GENERAL_END_COLUMN;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.GENERAL_START_COLUMN;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.INCEPTION_YEAR_WIDTH;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.INFO_LICENSES_COLUMNS;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.INFO_LICENSES_END_COLUMN;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.INFO_LICENSES_START_COLUMN;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.INFO_NOTICES_COLUMNS;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.INFO_NOTICES_END_COLUMN;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.INFO_NOTICES_START_COLUMN;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.INFO_SPDX_COLUMNS;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.INFO_SPDX_END_COLUMN;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.INFO_SPDX_START_COLUMN;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.LICENSES_COLUMNS;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.LICENSES_END_COLUMN;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.LICENSES_START_COLUMN;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.MANIFEST_END_COLUMN;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.MANIFEST_START_COLUMN;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.MAVEN_END_COLUMN;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.MAVEN_START_COLUMN;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.MISC_COLUMNS;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.MISC_END_COLUMN;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.MISC_START_COLUMN;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.PLUGIN_ID_END_COLUMN;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.PLUGIN_ID_START_COLUMN;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.TABLE_NAME;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.TIMEZONE_WIDTH;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.getDownloadColumn;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.codehaus.mojo.license.extended.spreadsheet.CalcFileWriter.*;

public class CalcFileWriterBlockTest {

    @Test
    public void testLine614() throws Exception {
        InfoFile infoFile = new InfoFile();
        List<InfoFile> notices = new ArrayList<>();
        List<InfoFile> licenses = new ArrayList<>();
        List<InfoFile> spdxs = new ArrayList<>();
        infoFile.setType(InfoFile.Type.LICENSE);
        infoFile.setFileName("hello.txt");
        switch(infoFile.getType()) {
            case LICENSE:
                licenses.add(infoFile);
                break;
            case NOTICE:
                notices.add(infoFile);
                break;
            case SPDX_LICENSE:
                spdxs.add(infoFile);
                break;
            default:
        }
        assertEquals("hello.txt", licenses.iterator().next().getFileName());
        assertFalse(licenses.isEmpty());
    }

    @Test
    public void testLine616() throws Exception {
        InfoFile infoFile = new InfoFile();
        List<InfoFile> notices = new ArrayList<>();
        List<InfoFile> licenses = new ArrayList<>();
        List<InfoFile> spdxs = new ArrayList<>();
        infoFile.setType(InfoFile.Type.NOTICE);
        infoFile.setFileName("hello.txt");
        switch(infoFile.getType()) {
            case LICENSE:
                licenses.add(infoFile);
                break;
            case NOTICE:
                notices.add(infoFile);
                break;
            case SPDX_LICENSE:
                spdxs.add(infoFile);
                break;
            default:
        }
        assertEquals("hello.txt", notices.iterator().next().getFileName());
        assertFalse(notices.isEmpty());
    }

    @Test
    public void testLine618() throws Exception {
        InfoFile infoFile = new InfoFile();
        List<InfoFile> notices = new ArrayList<>();
        List<InfoFile> licenses = new ArrayList<>();
        List<InfoFile> spdxs = new ArrayList<>();
        infoFile.setType(InfoFile.Type.SPDX_LICENSE);
        infoFile.setFileName("hello.txt");
        switch(infoFile.getType()) {
            case LICENSE:
                licenses.add(infoFile);
                break;
            case NOTICE:
                notices.add(infoFile);
                break;
            case SPDX_LICENSE:
                spdxs.add(infoFile);
                break;
            default:
        }
        assertEquals("hello.txt", spdxs.iterator().next().getFileName());
        assertFalse(spdxs.isEmpty());
    }
}
