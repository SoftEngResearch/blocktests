package org.jetlang.remote.client;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.BTest.lambdatest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.jetlang.channels.Channel;
import org.jetlang.channels.ChannelSubscription;
import org.jetlang.channels.Subscribable;
import org.jetlang.channels.Subscriber;
import org.jetlang.core.Callback;
import org.jetlang.core.Disposable;
import org.jetlang.core.DisposingExecutor;
import org.jetlang.fibers.Fiber;
import org.jetlang.remote.acceptor.MessageStreamWriter;
import org.jetlang.remote.core.CloseableChannel;
import org.jetlang.remote.core.ErrorHandler;
import org.jetlang.remote.core.HeartbeatEvent;
import org.jetlang.remote.core.JetlangRemotingInputStream;
import org.jetlang.remote.core.JetlangRemotingProtocol;
import org.jetlang.remote.core.MsgTypes;
import org.jetlang.remote.core.ReadTimeoutEvent;
import org.jetlang.remote.core.Serializer;
import org.jetlang.remote.core.SocketMessageStreamWriter;
import org.jetlang.remote.core.TcpSocket;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.jetlang.remote.client.JetlangTcpClient.*;

public class JetlangTcpClientBlockTest {

    @Test
    public void testLine227() throws Exception {
        Channel<ReadTimeoutEvent> ReadTimeout = null;
        AtomicBoolean lastRead = new AtomicBoolean(true);
        Runnable onReadTimeout = () -> ReadTimeout.publish(new ReadTimeoutEvent());
        if (!lastRead.get()) {
            lastRead.set(true);
        } else {
            lastRead.set(false);
        }
        assertFalse(lastRead.get());
    }

    @Test
    public void testLine228() throws Exception {
        Channel<ReadTimeoutEvent> ReadTimeout = null;
        AtomicBoolean lastRead = new AtomicBoolean(false);
        Runnable onReadTimeout = () -> ReadTimeout.publish(new ReadTimeoutEvent());
        if (!lastRead.get()) {
            lastRead.set(true);
        } else {
            lastRead.set(false);
        }
        assertTrue(lastRead.get());
    }

    @Test
    public void testLine347() throws Exception {
        Map<Integer, JetlangTcpClient.Req> pendingRequests = Collections.synchronizedMap(new HashMap<Integer, JetlangTcpClient.Req>() {

            {
                put(1, null);
                put(2, null);
            }
        });
        int id = 1;
        AtomicBoolean disposed = new AtomicBoolean(false);
        disposed.set(true);
        assertTrue(disposed.get());
        assertTrue(pendingRequests.containsKey(2));
        assertFalse(pendingRequests.containsKey(1));
    }
}
