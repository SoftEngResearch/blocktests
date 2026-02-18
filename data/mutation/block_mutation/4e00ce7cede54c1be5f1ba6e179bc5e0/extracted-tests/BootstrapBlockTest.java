package krpc.rpc.bootstrap;

import com.google.protobuf.DescriptorProtos.FileDescriptorProto;
import com.google.protobuf.DescriptorProtos.FileDescriptorSet;
import com.google.protobuf.Descriptors.*;
import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.UnknownFieldSet.Field;
import krpc.KrpcExt;
import krpc.common.*;
import krpc.rpc.cluster.*;
import krpc.rpc.core.*;
import krpc.rpc.core.proto.RpcMetas;
import krpc.rpc.dynamicroute.DefaultDynamicRouteManager;
import krpc.rpc.impl.*;
import krpc.rpc.impl.transport.DefaultRpcCodec;
import krpc.rpc.impl.transport.NettyClient;
import krpc.rpc.impl.transport.NettyServer;
import krpc.rpc.monitor.*;
import krpc.rpc.registry.DefaultRegistryManager;
import krpc.rpc.util.EnvNamesUtils;
import krpc.rpc.util.IpUtils;
import krpc.rpc.web.*;
import krpc.rpc.web.impl.DefaultRpcDataConverter;
import krpc.rpc.web.impl.DefaultWebRouteService;
import krpc.rpc.web.impl.NettyHttpServer;
import krpc.rpc.web.impl.WebServer;
import krpc.trace.Span;
import krpc.trace.Trace;
import krpc.trace.TraceAdapter;
import krpc.trace.sniffer.Advice;
import krpc.trace.sniffer.AdviceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static krpc.rpc.bootstrap.Bootstrap.*;

public class BootstrapBlockTest {

    @Test
    public void test1() throws Exception {
        ServerConfig lastServer = new ServerConfig("last");
        HashMap<String, ServerConfig> servers = new LinkedHashMap<>();
        RefererConfig c;
        c = new RefererConfig("a");
        c.setTransport("default");
        if (c.transport.equals("default") && servers.size() == 0) {
            servers.put("default", new ServerConfig("default"));
        } else if (c.transport.equals("default") && servers.size() == 1) {
            c.transport = lastServer.id;
        } else {
            throw new RuntimeException(String.format("referer transport %s not found", c.transport));
        }
        assertTrue(servers.containsKey("default"));
    }

    @Test
    public void test2() throws Exception {
        ServerConfig lastServer = new ServerConfig("last");
        HashMap<String, ServerConfig> servers = new LinkedHashMap<>();
        RefererConfig c;
        c = new RefererConfig("a");
        c.setTransport("default");
        servers.put("foo", new ServerConfig("foo"));
        if (c.transport.equals("default") && servers.size() == 0) {
            servers.put("default", new ServerConfig("default"));
        } else if (c.transport.equals("default") && servers.size() == 1) {
            c.transport = lastServer.id;
        } else {
            throw new RuntimeException(String.format("referer transport %s not found", c.transport));
        }
        assertTrue(c.transport.equals("last"));
    }

    @Test(expected = RuntimeException.class)
    public void test3() throws Exception {
        ServerConfig lastServer = new ServerConfig("last");
        HashMap<String, ServerConfig> servers = new LinkedHashMap<>();
        RefererConfig c;
        c = new RefererConfig("a");
        c.setTransport("foo");
        try {
            if (c.transport.equals("default") && servers.size() == 0) {
                servers.put("default", new ServerConfig("default"));
            } else if (c.transport.equals("default") && servers.size() == 1) {
                c.transport = lastServer.id;
            } else {
                throw new RuntimeException(String.format("referer transport %s not found", c.transport));
            }
            return;
        } finally {
        }
    }
}
