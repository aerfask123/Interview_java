package com.interview.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

public class StreamTest2 {

    public static void main(String[] args) throws IOException {
        Path path = Path.of("F:\\movie\\lecture-langchain-20250525\\documents\\nocode.txt");
        Stream<String> lines = Files.lines(path, java.nio.charset.StandardCharsets.UTF_8);
//        Optional<String> any = lines.parallel().filter(line -> line.contains("编辑")).findFirst();
//        any.ifPresent(System.out::println);
        Optional<String> any = lines.reduce((a, b) -> a + "\n" + b);
        any.ifPresent(System.out::println);
    }
}
