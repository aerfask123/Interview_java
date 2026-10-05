package com.interview.threadlocal;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Slf4j
public class ThreadLocalTest1 {

    private static ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static <T> CompletableFuture<T> invokeToCompletableFuture(Supplier<T> supplier) {
        String context = contextHolder.get();

        Supplier<T> newSupplier = () -> {
            String origin = contextHolder.get();
            try {
                contextHolder.set(context);
                return supplier.get();
            } finally {
                contextHolder.set(origin);
                log.info("origin:{}", origin);
            }
        };

        return CompletableFuture.supplyAsync(newSupplier).exceptionally(e -> {
            throw new RuntimeException("设置threadlocal错误", e);
        });
    }

    public static void main(String[] args) {
        contextHolder.set("main");
        log.info(contextHolder.get());
        CompletableFuture<String> context = invokeToCompletableFuture(() -> contextHolder.get());
        log.info(context.join());
    }
}
