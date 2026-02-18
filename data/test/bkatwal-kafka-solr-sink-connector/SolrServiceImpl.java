package com.bkatwal.kafkaproject.service;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.types.Flow.IfStmt;
import static org.blocktest.utils.Constant.*;

import com.bkatwal.kafkaproject.api.SolrService;
import com.bkatwal.kafkaproject.utils.SolrAtomicUpdateOperations;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static org.apache.solr.common.SolrException.ErrorCode.SERVER_ERROR;

/** @author "Bikas Katwal" 06/09/18 */
@Slf4j
@RequiredArgsConstructor
public class SolrServiceImpl implements SolrService {

  @NonNull private final SolrClient solrClient;

  @Override
  public UpdateResponse deleteById(
          final String collection, final String id, final int commitWithin) {

    try {
      if (commitWithin == -1) {
        return solrClient.deleteById(collection, id);
      }

      return solrClient.deleteById(collection, id, commitWithin);

    } catch (Exception e) {
      throw new SolrException(SERVER_ERROR, "Failed to delete record with id: ".concat(id), e);
    }
  }

  @Override
  public UpdateResponse updateSingleDoc(
          final String collection, final Map<String, Object> record, final int commitWithin) {
    try {
      if (commitWithin == -1) {
        return solrClient.add(collection, convertToSolrInputDocument(record));
      }
      return solrClient.add(collection, convertToSolrInputDocument(record), commitWithin);
    } catch (Exception e) {
      throw new SolrException(SERVER_ERROR, "Failed to update single record: ", e);
    }
  }

  @Override
  public UpdateResponse updateSingleDoc(
          String collection, SolrInputDocument record, final int commitWithin) {
    try {
      if (commitWithin == -1) {
        return solrClient.add(collection, record);
      }
      return solrClient.add(collection, record, commitWithin);
    } catch (Exception e) {
      throw new SolrException(SERVER_ERROR, "Failed to update single record: ", e);
    }
  }

  @Override
  public <T> UpdateResponse updateSingleDoc(
          final String collection, final T record, final int commitWithin) {

    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> map =
            objectMapper.convertValue(record, new TypeReference<Map<String, Object>>() {});
    return updateSingleDoc(collection, map, commitWithin);
  }

  @Override
  public UpdateResponse updateFieldsInDoc(
          final String collection,
          final String id,
          final String field,
          final SolrAtomicUpdateOperations solrAtomicUpdateOperations,
          final Object newVal,
          final int commitWithin) {

    try {
      SolrInputDocument sdoc = new SolrInputDocument();
      sdoc.addField("id", id);
      Map<String, Object> fieldModifier = new HashMap<>(1);
      fieldModifier.put(solrAtomicUpdateOperations.name().toLowerCase(), newVal);
      sdoc.addField(field, fieldModifier);

      return solrClient.add(collection, sdoc);
    } catch (Exception e) {
      throw new SolrException(SERVER_ERROR, "Atomic update failed: ", e);
    }
  }

  @Override
  public void closeSolrClient() {
    try {
      solrClient.close();
    } catch (IOException e) {
      log.error("could not close solr client! {}", e);
    }
  }

  // BKTODO validate unrecognized type in value
  private SolrInputDocument convertToSolrInputDocument(Map<String, Object> record) {
    SolrInputDocument doc = new SolrInputDocument();
    record.forEach(
            (key, val) -> {
              if (val != null) {
                if ("_childDocuments_".equalsIgnoreCase(key)) {
                  doc.addChildDocuments(getChildDocuments(val));
                } else {
                  if (val instanceof BigDecimal) {
                    val = ((BigDecimal) val).doubleValue();
                  }
                  doc.setField(key, val);
                }
              }
            });
    return doc;
  }

  private Collection<SolrInputDocument> getChildDocuments(Object childDocuments) {

    List<Map<String, Object>> childDocsList = (List<Map<String, Object>>) childDocuments;

    List<SolrInputDocument> solrInputDocuments = new ArrayList<>(childDocsList.size());
    for (Map<String, Object> record : childDocsList) {
      SolrInputDocument solrInputDocument = new SolrInputDocument();
      record.forEach(
              (key, val) -> {
                // BLOCKTEST EVAL: https://github.com/bkatwal/kafka-solr-sink-connector/blob/f0d022607f3d5419053bfb0c87a710fb37e00896/src/main/java/com/bkatwal/kafkaproject/service/SolrServiceImpl.java#L138-L145
                blocktest().given(key, "key").given(val, new BigDecimal(1.0001)).given(solrInputDocument, new SolrInputDocument())
                        .checkEq(solrInputDocument.getFieldValue("key").toString(), "1.0001").checkFlow(IfStmt().Then().IfStmt().Then());
                blocktest().given(key, "key").given(val, null).given(solrInputDocument, new SolrInputDocument())
                        .checkEq(solrInputDocument.getFieldValue("key"), null);
                blocktest().given(key, "key").given(val, "foo").given(solrInputDocument, new SolrInputDocument())
                        .checkEq(solrInputDocument.getFieldValue("key").toString(), "foo").checkFlow(IfStmt().Then().IfStmt());
                if (val != null) {
                  if (val instanceof BigDecimal) {
                    val = ((BigDecimal) val).doubleValue();
                  }
                  solrInputDocument.setField(key, val);
                }
              });
      solrInputDocuments.add(solrInputDocument);
    }
    return solrInputDocuments;
  }
}
