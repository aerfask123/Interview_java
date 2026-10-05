package com.interview;

import com.interview.synchonized.ObjectLock;
import com.interview.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.openjdk.jol.vm.VM;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class InnerLockTest {

    final int MAX_TURN = 1000;

    @Test
    public void showNoLockObject() {
        log.info(VM.current().details());

        ObjectLock objectLock = new ObjectLock();
        objectLock.printSelf();
    }

    @Test
    public void showBiasedLock() throws InterruptedException {
        log.info(VM.current().details());

        ThreadUtil.sleepSeconds(5);

        ObjectLock lock = new ObjectLock();
        // lock.hexHash();

        log.info("抢锁前lock的状态：");
        lock.printObjectStruct();

        ThreadUtil.sleepSeconds(5);

        CountDownLatch latch = new CountDownLatch(1);

        Runnable runnable = () -> {
            for (int i = 0; i < MAX_TURN; i++) {
                synchronized (lock) {
                    lock.increase();
                    if (i % (MAX_TURN / 4) == 0) {
                        log.info("第{}轮抢锁成功，lock的状态：", i);
                        lock.printObjectStruct();
                    }
                }
                ThreadUtil.sleepMillis(10);
            }
            latch.countDown();
        };

        new Thread(runnable, "biased-demo-thread").start();
        latch.await();
        ThreadUtil.sleepSeconds(5);
        log.info("释放锁后，lock的状态:");
        lock.printObjectStruct();
    }

    @Test
    public void showLightweightLock() throws InterruptedException {
        log.info(VM.current().details());

        ThreadUtil.sleepSeconds(5);

        ObjectLock lock = new ObjectLock();

        log.info("抢锁前lock的状态：");
        lock.printObjectStruct();

        ThreadUtil.sleepSeconds(5);

        CountDownLatch latch = new CountDownLatch(2);

        Runnable runnable = () -> {
            for (int i = 0; i < MAX_TURN; i++) {
                synchronized (lock) {
                    lock.increase();
                    if (i == 1 || i == 500) {
                        log.info("第一个线程抢到锁了，lock的状态");
                        lock.printObjectStruct();
                    }
                }
                ThreadUtil.sleepMillis(10);
            }
            latch.countDown();

            for (int j = 0; ; j++) {
                ThreadUtil.sleepSeconds(1);
            }
        };
        new Thread(runnable).start();

        ThreadUtil.sleepSeconds(1);

        Runnable lightweightRunnable = () -> {
            for (int i = 0; i < MAX_TURN; i++) {
                synchronized (lock) {
                    lock.increase();
                    if (i == MAX_TURN / 2) {
                        log.info("第二个线程占有锁,lock的状态：");
                        lock.printObjectStruct();
                    }
                    ThreadUtil.sleepMillis(1);
                }
            }
            latch.countDown();
        };

        new Thread(lightweightRunnable).start();

        latch.await();
        ThreadUtil.sleepSeconds(2);

        log.info("释放锁后,lock的状态：");
        lock.printObjectStruct();
    }

    @Test
    public void showHeavyweightLock() throws InterruptedException {
        log.info(VM.current().details());

        ThreadUtil.sleepSeconds(5);

        ObjectLock lock = new ObjectLock();
        log.info("初始化锁的状态：");
        lock.printObjectStruct();

        ThreadUtil.sleepSeconds(5);
        CountDownLatch latch = new CountDownLatch(3);

        Runnable runnable = () -> {
            for (int i = 0; i < MAX_TURN; i++) {
                synchronized (lock) {
                    lock.increase();
                    if (i == 0) {
                        log.info("第一个线程占有锁，lock的状态：");
                        lock.printObjectStruct();
                    }
                }
            }
            latch.countDown();

            for (int j = 0; ; j++) {
                ThreadUtil.sleepSeconds(1);
            }
        };
        new Thread(runnable).start();

        ThreadUtil.sleepSeconds(1);

        Runnable lightWeightRunnable = () -> {
            for (int i = 0; i < MAX_TURN; i++) {
                synchronized (lock) {
                    lock.increase();
                    if (i == 0) {
                        log.info("占有锁，lock的状态");
                        lock.printObjectStruct();
                    }
                    ThreadUtil.sleepMillis(1);
                }
            }
            latch.countDown();
        };
        new Thread(lightWeightRunnable, "抢锁线程1").start();
        ThreadUtil.sleepMillis(100);
        new Thread(lightWeightRunnable, "抢锁线程2").start();

        latch.await();
        ThreadUtil.sleepSeconds(2);
        log.info("释放锁后，lock的状态：");
        lock.printObjectStruct();

        synchronized (lock) {
            lock.increase();
            log.info("占有锁，lock的状态");
            lock.printObjectStruct();

            ThreadUtil.sleepMillis(1);
        }
    }
}
