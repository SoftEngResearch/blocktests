package org.beigesoft.webstore.processor;

/*
 * Copyright (c) 2018 Beigesoft™
 *
 * Licensed under the GNU General Public License (GPL), Version 2.0
 * (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;

import java.util.Arrays;import java.util.Map;
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

/**
 * <p>Service that refresh webstore item in ItemInList according current
 * GoodsAvailiable, GoodsSpecifics, PriceGoods, GoodsRating, etc.
 * This is non-public processor.</p>
 *
 * @param <RS> platform dependent record set type
 * @author Yury Demidenko
 */
public class PrcRefreshItemsInList<RS> implements IProcessor {

  /**
   * <p>Database service.</p>
   **/
  private ISrvDatabase<RS> srvDatabase;

  /**
   * <p>ORM service.</p>
   **/
  private ISrvOrm<RS> srvOrm;

  /**
   * <p>Service print number.</p>
   **/
  private ISrvNumberToString srvNumberToString;

  /**
   * <p>Process entity request.</p>
   * @param pReqVars additional param
   * @param pRequestData Request Data
   * @throws Exception - an exception
   **/
  @Override
  public final void process(final Map<String, Object> pReqVars,
                            final IRequestData pRequestData) throws Exception {
    retrieveStartData(pReqVars);
    List<PriceCategory> defPcs = this.srvOrm.retrieveListWithConditions(pReqVars, PriceCategory.class, "where DFOL=1");
    if (defPcs.size() != 1) {
      throw new ExceptionWithCode(ExceptionWithCode.CONFIGURATION_MISTAKE, "where_is_no_only_default_online_price_category");
    }
    SettingsAdd settingsAdd = (SettingsAdd) pReqVars.get("setAdd");
    TradingSettings tradSet = (TradingSettings) pReqVars.get("tradSet");
    GoodsInListLuv goodsInListLuv = (GoodsInListLuv) pReqVars.get("goodsInListLuv");
    pReqVars.remove("goodsInListLuv");
    String refreshAll = pRequestData.getParameter("refreshAll");
    List<GoodsSpecifics> goodsSpecificsLst;
    if (refreshAll != null) {
      goodsSpecificsLst = retrieveItemSpecificsLst(pReqVars, null, GoodsSpecifics.class);
    } else {
      goodsSpecificsLst = retrieveItemSpecificsLst(pReqVars, goodsInListLuv.getGoodsSpecificLuv(), GoodsSpecifics.class);
    }
    updateForItemSpecificsList(pReqVars, goodsSpecificsLst, settingsAdd, goodsInListLuv, tradSet, I18nInvItem.class, EShopItemType.GOODS);
    pRequestData.setAttribute("totalUpdatedGdSp", goodsSpecificsLst.size());
    goodsSpecificsLst = null;
    List<PriceGoods> goodsPriceLst;
    if (refreshAll != null) {
      goodsPriceLst = retrieveItemPriceLst(pReqVars, null, PriceGoods.class, defPcs.get(0).getItsId());
    } else {
      goodsPriceLst = retrieveItemPriceLst(pReqVars, goodsInListLuv.getGoodsPriceLuv(), PriceGoods.class, defPcs.get(0).getItsId());
    }
    updateForItemPriceList(pReqVars, goodsPriceLst, settingsAdd, goodsInListLuv, EShopItemType.GOODS);
    pRequestData.setAttribute("totalUpdatedGdPr", goodsPriceLst.size());
    goodsPriceLst = null;
    List<GoodsPlace> goodsPlaceLst;
    if (refreshAll != null) {
      goodsPlaceLst = retrieveItemPlaceLst(pReqVars, null, GoodsPlace.class);
    } else {
      goodsPlaceLst = retrieveItemPlaceLst(pReqVars, goodsInListLuv.getGoodsAvailableLuv(), GoodsPlace.class);
    }
    updateForItemPlaceList(pReqVars, goodsPlaceLst, settingsAdd, goodsInListLuv, EShopItemType.GOODS);
    pRequestData.setAttribute("totalUpdatedGdAv", goodsPlaceLst.size());
    goodsPlaceLst = null;
    List<ServiceSpecifics> serviceSpecificsLst;
    if (refreshAll != null) {
      serviceSpecificsLst = retrieveItemSpecificsLst(pReqVars, null, ServiceSpecifics.class);
    } else {
      serviceSpecificsLst = retrieveItemSpecificsLst(pReqVars, goodsInListLuv.getServiceSpecificLuv(), ServiceSpecifics.class);
    }
    updateForItemSpecificsList(pReqVars, serviceSpecificsLst, settingsAdd, goodsInListLuv, tradSet, I18nServiceToSale.class, EShopItemType.SERVICE);
    pRequestData.setAttribute("totalUpdatedServSp", serviceSpecificsLst.size());
    serviceSpecificsLst = null;
    List<ServicePrice> servicePriceLst;
    if (refreshAll != null) {
      servicePriceLst = retrieveItemPriceLst(pReqVars, null, ServicePrice.class, defPcs.get(0).getItsId());
    } else {
      servicePriceLst = retrieveItemPriceLst(pReqVars, goodsInListLuv.getServicePriceLuv(), ServicePrice.class, defPcs.get(0).getItsId());
    }
    updateForItemPriceList(pReqVars, servicePriceLst, settingsAdd, goodsInListLuv, EShopItemType.SERVICE);
    pRequestData.setAttribute("totalUpdatedServPr", servicePriceLst.size());
    servicePriceLst = null;
    List<ServicePlace> servicePlaceLst;
    if (refreshAll != null) {
      servicePlaceLst = retrieveItemPlaceLst(pReqVars, null, ServicePlace.class);
    } else {
      servicePlaceLst = retrieveItemPlaceLst(pReqVars, goodsInListLuv.getServicePlaceLuv(), ServicePlace.class);
    }
    updateForItemPlaceList(pReqVars, servicePlaceLst, settingsAdd, goodsInListLuv, EShopItemType.SERVICE);
    pRequestData.setAttribute("totalUpdatedServAv", servicePlaceLst.size());
    servicePlaceLst = null;
    List<SeGoodsSpecifics> seGoodSpecificsLst;
    if (refreshAll != null) {
      seGoodSpecificsLst = retrieveItemSpecificsLst(pReqVars, null, SeGoodsSpecifics.class);
    } else {
      seGoodSpecificsLst = retrieveItemSpecificsLst(pReqVars, goodsInListLuv.getSeGoodSpecificLuv(), SeGoodsSpecifics.class);
    }
    updateForItemSpecificsList(pReqVars, seGoodSpecificsLst, settingsAdd, goodsInListLuv, tradSet, I18nSeGoods.class, EShopItemType.SEGOODS);
    pRequestData.setAttribute("totalUpdatedSeGoodSp", seGoodSpecificsLst.size());
    seGoodSpecificsLst = null;
    List<SeGoodsPrice> seGoodPriceLst;
    if (refreshAll != null) {
      seGoodPriceLst = retrieveItemPriceLst(pReqVars, null, SeGoodsPrice.class, defPcs.get(0).getItsId());
    } else {
      seGoodPriceLst = retrieveItemPriceLst(pReqVars, goodsInListLuv.getSeGoodPriceLuv(), SeGoodsPrice.class, defPcs.get(0).getItsId());
    }
    updateForItemPriceList(pReqVars, seGoodPriceLst, settingsAdd, goodsInListLuv, EShopItemType.SEGOODS);
    pRequestData.setAttribute("totalUpdatedSeGoodPr", seGoodPriceLst.size());
    seGoodPriceLst = null;
    List<SeGoodsPlace> seGoodPlaceLst;
    if (refreshAll != null) {
      seGoodPlaceLst = retrieveItemPlaceLst(pReqVars, null, SeGoodsPlace.class);
    } else {
      seGoodPlaceLst = retrieveItemPlaceLst(pReqVars, goodsInListLuv.getSeGoodPlaceLuv(), SeGoodsPlace.class);
    }
    updateForItemPlaceList(pReqVars, seGoodPlaceLst, settingsAdd, goodsInListLuv, EShopItemType.SEGOODS);
    pRequestData.setAttribute("totalUpdatedSeGoodAv", seGoodPlaceLst.size());
    seGoodPlaceLst = null;
    List<SeServiceSpecifics> seServiceSpecificsLst;
    if (refreshAll != null) {
      seServiceSpecificsLst = retrieveItemSpecificsLst(pReqVars, null, SeServiceSpecifics.class);
    } else {
      seServiceSpecificsLst = retrieveItemSpecificsLst(pReqVars, goodsInListLuv.getSeServiceSpecificLuv(), SeServiceSpecifics.class);
    }
    updateForItemSpecificsList(pReqVars, seServiceSpecificsLst, settingsAdd, goodsInListLuv, tradSet, I18nSeService.class, EShopItemType.SESERVICE);
    pRequestData.setAttribute("totalUpdatedSeServiceSp", seServiceSpecificsLst.size());
    seServiceSpecificsLst = null;
    List<SeServicePrice> seServicePriceLst;
    if (refreshAll != null) {
      seServicePriceLst = retrieveItemPriceLst(pReqVars, null, SeServicePrice.class, defPcs.get(0).getItsId());
    } else {
      seServicePriceLst = retrieveItemPriceLst(pReqVars, goodsInListLuv.getSeServicePriceLuv(), SeServicePrice.class, defPcs.get(0).getItsId());
    }
    updateForItemPriceList(pReqVars, seServicePriceLst, settingsAdd, goodsInListLuv, EShopItemType.SESERVICE);
    pRequestData.setAttribute("totalUpdatedSeServicePr", seServicePriceLst.size());
    seServicePriceLst = null;
    List<SeServicePlace> seServicePlaceLst;
    if (refreshAll != null) {
      seServicePlaceLst = retrieveItemPlaceLst(pReqVars, null, SeServicePlace.class);
    } else {
      seServicePlaceLst = retrieveItemPlaceLst(pReqVars, goodsInListLuv.getSeServicePlaceLuv(), SeServicePlace.class);
    }
    updateForItemPlaceList(pReqVars, seServicePlaceLst, settingsAdd, goodsInListLuv, EShopItemType.SESERVICE);
    pRequestData.setAttribute("totalUpdatedSeServiceAv", seServicePlaceLst.size());
    seServicePlaceLst = null;
    pReqVars.remove("langPreferences");
  }

