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
import org.junit.Test;
import static org.junit.Assert.*;
import static com.bkatwal.kafkaproject.service.SolrServiceImpl.*;

public class SolrServiceImplBlockTest {

    @Test
    public void testLine146() throws Exception {
        java.lang.String key = "key";
        java.lang.Object val = new BigDecimal(1.0001);
        SolrInputDocument solrInputDocument = new SolrInputDocument();
        boolean[] _blocktest_flow_testLine146 = new boolean[2];
        if (val != null) {
            _blocktest_flow_testLine146[0] = true;
            if (val instanceof BigDecimal) {
                _blocktest_flow_testLine146[1] = true;
            }
            solrInputDocument.setField(key, val);
        }
        for (int _blocktest_testLine146_i = 0; _blocktest_testLine146_i < 2; _blocktest_testLine146_i += 1) {
            assertTrue(_blocktest_flow_testLine146[_blocktest_testLine146_i]);
        }
        assertEquals("1.0001", solrInputDocument.getFieldValue("key").toString());
    }

    @Test
    public void testLine148() throws Exception {
        java.lang.String key = "key";
        java.lang.Object val = null;
        SolrInputDocument solrInputDocument = new SolrInputDocument();
        if (val != null) {
            if (val instanceof BigDecimal) {
            }
            solrInputDocument.setField(key, val);
        }
        assertEquals(null, solrInputDocument.getFieldValue("key"));
    }

    @Test
    public void testLine150() throws Exception {
        java.lang.String key = "key";
        java.lang.Object val = "foo";
        SolrInputDocument solrInputDocument = new SolrInputDocument();
        boolean[] _blocktest_flow_testLine150 = new boolean[1];
        if (val != null) {
            _blocktest_flow_testLine150[0] = true;
            if (val instanceof BigDecimal) {
            }
            solrInputDocument.setField(key, val);
        }
        for (int _blocktest_testLine150_i = 0; _blocktest_testLine150_i < 1; _blocktest_testLine150_i += 1) {
            assertTrue(_blocktest_flow_testLine150[_blocktest_testLine150_i]);
        }
        assertEquals("foo", solrInputDocument.getFieldValue("key").toString());
    }
}
