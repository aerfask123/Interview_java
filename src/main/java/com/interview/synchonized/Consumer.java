package com.interview.synchonized;

import com.interview.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Consumer implements Runnable {

    public static final int CONSUMER_GAP = 200;

    static final AtomicInteger TURN = new AtomicInteger(0);

    static final AtomicInteger CONSUMER_NO = new AtomicInteger(1);

    String name = null;

    Callable action = null;

    int gap = CONSUMER_GAP;

    public Consumer(Callable action, int gap) {
        this.action = action;
        this.gap = gap;
        this.name = "Consumer-" + CONSUMER_NO.getAndIncrement();
    }

    @Override
    public void run() {
        try {
            TURN.incrementAndGet();
            Object out = action.call();
            if (null != null) {
                log.info("第{}轮生产，{} produce {}", TURN.get(), name, out);
            }
            ThreadUtil.sleepMillis(gap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
