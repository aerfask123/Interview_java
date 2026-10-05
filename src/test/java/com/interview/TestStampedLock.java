package com.interview;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class TestStampedLock {

    private static final int LG_READERS = 7;

    // Values for lock state and stamp operations
    private static final long RUNIT = 1L;
    private static final long WBIT = 1L << LG_READERS;
    private static final long RBITS = WBIT - 1L;
    private static final long RFULL = RBITS - 1L;
    private static final long ABITS = RBITS | WBIT;
    private static final long SBITS = ~RBITS; // note overlap with ABITS

    @Test
    public void test() {
        log.info("SBITS:{}", Long.toBinaryString(SBITS));
        log.info("thread state:{},ordinal:{}", Thread.currentThread().getState(), Thread.currentThread().getState().ordinal());
    }
}