  /**
   * <p>Retrieve start data.</p>
   * @param pReqVars additional param
   * @throws Exception - an exception
   **/
  public final void retrieveStartData(final Map<String, Object> pReqVars) throws Exception {
    try {
      this.srvDatabase.setIsAutocommit(false);
      this.srvDatabase.setTransactionIsolation(ISrvDatabase.TRANSACTION_READ_UNCOMMITTED);
      this.srvDatabase.beginTransaction();
      GoodsInListLuv goodsInListLuv = getSrvOrm().retrieveEntityById(pReqVars, GoodsInListLuv.class, 1L);
      if (goodsInListLuv == null) {
        goodsInListLuv = new GoodsInListLuv();
        goodsInListLuv.setItsId(1L);
        getSrvOrm().insertEntity(pReqVars, goodsInListLuv);
      }
      pReqVars.put("goodsInListLuv", goodsInListLuv);
      List<LangPreferences> langPreferences = this.srvOrm.retrieveList(pReqVars, LangPreferences.class);
      pReqVars.put("langPreferences", langPreferences);
      this.srvDatabase.commitTransaction();
    } catch (Exception ex) {
      if (!this.srvDatabase.getIsAutocommit()) {
        this.srvDatabase.rollBackTransaction();
      }
      throw ex;
    } finally {
      this.srvDatabase.releaseResources();
    }
  }

  /**
   * <p>Retrieve Item Places (outdated or all).</p>
   * @param <T> item place type
   * @param pReqVars additional param
   * @param pLuv last updated version or null for all
   * @param pItemPlaceCl Item Place Class
   * @return Outdated Item Place list
   * @throws Exception - an exception
   **/
  public final <T extends AItemPlace<?, ?>> List<T> retrieveItemPlaceLst(final Map<String, Object> pReqVars,
                                                                         final Long pLuv, final Class<T> pItemPlaceCl) throws Exception {
    List<T> result = null;
    try {
      this.srvDatabase.setIsAutocommit(false);
      this.srvDatabase.setTransactionIsolation(ISrvDatabase.TRANSACTION_READ_UNCOMMITTED);
      this.srvDatabase.beginTransaction();
      String tblNm = pItemPlaceCl.getSimpleName().toUpperCase();
      String verCond;
      if (pLuv != null) {
        verCond = " where " + tblNm + ".ITSVERSION>" + pLuv.toString();
      } else {
        verCond = "";
      }
      result = getSrvOrm().retrieveListWithConditions(pReqVars, pItemPlaceCl, verCond + " order by " + tblNm + ".ITSVERSION");
      this.srvDatabase.commitTransaction();
    } catch (Exception ex) {
      if (!this.srvDatabase.getIsAutocommit()) {
        this.srvDatabase.rollBackTransaction();
      }
      throw ex;
    } finally {
      this.srvDatabase.releaseResources();
    }
    return result;
  }

  /**
   * <p>Update ItemInList with Item Place.</p>
   * @param <T> item place type
   * @param <I> item type
   * @param pReqVars additional param
   * @param pItemPlace Item Place
   * @param pItemType EShopItemType
   * @throws Exception - an exception
   **/
  public final <T extends AItemPlace<I, ?>, I extends IHasIdLongVersionName> void updateForItemPlace(
          final Map<String, Object> pReqVars, final T pItemPlace, final EShopItemType pItemType) throws Exception {
    String whereStr = "where ITSTYPE=" + pItemType.ordinal() + " and ITEMID=" + pItemPlace.getItem().getItsId();
    ItemInList itemInList = getSrvOrm().retrieveEntityWithConditions(pReqVars, ItemInList.class, whereStr);
    if (itemInList == null) {
      itemInList = createItemInList(pReqVars, pItemPlace.getItem());
    }
    itemInList.setAvailableQuantity(pItemPlace.getItsQuantity());
    if (itemInList.getIsNew()) {
      getSrvOrm().insertEntity(pReqVars, itemInList);
    } else {
      getSrvOrm().updateEntity(pReqVars, itemInList);
    }
  }

  /**
   * <p>Update ItemInList withitem place list.
   * It does it with [N]-records per transaction method.</p>
   * @param <T> item place type
   * @param <I> item type
   * @param pReqVars additional param
   * @param pItemPlaceLst item place list
   * @param pSettingsAdd settings Add
   * @param pGoodsInListLuv goodsInListLuv
   * @param pItemType EShopItemType
   * @throws Exception - an exception
   **/
  public final <T extends AItemPlace<I, ?>, I extends IHasIdLongVersionName> void updateForItemPlaceList(
          final Map<String, Object> pReqVars, final List<T> pItemPlaceLst,
          final SettingsAdd pSettingsAdd, final GoodsInListLuv pGoodsInListLuv, final EShopItemType pItemType) throws Exception {
    if (pItemPlaceLst.size() == 0) {
      //Beige ORM may return empty list
      return;
    }
    int steps = pItemPlaceLst.size() / pSettingsAdd.getRecordsPerTransaction();
    int currentStep = 1;
    Long lastUpdatedVersion = null;
    do {
      try {
        this.srvDatabase.setIsAutocommit(false);
        this.srvDatabase.setTransactionIsolation(ISrvDatabase.TRANSACTION_READ_UNCOMMITTED);
        this.srvDatabase.beginTransaction();
        int stepLen = Math.min(pItemPlaceLst.size(), currentStep * pSettingsAdd.getRecordsPerTransaction());
        for (int i = (currentStep - 1) * pSettingsAdd.getRecordsPerTransaction(); i < stepLen; i++) {
          T itemPlace = pItemPlaceLst.get(i);
          updateForItemPlace(pReqVars, itemPlace,  pItemType);
          lastUpdatedVersion = itemPlace.getItsVersion();
        }
        if (pItemType == EShopItemType.GOODS) {
          pGoodsInListLuv.setGoodsAvailableLuv(lastUpdatedVersion);
        } else if (pItemType == EShopItemType.SERVICE) {
          pGoodsInListLuv.setServicePlaceLuv(lastUpdatedVersion);
        } else if (pItemType == EShopItemType.SEGOODS) {
          pGoodsInListLuv.setSeGoodPlaceLuv(lastUpdatedVersion);
        } else if (pItemType == EShopItemType.SESERVICE) {
          pGoodsInListLuv.setSeServicePlaceLuv(lastUpdatedVersion);
        } else {
          throw new Exception("NEI for " + pItemType);
        }
        getSrvOrm().updateEntity(pReqVars, pGoodsInListLuv);
        this.srvDatabase.commitTransaction();
      } catch (Exception ex) {
        if (!this.srvDatabase.getIsAutocommit()) {
          this.srvDatabase.rollBackTransaction();
        }
        throw ex;
      } finally {
        this.srvDatabase.releaseResources();
      }
    } while (currentStep++ < steps);
  }

