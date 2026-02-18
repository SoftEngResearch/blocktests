package io.lettuce.core;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import static io.lettuce.core.protocol.CommandType.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.push.PushListener;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.output.MultiOutput;
import io.lettuce.core.output.StatusOutput;
import io.lettuce.core.protocol.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static io.lettuce.core.StatefulRedisConnectionImpl.*;

public class StatefulRedisConnectionImplBlockTest {

    @Test
    public void testLine197() throws Exception {
        MultiOutput<String, String> multi = new MultiOutput<String, String>(StringCodec.UTF8);
        Throwable e = null;
        if (1 == 1) {
            multi = null;
        }
        assertEquals(new MultiOutput<>(StringCodec.UTF8).toString(), multi.toString());
    }

    @Test
    public void testLine199() throws Exception {
        MultiOutput<String, String> multi = new MultiOutput<String, String>(StringCodec.UTF8);
        Throwable e = new Throwable("foo");
        if (1 == 1) {
            multi = null;
        }
        assertEquals(null, multi);
    }

    @Test
    public void testLine234() throws Exception {
        ConnectionState state = new ConnectionState();
        java.lang.String status = "OK";
        Command<String, String, String> command = new Command<String, String, String>(CommandType.GET, null, new CommandArgs<>(StringCodec.UTF8).add("SLEEP").add(1));
        if ("OK".equals(status)) {
            Long db = CommandArgsAccessor.getFirstInteger(command.getArgs());
            if (db != null) {
                state.setDb(db.intValue());
            }
        }
        assertEquals(1, state.getDb());
    }

    @Test
    public void testLine235() throws Exception {
        ConnectionState state = new ConnectionState();
        java.lang.String status = "OK";
        Command<String, String, String> command = new Command<String, String, String>(CommandType.GET, null, new CommandArgs<>(StringCodec.UTF8).add("SLEEP").add(10));
        if ("OK".equals(status)) {
            Long db = CommandArgsAccessor.getFirstInteger(command.getArgs());
            if (db != null) {
                state.setDb(db.intValue());
            }
        }
        assertEquals(10, state.getDb());
    }

    @Test
    public void testLine236() throws Exception {
        ConnectionState state = new ConnectionState();
        java.lang.String status = "OK";
        Command<String, String, String> command = new Command<String, String, String>(CommandType.GET, null, new CommandArgs<>(StringCodec.UTF8).add("SLEEP"));
        if ("OK".equals(status)) {
            Long db = CommandArgsAccessor.getFirstInteger(command.getArgs());
            if (db != null) {
                state.setDb(db.intValue());
            }
        }
        assertEquals(0, state.getDb());
    }

    @Test
    public void testLine237() throws Exception {
        ConnectionState state = new ConnectionState();
        java.lang.String status = "NOTOK";
        Command<String, String, String> command = new Command<String, String, String>(CommandType.GET, null, new CommandArgs<>(StringCodec.UTF8).add("SLEEP").add(1));
        if ("OK".equals(status)) {
            Long db = CommandArgsAccessor.getFirstInteger(command.getArgs());
            if (db != null) {
                state.setDb(db.intValue());
            }
        }
        assertEquals(0, state.getDb());
    }
}
