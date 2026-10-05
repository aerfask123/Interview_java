package com.interview.threadlocal;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class InheritableTlTest {

    private static InheritableThreadLocal<String> tl = new InheritableThreadLocal<>();

    public static void main(String[] args) {
        Objects.requireNonNull(tl, "threadLocal不能是空");
        tl.set("hello");
        log.info("value:{}", tl.get());

        Thread thread = new Thread(() -> {
            log.info("value:{}", tl.get());
        });
        thread.start();
    }
}
