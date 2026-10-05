package com.interview.annotation;

import java.util.Arrays;

@Mybook(name = "java", age = 18, addresses = {"北京", "上海"})
@Pen("pen")
public class AnnotationDemo1 {

    public static void main(String[] args) {
        AnnotationDemo1 annotationDemo1 = new AnnotationDemo1();
        Mybook mybook = annotationDemo1.getClass().getAnnotation(Mybook.class);
        System.out.println(mybook.name());
        System.out.println(mybook.age());
        Arrays.stream(mybook.addresses()).forEach(System.out::println);
    }
}
