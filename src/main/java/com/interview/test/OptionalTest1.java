package com.interview.test;

import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

public class OptionalTest1 {

    public static void main(String[] args) {
        Optional<String> optional = Optional.empty();
        String result = optional.orElse("N/A");
        System.out.println("result:" + result);
        result = optional.orElseGet(() -> Locale.getDefault().getDisplayName());
        System.out.println("result:" + result);
        try {
            result = optional.orElseThrow(IllegalStateException::new);
            System.out.println("result:" + result);
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }

        Optional<Double> re = inverse(0.0d).flatMap(OptionalTest1::squareRoot);
        Double dou = re.orElseGet(() -> 0.0d);
        System.out.println("dou:" + dou);

//        dou = Optional.of(-2.0).flatMap(OptionalTest1::inverse).flatMap(OptionalTest1::squareRoot).get();
//        System.out.println("dou2:" + dou);

        Optional<String> words = Optional.of("Jack");
        Set<String> results = new HashSet<>();
        words.ifPresent(results::add);
        Optional<Boolean> added = words.map(results::add);
        System.out.println(added);
        
        System.out.println(inverse(4.0).flatMap(OptionalTest1::squareRoot));
        System.out.println(inverse(-1.0).flatMap(OptionalTest1::squareRoot));
        System.out.println(inverse(0.0).flatMap(OptionalTest1::squareRoot));
    }

    public static Optional<Double> inverse(Double x) {
        return x == 0 ? Optional.empty() : Optional.of(1 / x);
    }

    public static Optional<Double> squareRoot(Double x) {
        return x < 0 ? Optional.empty() : Optional.of(Math.sqrt(x));
    }
}
