package com.interview.concurrent;

import com.interview.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class TwoLockDemo {

    public static void useTwolockInterruptiblyLock(Lock lock1, Lock lock2) {
        String lock1Name = lock1.toString().replace("java.util.concurrent.locks.", "");
        String lock2Name = lock2.toString().replace("java.util.concurrent.locks.", "");
        log.info("开始抢第一把锁：{}", lock1Name);
        try {
            lock1.lockInterruptibly();
            log.info("抢到第一把锁：{}", lock1Name);
        } catch (InterruptedException e) {
            log.error("抢锁失败：{}", lock1Name);
            return;
        }

        try {
            log.info("开始抢第二把锁：{}", lock2Name);
            try {
                lock2.lockInterruptibly();
                log.info("抢到第二把锁：{}", lock2Name);
            } catch (InterruptedException e) {
                log.error("抢锁失败：{}", lock2Name);
                return;
            }

            try {
                log.info("两把锁都抢到，开始执行业务逻辑");
                ThreadUtil.sleepSeconds(1);
            } finally {
                log.info("释放锁：{}", lock2Name);
                lock2.unlock();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.info("释放锁：{}", lock1Name);
            lock1.unlock();
        }
    }

    public static ThreadMXBean mbean = ManagementFactory.getThreadMXBean();

    public static void main(String[] args) {

        Lock lock1 = new ReentrantLock();
        Lock lock2 = new ReentrantLock();

        Runnable r1 = () -> TwoLockDemo.useTwolockInterruptiblyLock(lock1, lock2);

        Runnable r2 = () -> TwoLockDemo.useTwolockInterruptiblyLock(lock2, lock1);

        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        t1.start();
        t2.start();

        ThreadUtil.sleepSeconds(2);

        log.info("等待两秒");

        long[] deadlockedThreads = mbean.findDeadlockedThreads();
        if (deadlockedThreads.length > 0) {
            log.info("死锁线程：{}", deadlockedThreads);
        }

        for (long pid : deadlockedThreads) {
            ThreadInfo threadInfo = mbean.getThreadInfo(pid, Integer.MAX_VALUE);
            log.warn("死锁线程信息：{}", threadInfo);
        }
        log.info("中断一个死锁线程:{}", t1);
        t1.interrupt();
    }
}