  /**
   * <p>Retrieve item price list (outdated or all).</p>
   * @param <T> item price type
   * @param pReqVars additional param
   * @param pLuv last updated version or null for all
   * @param pItemPriceCl Item Price Class
   * @param pPrCatId default online price category ID
   * @return Outdated item price list
   * @throws Exception - an exception
   **/
  public final <T extends AItemPrice<?, ?>> List<T> retrieveItemPriceLst(
          final Map<String, Object> pReqVars, final Long pLuv, final Class<T> pItemPriceCl,
          final Long pPrCatId) throws Exception {
    List<T> result = null;
    try {
      this.srvDatabase.setIsAutocommit(false);
      this.srvDatabase.setTransactionIsolation(ISrvDatabase.TRANSACTION_READ_UNCOMMITTED);
      this.srvDatabase.beginTransaction();
      String tblNm = pItemPriceCl.getSimpleName().toUpperCase();
      String verCond = "where PRICECATEGORY=" + pPrCatId;
      if (pLuv != null) {
        verCond += " and " + tblNm + ".ITSVERSION>" + pLuv.toString();
      }
      result = getSrvOrm().retrieveListWithConditions(pReqVars, pItemPriceCl, verCond + " order by " + tblNm + ".ITSVERSION");
      this.srvDatabase.commitTransaction();
    } catch (Exception ex) {
      if (!this.srvDatabase.getIsAutocommit()) {
        this.srvDatabase.rollBackTransaction();
      }
      throw ex;
    } finally {
      this.srvDatabase.releaseResources();
    }
    return result;
  }

  /**
   * <p>Update ItemInList for Item Price.</p>
   * @param <T> item price type
   * @param <I> item type
   * @param pReqVars additional param
   * @param pItemPrice Item Price
   * @param pItemType EShopItemType
   * @throws Exception - an exception
   **/
  public final <T extends AItemPrice<I, ?>, I extends IHasIdLongVersionName> void updateForItemPrice(
          final Map<String, Object> pReqVars, final T pItemPrice, final EShopItemType pItemType) throws Exception {
    String whereStr = "where ITSTYPE=" + pItemType.ordinal() + " and ITEMID=" + pItemPrice.getItem().getItsId();
    ItemInList itemInList = getSrvOrm().retrieveEntityWithConditions(pReqVars, ItemInList.class, whereStr);
    if (itemInList == null) {
      itemInList = createItemInList(pReqVars, pItemPrice.getItem());
    }
    itemInList.setItsPrice(pItemPrice.getItsPrice());
    itemInList.setUnStep(pItemPrice.getUnStep());
    itemInList.setPreviousPrice(pItemPrice.getPreviousPrice());
    itemInList.setUnitOfMeasure(pItemPrice.getUnitOfMeasure());
    if (itemInList.getIsNew()) {
      getSrvOrm().insertEntity(pReqVars, itemInList);
    } else {
      getSrvOrm().updateEntity(pReqVars, itemInList);
    }
  }

  /**
   * <p>Update ItemInList with item price list.
   * It does it with [N]-records per transaction method.</p>
   * @param <T> item price type
   * @param <I> item type
   * @param pReqVars additional param
   * @param pItemPriceList item price list
   * @param pSettingsAdd settings Add
   * @param pGoodsInListLuv goodsInListLuv
   * @param pItemType EShopItemType
   * @throws Exception - an exception
   **/
  public final <T extends AItemPrice<I, ?>, I extends IHasIdLongVersionName> void updateForItemPriceList(
          final Map<String, Object> pReqVars, final List<T> pItemPriceList,
          final SettingsAdd pSettingsAdd, final GoodsInListLuv pGoodsInListLuv, final EShopItemType pItemType) throws Exception {
    if (pItemPriceList.size() == 0) {
      //Beige ORM may return empty list
      return;
    }
    int steps = pItemPriceList.size() / pSettingsAdd.getRecordsPerTransaction();
    int currentStep = 1;
    Long lastUpdatedVersion = null;
    do {
      try {
        this.srvDatabase.setIsAutocommit(false);
        this.srvDatabase.setTransactionIsolation(ISrvDatabase.TRANSACTION_READ_UNCOMMITTED);
        this.srvDatabase.beginTransaction();
        int stepLen = Math.min(pItemPriceList.size(), currentStep * pSettingsAdd.getRecordsPerTransaction());
        for (int i = (currentStep - 1) * pSettingsAdd.getRecordsPerTransaction(); i < stepLen; i++) {
          T itemPrice = pItemPriceList.get(i);
          updateForItemPrice(pReqVars, itemPrice, pItemType);
          lastUpdatedVersion = itemPrice.getItsVersion();
        }
        if (pItemType == EShopItemType.GOODS) {
          pGoodsInListLuv.setGoodsPriceLuv(lastUpdatedVersion);
        } else if (pItemType == EShopItemType.SERVICE) {
          pGoodsInListLuv.setServicePriceLuv(lastUpdatedVersion);
        } else if (pItemType == EShopItemType.SEGOODS) {
          pGoodsInListLuv.setSeGoodPriceLuv(lastUpdatedVersion);
        } else if (pItemType == EShopItemType.SESERVICE) {
          pGoodsInListLuv.setSeServicePriceLuv(lastUpdatedVersion);
        } else {
          throw new Exception("NEI for " + pItemType);
        }
        getSrvOrm().updateEntity(pReqVars, pGoodsInListLuv);
        this.srvDatabase.commitTransaction();
      } catch (Exception ex) {
        if (!this.srvDatabase.getIsAutocommit()) {
          this.srvDatabase.rollBackTransaction();
        }
        throw ex;
      } finally {
        this.srvDatabase.releaseResources();
      }
    } while (currentStep++ < steps);
  }

