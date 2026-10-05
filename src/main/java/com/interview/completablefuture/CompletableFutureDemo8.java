package com.interview.completablefuture;

import com.interview.util.ThreadUtil;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class CompletableFutureDemo8 {

    public static void main(String[] args) {
        List<CompletableFuture<String>> futures = Arrays.asList(
                CompletableFuture.supplyAsync(() -> "任务一"),
                CompletableFuture.supplyAsync(() -> "任务二"),
                CompletableFuture.supplyAsync(() -> "任务三")
        );

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        List<String> results = allOf.thenApplyAsync(v -> {
            List<String> list = futures.stream().map(CompletableFuture::join)
                    .collect(Collectors.toList());
            return list;
        }).join();

        System.out.println("Result is " + results);

        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() ->
        {
            ThreadUtil.sleepSeconds(1);
            return "任务四";
        });

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() ->
        {
            ThreadUtil.sleepSeconds(2);
            return "任务五";
        });

        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() ->
        {
            ThreadUtil.sleepSeconds(3);
            return "任务六";
        });

        CompletableFuture<Object> anyOf = CompletableFuture.anyOf(future1, future2, future3);

        System.out.println(anyOf.join());
    }
}
