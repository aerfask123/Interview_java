package com.interview.annotation;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.stream.Stream;

public class ProcessUnitTest {

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        Class clazz = LaywerRegisterTest.class;
        LaywerRegisterTest laywerRegisterTest = (LaywerRegisterTest) clazz.newInstance();
//        for (Method method : clazz.getDeclaredMethods()) {
//            if (Modifier.isPublic(method.getModifiers())) {
//                if (method.isAnnotationPresent(UnitTest.class)) {
//                    try {
//                        method.invoke(laywerRegisterTest);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }

        Stream<Method> stream = Stream.of(clazz.getDeclaredMethods());
        stream.filter(m -> m.isAnnotationPresent(UnitTest.class))
                .sorted(Comparator.comparing(m -> m.getAnnotation(UnitTest.class).order()))
                .forEach(m -> {
                    try {
                        m.invoke(laywerRegisterTest);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}