  /**
   * <p>Retrieve Item Specifics list outdated or all.</p>
   * @param <T> Item Specifics type
   * @param <I> Item type
   * @param pReqVars additional param
   * @param pLuv last updated version - null all
   * @param pItemSpecClass ItemSpec Class
   * @return Item Specifics list
   * @throws Exception - an exception
   **/
  public final <T extends AItemSpecifics<I, ?>, I extends IHasIdLongVersionName> List<T> retrieveItemSpecificsLst(
          final Map<String, Object> pReqVars, final Long pLuv, final Class<T> pItemSpecClass) throws Exception {
    List<T> result = null;
    String verCond;
    if (pLuv != null) {
      String tblNm = pItemSpecClass.getSimpleName().toUpperCase();
      verCond = " where " + tblNm + ".ITEM in " + " (select distinct  ITEM from " + tblNm + " join SPECIFICSOFITEM on "
              + tblNm + ".SPECIFICS=SPECIFICSOFITEM.ITSID where " + tblNm + ".ITSVERSION>" + pLuv.toString() + ")";
    } else {
      verCond = "";
    }
    try {
      this.srvDatabase.setIsAutocommit(false);
      this.srvDatabase.setTransactionIsolation(ISrvDatabase.TRANSACTION_READ_UNCOMMITTED);
      this.srvDatabase.beginTransaction();
      pReqVars.put(pItemSpecClass.getSimpleName() + "specificsdeepLevel", 2); //HTML templates only ID
      pReqVars.put("SpecificsOfItemtempHtmldeepLevel", 1); //HTML templates only ID
      HashSet<String> goodsFldNms = new HashSet<String>();
      goodsFldNms.add("itsId");
      goodsFldNms.add("itsName");
      pReqVars.put("InvItemneededFields", goodsFldNms);
      HashSet<String> soiFldNms = new HashSet<String>();
      soiFldNms.add("itsId");
      soiFldNms.add("itsName");
      soiFldNms.add("isShowInList");
      soiFldNms.add("itsType");
      soiFldNms.add("itsGroop");
      soiFldNms.add("tempHtml");
      pReqVars.put("SpecificsOfItemneededFields", soiFldNms);
      HashSet<String> soigFldNms = new HashSet<String>();
      soigFldNms.add("itsId");
      soigFldNms.add("itsName");
      soigFldNms.add("templateStart");
      soigFldNms.add("templateEnd");
      soigFldNms.add("templateDetail");
      pReqVars.put("SpecificsOfItemGroupneededFields", soigFldNms);
      result = getSrvOrm().retrieveListWithConditions(pReqVars, pItemSpecClass, verCond + " order by ITEM.ITSID, SPECIFICS.ITSINDEX");
      pReqVars.remove(pItemSpecClass.getSimpleName() + "specificsdeepLevel");
      pReqVars.remove("SpecificsOfItemtempHtmldeepLevel");
      pReqVars.remove("InvItemneededFields");
      pReqVars.remove("SpecificsOfItemneededFields");
      pReqVars.remove("SpecificsOfItemGroupneededFields");
      this.srvDatabase.commitTransaction();
    } catch (Exception ex) {
      if (!this.srvDatabase.getIsAutocommit()) {
        this.srvDatabase.rollBackTransaction();
      }
      throw ex;
    } finally {
      this.srvDatabase.releaseResources();
    }
    //make list of goods ordered by max(last updated specific) for that goods
    //goods itsVersion holds max(last updated specific):
    List<I> itemsForSpecifics = new ArrayList<I>();
    //also list of all used HTML templates ID to retrieve their full-filled list
    Set<Long> htmlTemplatesIds = new HashSet<Long>();
    I currItem = null;
    for (AItemSpecifics<I, ?> gs : result) {
      if (gs.getSpecifics().getTempHtml() != null) {
        htmlTemplatesIds.add(gs.getSpecifics().getTempHtml().getItsId());
      }
      if (gs.getSpecifics().getItsGroop() != null) {
        if (gs.getSpecifics().getItsGroop().getTemplateStart() != null) {
          htmlTemplatesIds.add(gs.getSpecifics().getItsGroop().getTemplateStart().getItsId());
        }
        if (gs.getSpecifics().getItsGroop().getTemplateEnd() != null) {
          htmlTemplatesIds.add(gs.getSpecifics().getItsGroop().getTemplateEnd().getItsId());
        }
        if (gs.getSpecifics().getItsGroop().getTemplateStart() != null) {
          htmlTemplatesIds.add(gs.getSpecifics().getItsGroop().getTemplateDetail().getItsId());
        }
      }
      if (currItem == null || !gs.getItem().getItsId().equals(currItem.getItsId())) {
        currItem = gs.getItem();
        currItem.setItsVersion(gs.getItsVersion());
        itemsForSpecifics.add(currItem);
      } else { //2-nd, 3-d... specifics of this goods
        if (currItem.getItsVersion() < gs.getItsVersion()) {
          currItem.setItsVersion(gs.getItsVersion());
        }
      }
    }
    CmprHasVersion<I> cmp = new CmprHasVersion<I>();
    Collections.sort(itemsForSpecifics, cmp);
    pReqVars.put("itemsForSpecifics", itemsForSpecifics);
    pReqVars.put("htmlTemplatesIds", htmlTemplatesIds);
    return result;
  }

  /**
   * <p>Update ItemInList.SpecificInList with outdated GoodsSpecifics.</p>
   * @param <T> item specifics type
   * @param pReqVars additional param
   * @param pSettingsAdd SettingsAdd
   * @param pOutdGdSp outdated GoodsSpecifics
   * @param pItemInList ItemInList
   * @param pSpecificsOfItemGroupWas SpecificsOfItemGroup previous
   * @param pI18nSpecInListLst I18nSpecificInList list
   * @param pI18nSpecLst I18nSpecificsOfItem list
   * @param pI18nChSpecLst I18nChooseableSpecifics list
   * @param pI18nUomLst I18nUnitOfMeasure list
   * @throws Exception - an exception
   **/
  public final <T extends AItemSpecifics<?, ?>> void updateGoodsSpecificsInList(
          final Map<String, Object> pReqVars, final SettingsAdd pSettingsAdd, final T pOutdGdSp,
          final ItemInList pItemInList, final SpecificsOfItemGroup pSpecificsOfItemGroupWas,
          final List<I18nSpecificInList> pI18nSpecInListLst, final List<I18nSpecificsOfItem> pI18nSpecLst,
          final List<I18nChooseableSpecifics> pI18nChSpecLst, final List<I18nUnitOfMeasure> pI18nUomLst) throws Exception {
    String val1 = "";
    String val2 = "";
    @SuppressWarnings("unchecked")
    List<LangPreferences> langPreferences = (List<LangPreferences>) pReqVars.get("langPreferences");
    LangPreferences currLp = langPreferences.get(0);
    for (LangPreferences lp : langPreferences) {
      if (lp.getIsDefault()) {
        currLp = lp;
        break;
      }
    }
    if (pOutdGdSp.getSpecifics().getItsType().equals(ESpecificsItemType.TEXT)) {
      //i18n not implemented, use chooseable specifics instead
      val1 = pOutdGdSp.getStringValue1();
    } else if (pOutdGdSp.getSpecifics().getItsType().equals(ESpecificsItemType.BIGDECIMAL)) {
      val1 = srvNumberToString.print(pOutdGdSp.getNumericValue1().toString(),
              currLp.getDecimalSep().getItsId(), currLp.getDecimalGroupSep().getItsId(),
              Integer.valueOf(pOutdGdSp.getLongValue1().intValue()), currLp.getDigitsInGroup());
      if (pOutdGdSp.getStringValue1() != null) {
        val2 = pOutdGdSp.getStringValue1();
      }
    } else if (pOutdGdSp.getSpecifics().getItsType().equals(ESpecificsItemType.INTEGER)) {
      val1 = pOutdGdSp.getLongValue1().toString();
      if (pOutdGdSp.getStringValue1() != null) {
        val2 = pOutdGdSp.getStringValue1();
      }
    } else if (pOutdGdSp.getSpecifics().getItsType().equals(ESpecificsItemType.CHOOSEABLE_SPECIFICS)) {
      val1 =  pOutdGdSp.getStringValue1();
    } else {
      return;
    }
    String templateDetail;
    if (pOutdGdSp.getSpecifics().getTempHtml() != null) {
      templateDetail = pOutdGdSp.getSpecifics().getTempHtml().getHtmlTemplate();
    } else if (pOutdGdSp.getSpecifics().getItsGroop() != null && pOutdGdSp.getSpecifics().getItsGroop().getTemplateDetail() != null) {
      templateDetail = pOutdGdSp.getSpecifics().getItsGroop().getTemplateDetail().getHtmlTemplate();
    } else {
      templateDetail = " <b>:SPECNM:</b> :VAL1:VAL2";
    }
    String spdet = templateDetail.replace(":SPECNM", pOutdGdSp.getSpecifics().getItsName());
    spdet = spdet.replace(":VAL1", val1);
    spdet = spdet.replace(":VAL2", val2);
    if (pOutdGdSp.getSpecifics().getItsGroop() != null && pSpecificsOfItemGroupWas != null
            && pOutdGdSp.getSpecifics().getItsGroop().getItsId().equals(pSpecificsOfItemGroupWas.getItsId())) {
      pItemInList.setSpecificInList(pItemInList.getSpecificInList() + pSettingsAdd.getSpecSeparator() + spdet);
    } else {
      pItemInList.setSpecificInList(pItemInList.getSpecificInList() + spdet);
    }
    if (pI18nSpecInListLst != null) {
      for (I18nSpecificInList i18nspl : pI18nSpecInListLst) {
        for (LangPreferences lp : langPreferences) {
          if (lp.getLang().getItsId().equals(i18nspl.getLang().getItsId())) {
            currLp = lp;
            break;
          }
        }
        if (pOutdGdSp.getSpecifics().getItsType().equals(ESpecificsItemType.BIGDECIMAL)) {
          val1 = srvNumberToString.print(pOutdGdSp.getNumericValue1().toString(),
                  currLp.getDecimalSep().getItsId(), currLp.getDecimalGroupSep().getItsId(),
                  Integer.valueOf(pOutdGdSp.getLongValue1().intValue()), currLp.getDigitsInGroup());
        }
        if (pOutdGdSp.getSpecifics().getItsType().equals(ESpecificsItemType.BIGDECIMAL)
                || pOutdGdSp.getSpecifics().getItsType().equals(ESpecificsItemType.INTEGER)
                && pOutdGdSp.getLongValue2() != null) {
          UnitOfMeasure uom = new UnitOfMeasure();
          uom.setItsName(pOutdGdSp.getStringValue1());
          uom.setItsId(pOutdGdSp.getLongValue2());
          val2 = findUomName(pI18nUomLst, uom, i18nspl.getLang());
        } else if (pOutdGdSp.getSpecifics().getItsType().equals(ESpecificsItemType.CHOOSEABLE_SPECIFICS)) {
          ChooseableSpecifics sp = new ChooseableSpecifics();
          sp.setItsName(pOutdGdSp.getStringValue1());
          sp.setItsId(pOutdGdSp.getLongValue1());
          val1 =  findChSpecName(pI18nChSpecLst, sp, i18nspl.getLang());
        }
        spdet = templateDetail.replace(":SPECNM", findSpecName(pI18nSpecLst, pOutdGdSp.getSpecifics(), i18nspl.getLang()));
        spdet = spdet.replace(":VAL1", val1);
        spdet = spdet.replace(":VAL2", val2);
        if (pOutdGdSp.getSpecifics().getItsGroop() != null && pSpecificsOfItemGroupWas != null
                && pOutdGdSp.getSpecifics().getItsGroop().getItsId().equals(pSpecificsOfItemGroupWas.getItsId())) {
          i18nspl.setSpecificInList(i18nspl.getSpecificInList() + pSettingsAdd.getSpecSeparator() + spdet);
        } else {
          i18nspl.setSpecificInList(i18nspl.getSpecificInList() + spdet);
        }
      }
    }
  }

