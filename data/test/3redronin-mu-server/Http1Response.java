package io.muserver;

import io.netty.channel.*;
import io.netty.channel.local.LocalServerChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.*;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

class Http1Response extends NettyResponseAdaptor {

    final ChannelHandlerContext ctx;
    private final Http1Headers headers;

    Http1Response(ChannelHandlerContext ctx, NettyRequestAdapter request, Http1Headers headers) {
        super(request, headers);
        this.ctx = ctx;
        this.headers = headers;
    }

    @Override
    protected ChannelFuture startStreaming() {
        super.startStreaming();
        HttpResponse response = isHead ? new EmptyHttpResponse(httpStatus()) : new DefaultHttpResponse(HTTP_1_1, httpStatus(), false);
        if (declaredLength == -1) {
            headers.set(HeaderNames.TRANSFER_ENCODING, HeaderValues.CHUNKED);
        }
        writeHeaders(response);
        return ctx.write(response);
    }

    @Override
    protected void onContentLengthMismatch() {
        throw new IllegalStateException("The declared content length for " + request + " was " + declaredLength + " bytes. " +
            "The current write is being aborted and the connection is being closed because it would have resulted in " +
            bytesStreamed + " bytes being sent.");
    }

    private void writeHeaders(HttpResponse response) {
        HttpHeaders rh = response.headers();
        for (Map.Entry<String, String> header : this.headers) {
            rh.add(header.getKey(), header.getValue());
        }
    }


    @Override
    ChannelFuture writeAndFlushToChannel(boolean isLast, ByteBuf content) {
        HttpContent msg = isLast ? new DefaultLastHttpContent(content) : new DefaultHttpContent(content);
        return ctx.writeAndFlush(msg);
    }

    @Override
    protected ChannelFuture writeFullResponse(ByteBuf body) {
        if (!ctx.executor().inEventLoop()) {
            ChannelPromise promise = ctx.newPromise();
            ctx.executor().submit(() -> {
                writeFullResponse(body).addListener(future -> {
                    // BLOCKTEST EVAL: https://github.com/3redronin/mu-server/blob/69683cf8009d7a0ac52452fd4b0abe06ffd0bf87/src/main/java/io/muserver/Http1Response.java#L61-L67
                    blocktest().given(future, new Future<Object>() {
                        @Override
                        public boolean isSuccess() {
                            return true;
                        }

                        @Override
                        public boolean isCancellable() {
                            return false;
                        }

                        @Override
                        public Throwable cause() {
                            return null;
                        }

                        @Override
                        public Future sync() throws InterruptedException {
                            return null;
                        }

                        @Override
                        public Future syncUninterruptibly() {
                            return null;
                        }

                        @Override
                        public Future await() throws InterruptedException {
                            return null;
                        }

                        @Override
                        public Future awaitUninterruptibly() {
                            return null;
                        }

                        @Override
                        public boolean await(long l, TimeUnit timeUnit) throws InterruptedException {
                            return false;
                        }

                        @Override
                        public boolean await(long l) throws InterruptedException {
                            return false;
                        }

                        @Override
                        public boolean awaitUninterruptibly(long l, TimeUnit timeUnit) {
                            return false;
                        }

                        @Override
                        public boolean awaitUninterruptibly(long l) {
                            return false;
                        }

                        @Override
                        public Object getNow() {
                            return null;
                        }

                        @Override
                        public boolean cancel(boolean b) {
                            return false;
                        }

                        @Override
                        public boolean isCancelled() {
                            return false;
                        }

                        @Override
                        public boolean isDone() {
                            return false;
                        }

                        @Override
                        public Object get() throws InterruptedException, ExecutionException {
                            return null;
                        }

                        @Override
                        public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                            return null;
                        }

                        @Override
                        public Future removeListeners(GenericFutureListener[] genericFutureListeners) {
                            return null;
                        }

                        @Override
                        public Future removeListener(GenericFutureListener genericFutureListener) {
                            return null;
                        }

                        @Override
                        public Future addListeners(GenericFutureListener[] genericFutureListeners) {
                            return null;
                        }

                        @Override
                        public Future addListener(GenericFutureListener genericFutureListener) {
                            return null;
                        }
                    }).given(promise, new DefaultChannelProgressivePromise(new LocalServerChannel())).checkTrue(promise.isSuccess());
                    blocktest().given(future, new Future<Object>() {
                        @Override
                        public boolean isSuccess() {
                            return false;
                        }

                        @Override
                        public boolean isCancellable() {
                            return false;
                        }

                        @Override
                        public Throwable cause() {
                            return new FileNotFoundException();
                        }

                        @Override
                        public Future sync() throws InterruptedException {
                            return null;
                        }

                        @Override
                        public Future syncUninterruptibly() {
                            return null;
                        }

                        @Override
                        public Future await() throws InterruptedException {
                            return null;
                        }

                        @Override
                        public Future awaitUninterruptibly() {
                            return null;
                        }

                        @Override
                        public boolean await(long l, TimeUnit timeUnit) throws InterruptedException {
                            return false;
                        }

                        @Override
                        public boolean await(long l) throws InterruptedException {
                            return false;
                        }

                        @Override
                        public boolean awaitUninterruptibly(long l, TimeUnit timeUnit) {
                            return false;
                        }

                        @Override
                        public boolean awaitUninterruptibly(long l) {
                            return false;
                        }

                        @Override
                        public Object getNow() {
                            return null;
                        }

                        @Override
                        public boolean cancel(boolean b) {
                            return false;
                        }

                        @Override
                        public boolean isCancelled() {
                            return false;
                        }

                        @Override
                        public boolean isDone() {
                            return false;
                        }

                        @Override
                        public Object get() throws InterruptedException, ExecutionException {
                            return null;
                        }

                        @Override
                        public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                            return null;
                        }

                        @Override
                        public Future removeListeners(GenericFutureListener[] genericFutureListeners) {
                            return null;
                        }

                        @Override
                        public Future removeListener(GenericFutureListener genericFutureListener) {
                            return null;
                        }

                        @Override
                        public Future addListeners(GenericFutureListener[] genericFutureListeners) {
                            return null;
                        }

                        @Override
                        public Future addListener(GenericFutureListener genericFutureListener) {
                            return null;
                        }
                    }).given(promise, new DefaultChannelProgressivePromise(new LocalServerChannel())).checkFalse(promise.isSuccess()).checkEq(promise.cause().toString(), "java.io.FileNotFoundException");
                    if (future.isSuccess()) {
                        promise.setSuccess();
                    } else {
                        promise.setFailure(future.cause());
                    }
                });
            });
            return promise;
        }

        FullHttpResponse resp = isHead ?
            new EmptyHttpResponse(httpStatus())
            : new DefaultFullHttpResponse(HTTP_1_1, httpStatus(), body, false);
        writeHeaders(resp);
        return ctx.writeAndFlush(resp);
    }

    @Override
    protected ChannelFuture sendEmptyResponse(boolean addContentLengthHeader) {
        HttpResponse msg = isHead ?
            new EmptyHttpResponse(httpStatus()) :
            new DefaultFullHttpResponse(HTTP_1_1, httpStatus(), false);
        writeHeaders(msg);
        if (addContentLengthHeader) {
            msg.headers().set(HeaderNames.CONTENT_LENGTH, HeaderValues.ZERO);
        }
        return ctx.writeAndFlush(msg);
    }

    @Override
    protected ChannelFuture writeLastContentMarker() {
        return ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
    }

    @Override
    public String toString() {
        return "Http1Response{" +
            "outputState=" + outputState() +
            ", status=" + status +
            "}";
    }
}
