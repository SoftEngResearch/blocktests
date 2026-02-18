package com.mark.concurrent24;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.mark.concurrent24.TicketSeller4.*;

public class TicketSeller4BlockTest {

    @Test
    public void testLine32() throws Exception {
        ConcurrentLinkedQueue<java.lang.String> TicketSeller4__tickets = new ConcurrentLinkedQueue<>(java.util.Arrays.asList("1", "2", "3"));
        while (true) {
            String s = TicketSeller4__tickets.poll();
            // if ... else ...
            if (s == null)
                break;
            else
                System.out.println("--" + s);
        }
        assertTrue(TicketSeller4__tickets.isEmpty());
    }
}
