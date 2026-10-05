package com.interview.concurrent;

import com.interview.util.ThreadUtil;

import java.util.concurrent.locks.LockSupport;

public class LockSupportDemo {

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            ThreadUtil.sleepSeconds(1);

            System.out.println("即将阻塞第一次");

            LockSupport.park();

            System.out.println("阻塞结束第一次");
            ThreadUtil.sleepSeconds(1);
            System.out.println("即将阻塞第二次");
            LockSupport.park();
            System.out.println("阻塞结束第二次");
        });

        t1.start();

        LockSupport.unpark(t1);

        System.out.println("发放许可第一次");
        ThreadUtil.sleepSeconds(10);
        LockSupport.unpark(t1);
        System.out.println("发放许可第二次");
        System.out.println("主线程结束");
    }
}
