package com.microsoft.azure.cassandraproxy;

import io.vertx.core.net.impl.NetSocketImpl;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import com.datastax.oss.protocol.internal.Compressor;
import com.datastax.oss.protocol.internal.Frame;
import com.datastax.oss.protocol.internal.FrameCodec;
import com.datastax.oss.protocol.internal.ProtocolConstants;
import com.datastax.oss.protocol.internal.request.AuthResponse;
import com.datastax.oss.protocol.internal.request.Execute;
import com.datastax.oss.protocol.internal.response.Supported;
import com.datastax.oss.protocol.internal.util.Bytes;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import io.vertx.core.net.NetClientOptions;
import io.vertx.micrometer.backends.BackendRegistries;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.microsoft.azure.cassandraproxy.ProxyClient.*;

public class ProxyClientBlockTest {

    @Test(expected = NullPointerException.class)
    public void testLine255() throws Exception {
        boolean metrics = true;
        java.lang.String socket__remoteAddress____toString__ = "10.0.1.1";
        boolean wait = true;
        long startPause = 1000000;
        if (metrics) {
            MeterRegistry registry = BackendRegistries.getDefaultNow();
            Timer.builder("cassandraProxy.clientSocket.paused").tag("clientAddress", socket__remoteAddress____toString__).tag("wait", String.valueOf(wait)).register(registry).record(System.nanoTime() % startPause, TimeUnit.NANOSECONDS);
        }
    }

    @Test
    public void testLine258() throws Exception {
        boolean metrics = false;
        java.lang.String socket__remoteAddress____toString__ = "10.0.1.1";
        boolean wait = true;
        long startPause = 1000000;
        if (metrics) {
            MeterRegistry registry = BackendRegistries.getDefaultNow();
            Timer.builder("cassandraProxy.clientSocket.paused").tag("clientAddress", socket__remoteAddress____toString__).tag("wait", String.valueOf(wait)).register(registry).record(System.nanoTime() % startPause, TimeUnit.NANOSECONDS);
        }
    }
}
