package ru.sberned.kafkalogback;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.types.Flow.IfStmt;
import static org.blocktest.utils.Constant.*;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.Layout;
import lombok.Setter;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import org.junit.Test;
import static org.junit.Assert.*;
import static ru.sberned.kafkalogback.KafkaAppender.*;

public class KafkaAppenderBlockTest {

    @Test
    public void testLine101() throws Exception {
        java.lang.String property = "a|b";
        Properties properties = new Properties();
        boolean failOnStartup = true;
        String[] p = property.split("\\|");
        if (p.length == 2) {
            properties.put(p[0], p[1]);
        } else {
            if (failOnStartup) {
            } else {
            }
        }
        assertEquals("b", properties.getProperty("a"));
    }

    @Test
    public void testLine103() throws Exception {
        java.lang.String property = "a|b|c";
        Properties properties = new Properties();
        boolean failOnStartup = true;
        boolean[] _blocktest_flow_testLine103 = new boolean[2];
        String[] p = property.split("\\|");
        if (p.length == 2) {
            properties.put(p[0], p[1]);
        } else {
            _blocktest_flow_testLine103[0] = true;
            if (failOnStartup) {
                _blocktest_flow_testLine103[1] = true;
            } else {
            }
        }
        for (int _blocktest_testLine103_i = 0; _blocktest_testLine103_i < 2; _blocktest_testLine103_i += 1) {
            assertTrue(_blocktest_flow_testLine103[_blocktest_testLine103_i]);
        }
        assertEquals(null, properties.getProperty("a"));
    }

    @Test
    public void testLine105() throws Exception {
        java.lang.String property = "a|b|c";
        Properties properties = new Properties();
        boolean failOnStartup = false;
        boolean[] _blocktest_flow_testLine105 = new boolean[2];
        String[] p = property.split("\\|");
        if (p.length == 2) {
            properties.put(p[0], p[1]);
        } else {
            _blocktest_flow_testLine105[0] = true;
            if (failOnStartup) {
            } else {
                _blocktest_flow_testLine105[1] = true;
            }
        }
        for (int _blocktest_testLine105_i = 0; _blocktest_testLine105_i < 2; _blocktest_testLine105_i += 1) {
            assertTrue(_blocktest_flow_testLine105[_blocktest_testLine105_i]);
        }
        assertEquals(null, properties.getProperty("a"));
    }
}
