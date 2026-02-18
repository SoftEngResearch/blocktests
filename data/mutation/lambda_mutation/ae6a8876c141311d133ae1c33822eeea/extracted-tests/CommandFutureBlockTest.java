package com.github.theholywaffle.teamspeak3.api;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.api.exception.TS3Exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.github.theholywaffle.teamspeak3.api.CommandFuture.*;

public class CommandFutureBlockTest {

    @Test
    public void test1() throws Exception {
        String[] results = new String[] { "a", "b" };
        int index = 0;
        AtomicInteger successCounter = new AtomicInteger(1);
        CommandFuture<List<String>> combined = new CommandFuture<>();
        String result = "bar";
        results[index] = result;
        if (successCounter.decrementAndGet() == 0) {
            combined.set(Arrays.asList(results));
        }
        assertEquals("[bar, b]", combined.get().toString());
    }
}
