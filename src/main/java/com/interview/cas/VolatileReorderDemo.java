package com.interview.cas;

public class VolatileReorderDemo {

    int x = 10;
    int doubleValue = 0;
    volatile boolean flag = false;

    public void update() {
        doubleValue = 100;
        flag = true;
    }

    public void doubleX() {
        if (flag) {
            doubleValue = x * 2;
        }
    }
}
