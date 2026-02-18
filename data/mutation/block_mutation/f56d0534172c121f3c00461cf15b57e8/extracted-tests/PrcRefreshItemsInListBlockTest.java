package org.beigesoft.webstore.processor;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.util.Arrays;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.math.BigDecimal;
import org.beigesoft.exception.ExceptionWithCode;
import org.beigesoft.model.IHasIdLongVersionName;
import org.beigesoft.model.IRequestData;
import org.beigesoft.comparator.CmprHasVersion;
import org.beigesoft.service.IProcessor;
import org.beigesoft.service.ISrvDatabase;
import org.beigesoft.service.ISrvOrm;
import org.beigesoft.service.ISrvNumberToString;
import org.beigesoft.accounting.persistable.ServiceToSale;
import org.beigesoft.accounting.persistable.InvItem;
import org.beigesoft.accounting.persistable.I18nInvItem;
import org.beigesoft.accounting.persistable.I18nServiceToSale;
import org.beigesoft.accounting.persistable.I18nUnitOfMeasure;
import org.beigesoft.accounting.persistable.UnitOfMeasure;
import org.beigesoft.webstore.model.ESpecificsItemType;
import org.beigesoft.webstore.model.EShopItemType;
import org.beigesoft.persistable.Languages;
import org.beigesoft.persistable.LangPreferences;
import org.beigesoft.persistable.AI18nName;
import org.beigesoft.webstore.persistable.base.AItemSpecifics;
import org.beigesoft.webstore.persistable.base.AItemPlace;
import org.beigesoft.webstore.persistable.base.AItemPrice;
import org.beigesoft.webstore.persistable.GoodsSpecifics;
import org.beigesoft.webstore.persistable.SpecificsOfItem;
import org.beigesoft.webstore.persistable.ChooseableSpecifics;
import org.beigesoft.webstore.persistable.SpecificsOfItemGroup;
import org.beigesoft.webstore.persistable.HtmlTemplate;
import org.beigesoft.webstore.persistable.PriceGoods;
import org.beigesoft.webstore.persistable.GoodsPlace;
import org.beigesoft.webstore.persistable.ServicePlace;
import org.beigesoft.webstore.persistable.ServicePrice;
import org.beigesoft.webstore.persistable.ServiceSpecifics;
import org.beigesoft.webstore.persistable.SeService;
import org.beigesoft.webstore.persistable.I18nSeService;
import org.beigesoft.webstore.persistable.SeServicePlace;
import org.beigesoft.webstore.persistable.SeServicePrice;
import org.beigesoft.webstore.persistable.SeServiceSpecifics;
import org.beigesoft.webstore.persistable.SeGoods;
import org.beigesoft.webstore.persistable.I18nSeGoods;
import org.beigesoft.webstore.persistable.SeGoodsPlace;
import org.beigesoft.webstore.persistable.SeGoodsPrice;
import org.beigesoft.webstore.persistable.SeGoodsSpecifics;
import org.beigesoft.webstore.persistable.GoodsInListLuv;
import org.beigesoft.webstore.persistable.TradingSettings;
import org.beigesoft.webstore.persistable.SettingsAdd;
import org.beigesoft.webstore.persistable.ItemInList;
import org.beigesoft.webstore.persistable.I18nChooseableSpecifics;
import org.beigesoft.webstore.persistable.I18nSpecificsOfItem;
import org.beigesoft.webstore.persistable.I18nSpecificsOfItemGroup;
import org.beigesoft.webstore.persistable.I18nSpecificInList;
import org.beigesoft.webstore.persistable.PriceCategory;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.beigesoft.webstore.processor.PrcRefreshItemsInList.*;

public class PrcRefreshItemsInListBlockTest {

    @Test
    public void testLine782() throws Exception {
        Set<Long> htmlTemplatesIds = new HashSet<>(Arrays.asList(1L, 2L, 3L));
        boolean isFirst = true;
        StringBuffer whereStr = new StringBuffer("where ITSID in (");
        for (Long id : htmlTemplatesIds) {
            if (isFirst) {
                isFirst = false;
            } else {
                whereStr.append(", ");
            }
            whereStr.append(id.toString());
        }
        whereStr.append(")");
        assertEquals("where ITSID in (1, 2, 3)", whereStr.toString());
    }
}
