package io.playpen.core.coordinator.network;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.playpen.core.Bootstrap;
import io.playpen.core.Initialization;
import io.playpen.core.coordinator.CoordinatorMode;
import io.playpen.core.coordinator.PlayPen;
import io.playpen.core.coordinator.network.authenticator.IAuthenticator;
import io.playpen.core.networking.TransactionInfo;
import io.playpen.core.networking.TransactionManager;
import io.playpen.core.networking.netty.AuthenticatedMessageInitializer;
import io.playpen.core.p3.P3Package;
import io.playpen.core.p3.PackageException;
import io.playpen.core.p3.PackageManager;
import io.playpen.core.plugin.EventManager;
import io.playpen.core.plugin.IPlugin;
import io.playpen.core.plugin.PluginManager;
import io.playpen.core.protocol.Commands;
import io.playpen.core.protocol.Coordinator;
import io.playpen.core.protocol.P3;
import io.playpen.core.protocol.Protocol;
import io.playpen.core.utils.AuthUtils;
import lombok.Data;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import java.net.InetAddress;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static io.playpen.core.coordinator.network.Network.*;

public class NetworkBlockTest {

    @Test
    public void testLine382() throws Exception {
        Map<String, ConsoleInfo> consoles = new HashMap<>();
        LocalCoordinator local = new LocalCoordinator();
        local.setUuid("123");
        ConsoleInfo c1 = new ConsoleInfo();
        c1.setAttached("123");
        c1.setCoordinator("coord1");
        consoles.put("console1", c1);
        ConsoleInfo c2 = new ConsoleInfo();
        c2.setAttached("456");
        c2.setCoordinator("coord2");
        consoles.put("console2", c2);
        Iterator<Map.Entry<String, ConsoleInfo>> itr = consoles.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, ConsoleInfo> entry = itr.next();
            if (Objects.equals(entry.getValue().getAttached(), local.getUuid())) {
                itr.remove();
            }
            continue;
        }
        assertEquals(1, consoles.size());
    }

    @Test
    public void testLine397() throws Exception {
        Map<String, ConsoleInfo> consoles = new HashMap<>();
        LocalCoordinator local = new LocalCoordinator();
        local.setUuid("123");
        ConsoleInfo c1 = new ConsoleInfo();
        c1.setAttached("123");
        c1.setCoordinator("coord1");
        consoles.put("console1", c1);
        ConsoleInfo c2 = new ConsoleInfo();
        c2.setAttached("123");
        c2.setCoordinator("coord2");
        consoles.put("console2", c2);
        Iterator<Map.Entry<String, ConsoleInfo>> itr = consoles.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, ConsoleInfo> entry = itr.next();
            if (Objects.equals(entry.getValue().getAttached(), local.getUuid())) {
                itr.remove();
            }
            continue;
        }
        assertEquals(0, consoles.size());
    }

    @Test
    public void testLine412() throws Exception {
        Map<String, ConsoleInfo> consoles = new HashMap<>();
        LocalCoordinator local = new LocalCoordinator();
        local.setUuid("456");
        ConsoleInfo c1 = new ConsoleInfo();
        c1.setAttached("123");
        c1.setCoordinator("coord1");
        consoles.put("console1", c1);
        ConsoleInfo c2 = new ConsoleInfo();
        c2.setAttached("123");
        c2.setCoordinator("coord2");
        consoles.put("console2", c2);
        Iterator<Map.Entry<String, ConsoleInfo>> itr = consoles.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, ConsoleInfo> entry = itr.next();
            if (Objects.equals(entry.getValue().getAttached(), local.getUuid())) {
                itr.remove();
            }
            continue;
        }
        assertEquals(2, consoles.size());
    }
}
