package com.jessin.practice.dubbo.invoker;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import com.jessin.practice.dubbo.config.InterfaceConfig;
import com.jessin.practice.dubbo.netty.NettyClient;
import com.jessin.practice.dubbo.netty.NettyManager;
import com.jessin.practice.dubbo.transport.DefaultFuture;
import com.jessin.practice.dubbo.transport.Request;
import com.jessin.practice.dubbo.transport.Response;
import java.io.FileNotFoundException;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.jessin.practice.dubbo.invoker.DubboInvoker.*;

public class DubboInvokerBlockTest {

    @Test
    public void testLine64() throws Exception {
        java.lang.String result = "foo";
        FileNotFoundException exception = new FileNotFoundException();
        CompletableFuture ret = new CompletableFuture();
        if (exception != null) {
            ret.completeExceptionally(exception);
        } else {
        }
        assertTrue(ret.isCompletedExceptionally());
    }

    @Test
    public void testLine65() throws Exception {
        java.lang.String result = "foo";
        FileNotFoundException exception = null;
        CompletableFuture ret = new CompletableFuture();
        if (exception != null) {
            ret.completeExceptionally(exception);
        } else {
        }
        assertTrue(ret.isDone());
    }
}
