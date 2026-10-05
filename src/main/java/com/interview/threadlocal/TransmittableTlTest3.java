package com.interview.threadlocal;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class TransmittableTlTest3 {

    static TransmittableThreadLocal<String> context = new TransmittableThreadLocal<>() {

        @Override
        protected void beforeExecute() {
            log.info("beforeExecute:{}", context.get());
        }

        @Override
        protected void afterExecute() {
            log.info("afterExecute:{}", context.get());
        }
    };

    static TransmittableThreadLocal<String> context2 = new TransmittableThreadLocal<>();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executors = Executors.newFixedThreadPool(1);
        executors.submit(() -> {
        });
        executors = TtlExecutors.getTtlExecutorService(executors);

        context.set("hello");
        context2.set("hello2");
        log.info("context:{}", context.get());

        Callable callable = () -> {
            log.info("context:{}", context.get());
            log.info("context2:{}", context2.get());
            return context.get();
        };

        Future future = executors.submit(callable);

        log.info("future:{}", future.get());

        context.set("world");
        context2.set("world2");

        executors.submit(callable);

        executors.shutdown();

    }

}
