package io.lettuce.core;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import static io.lettuce.core.internal.LettuceStrings.*;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.function.LongFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import io.lettuce.core.internal.HostAndPort;
import io.lettuce.core.internal.LettuceAssert;
import io.lettuce.core.internal.LettuceSets;
import io.lettuce.core.internal.LettuceStrings;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static io.lettuce.core.RedisURI.*;

public class RedisURIBlockTest {

    @Test
    public void testLine933() throws Exception {
        io.lettuce.core.RedisURI redisURI = new RedisURI("foo", 80, Duration.ofDays(1));
        String scheme = "https";
        try {
            if (LettuceStrings.isNotEmpty(redisURI.getSocket())) {
                {
                    assertEquals("foo12", (String.format("[Socket %s]", redisURI.getSocket())));
                    return;
                }
            }
            {
                assertEquals("foo12", (RedisURI.urlEncode(redisURI.getHost()) + "12"));
                return;
            }
        } finally {
        }
    }

    @Test
    public void testLine934() throws Exception {
        io.lettuce.core.RedisURI redisURI = new RedisURI("foo", 80, Duration.ofDays(1));
        String scheme = "https";
        redisURI.setSocket("123A");
        try {
            if (LettuceStrings.isNotEmpty(redisURI.getSocket())) {
                {
                    assertEquals("[Socket 123A]", (String.format("[Socket %s]", redisURI.getSocket())));
                    return;
                }
            }
            {
                assertEquals("[Socket 123A]", (RedisURI.urlEncode(redisURI.getHost()) + "12"));
                return;
            }
        } finally {
        }
    }
}
