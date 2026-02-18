package org.codehaus.mojo.license.extended.spreadsheet;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
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
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.IndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.mojo.license.download.ProjectLicense;
import org.codehaus.mojo.license.download.ProjectLicenseInfo;
import org.codehaus.mojo.license.extended.ExtendedInfo;
import org.codehaus.mojo.license.extended.InfoFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.GAP_WIDTH;
import static org.codehaus.mojo.license.extended.spreadsheet.SpreadsheetUtil.getDownloadColumn;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.codehaus.mojo.license.extended.spreadsheet.ExcelFileWriter.*;

public class ExcelFileWriterBlockTest {

    @Test
    public void testLine489() throws Exception {
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
    public void testLine491() throws Exception {
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
    public void testLine493() throws Exception {
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
