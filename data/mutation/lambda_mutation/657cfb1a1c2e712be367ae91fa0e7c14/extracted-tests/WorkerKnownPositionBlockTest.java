package com.pawelgorny.lostword;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.BTest.lambdatest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import org.bitcoinj.crypto.MnemonicException;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.macs.HMac;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.pawelgorny.lostword.WorkerKnownPosition.*;

public class WorkerKnownPositionBlockTest {

    @Test
    public void testLine1() throws Exception {
        CountDownLatch latch = null;
        List<String> SEED = new LinkedList<>(Arrays.asList("foo", "bar"));
        boolean REPORTER = true;
        int T_NUMBER = 0;
        List<HMac> SHA_512_DIGESTS = new ArrayList<>(Arrays.asList(new HMac(new SHA512Digest())));
        List<MessageDigest> SHA_256_DIGESTS = new ArrayList<>(Arrays.asList(MessageDigest.getInstance("SHA-256")));
        int WORKING_POSITION = 0;
        List<String> WORDS_TO_WORK = new ArrayList<>(Arrays.asList("a", "b"));
        long start = 0;
        if (REPORTER) {
            start = System.currentTimeMillis();
        }
        HMac LOCAL_SHA_512_DIGEST = SHA_512_DIGESTS.get(T_NUMBER);
        MessageDigest LOCAL_SHA_256_DIGEST = SHA_256_DIGESTS.get(T_NUMBER);
        try {
            int WORKING_POSITION_PLUS = WORKING_POSITION + 1;
            for (int bipPosition = 0; RESULT == null && bipPosition < WORDS_TO_WORK.size(); bipPosition++) {
                SEED.set(WORKING_POSITION, WORDS_TO_WORK.get(bipPosition));
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
        assertEquals("b", SEED.get(0));
        assertEquals("bar", SEED.get(1));
    }
}