  /**
   * <p>Retrieve I18nItem list.</p>
   * @param <T> item type
   * @param pReqVars additional param
   * @param pI18nItemClass I18nItem Class
   * @param pItemIdsIn Item's IDs
   * @return list
   * @throws Exception - an exception
   **/
  public final <T extends AI18nName<?, ?>> List<T> retrieveI18nItem(final Map<String, Object> pReqVars, final Class<T> pI18nItemClass, final String pItemIdsIn) throws Exception {
    pReqVars.put(pI18nItemClass.getSimpleName() + "hasNamedeepLevel", 1);
    pReqVars.put(pI18nItemClass.getSimpleName() + "langdeepLevel", 1);
    List<T> i18nItemLst = getSrvOrm().retrieveListWithConditions(pReqVars, pI18nItemClass, "where HASNAME in " + pItemIdsIn);
    pReqVars.remove(pI18nItemClass.getSimpleName() + "hasNamedeepLevel");
    pReqVars.remove(pI18nItemClass.getSimpleName() + "langdeepLevel");
    return i18nItemLst;
  }

  /**
   * <p>Update ItemInList with outdated item specifics list.
   * It does it with [N]-records per transaction method.</p>
   * @param <I> item type
   * @param <T> item specifics type
   * @param pReqVars additional param
   * @param pOutdGdSpList outdated GoodsSpecifics list
   * @param pSettingsAdd settings Add
   * @param pGoodsInListLuv goodsInListLuv
   * @param pTradingSettings trading settings
   * @param pI18nItemClass I18nItem Class
   * @param pItemType EShopItemType
   * @throws Exception - an exception
   **/
  public final <I extends AI18nName<?, ?>, T extends AItemSpecifics<?, ?>> void updateForItemSpecificsList(
          final Map<String, Object> pReqVars, final List<T> pOutdGdSpList,
          final SettingsAdd pSettingsAdd, final GoodsInListLuv pGoodsInListLuv,
          final TradingSettings pTradingSettings, final Class<I> pI18nItemClass, final EShopItemType pItemType) throws Exception {
    if (pOutdGdSpList.size() == 0) {
      //Beige ORM may return empty list
      return;
    }
    @SuppressWarnings("unchecked")
    List<IHasIdLongVersionName> itemsForSpecifics = (List<IHasIdLongVersionName>) pReqVars.get("itemsForSpecifics");
    pReqVars.remove("itemsForSpecifics");
    @SuppressWarnings("unchecked")
    Set<Long> htmlTemplatesIds = (Set<Long>) pReqVars.get("htmlTemplatesIds");
    pReqVars.remove("htmlTemplatesIds");
    List<HtmlTemplate> htmlTemplates = null;
    List<ItemInList> itemsInList = null;
    List<I18nChooseableSpecifics> i18nChooseableSpecificsLst = null;
    List<I18nSpecificsOfItemGroup> i18nSpecificsOfItemGroupLst = null;
    List<I18nSpecificsOfItem> i18nSpecificsOfItemLst = null;
    List<I> i18nItemLst = null;
    List<I18nSpecificInList> i18nSpecificInListLst = null;
    List<I18nUnitOfMeasure> i18nUomLst = null;
    try {
      this.srvDatabase.setIsAutocommit(false);
      this.srvDatabase.
              setTransactionIsolation(ISrvDatabase.TRANSACTION_READ_UNCOMMITTED);
      this.srvDatabase.beginTransaction();
      StringBuffer itemsIdsIn = new StringBuffer("(");
      boolean isFirst = true;
      for (IHasIdLongVersionName it : itemsForSpecifics) {
        if (isFirst) {
          isFirst = false;
        } else {
          itemsIdsIn.append(", ");
        }
        itemsIdsIn.append(it.getItsId().toString());
      }
      itemsIdsIn.append(")");
      itemsInList = getSrvOrm().retrieveListWithConditions(pReqVars, ItemInList.class, "where ITSTYPE=" + pItemType.ordinal() + " and ITEMID in " + itemsIdsIn.toString());
      if (htmlTemplatesIds.size() > 0) {
        StringBuffer whereStr = new StringBuffer("where ITSID in (");
        isFirst = true;
        // BLOCKTEST EVAL: https://github.com/demidenko05/beigesoft-webstore/blob/624d41d82547ff897ca5b3740d8d73cb2dacf052/src/main/java/org/beigesoft/webstore/processor/PrcRefreshItemsInList.java#L776C1-L783C10
        blocktest()
                .given(isFirst, true).given(whereStr, new StringBuffer("where ITSID in ("))
                .given(htmlTemplatesIds, new HashSet<>(Arrays.asList(1L, 2L, 3L))).end(FIRST_STATEMENT, 2).checkEq(whereStr.toString(), "where ITSID in (1, 2, 3)");
        for (Long id : htmlTemplatesIds) {
          if (isFirst) {
            isFirst = false;
          } else {
            whereStr.append(", ");
          }
          whereStr.append(id.toString());
        }
        whereStr.append(")");
        htmlTemplates = getSrvOrm().retrieveListWithConditions(pReqVars, HtmlTemplate.class, whereStr.toString());
      }
      if (pTradingSettings.getUseAdvancedI18n()) {
        i18nItemLst = retrieveI18nItem(pReqVars, pI18nItemClass, itemsIdsIn.toString());
        if (i18nItemLst.size() > 0) {
          i18nSpecificInListLst = getSrvOrm().retrieveListWithConditions(pReqVars, I18nSpecificInList.class, "where ITSTYPE=" + pItemType.ordinal() + " and ITEMID in " + itemsIdsIn.toString());
          StringBuffer specGrIdIn = null;
          StringBuffer specChIdIn = null;
          StringBuffer specIdIn = new StringBuffer("(");
          isFirst = true;
          boolean isFirstGr = true;
          boolean isFirstCh = true;
          for (AItemSpecifics<?, ?> gs : pOutdGdSpList) {
            // BLOCKTEST EVAL: https://github.com/demidenko05/beigesoft-webstore/blob/624d41d82547ff897ca5b3740d8d73cb2dacf052/src/main/java/org/beigesoft/webstore/processor/PrcRefreshItemsInList.java#L798C13-L821C14
            // MUST PROVIDE TYPE
            /* @blocktest("test1")
            .given(isFirst, true).given(whereStr, new StringBuffer("where ITSID in (")).given(specIdIn, new StringBuffer("(")).given(isFirstCh, true).given(specChIdIn, null).given(isFirstGr, true).given(specGrIdIn, null)
            .given(gs, new GoodsSpecifics(), "GoodsSpecifics").setup(() -> {
              SpecificsOfItem i = new SpecificsOfItem();
              SpecificsOfItemGroup g = new SpecificsOfItemGroup();
              i.setItsType(ESpecificsItemType.CHOOSEABLE_SPECIFICS);
              i.setItsId(123l);
              g.setItsId(911l);
              i.setItsGroop(g);
              gs.setSpecifics(i);
              gs.setLongValue1(321l);
            }).checkEq(specChIdIn.toString(), "(321").checkEq(specGrIdIn.toString(), "(911").checkEq(specIdIn.toString(), "(123").checkFalse(isFirst).checkFalse(isFirstCh).checkFalse(isFirstGr); */
            /* @blocktest("test2")
            .given(isFirst, true).given(whereStr, new StringBuffer("where ITSID in (")).given(specIdIn, new StringBuffer("(")).given(isFirstCh, true).given(specChIdIn, null).given(isFirstGr, true).given(specGrIdIn, null)
            .given(gs, new GoodsSpecifics(), "GoodsSpecifics").setup(() -> {
              SpecificsOfItem i = new SpecificsOfItem();
              SpecificsOfItemGroup g = new SpecificsOfItemGroup();
              i.setItsType(ESpecificsItemType.CHOOSEABLE_SPECIFICS);
              i.setItsId(123l);
              g.setItsId(911l);
              gs.setSpecifics(i);
              gs.setLongValue1(321l);
            }).checkEq(specChIdIn.toString(), "(321").checkEq(specGrIdIn, null).checkEq(specIdIn.toString(), "(123").checkFalse(isFirst).checkFalse(isFirstCh).checkTrue(isFirstGr); */
            /* @blocktest("test3")
            .given(isFirst, true).given(whereStr, new StringBuffer("where ITSID in (")).given(specIdIn, new StringBuffer("(")).given(isFirstCh, true).given(specChIdIn, null).given(isFirstGr, true).given(specGrIdIn, null)
            .given(gs, new GoodsSpecifics(), "GoodsSpecifics").setup(() -> {
              SpecificsOfItem i = new SpecificsOfItem();
              SpecificsOfItemGroup g = new SpecificsOfItemGroup();
              i.setItsType(ESpecificsItemType.BIGDECIMAL);
              i.setItsId(123l);
              g.setItsId(911l);
              i.setItsGroop(g);
              gs.setSpecifics(i);
              gs.setLongValue1(321l);
            }).checkEq(specChIdIn, null).checkEq(specGrIdIn.toString(), "(911").checkEq(specIdIn.toString(), "(123").checkFalse(isFirst).checkTrue(isFirstCh).checkFalse(isFirstGr); */
            /* @blocktest("test4")
            .given(whereStr, new StringBuffer("where ITSID in (")).given(specIdIn, new StringBuffer("("))
            .given(isFirstCh, false).given(isFirst, false).given(isFirstGr, false).given(gs, new GoodsSpecifics(), "GoodsSpecifics").setup(() -> {
              SpecificsOfItem i = new SpecificsOfItem();
              SpecificsOfItemGroup g = new SpecificsOfItemGroup();
              i.setItsType(ESpecificsItemType.CHOOSEABLE_SPECIFICS);
              i.setItsId(123l);
              g.setItsId(911l);
              i.setItsGroop(g);
              gs.setSpecifics(i);
              gs.setLongValue1(321l);
            }).given(specChIdIn, new StringBuffer("(1")).given(specGrIdIn, new StringBuffer("(2")).checkEq(specChIdIn.toString(), "(1, 321").checkEq(specGrIdIn.toString(), "(2, 911").checkEq(specIdIn.toString(), "(, 123").checkFalse(isFirst).checkFalse(isFirstCh).checkFalse(isFirstGr); */
            if (isFirst) {
              isFirst = false;
            } else {
              specIdIn.append(", ");
            }
            specIdIn.append(gs.getSpecifics().getItsId().toString());
            if (gs.getSpecifics().getItsGroop() != null) {
              if (isFirstGr) {
                specGrIdIn = new StringBuffer("(");
                isFirstGr = false;
              } else {
                specGrIdIn.append(", ");
              }
              specGrIdIn.append(gs.getSpecifics().getItsGroop().getItsId().toString());
            }
            if (gs.getSpecifics().getItsType().equals(ESpecificsItemType.CHOOSEABLE_SPECIFICS)) {
              if (isFirstCh) {
                specChIdIn = new StringBuffer("(");
                isFirstCh = false;
              } else {
                specChIdIn.append(", ");
              }
              specChIdIn.append(gs.getLongValue1().toString());
            }
          }
          specIdIn.append(")");
          if (specGrIdIn != null) {
            specGrIdIn.append(")");
          }
          if (specChIdIn != null) {
            specChIdIn.append(")");
          }
          pReqVars.put("I18nSpecificsOfItemhasNamedeepLevel", 1);
          pReqVars.put("I18nSpecificsOfItemlangdeepLevel", 1);
          i18nSpecificsOfItemLst = getSrvOrm().retrieveListWithConditions(pReqVars, I18nSpecificsOfItem.class, "where HASNAME in " + specIdIn.toString());
          pReqVars.remove("I18nSpecificsOfItemhasNamedeepLevel");
          pReqVars.remove("I18nSpecificsOfItemlangdeepLevel");
          pReqVars.put("I18nUnitOfMeasurehasNamedeepLevel", 1);
          pReqVars.put("I18nUnitOfMeasurelangdeepLevel", 1);
          i18nUomLst = getSrvOrm().retrieveList(pReqVars, I18nUnitOfMeasure.class);
          pReqVars.remove("I18nUnitOfMeasurehasNamedeepLevel");
          pReqVars.remove("I18nUnitOfMeasurelangdeepLevel");
          if (specGrIdIn != null) {
            pReqVars.put("I18nSpecificsOfItemGrouphasNamedeepLevel", 1);
            pReqVars.put("I18nSpecificsOfItemGrouplangdeepLevel", 1);
            i18nSpecificsOfItemGroupLst = getSrvOrm().retrieveListWithConditions(pReqVars, I18nSpecificsOfItemGroup.class, "where HASNAME in " + specGrIdIn.toString());
            pReqVars.remove("I18nSpecificsOfItemGrouphasNamedeepLevel");
            pReqVars.remove("I18nSpecificsOfItemGrouplangdeepLevel");
          }
          if (specChIdIn != null) {
            pReqVars.put("I18nChooseableSpecificshasNamedeepLevel", 1);
            pReqVars.put("I18nChooseableSpecificslangdeepLevel", 1);
            i18nChooseableSpecificsLst = getSrvOrm().retrieveListWithConditions(pReqVars, I18nChooseableSpecifics.class, "where HASNAME in " + specChIdIn.toString());
            pReqVars.remove("I18nChooseableSpecificshasNamedeepLevel");
            pReqVars.remove("I18nChooseableSpecificslangdeepLevel");
          }
        }
      }
    } catch (Exception ex) {
      if (!this.srvDatabase.getIsAutocommit()) {
        this.srvDatabase.rollBackTransaction();
      }
      throw ex;
    } finally {
      this.srvDatabase.releaseResources();
    }
    if (htmlTemplates != null && htmlTemplates.size() > 0) {
      for (AItemSpecifics<?, ?> gs : pOutdGdSpList) {
        if (gs.getSpecifics().getTempHtml() != null) {
          gs.getSpecifics().setTempHtml(
                  findTemplate(htmlTemplates, gs.getSpecifics().getTempHtml().getItsId()));
        }
        if (gs.getSpecifics().getItsGroop() != null) {
          if (gs.getSpecifics().getItsGroop().getTemplateStart() != null) {
            gs.getSpecifics().getItsGroop().setTemplateStart(findTemplate(htmlTemplates, gs.getSpecifics().getItsGroop().getTemplateStart().getItsId()));
          }
          if (gs.getSpecifics().getItsGroop().getTemplateEnd() != null) {
            gs.getSpecifics().getItsGroop().setTemplateEnd(findTemplate(htmlTemplates, gs.getSpecifics().getItsGroop().getTemplateEnd().getItsId()));
          }
          if (gs.getSpecifics().getItsGroop().getTemplateStart() != null) {
            gs.getSpecifics().getItsGroop().setTemplateDetail(findTemplate(htmlTemplates, gs.getSpecifics().getItsGroop().getTemplateDetail().getItsId()));
          }
        }
      }
    }
    int steps = itemsForSpecifics.size() / pSettingsAdd.getRecordsPerTransaction();
    int currentStep = 1;
    Long lastUpdatedVersion = null;
    do {
      try {
        this.srvDatabase.setIsAutocommit(false);
        this.srvDatabase.setTransactionIsolation(ISrvDatabase.TRANSACTION_READ_UNCOMMITTED);
        this.srvDatabase.beginTransaction();
        int stepLen = Math.min(itemsForSpecifics.size(), currentStep * pSettingsAdd.getRecordsPerTransaction());
        for (int i = (currentStep - 1) * pSettingsAdd.getRecordsPerTransaction(); i < stepLen; i++) {
          IHasIdLongVersionName item = itemsForSpecifics.get(i);
          ItemInList itemInList = findItemInListFor(itemsInList, item.getItsId(), pItemType);
          if (itemInList == null) {
            itemInList = createItemInList(pReqVars, item);
          }
          int j = findFirstIdxFor(pOutdGdSpList, item);
          SpecificsOfItemGroup specificsOfItemGroupWas = null;
          //reset any way:
          itemInList.setItsName(item.getItsName());
          itemInList.setDetailsMethod(null);
          itemInList.setImageUrl(null);
          //i18n:
          List<I18nSpecificInList> i18nSpInLsLstFg = null;
          if (i18nItemLst != null) {
            for (I i18nItem : i18nItemLst) {
              if (i18nSpInLsLstFg == null) {
                i18nSpInLsLstFg = new ArrayList<I18nSpecificInList>();
              }
              if (i18nItem.getHasName().getItsId().equals(item.getItsId())) {
                I18nSpecificInList i18nspInLs = findI18nSpecificInListFor(i18nSpecificInListLst, item, pItemType, i18nItem.getLang());
                if (i18nspInLs == null) {
                  i18nspInLs = new I18nSpecificInList();
                  i18nspInLs.setIsNew(true);
                  i18nspInLs.setItsType(pItemType);
                  i18nspInLs.setItemId(item.getItsId());
                  i18nspInLs.setLang(i18nItem.getLang());
                }
                i18nspInLs.setItsName(i18nItem.getItsName());
                i18nSpInLsLstFg.add(i18nspInLs);
              }
            }
          }
          if (pSettingsAdd.getSpecHtmlStart() !=  null) {
            itemInList.setSpecificInList(pSettingsAdd.getSpecHtmlStart());
            if (i18nSpInLsLstFg != null) {
              for (I18nSpecificInList i18nspInLs : i18nSpInLsLstFg) {
                i18nspInLs.setSpecificInList(pSettingsAdd.getSpecHtmlStart());
              }
            }
          } else {
            itemInList.setSpecificInList("");
            if (i18nSpInLsLstFg != null) {
              for (I18nSpecificInList i18nspInLs : i18nSpInLsLstFg) {
                i18nspInLs.setSpecificInList("");
              }
            }
          }
          boolean wasGrStart = false;
          do {
            if (pOutdGdSpList.get(j).getSpecifics().getIsShowInList()) {
              if (pOutdGdSpList.get(j).getSpecifics().getItsType().equals(ESpecificsItemType.IMAGE)) {
                itemInList.setImageUrl(pOutdGdSpList.get(j).getStringValue1());
              } else { // build ItemInList.specificInList:
                if (pOutdGdSpList.get(j).getSpecifics().getItsGroop() == null || specificsOfItemGroupWas == null
                        || !pOutdGdSpList.get(j).getSpecifics().getItsGroop().getItsId().equals(specificsOfItemGroupWas.getItsId())) {
                  if (wasGrStart) {
                    if (pSettingsAdd.getSpecGrSeparator() != null && pSettingsAdd.getSpecGrHtmlEnd() != null) {
                      itemInList.setSpecificInList(itemInList.getSpecificInList() + pSettingsAdd.getSpecGrHtmlEnd() + pSettingsAdd.getSpecGrSeparator());
                    } else if (pSettingsAdd.getSpecGrHtmlEnd() != null) {
                      itemInList.setSpecificInList(itemInList.getSpecificInList() + pSettingsAdd.getSpecGrHtmlEnd());
                    } else if (pSettingsAdd.getSpecGrSeparator() != null) {
                      itemInList.setSpecificInList(itemInList.getSpecificInList() + pSettingsAdd.getSpecGrSeparator());
                    }
                  }
                  wasGrStart = true;
                  if (pSettingsAdd.getSpecGrHtmlStart() != null) {
                    itemInList.setSpecificInList(itemInList.getSpecificInList() + pSettingsAdd.getSpecGrHtmlStart());
                    if (i18nSpInLsLstFg != null) {
                      for (I18nSpecificInList i18nspInLs : i18nSpInLsLstFg) {
                        i18nspInLs.setSpecificInList(i18nspInLs.getSpecificInList() + pSettingsAdd.getSpecGrHtmlStart());
                      }
                    }
                  }
                  if (pOutdGdSpList.get(j).getSpecifics().getItsGroop() != null && pOutdGdSpList.get(j).getSpecifics().getItsGroop().getTemplateStart() != null) {
                    String grst = pOutdGdSpList.get(j).getSpecifics().getItsGroop().getTemplateStart()
                            .getHtmlTemplate().replace(":SPECGRNM", pOutdGdSpList.get(j).getSpecifics().getItsGroop().getItsName());
                    itemInList.setSpecificInList(itemInList.getSpecificInList() + grst);
                    if (i18nSpInLsLstFg != null) {
                      for (I18nSpecificInList i18nspInLs : i18nSpInLsLstFg) {
                        String gn = findI18nSpecGrName(i18nSpecificsOfItemGroupLst, pOutdGdSpList.get(j).getSpecifics().getItsGroop(), i18nspInLs.getLang());
                        grst = pOutdGdSpList.get(j).getSpecifics().getItsGroop().getTemplateStart()
                                .getHtmlTemplate().replace(":SPECGRNM", gn);
                        i18nspInLs.setSpecificInList(i18nspInLs.getSpecificInList() + grst);
                      }
                    }
                  }
                }
                updateGoodsSpecificsInList(pReqVars, pSettingsAdd, pOutdGdSpList.get(j), itemInList, specificsOfItemGroupWas, i18nSpInLsLstFg, i18nSpecificsOfItemLst, i18nChooseableSpecificsLst, i18nUomLst);
                specificsOfItemGroupWas = pOutdGdSpList.get(j).getSpecifics().getItsGroop();
              }
            } else {
              itemInList.setDetailsMethod(1);
            }
            j++;
          } while (j < pOutdGdSpList.size() && pOutdGdSpList.get(j).getItem().getItsId().equals(item.getItsId()));
          if (pSettingsAdd.getSpecGrHtmlEnd() != null) {
            itemInList.setSpecificInList(itemInList.getSpecificInList() + pSettingsAdd.getSpecGrHtmlEnd());
            if (i18nSpInLsLstFg != null) {
              for (I18nSpecificInList i18nspInLs : i18nSpInLsLstFg) {
                i18nspInLs.setSpecificInList(i18nspInLs.getSpecificInList() + pSettingsAdd.getSpecGrHtmlEnd());
              }
            }
          }
          if (pSettingsAdd.getSpecHtmlEnd() != null) {
            itemInList.setSpecificInList(itemInList.getSpecificInList() + pSettingsAdd.getSpecHtmlEnd());
            if (i18nSpInLsLstFg != null) {
              for (I18nSpecificInList i18nspInLs : i18nSpInLsLstFg) {
                i18nspInLs.setSpecificInList(i18nspInLs.getSpecificInList() + pSettingsAdd.getSpecHtmlEnd());
              }
            }
          }
          if (itemInList.getIsNew()) {
            getSrvOrm().insertEntity(pReqVars, itemInList);
          } else {
            getSrvOrm().updateEntity(pReqVars, itemInList);
          }
          if (i18nSpInLsLstFg != null) {
            for (I18nSpecificInList i18nspInLs : i18nSpInLsLstFg) {
              if (i18nspInLs.getIsNew()) {
                getSrvOrm().insertEntity(pReqVars, i18nspInLs);
              } else {
                getSrvOrm().updateEntity(pReqVars, i18nspInLs);
              }
            }
          }
          //item holds item-specifics version
          lastUpdatedVersion = item.getItsVersion();
        }
        if (pItemType.equals(EShopItemType.GOODS)) {
          pGoodsInListLuv.setGoodsSpecificLuv(lastUpdatedVersion);
        } else if (pItemType.equals(EShopItemType.SERVICE)) {
          pGoodsInListLuv.setServiceSpecificLuv(lastUpdatedVersion);
        } else if (pItemType.equals(EShopItemType.SEGOODS)) {
          pGoodsInListLuv.setSeGoodSpecificLuv(lastUpdatedVersion);
        } else if (pItemType.equals(EShopItemType.SESERVICE)) {
          pGoodsInListLuv.setSeServiceSpecificLuv(lastUpdatedVersion);
        } else {
          throw new Exception("NYI for " + pItemType);
        }
        getSrvOrm().updateEntity(pReqVars, pGoodsInListLuv);
        this.srvDatabase.commitTransaction();
      } catch (Exception ex) {
        if (!this.srvDatabase.getIsAutocommit()) {
          this.srvDatabase.rollBackTransaction();
        }
        throw ex;
      } finally {
        this.srvDatabase.releaseResources();
      }
    } while (currentStep++ < steps);
  }

