package com.interview.test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class ConsumerTest {

    public static void main(String[] args) {
        AtomicInteger sum = new AtomicInteger(0);

        Consumer<Integer> add = (s) -> {
            sum.updateAndGet(v -> v + s);
        };

        Consumer<Integer> multiple = (s) -> {
            sum.updateAndGet(v -> v * s);
        };

        Consumer<Integer> dou = (s) -> {
            sum.updateAndGet(v -> v << 1);
        };

        add.andThen(multiple).andThen(dou).accept(5);

        System.out.println(sum.get());
    }

}
