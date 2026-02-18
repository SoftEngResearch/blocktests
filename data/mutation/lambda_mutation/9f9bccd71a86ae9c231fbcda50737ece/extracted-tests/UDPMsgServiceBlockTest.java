package net.lvsq.jgossip.net.udp;

import io.vertx.core.AsyncResult;
import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.types.Flow.IfStmt;
import static org.blocktest.utils.Constant.*;
import io.netty.util.internal.StringUtil;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.datagram.DatagramSocketOptions;
import io.vertx.core.json.JsonObject;
import net.lvsq.jgossip.core.GossipManager;
import net.lvsq.jgossip.core.GossipMessageFactory;
import net.lvsq.jgossip.handler.Ack2MessageHandler;
import net.lvsq.jgossip.handler.AckMessageHandler;
import net.lvsq.jgossip.handler.MessageHandler;
import net.lvsq.jgossip.handler.RegularMessageHandler;
import net.lvsq.jgossip.handler.ShutdownMessageHandler;
import net.lvsq.jgossip.handler.SyncMessageHandler;
import net.lvsq.jgossip.model.MessageType;
import net.lvsq.jgossip.net.MsgService;
import org.blocktest.types.Flow;
import org.blocktest.types.Flow.*;
import org.junit.Test;
import static org.junit.Assert.*;
import static net.lvsq.jgossip.net.udp.UDPMsgService.*;

public class UDPMsgServiceBlockTest {

    @Test
    public void testLine107() throws Exception {
        AsyncResult<Void> asyncResult = new AsyncResult<Void>() {

            @Override
            public Void result() {
                return null;
            }

            @Override
            public Throwable cause() {
                return null;
            }

            @Override
            public boolean succeeded() {
                return true;
            }

            @Override
            public boolean failed() {
                return false;
            }
        };
        boolean[] _blocktest_flow_testLine107 = new boolean[1];
        if (asyncResult.succeeded()) {
            _blocktest_flow_testLine107[0] = true;
        } else {
        }
        for (int _blocktest_testLine107_i = 0; _blocktest_testLine107_i < 1; _blocktest_testLine107_i += 1) {
            assertTrue(_blocktest_flow_testLine107[_blocktest_testLine107_i]);
        }
    }

    @Test
    public void testLine128() throws Exception {
        AsyncResult<Void> asyncResult = new AsyncResult<Void>() {

            @Override
            public Void result() {
                return null;
            }

            @Override
            public Throwable cause() {
                return null;
            }

            @Override
            public boolean succeeded() {
                return false;
            }

            @Override
            public boolean failed() {
                return false;
            }
        };
        boolean[] _blocktest_flow_testLine128 = new boolean[1];
        if (asyncResult.succeeded()) {
        } else {
            _blocktest_flow_testLine128[0] = true;
        }
        for (int _blocktest_testLine128_i = 0; _blocktest_testLine128_i < 1; _blocktest_testLine128_i += 1) {
            assertTrue(_blocktest_flow_testLine128[_blocktest_testLine128_i]);
        }
    }
}
