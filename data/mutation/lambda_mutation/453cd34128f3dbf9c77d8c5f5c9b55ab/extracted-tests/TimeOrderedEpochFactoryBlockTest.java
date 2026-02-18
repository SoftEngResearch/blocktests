package com.github.f4b6a3.uuid.factory.rfc4122;

import org.blocktest.BTest;
import static org.blocktest.BTest.blocktest;
import static org.blocktest.BTest.lambdatest;
import static org.blocktest.types.EndAt.*;
import static org.blocktest.utils.Constant.*;
import java.time.Clock;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.IntFunction;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import com.github.f4b6a3.uuid.enums.UuidVersion;
import com.github.f4b6a3.uuid.factory.AbstCombFactory;
import com.github.f4b6a3.uuid.factory.nonstandard.PrefixCombFactory;
import com.github.f4b6a3.uuid.util.internal.ByteUtil;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.github.f4b6a3.uuid.factory.rfc4122.TimeOrderedEpochFactory.*;

public class TimeOrderedEpochFactoryBlockTest {

    @Test
    public void testLine458() throws Exception {
        long positive = 0x7fffffffffffffffL;
        PlusNFunction.BlockByteRandom random = new PlusNFunction.BlockByteRandom();
        int size = 10;
        Long incrementMax = TimeOrderedEpochFactory.INCREMENT_MAX_DEFAULT - 1;
        try {
            byte[] bytes = random.nextBytes(size);
            long entropy = ByteUtil.toNumber(bytes, 0, size);
            {
                assertEquals(3985711175L, (((entropy & positive) % incrementMax) + 1), 0.01);
                return;
            }
        } finally {
        }
    }

    @Test
    public void testLine438() throws Exception {
        PlusNFunction.BlockByteRandom random = new PlusNFunction.BlockByteRandom();
        Long incrementMax = TimeOrderedEpochFactory.INCREMENT_MAX_DEFAULT;
        try {
            byte[] bytes = random.nextBytes(4);
            {
                assertEquals(1.943345852E9, (ByteUtil.toNumber(bytes, 4, 0) + 1), 0.01);
                return;
            }
        } finally {
        }
    }
}
