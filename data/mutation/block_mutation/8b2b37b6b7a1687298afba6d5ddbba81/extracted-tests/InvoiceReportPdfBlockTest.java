package org.beigesoft.accounting.report;

import org.beigesoft.accounting.persistable.IdI18nCurrency;
import org.beigesoft.persistable.Languages;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Locale;
import java.math.BigDecimal;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import org.beigesoft.doc.model.Document;
import org.beigesoft.doc.model.DocTable;
import org.beigesoft.doc.model.EWraping;
import org.beigesoft.doc.model.EAlignHorizontal;
import org.beigesoft.doc.model.EUnitOfMeasure;
import org.beigesoft.doc.service.IDocumentMaker;
import org.beigesoft.pdf.model.PdfDocument;
import org.beigesoft.pdf.service.IPdfFactory;
import org.beigesoft.pdf.service.IPdfMaker;
import org.beigesoft.model.IRequestData;
import org.beigesoft.service.ISrvI18n;
import org.beigesoft.service.ISrvNumberToString;
import org.beigesoft.service.IEntityFileReporter;
import org.beigesoft.service.ISrvOrm;
import org.beigesoft.accounting.service.ISrvAccSettings;
import org.beigesoft.accounting.persistable.SalesInvoice;
import org.beigesoft.accounting.persistable.SalesInvoiceLine;
import org.beigesoft.accounting.persistable.SalesInvoiceTaxLine;
import org.beigesoft.accounting.persistable.SalesInvoiceServiceLine;
import org.beigesoft.accounting.persistable.AccSettings;
import org.beigesoft.accounting.persistable.I18nAccounting;
import org.beigesoft.accounting.persistable.Currency;
import org.beigesoft.accounting.persistable.I18nCurrency;
import org.beigesoft.accounting.persistable.I18nBuyer;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.beigesoft.accounting.report.InvoiceReportPdf.*;

public class InvoiceReportPdfBlockTest {

    @Test
    public void testLine148() throws Exception {
        List<I18nCurrency> i18nCurrencyLst = new java.util.ArrayList<I18nCurrency>();
        String lang = "baz2";
        Currency currency = new Currency();
        I18nCurrency i18nCurrency = null;
        I18nCurrency x = new I18nCurrency();
        Currency y = new Currency();
        Languages z = new Languages();
        y.setItsId(123L);
        z.setItsName("foo");
        z.setItsId("baz");
        x.setLang(z);
        x.setHasName(y);
        i18nCurrencyLst.add(x);
        I18nCurrency x2 = new I18nCurrency();
        Currency y2 = new Currency();
        Languages z2 = new Languages();
        y2.setItsId(456L);
        z2.setItsName("bar");
        z2.setItsId("baz2");
        x2.setLang(z2);
        x2.setHasName(y2);
        i18nCurrencyLst.add(x2);
        currency.setItsId(456L);
        for (I18nCurrency i18nCur : i18nCurrencyLst) {
            if (i18nCur.getHasName().getItsId().equals(currency.getItsId()) && i18nCur.getLang().getItsId().equals(lang)) {
                i18nCurrency = i18nCur;
                break;
            }
            continue;
        }
        assertEquals("bar", i18nCurrency.getLang().getItsName());
        assertTrue(i18nCurrency.getHasName().getItsId() == 456L);
        assertTrue(i18nCurrency != null);
    }
}