  /**
   * <p>Find find I18n Specific Group Name with given specifics group and lang.</p>
   * @param pSpecificsOfItemGroupLst list
   * @param pSpecGr Specific Group
   * @param pLang lang
   * @return group name
   **/
  protected final String findI18nSpecGrName(final List<I18nSpecificsOfItemGroup> pSpecificsOfItemGroupLst,
                                            final SpecificsOfItemGroup pSpecGr, final Languages pLang) {
    if (pSpecificsOfItemGroupLst != null) {
      for (I18nSpecificsOfItemGroup i18nspg : pSpecificsOfItemGroupLst) {
        if (i18nspg.getHasName().getItsId().equals(pSpecGr.getItsId())
                && i18nspg.getLang().getItsId().equals(pLang.getItsId())) {
          return i18nspg.getItsName();
        }
      }
    }
    return pSpecGr.getItsName();
  }

  /**
   * <p>Find find I18n UOM Name with given UOM and lang.</p>
   * @param pI18nUomLst list
   * @param pUom UOM
   * @param pLang lang
   * @return UOM name
   **/
  protected final String findUomName(final List<I18nUnitOfMeasure> pI18nUomLst,
                                     final UnitOfMeasure pUom, final Languages pLang) {
    for (I18nUnitOfMeasure i18uom : pI18nUomLst) {
      if (i18uom.getHasName().getItsId().equals(pUom.getItsId())
              && i18uom.getLang().getItsId().equals(pLang.getItsId())) {
        return i18uom.getItsName();
      }
    }
    return pUom.getItsName();
  }

