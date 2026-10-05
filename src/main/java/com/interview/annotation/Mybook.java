package com.interview.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) //注解作用在方法上
@Retention(RetentionPolicy.RUNTIME)
public @interface Mybook {

    String name();

    int age() default 18;

    String[] addresses();
}
