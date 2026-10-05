package com.interview.completablefuture;

import com.interview.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class CompletableFutureDemo9 {

    public static void main(String[] args) {
        CompletableFuture<Long> future = CompletableFuture.supplyAsync(() -> {
            long firstStep = 10L + 10L;
            log.info("FirstStep outcome is " + firstStep);
            return firstStep;
        });

        future.thenAccept(firstStep -> {
            ThreadUtil.sleepMillis(300);
            log.info("FirstStep thenAccept outcome is " + firstStep);
        });
        future.thenAcceptAsync(firstStep -> {
            ThreadUtil.sleepMillis(100);
            log.info("FirstStep thenAccept Async outcome is " + firstStep);
        });

        log.info("{}", future.join());
    }
}
