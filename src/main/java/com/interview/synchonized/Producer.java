package com.interview.synchonized;

import com.interview.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Producer implements Runnable {

    public static final int PRODUCER_GAP = 200;

    static final AtomicInteger TURN = new AtomicInteger(0);

    static final AtomicInteger PRODUCER_NO = new AtomicInteger(1);

    String name = null;

    Callable action = null;

    int gap = PRODUCER_GAP;

    public Producer(Callable action, int gap) {
        this.action = action;
        this.gap = gap;
        this.name = "Producer-" + PRODUCER_NO.getAndIncrement();
    }

    @Override
    public void run() {
        try {
            TURN.incrementAndGet();
            Object out = action.call();
            if (null != null) {
                log.info("第{}，轮生产，{} produce {}", TURN.get(), name, out);
            }
            ThreadUtil.sleepMillis(gap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
