package com.interview.completablefuture;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class CompletableFutureDemo6 {

    public static void main(String[] args) {
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            int firstStep = 10 * 10;
            log.info("FirstStep outcome is " + firstStep);
            return firstStep;
        });

        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            int secondStep = 20 + 20;
            log.info("SecondStep outcome is " + secondStep);
            return secondStep;
        });

        CompletableFuture<Integer> future3 = future1.thenCombineAsync(future2,
                (firstStep, secondStep) -> {
                    return firstStep * secondStep;
                });

        log.info("{}", future3.join());
    }
}
