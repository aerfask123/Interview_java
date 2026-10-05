package com.interview.cas;

import com.interview.util.JvmUtil;
import com.interview.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import sun.misc.Unsafe;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class TestCompareAndSwap {

    static class OptimisticLockingPlus {
        private static final int THREAD_COUNT = 10;
        private volatile int value;
        private static final Unsafe unsafe = JvmUtil.getUnsafe();

        private static long valueOffset = 0L;

        private static final AtomicLong failure = new AtomicLong(0);

        static {
            try {
                valueOffset = unsafe.objectFieldOffset(OptimisticLockingPlus.class.getDeclaredField("value"));
                log.info("valueOffset: {}", valueOffset);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        public final boolean unSafeCompareAndSet(int expect, int update) {
            return unsafe.compareAndSwapInt(this, valueOffset, expect, update);
        }

        public void selfPlus() {
            int oldValue = value;
            int i = 0;
            do {
                oldValue = value;
                if (i++ > 0) {
                    failure.incrementAndGet();
                }
            } while (!unSafeCompareAndSet(oldValue, oldValue + 1));
        }

        public static void main(String[] args) throws InterruptedException {
            final OptimisticLockingPlus cas = new OptimisticLockingPlus();
            CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
            for (int i = 0; i < THREAD_COUNT; i++) {
                ThreadUtil.getMixedTargetThreadPool().submit(() -> {
                    for (int j = 0; j < 1000; j++) {
                        cas.selfPlus();
                    }
                    latch.countDown();
                });
            }
            latch.await();
            log.info("value: {}, failure: {}", cas.value, failure.get());
        }
    }
}