  /**
   * <p>Find find I18n Chooseable Specific Name with given specifics and lang.</p>
   * @param pSpecificsOfItemLst list
   * @param pSpec Specific Group
   * @param pLang lang
   * @return specifics name
   **/
  protected final String findChSpecName(final List<I18nChooseableSpecifics> pSpecificsOfItemLst,
                                        final ChooseableSpecifics pSpec, final Languages pLang) {
    if (pSpecificsOfItemLst != null) {
      for (I18nChooseableSpecifics i18nsp : pSpecificsOfItemLst) {
        if (i18nsp.getHasName().getItsId().equals(pSpec.getItsId())
                && i18nsp.getLang().getItsId().equals(pLang.getItsId())) {
          return i18nsp.getItsName();
        }
      }
    }
    return pSpec.getItsName();
  }

  /**
   * <p>Find find I18n Specific Name with given specifics and lang.</p>
   * @param pSpecificsOfItemLst list
   * @param pSpec Specific Group
   * @param pLang lang
   * @return specifics name
   **/
  protected final String findSpecName(final List<I18nSpecificsOfItem> pSpecificsOfItemLst,
                                      final SpecificsOfItem pSpec, final Languages pLang) {
    for (I18nSpecificsOfItem i18nsp : pSpecificsOfItemLst) {
      if (i18nsp.getHasName().getItsId().equals(pSpec.getItsId())
              && i18nsp.getLang().getItsId().equals(pLang.getItsId())) {
        return i18nsp.getItsName();
      }
    }
    return pSpec.getItsName();
  }

