package io.lettuce.core;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import io.lettuce.core.api.AsyncCloseable;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.internal.LettuceAssert;
import io.lettuce.core.protocol.CommandExpiryWriter;
import io.lettuce.core.protocol.CommandWrapper;
import io.lettuce.core.protocol.ConnectionFacade;
import io.lettuce.core.protocol.RedisCommand;
import io.lettuce.core.protocol.TracedCommand;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.tracing.TraceContextProvider;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static io.lettuce.core.RedisChannelHandler.*;

public class RedisChannelHandlerBlockTest {

    @Test
    public void testLine189() throws Exception {
        CompletableFuture<Void> closeFuture = new CompletableFuture<>();
        java.lang.Throwable t = new Throwable();
        java.lang.Void v = null;
        CloseEvents closeEvents = new CloseEvents();
        closeEvents = new CloseEvents();
        if (t != null) {
            closeFuture.completeExceptionally(t);
        } else {
        }
        assertTrue(closeFuture.isCompletedExceptionally());
    }

    @Test
    public void testLine193() throws Exception {
        CompletableFuture<Void> closeFuture = new CompletableFuture<>();
        java.lang.Throwable t = null;
        java.lang.Void v = null;
        CloseEvents closeEvents = new CloseEvents();
        closeEvents = new CloseEvents();
        if (t != null) {
            closeFuture.completeExceptionally(t);
        } else {
        }
        assertFalse(closeFuture.isCompletedExceptionally());
    }
}
