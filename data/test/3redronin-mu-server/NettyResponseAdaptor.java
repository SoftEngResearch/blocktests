package io.muserver;

import io.netty.channel.DefaultChannelProgressivePromise;
import io.netty.channel.local.LocalServerChannel;
import io.netty.handler.timeout.TimeoutException;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static io.muserver.ContentTypes.TEXT_PLAIN_UTF8;

abstract class NettyResponseAdaptor implements MuResponse {
    private static final Logger log = LoggerFactory.getLogger(NettyResponseAdaptor.class);
    protected final boolean isHead;
    private volatile ResponseState state = ResponseState.NOTHING;
    protected final NettyRequestAdapter request;
    private final Headers headers;
    protected int status = 200;
    private volatile PrintWriter writer;
    private volatile OutputStream outputStream;
    protected long bytesStreamed = 0;
    protected long declaredLength = -1;
    private final List<ResponseStateChangeListener> listeners = new CopyOnWriteArrayList<>();
    protected HttpExchange httpExchange;

    public void setExchange(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
    }

    protected void outputState(ResponseState state) {
        assert request.ctx.executor().inEventLoop() : "Status change to " + state + " not in event loop";
        ResponseState oldStatus = this.state;
        if (oldStatus.endState()) {
            throw new IllegalStateException("Didn't expect to get a status update to " + state + " when the current status is " + oldStatus);
        }
        this.state = state;
        for (ResponseStateChangeListener listener : listeners) {
            listener.onStateChange(httpExchange, state);
        }
    }

    /**
     * Sets the output state to the given value after the given future is completed if all goes well, otherwise sets
     * it to ERRORED
     *
     * @param future       A future to wait for, or null to set the state now
     * @param successState The state to set if the future completes successfully
     */
    protected void outputState(io.netty.util.concurrent.Future<? super Void> future, ResponseState successState) {
        if (future == null) {
            outputState(successState);
            return;
        }

        future.addListener(result -> {
            if (result.isSuccess()) {
                outputState(successState);
            } else {
                if (!state.endState()) {
                    outputState(ResponseState.ERRORED);
                }
            }
        });
    }


    protected ResponseState outputState() {
        return state;
    }

    void addChangeListener(ResponseStateChangeListener responseStateChangeListener) {
        this.listeners.add(responseStateChangeListener);
    }

    void setWebsocket() {
        outputState(ResponseState.UPGRADED);
    }

    void onCancelled(ResponseState reason) {
        if (!state.endState()) {
            outputState(reason);
        }
    }

    NettyResponseAdaptor(NettyRequestAdapter request, Headers headers) {
        this.headers = headers;
        this.request = request;
        this.isHead = request.method() == Method.HEAD;
        this.headers.set(HeaderNames.DATE, Mutils.toHttpDate(new Date()));
    }

    public int status() {
        return status;
    }

    public void status(int value) {
        if (state != ResponseState.NOTHING) {
            throw new IllegalStateException("Cannot set the status after the headers have already been sent");
        }
        status = value;
    }

    protected ChannelFuture startStreaming() {
        assert httpExchange.inLoop() : "Not in event loop";
        if (state != ResponseState.NOTHING) {
            throw new IllegalStateException("Cannot start streaming when state is " + state);
        }
        declaredLength = headers.contains(HeaderNames.CONTENT_LENGTH)
            ? Long.parseLong(headers.get(HeaderNames.CONTENT_LENGTH))
            : -1;
        outputState(ResponseState.STREAMING);
        return null;
    }

    static CharSequence getVaryWithAE(String curValue) {
        if (Mutils.nullOrEmpty(curValue)) {
            return HeaderNames.ACCEPT_ENCODING;
        } else {
            if (!curValue.toLowerCase().contains(HeaderNames.ACCEPT_ENCODING)) {
                return curValue + ", " + HeaderNames.ACCEPT_ENCODING;
            } else {
                return curValue;
            }
        }
    }

