package com.interview.annotation;

import java.lang.reflect.Method;
import java.util.Arrays;

public class AnnotationTest {

    public static void main(String[] args) {
        AnnotationTest test = new AnnotationTest();
        test.parseClass();
        test.parseMethod();
    }

    public void parseClass() {
        Class clazz = Demo.class;

        if (clazz.isAnnotationPresent(MyTest.class)) {
            MyTest test = (MyTest) clazz.getDeclaredAnnotation(MyTest.class);
            System.out.println(test.value());
            System.out.println(test.price());
            Arrays.stream(test.addresses()).forEach(System.out::println);
        }
    }

    public void parseMethod() {
        Class clazz = Demo.class;
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(MyTest.class)) {
                MyTest test = method.getDeclaredAnnotation(MyTest.class);
                System.out.println(test.value());
                System.out.println(test.price());
                Arrays.stream(test.addresses()).forEach(System.out::println);
            }
        }
    }
}
