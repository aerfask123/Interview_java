package com.interview.test;

import java.util.stream.Stream;

public class StreamTest1 {

    public static void main(String[] args) {
        Stream<Long> randoms = Stream.generate(() -> Math.round(Math.random() * Long.MAX_VALUE));
        randoms.limit(10000).forEach(System.out::println);

        Stream<Integer> integers = Stream.iterate(0, n -> n + 1);
        integers.limit(10000).forEach(System.out::println);
    }
}
