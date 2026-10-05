package com.interview.test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StatisticsTest {

    public static void main(String[] args) {
        String[] ints = {"1", "256"};
        IntSummaryStatistics summary = Arrays.stream(ints).collect(Collectors.summarizingInt(String::length));
        System.out.println(
                summary.getCount() + " " +
                        summary.getMax() + " " +
                        summary.getMin() + " " +
                        summary.getSum() + " " +
                        summary.getAverage()
        );

        Stream<Locale> locales = Stream.of(Locale.getAvailableLocales());
//        Map<String, String> languageNames =
//                locales.collect(Collectors.toMap(Locale::getDisplayLanguage,
//                        l -> l.getDisplayLanguage(l), (s1, s2) -> s1));
//        languageNames.forEach((k, v) -> System.out.println(k + " " + v));

        Map<String, Set<String>> countryLanguageSets = locales.collect(Collectors.toMap(Locale::getDisplayCountry,
                l -> Collections.singleton(l.getDisplayLanguage()), (s1, s2) ->
                {
                    Set<String> union = new HashSet<>(s1);
                    union.addAll(s2);
                    return union;
                }));
        countryLanguageSets.forEach((k, v) -> System.out.println(k + " " + v));
    }
}
