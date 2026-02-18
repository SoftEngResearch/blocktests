package org.javacore.concurrent;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.javacore.concurrent.CountDownLatchT.*;

public class CountDownLatchTBlockTest {

    @Test
    public void testLine32() throws Exception {
        CountDownLatch CountDownLatchT__count = new CountDownLatch(5);
        try {
            int time = new Random().nextInt(5);
            TimeUnit.SECONDS.sleep(time);
            System.out.printf("Thread %s ## :%d\n", Thread.currentThread().getId(), time);
            // ,
            CountDownLatchT__count.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(4, CountDownLatchT__count.getCount());
    }

    @Test
    public void testLine33() throws Exception {
        CountDownLatch CountDownLatchT__count = new CountDownLatch(10);
        try {
            int time = new Random().nextInt(5);
            TimeUnit.SECONDS.sleep(time);
            System.out.printf("Thread %s ## :%d\n", Thread.currentThread().getId(), time);
            // ,
            CountDownLatchT__count.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(9, CountDownLatchT__count.getCount());
    }
}
