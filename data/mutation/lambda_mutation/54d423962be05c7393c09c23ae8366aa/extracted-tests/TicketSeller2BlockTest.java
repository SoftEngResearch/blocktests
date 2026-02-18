package com.mark.concurrent24;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.util.Arrays;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.mark.concurrent24.TicketSeller2.*;

public class TicketSeller2BlockTest {

    @Test
    public void testLine32() throws Exception {
        Vector<java.lang.String> TicketSeller2__tickets = new Vector<>(Arrays.asList("1", "2", "3"));
        while (TicketSeller2__tickets.size() > 0) {
            // 
            System.out.println("--" + TicketSeller2__tickets.remove(0));
        }
        assertTrue(TicketSeller2__tickets.isEmpty());
    }
}
