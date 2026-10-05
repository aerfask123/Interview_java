package com.interview.completablefuture;

import com.interview.util.ThreadUtil;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureDemo7 {

    public static void main(String[] args) {
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            ThreadUtil.sleepSeconds(2);
            int firstStep = 10 * 10;
            return firstStep;
        });

        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            int secondStep = 20 * 20;
            return secondStep;
        });

        CompletableFuture<Integer> future3 = future1.applyToEither(future2, (eitherOutCome) -> {
            return eitherOutCome;
        });

        Integer result = future3.join();
        System.out.println("Result is " + result);
    }
}