    private void throwIfFinished() {
        if (state.endState()) {
            throw new IllegalStateException("Cannot write data as response has already completed");
        }
    }

    ChannelFuture writeAndFlush(ByteBuffer data) {
        if (!httpExchange.inLoop()) {
            ChannelPromise promise = httpExchange.ctx.newPromise();
            httpExchange.ctx.executor().submit(() -> writeAndFlush(data).addListener(f -> {
                // BLOCKTEST EVAL: https://github.com/3redronin/mu-server/blob/69683cf8009d7a0ac52452fd4b0abe06ffd0bf87/src/main/java/io/muserver/NettyResponseAdaptor.java#L152-L158
                blocktest().given(f, new Future<Object>() {
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
                blocktest().given(f, new Future<Object>() {
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
                if (f.isSuccess()) {
                    promise.setSuccess();
                } else {
                    promise.setFailure(f.cause());
                }
            }));
            return promise;
        } else {
            try {
                if (state.endState()) {
                    throw new IllegalStateException("Cannot write when response state is " + state);
                }
                if (state == ResponseState.NOTHING) {
                    startStreaming();
                }
                return writeAndFlush(Unpooled.wrappedBuffer(data));
            } catch (Throwable e) {
                return httpExchange.ctx.newFailedFuture(e);
            }
        }
    }

    protected final ChannelFuture writeAndFlush(ByteBuf data) {
        throwIfFinished();
        int size = data.writerIndex();

        bytesStreamed += size;
        boolean isLast = bytesStreamed == declaredLength;

        if (declaredLength > -1 && bytesStreamed > declaredLength) {
            onContentLengthMismatch();
            isLast = true;
        }

        ByteBuf content = Unpooled.wrappedBuffer(data);
        ChannelFuture future = writeAndFlushToChannel(isLast, content);
        if (isLast) {
            future.addListener(wf -> {
                if (wf.isSuccess()) {
                    outputState(ResponseState.FULL_SENT);
                } else if (!this.state.endState()) {
                    outputState(ResponseState.ERRORED);
                }
            });
        }
        return future;
    }

    protected abstract void onContentLengthMismatch();

    abstract ChannelFuture writeAndFlushToChannel(boolean isLast, ByteBuf content);

    public void sendChunk(String text) {
        throwIfAsync();
        httpExchange.block(() -> {
            throwIfFinished();
            if (state == ResponseState.NOTHING) {
                startStreaming();
            }
            return writeAndFlush(textToBuffer(text));
        });
    }

    private ByteBuf textToBuffer(String text) {
        if (text == null) text = "";
        Charset charset = NettyRequestAdapter.bodyCharset(headers, false);
        return Unpooled.copiedBuffer(text, charset);
    }

    public void redirect(String newLocation) {
        redirect(URI.create(newLocation));
    }

    public Headers headers() {
        return headers;
    }

    public void contentType(CharSequence contentType) {
        headers.set(HeaderNames.CONTENT_TYPE, contentType);
    }

    public void addCookie(Cookie cookie) {
        headers.add(HeaderNames.SET_COOKIE, ServerCookieEncoder.LAX.encode(cookie.nettyCookie));
    }

    @Override
    public OutputStream outputStream() {
        return outputStream(4096);
    }

    @Override
    public OutputStream outputStream(int bufferSize) {
        if (this.outputStream == null) {
            ChunkedHttpOutputStream nonBuffered = new ChunkedHttpOutputStream(this);
            httpExchange.block(() -> {
                startStreaming();
                outputStream = bufferSize > 0 ? new BufferedOutputStream(nonBuffered, bufferSize) : nonBuffered;
            });
        }
        return this.outputStream;
    }

    private void throwIfAsync() {
        if (request.isAsync()) {
            throw new IllegalStateException("Cannot use blocking methods when in async mode");
        }
    }

    public PrintWriter writer() {
        throwIfAsync();
        if (this.writer == null) {
            if (!headers.contains(HeaderNames.CONTENT_TYPE)) {
                headers.set(HeaderNames.CONTENT_TYPE, TEXT_PLAIN_UTF8);
            }
            OutputStreamWriter os = new OutputStreamWriter(outputStream(), StandardCharsets.UTF_8);
            this.writer = new PrintWriter(os);
        }
        return this.writer;
    }

    @Override
    public boolean hasStartedSendingData() {
        return state != ResponseState.NOTHING;
    }

    @Override
    public ResponseState responseState() {
        return state;
    }

    void flushAndCloseOutputStream() {
        Mutils.closeSilently(writer);
        Mutils.closeSilently(outputStream);
    }

    void complete() {
        assert httpExchange.inLoop() : "Not in event loop";

        ResponseState finalState = ResponseState.FINISHED;

        ResponseState state = this.state;
        if (state.endState()) {
            return;
        }
        outputState(ResponseState.FINISHING);
        boolean isFixedLength = headers.contains(HeaderNames.CONTENT_LENGTH);
        ChannelFuture finishedFuture = null;
        if (state == ResponseState.NOTHING) {
            boolean addContentLengthHeader = !isHead && !isFixedLength && status != 204 && status != 205 && status != 304;
            finishedFuture = sendEmptyResponse(addContentLengthHeader);
        } else if (state == ResponseState.STREAMING) {
            boolean badFixedLength = !isHead && isFixedLength && declaredLength != bytesStreamed && status != 304;
            if (badFixedLength) {
                log.warn("Invalid response for " + request + " because " + declaredLength + " bytes was the " +
                    "expected length, however " + bytesStreamed + " bytes were sent.");
                finalState = ResponseState.ERRORED;
            }
            if (finalState == ResponseState.FINISHED) {
                finishedFuture = writeLastContentMarker();
            }
        }
        outputState(finishedFuture, finalState);
    }

    @Override
    public void write(String text) {
        throwIfAsync();
        httpExchange.block(() -> writeOnLoop(text).addListener(f -> outputState(f, ResponseState.FULL_SENT)));
    }

    ChannelFuture writeOnLoop(String text) {
        throwIfFinished();
        if (state != ResponseState.NOTHING) {
            String what = state == ResponseState.FULL_SENT ? "twice for one response" : "after sending chunks";
            throw new IllegalStateException("You cannot call write " + what + ". If you want to send text in multiple chunks, use sendChunk instead.");
        }
        ByteBuf body = textToBuffer(text);
        long bodyLength = body.writerIndex();

        if (!headers.contains(HeaderNames.CONTENT_TYPE)) {
            headers.set(HeaderNames.CONTENT_TYPE, TEXT_PLAIN_UTF8);
        }
        headers.set(HeaderNames.CONTENT_LENGTH, bodyLength);
        return writeFullResponse(body);
    }

    protected abstract ChannelFuture writeFullResponse(ByteBuf body);

    protected abstract ChannelFuture writeLastContentMarker();

    public final void redirect(URI newLocation) {
        if (!httpExchange.inLoop()) {
            httpExchange.ctx.executor().execute(() -> redirect(newLocation));
        } else {
            URI absoluteUrl = request.uri().resolve(newLocation).normalize();
            if (status < 300 || status > 303) {
                status(302);
            }
            headers.set(HeaderNames.LOCATION, absoluteUrl.toString());
        }
    }

    protected abstract ChannelFuture sendEmptyResponse(boolean addContentLengthHeader);

    HttpResponseStatus httpStatus() {
        return HttpResponseStatus.valueOf(status());
    }

    static class EmptyHttpResponse extends DefaultFullHttpResponse {
        EmptyHttpResponse(HttpResponseStatus status) {
            super(HttpVersion.HTTP_1_1, status, false);
        }
    }

}
