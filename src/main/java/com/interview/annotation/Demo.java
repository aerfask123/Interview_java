package com.interview.annotation;

@MyTest(value = "章若楠", price = 120000, addresses = {"深圳", "广州", "上海"})
public class Demo {

    @MyTest(value = "张雨绮", price = 100000, addresses = {"北京", "上海"})
    public void test1() {

    }
}
