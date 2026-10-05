package com.interview.threadlocal;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class TransmittableTlTest {

    public static void main(String[] args) {
        TransmittableThreadLocal<String> context = new TransmittableThreadLocal<>();
        context.set("hello");
        log.info("context:{}", context.get());

        CompletableFuture future = CompletableFuture.supplyAsync(() -> {
            log.info("context:{}", context.get());
            return context.get();
        });

        log.info("future:{}", future.join());
    }

}
