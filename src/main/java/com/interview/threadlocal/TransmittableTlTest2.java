package com.interview.threadlocal;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlCallable;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class TransmittableTlTest2 {

    static TransmittableThreadLocal<String> context = new TransmittableThreadLocal<>();

    static TransmittableThreadLocal<String> context2 = new TransmittableThreadLocal<>();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executors = Executors.newFixedThreadPool(1);
        executors.submit(() -> {
        });

        context.set("hello");
        context2.set("hello2");
        log.info("context:{}", context.get());

        Callable callable = () -> {
            log.info("context:{}", context.get());
            log.info("context2:{}", context2.get());
            return context.get();
        };

        TtlCallable ttlCallable = TtlCallable.get(callable);

        Future future = executors.submit(ttlCallable);

        log.info("future:{}", future.get());

        context.set("world");
        context2.set("world2");

        executors.submit(TtlCallable.get(callable));

        executors.shutdown();
    }

}
