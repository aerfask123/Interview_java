package com.interview.completablefuture;

import com.interview.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
public class CompletableFutureDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            ThreadUtil.sleepSeconds(1);
            log.info("runAsync");
        });

        CompletableFuture<Long> future2 = CompletableFuture.supplyAsync(() -> {
            long start = System.currentTimeMillis();
            ThreadUtil.sleepSeconds(1);
            log.info("supplyAsync");
            return System.currentTimeMillis() - start;
        });

        future2.thenApplyAsync((t) -> {
            long ret = t << 1;
            log.info("thenApply,ret:{}", ret);
            return ret;
        }).thenApplyAsync((t) -> {
            long ret = t << 1;
            log.info("thenApply,ret:{}", ret);
            return ret;
        }).thenApplyAsync((t) -> {
            long ret = t << 1;
            log.info("thenApply,ret:{}", ret);
            return ret;
        }).thenAccept(
                (t) -> {
                    log.info("thenAccept,ret:{}", t);
                }
        );

        System.out.println("main wait");
        Long time = future2.get(2, TimeUnit.SECONDS);
        log.info("异步执行耗时:{}", time);
    }
}
