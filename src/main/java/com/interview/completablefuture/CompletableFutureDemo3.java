package com.interview.completablefuture;

import com.interview.util.ThreadUtil;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureDemo3 {

    public static void main(String[] args) throws Exception {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            System.out.println("runAsync");
            ThreadUtil.sleepSeconds(1);
            throw new RuntimeException("runAsync error");
        });

        future.handle((v, t) -> {
            if (t != null) {
                System.out.println("handle error");
            } else {
                System.out.println("handle success");
            }
            return null;
        });

        System.out.println("main waiting end...");
        ThreadUtil.sleepSeconds(2);
    }
}
