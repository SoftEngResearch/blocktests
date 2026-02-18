package org.netpreserve.jwarc;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.net.URI;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.netpreserve.jwarc.WarcFilterCompiler.*;

public class WarcFilterCompilerBlockTest {

    @Test
    public void testLine142() throws Exception {
        WarcRecord record = new WarcRecord(null, null, null);
        try {
            try {
                if (!(record instanceof WarcResponse)) {
                    assertEquals(Optional.empty(), (Optional.empty()));
                    return;
                }
                if (!record.contentType().equals(MediaType.HTTP_RESPONSE)) {
                    assertEquals(Optional.empty(), (Optional.empty()));
                    return;
                }
                {
                    assertEquals(Optional.empty(), (Optional.of(Integer.toString(((WarcResponse) record).http().status()))));
                    return;
                }
            } catch (Exception e) {
                {
                    assertEquals(Optional.empty(), (Optional.empty()));
                    return;
                }
            }
        } finally {
        }
    }

    @Test
    public void testLine144() throws Exception {
        WarcRecord record = new WarcResponse.Builder(URI.create("http://example.org/")).setHeader("five", "5").build();
        try {
            try {
                if (!(record instanceof WarcResponse)) {
                    assertEquals(Optional.empty(), (Optional.empty()));
                    return;
                }
                if (!record.contentType().equals(MediaType.HTTP_RESPONSE)) {
                    assertEquals(Optional.empty(), (Optional.empty()));
                    return;
                }
                {
                    assertEquals(Optional.empty(), (Optional.of(Integer.toString(((WarcResponse) record).http().status()))));
                    return;
                }
            } catch (Exception e) {
                {
                    assertEquals(Optional.empty(), (Optional.empty()));
                    return;
                }
            }
        } finally {
        }
    }

    @Test
    public void testLine1() throws Exception {
        WarcResponse record = new WarcResponse.Builder(URI.create("http://example.org/")).setHeader("five", "5").body(new HttpResponse.Builder(200, "OK").setHeader("Transfer-Encoding", "chunked").build()).build();
        try {
            try {
                if (!(record instanceof WarcResponse)) {
                    assertEquals(Optional.of("200"), (Optional.empty()));
                    return;
                }
                if (!record.contentType().equals(MediaType.HTTP_RESPONSE)) {
                    assertEquals(Optional.of("200"), (Optional.empty()));
                    return;
                }
                {
                    assertEquals(Optional.of("200"), (Optional.of(Integer.toString(((WarcResponse) record).http().status()))));
                    return;
                }
            } catch (Exception e) {
                {
                    assertEquals(Optional.of("200"), (Optional.empty()));
                    return;
                }
            }
        } finally {
        }
    }
}
