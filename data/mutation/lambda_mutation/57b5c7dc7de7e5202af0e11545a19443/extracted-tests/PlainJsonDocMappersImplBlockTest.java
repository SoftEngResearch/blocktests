package com.bkatwal.kafkaproject.service;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.types.Flow.IfStmt;
import static org.blocktest.utils.Constant.*;
import com.bkatwal.kafkaproject.api.JsonDocMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.solr.common.SolrInputDocument;
import org.blocktest.types.Flow;
import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.bkatwal.kafkaproject.service.PlainJsonDocMappersImpl.*;

public class PlainJsonDocMappersImplBlockTest {

    @Test
    public void testLine111() throws Exception {
        java.lang.String key = "key";
        java.lang.Object val = new BigDecimal(12.25);
        SolrInputDocument solrInputDocument = new SolrInputDocument();
        boolean[] _blocktest_flow_testLine111 = new boolean[2];
        if (val != null) {
            _blocktest_flow_testLine111[0] = true;
            if (val instanceof BigDecimal) {
                _blocktest_flow_testLine111[1] = true;
            }
            solrInputDocument.setField(key, val);
        }
        for (int _blocktest_testLine111_i = 0; _blocktest_testLine111_i < 2; _blocktest_testLine111_i += 1) {
            assertTrue(_blocktest_flow_testLine111[_blocktest_testLine111_i]);
        }
        assertEquals("12.25", solrInputDocument.getFieldValue("key").toString());
    }

    @Test
    public void testLine113() throws Exception {
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
    public void testLine115() throws Exception {
        java.lang.String key = "key";
        java.lang.Object val = Double.valueOf(12.55);
        SolrInputDocument solrInputDocument = new SolrInputDocument();
        boolean[] _blocktest_flow_testLine115 = new boolean[1];
        if (val != null) {
            _blocktest_flow_testLine115[0] = true;
            if (val instanceof BigDecimal) {
            }
            solrInputDocument.setField(key, val);
        }
        for (int _blocktest_testLine115_i = 0; _blocktest_testLine115_i < 1; _blocktest_testLine115_i += 1) {
            assertTrue(_blocktest_flow_testLine115[_blocktest_testLine115_i]);
        }
        assertEquals("12.55", solrInputDocument.getFieldValue("key").toString());
    }
}
