package com.interview.completablefuture;

import com.interview.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
public class CompletableFutureDemo4 {

    public static void main(String[] args) {

        ThreadPoolExecutor pool = ThreadUtil.getIOIntenseTargetThreadPool();

        CompletableFuture<Long> future = CompletableFuture.supplyAsync(() -> {
            log.info("run start...");
            long start = System.currentTimeMillis();
            ThreadUtil.sleepSeconds(1);
            log.info("run end...");
            return System.currentTimeMillis() - start;
        }, pool);

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            return "hello";
        }, pool);

        future.thenCombine(future2, (t, u) -> {
            String s = u + t;
            log.info("{}", s);
            return s;
        });

        CompletableFuture<String> future3 = future.thenCompose(t -> {
            return CompletableFuture.supplyAsync(() -> {
                return t + " world";
            });
        });

        long time = future.join();
        log.info("异步执行耗时:{}", time);
        log.info("异步执行结果future3:{}", future3.join());
    }
}
