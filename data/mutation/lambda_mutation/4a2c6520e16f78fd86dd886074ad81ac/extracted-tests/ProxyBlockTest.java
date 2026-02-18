package com.microsoft.azure.cassandraproxy;

import com.datastax.oss.protocol.internal.response.event.SchemaChangeEvent;
import com.datastax.oss.protocol.internal.response.result.RowsMetadata;
import io.vertx.core.buffer.impl.BufferImpl;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.types.Flow.IfStmt;
import static org.blocktest.utils.Constant.*;
import com.datastax.oss.protocol.internal.*;
import com.datastax.oss.protocol.internal.request.Batch;
import com.datastax.oss.protocol.internal.request.Execute;
import com.datastax.oss.protocol.internal.request.Prepare;
import com.datastax.oss.protocol.internal.request.Query;
import com.datastax.oss.protocol.internal.response.Error;
import com.datastax.oss.protocol.internal.response.result.ColumnSpec;
import com.datastax.oss.protocol.internal.response.result.DefaultRows;
import com.datastax.oss.protocol.internal.response.result.Prepared;
import com.datastax.oss.protocol.internal.response.result.Rows;
import io.micrometer.core.instrument.*;
import io.micrometer.core.instrument.Timer;
import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.cli.*;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.*;
import io.vertx.micrometer.MicrometerMetricsOptions;
import io.vertx.micrometer.VertxPrometheusOptions;
import io.vertx.micrometer.backends.BackendRegistries;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import java.util.regex.Pattern;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.microsoft.azure.cassandraproxy.Proxy.*;

public class ProxyBlockTest {

    @Test
    public void testLine357() throws Exception {
        Map<ByteBuffer, Prepare> prepareMap = new ConcurrentHashMap<>();
        Message r1__message = new Prepared(new byte[] { 1, 2, 3, 4 }, new byte[] { 5, 6, 7, 8 }, null, null);
        Prepare prepareCache = new Prepare("foo", "bar");
        if (r1__message instanceof Prepared) {
            Prepared res1 = (Prepared) r1__message;
            prepareMap.put(ByteBuffer.wrap(res1.preparedQueryId), prepareCache);
        }
        assertEquals(new Prepare("foo", "bar").toString(), prepareMap.get(ByteBuffer.wrap(new byte[] { 1, 2, 3, 4 })).toString());
    }

    @Test
    public void testLine359() throws Exception {
        Map<ByteBuffer, Prepare> prepareMap = new ConcurrentHashMap<>();
        Message r1__message = new SchemaChangeEvent("1", "2", "3", "4", null);
        Prepare prepareCache = new Prepare("foo", "bar");
        if (r1__message instanceof Prepared) {
            Prepared res1 = (Prepared) r1__message;
            prepareMap.put(ByteBuffer.wrap(res1.preparedQueryId), prepareCache);
        }
        assertEquals(null, prepareMap.get(ByteBuffer.wrap(new byte[] { 1, 2, 3, 4 })));
    }

    @Test
    public void testLine389() throws Exception {
        Map<ByteBuffer, Prepare> prepareMap = new ConcurrentHashMap<>();
        FastDecode.State state = FastDecode.State.query;
        Buffer clientBuffer = BufferImpl.buffer("aaaaaaaaaa");
        boolean[] _blocktest_flow_testLine389 = new boolean[1];
        if (state == FastDecode.State.query) {
            _blocktest_flow_testLine389[0] = true;
            Proxy.LOG.error("Query: " + "foo");
        } else if (state == FastDecode.State.execute) {
            byte[] b = FastDecode.getQueryId(clientBuffer);
            Prepare prepare = prepareMap.get(ByteBuffer.wrap(b));
            if (prepare != null) {
                Proxy.LOG.error("Error only target: Prepared Statement Keyspace: " + prepare.keyspace + " Query: " + prepare.cqlQuery);
            } else {
                Proxy.LOG.error("Error only target: Prepared Statement not found");
            }
        }
        for (int _blocktest_testLine389_i = 0; _blocktest_testLine389_i < 1; _blocktest_testLine389_i += 1) {
            assertTrue(_blocktest_flow_testLine389[_blocktest_testLine389_i]);
        }
    }

    @Test
    public void testLine390() throws Exception {
        Map<ByteBuffer, Prepare> prepareMap = new ConcurrentHashMap<>();
        FastDecode.State state = FastDecode.State.execute;
        Buffer clientBuffer = BufferImpl.buffer("aaaaaaaaaa");
        boolean[] _blocktest_flow_testLine390 = new boolean[0];
        if (state == FastDecode.State.query) {
            Proxy.LOG.error("Query: " + FastDecode.getQuery(clientBuffer));
        } else if (state == FastDecode.State.execute) {
            byte[] b = new byte[] { 1, 2, 3, 4 };
            Prepare prepare = prepareMap.get(ByteBuffer.wrap(b));
            if (prepare != null) {
                Proxy.LOG.error("Error only target: Prepared Statement Keyspace: " + prepare.keyspace + " Query: " + prepare.cqlQuery);
            } else {
                Proxy.LOG.error("Error only target: Prepared Statement not found");
            }
        }
        for (int _blocktest_testLine390_i = 0; _blocktest_testLine390_i < 0; _blocktest_testLine390_i += 1) {
            assertTrue(_blocktest_flow_testLine390[_blocktest_testLine390_i]);
        }
    }

    @Test
    public void testLine391() throws Exception {
        Map<ByteBuffer, Prepare> prepareMap = new ConcurrentHashMap<ByteBuffer, Prepare>() {

            {
                put(ByteBuffer.wrap(new byte[] { 1, 2, 3, 4 }), new Prepare("abc", "def"));
            }
        };
        FastDecode.State state = FastDecode.State.execute;
        Buffer clientBuffer = BufferImpl.buffer("aaaaaaaaaa");
        boolean[] _blocktest_flow_testLine391 = new boolean[0];
        if (state == FastDecode.State.query) {
            Proxy.LOG.error("Query: " + FastDecode.getQuery(clientBuffer));
        } else if (state == FastDecode.State.execute) {
            byte[] b = new byte[] { 1, 2, 3, 4 };
            Prepare prepare = prepareMap.get(ByteBuffer.wrap(b));
            if (prepare != null) {
                Proxy.LOG.error("Error only target: Prepared Statement Keyspace: " + prepare.keyspace + " Query: " + prepare.cqlQuery);
            } else {
                Proxy.LOG.error("Error only target: Prepared Statement not found");
            }
        }
        for (int _blocktest_testLine391_i = 0; _blocktest_testLine391_i < 0; _blocktest_testLine391_i += 1) {
            assertTrue(_blocktest_flow_testLine391[_blocktest_testLine391_i]);
        }
    }
}
