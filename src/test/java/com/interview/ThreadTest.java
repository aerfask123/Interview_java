package com.interview;

import org.junit.jupiter.api.Test;

public class ThreadTest {

    volatile boolean stop = false;

    @Test
    public void testInterrupt() {
        Thread thread = new Thread(() -> {
            while (!stop) {
                try {
                    System.out.println("线程正在运行");
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    System.out.println("线程被中断");
                }
            }
        });

        thread.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        stop = true;
        thread.interrupt();
    }
}
