package com.interview.annotation;

public class LaywerRegisterTest {
    
    @UnitTest(order = 1)
    public void login() {
        System.out.println("login run!");
    }

    @UnitTest(order = 2)
    public void upload() {
        System.out.println("upload run!");
    }

    public void register() {
        System.out.println("register run!");
    }

    @UnitTest
    public void authenticate() {
        System.out.println("authenticate run!");
    }
}