  /**
   * <p>Find I18nSpecificInList with given item, type and lang.</p>
   * @param pSpecificInListLst list
   * @param pItem item
   * @param pItemType item type
   * @param pLang lang
   * @return I18nSpecificInList with given goods and lang if exist or null
   **/
  protected final I18nSpecificInList findI18nSpecificInListFor(final List<I18nSpecificInList> pSpecificInListLst,
                                                               final IHasIdLongVersionName pItem, final EShopItemType pItemType, final Languages pLang) {
    int j = 0;
    while (j < pSpecificInListLst.size()) {
      if (pSpecificInListLst.get(j).getItemId().equals(pItem.getItsId())
              && pSpecificInListLst.get(j).getItsType().equals(pItemType)
              && pSpecificInListLst.get(j).getLang().getItsId().equals(pLang.getItsId())) {
        return pSpecificInListLst.get(j);
      }
      j++;
    }
    return null;
  }

  /**
   * <p>Find ItemInList with given item ID and type.</p>
   * @param pItemsList items list
   * @param pItemId Item ID
   * @param pItemType Item type
   * @return ItemInList with given item and type if exist or null
   **/
  protected final ItemInList findItemInListFor(final List<ItemInList> pItemsList, final Long pItemId, final EShopItemType pItemType) {
    int j = 0;
    while (j < pItemsList.size()) {
      if (pItemsList.get(j).getItsType().equals(pItemType)
              &&  pItemsList.get(j).getItemId().equals(pItemId)) {
        return pItemsList.get(j);
      }
      j++;
    }
    return null;
  }

  /**
   * <p>Find the first index of specific with given item.</p>
   * @param <T> item specifics type
   * @param pOutdGdSpList GS list
   * @param pItem Goods
   * @return the first index of specific with given item
   * it throws Exception - if out of bounds
   **/
  protected final <T extends AItemSpecifics<?,  ?>> int findFirstIdxFor(final List<T> pOutdGdSpList, final IHasIdLongVersionName pItem) {
    int j = 0;
    while (!pOutdGdSpList.get(j).getItem().getItsId().equals(pItem.getItsId())) {
      j++;
    }
    return j;
  }

  /**
   * <p>Find Html Template with given ID.</p>
   * @param pHtmlTemplates Html Templates
   * @param pId ID
   * @return template with given ID
   * @throws Exception - if not found
   **/
  protected final HtmlTemplate findTemplate(final List<HtmlTemplate> pHtmlTemplates, final Long pId) throws Exception {
    for (HtmlTemplate tmpl : pHtmlTemplates) {
      if (tmpl.getItsId().equals(pId)) {
        return tmpl;
      }
    }
    throw new Exception("Algorithm error/or template deleted: template not found for ID" + pId);
  }

  /**
   * <p>Create ItemInList for item.</p>
   * @param <I> item type
   * @param pReqVars additional param
   * @param pItem Item
   * @return ItemInList
   * @throws Exception - an exception
   **/
  protected final <I extends IHasIdLongVersionName> ItemInList createItemInList(final Map<String, Object> pReqVars, final I pItem) throws Exception {
    ItemInList itemInList = new ItemInList();
    itemInList.setIsNew(true);
    EShopItemType itemType;
    if (pItem.getClass() == InvItem.class) {
      itemType = EShopItemType.GOODS;
    } else if (pItem.getClass() == ServiceToSale.class) {
      itemType = EShopItemType.SERVICE;
    } else if (pItem.getClass() == SeGoods.class) {
      itemType = EShopItemType.SEGOODS;
    } else if (pItem.getClass() == SeService.class) {
      itemType = EShopItemType.SESERVICE;
    } else {
      throw new Exception("NYI for " + pItem.getClass());
    }
    itemInList.setItsType(itemType);
    itemInList.setItemId(pItem.getItsId());
    itemInList.setItsName(pItem.getItsName());
    itemInList.setAvailableQuantity(BigDecimal.ZERO);
    return itemInList;
  }

  //Simple getters and setters:
  /**
   * <p>Getter for srvDatabase.</p>
   * @return ISrvDatabase<RS>
   **/
  public final ISrvDatabase<RS> getSrvDatabase() {
    return this.srvDatabase;
  }

  /**
   * <p>Setter for srvDatabase.</p>
   * @param pSrvDatabase reference
   **/
  public final void setSrvDatabase(final ISrvDatabase<RS> pSrvDatabase) {
    this.srvDatabase = pSrvDatabase;
  }

  /**
   * <p>Geter for srvOrm.</p>
   * @return ISrvOrm<RS>
   **/
  public final ISrvOrm<RS> getSrvOrm() {
    return this.srvOrm;
  }

  /**
   * <p>Setter for srvOrm.</p>
   * @param pSrvOrm reference
   **/
  public final void setSrvOrm(final ISrvOrm<RS> pSrvOrm) {
    this.srvOrm = pSrvOrm;
  }

  /**
   * <p>Getter for srvNumberToString.</p>
   * @return ISrvNumberToString
   **/
  public final ISrvNumberToString getSrvNumberToString() {
    return this.srvNumberToString;
  }

  /**
   * <p>Setter for srvNumberToString.</p>
   * @param pSrvNumberToString reference
   **/
  public final void setSrvNumberToString(
          final ISrvNumberToString pSrvNumberToString) {
    this.srvNumberToString = pSrvNumberToString;
  }
}
