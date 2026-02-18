package io.lettuce.core.cluster;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.net.SocketAddress;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import io.lettuce.core.*;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.cluster.api.NodeSelectionSupport;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import io.lettuce.core.cluster.event.ClusterTopologyChangedEvent;
import io.lettuce.core.cluster.event.TopologyRefreshEvent;
import io.lettuce.core.cluster.models.partitions.Partitions;
import io.lettuce.core.cluster.models.partitions.RedisClusterNode;
import io.lettuce.core.cluster.pubsub.StatefulRedisClusterPubSubConnection;
import io.lettuce.core.cluster.topology.ClusterTopologyRefresh;
import io.lettuce.core.cluster.topology.NodeConnectionFactory;
import io.lettuce.core.cluster.topology.TopologyComparators;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.event.jfr.EventRecorder;
import io.lettuce.core.internal.Exceptions;
import io.lettuce.core.internal.Futures;
import io.lettuce.core.internal.LettuceAssert;
import io.lettuce.core.internal.LettuceLists;
import io.lettuce.core.output.KeyValueStreamingChannel;
import io.lettuce.core.protocol.CommandExpiryWriter;
import io.lettuce.core.protocol.CommandHandler;
import io.lettuce.core.protocol.DefaultEndpoint;
import io.lettuce.core.protocol.PushHandler;
import io.lettuce.core.pubsub.PubSubCommandHandler;
import io.lettuce.core.pubsub.PubSubEndpoint;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnectionImpl;
import io.lettuce.core.resource.ClientResources;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import reactor.core.publisher.Mono;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static io.lettuce.core.cluster.RedisClusterClient.*;

public class RedisClusterClientBlockTest {

    @Test
    public void testLine1004() throws Exception {
        Partitions nextNodes = null;
        FileNotFoundException nextThrowable = new FileNotFoundException("Simulated");
        IndexOutOfBoundsException throwable = new IndexOutOfBoundsException("Another");
        CompletableFuture<Partitions> future = new CompletableFuture<>();
        if (nextThrowable != null) {
            Throwable exception = Exceptions.unwrap(nextThrowable);
            exception.addSuppressed(Exceptions.unwrap(throwable));
            future.completeExceptionally(exception);
        } else {
        }
        assertTrue(future.isCompletedExceptionally());
    }

    @Test
    public void testLine1006() throws Exception {
        Partitions nextNodes = null;
        FileNotFoundException nextThrowable = null;
        IndexOutOfBoundsException throwable = new IndexOutOfBoundsException("Another");
        CompletableFuture<Partitions> future = new CompletableFuture<>();
        if (nextThrowable != null) {
            Throwable exception = Exceptions.unwrap(nextThrowable);
            exception.addSuppressed(Exceptions.unwrap(throwable));
            future.completeExceptionally(exception);
        } else {
        }
        assertFalse(future.isCompletedExceptionally());
    }

    @Test
    public void testLine1027() throws Exception {
        Predicate<RedisClusterNode> nodeFilter = ClusterClientOptions.DEFAULT_NODE_FILTER;
        Partitions partitions = new Partitions();
        partitions.addPartition(new RedisClusterNode());
        partitions.addPartition(new RedisClusterNode());
        List<RedisClusterNode> toRemove = new ArrayList<>();
        for (RedisClusterNode partition : partitions) {
            if (!nodeFilter.test(partition)) {
                System.out.println("REMOVED");
                toRemove.add(partition);
            }
        }
        partitions.removeAll(toRemove);
        assertFalse(partitions.isEmpty());
    }
}
