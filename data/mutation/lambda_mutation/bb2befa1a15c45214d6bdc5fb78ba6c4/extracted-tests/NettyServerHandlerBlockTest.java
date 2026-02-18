package com.jessin.practice.dubbo.netty;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import com.jessin.practice.dubbo.exporter.DubboExporter;
import com.jessin.practice.dubbo.invoker.RpcInvocation;
import com.jessin.practice.dubbo.transport.Request;
import com.jessin.practice.dubbo.transport.Response;
import com.jessin.practice.dubbo.utils.StringUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.timeout.IdleStateEvent;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.jessin.practice.dubbo.netty.NettyServerHandler.*;

public class NettyServerHandlerBlockTest {

    @Test
    public void testLine110() throws Exception {
        java.lang.Throwable exception = new FileNotFoundException();
        Object result = null;
        Response response = new Response();
        if (exception != null) {
            response.setException(true);
            response.setResult(StringUtils.toString((Throwable) exception));
        } else {
            response.setResult(result);
        }
        assertTrue(response.getResult().toString().contains("java.io.FileNotFoundException"));
        assertTrue(response.isException());
    }

    @Test
    public void testLine112() throws Exception {
        java.lang.Throwable exception = null;
        Object result = "Test";
        Response response = new Response();
        if (exception != null) {
            response.setException(true);
            response.setResult(StringUtils.toString((Throwable) exception));
        } else {
            response.setResult(result);
        }
        assertEquals("Test", response.getResult().toString());
        assertFalse(response.isException());
    }
}
