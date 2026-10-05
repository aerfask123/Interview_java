package com.interview.threadlocal;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class ThreadLocalTest2 {
    private static ThreadLocal<Optional<String>> contextHolder = new ThreadLocal<>();

    private static ExecutorService executor = Executors.newFixedThreadPool(5);

    public static void main(String[] args) {
        Optional<String> optional = Optional.of("main");
        contextHolder.set(optional);

        log.info("contextHolder:{}", contextHolder.get().get());

        execute(() -> {
            log.info("contextHolder:{}", contextHolder.get().get());
        });

        executor.shutdown();
    }

    public static final void execute(Runnable task) {
        executor.execute(wrap(task));
    }

    protected static final Runnable wrap(Runnable task) {
        return new DelegatingContextRunnable(task);
    }

    static class DelegatingContextRunnable implements Runnable {

        private final Runnable delegate;

        private final Optional<String> delegateContext;

        public DelegatingContextRunnable(Runnable delegate, Optional<String> delegateContext) {
            assert delegate != null;
            assert delegateContext != null;

            this.delegate = delegate;
            this.delegateContext = delegateContext;
            log.info("init");
        }

        public DelegatingContextRunnable(Runnable delegate) {
            this(delegate, contextHolder.get());
        }

        @Override
        public void run() {
            Optional<String> originalContext = contextHolder.get();

            try {
                contextHolder.set(delegateContext);
                delegate.run();
            } finally {
                log.info("contextHolder:{}", originalContext);
                contextHolder.set(originalContext);
            }
        }
    }

}