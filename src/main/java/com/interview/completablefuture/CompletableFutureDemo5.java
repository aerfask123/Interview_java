package com.interview.completablefuture;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class CompletableFutureDemo5 {

    public static void main(String[] args) {
        CompletableFuture<Long> future = CompletableFuture.supplyAsync(() -> {
                    long firstStep = 10L + 10L;
                    log.info("FirstStep outcome is " + firstStep);
                    return firstStep;
                }).thenApplyAsync(firstStep -> firstStep * 2)
                .thenApplyAsync(firstStep -> firstStep + 3);

        log.info("{}", future.join());
    }
}
