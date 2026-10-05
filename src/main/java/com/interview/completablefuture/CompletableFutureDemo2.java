package com.interview.completablefuture;

import com.interview.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class CompletableFutureDemo2 {

    public static void main(String[] args) {

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            ThreadUtil.sleepSeconds(1);
            log.info("runAsync");
            throw new RuntimeException("异常");
        });


        future.whenComplete((v, t) -> {
            if (t != null) {
                log.info("whenComplete error:{}", t.getMessage());
            } else {
                log.info("whenComplete");
            }
        });

        future.whenComplete((v, t) -> {
            if (t != null) {
                log.info("whenComplete error2:{}", t.getMessage());
            } else {
                log.info("whenComplete2");
            }
        });

        future.exceptionally(t -> {
            log.info("执行失败" + t.getMessage());
            return null;
        });

        System.out.println("main waiting end...");

        ThreadUtil.sleepSeconds(30000);
    }
}
