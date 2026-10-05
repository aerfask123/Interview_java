package com.interview.annotation;

public @interface Pen {

    String value();

    String color() default "black";
}
