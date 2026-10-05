package com.interview;

import com.interview.cas.User;
import com.interview.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.*;

@Slf4j
public class AtomicTest {

    @Test
    public void testAtomicReference() {
        AtomicReference<User> userRef = new AtomicReference<>();
        User user = new User("1", "张三", 18);
        userRef.set(user);
        log.info("user: {}", userRef.get());
        user.setUid("3");
        user.setAge(19);
        log.info("user: {}", userRef.get());

        User newUser = new User("2", "李四", 19);

        boolean success = userRef.compareAndSet(user, newUser);
        log.info("Cas result is: {}", success);
        log.info("After cas,user: {}", userRef.get());
    }

    @Test
    public void testAtomicIntegerFieldUpdater() {
        AtomicIntegerFieldUpdater<User> updater = AtomicIntegerFieldUpdater.newUpdater(User.class, "age");
        User user = new User("1", "张三", 18);
        log.info("user age: {}", updater.getAndIncrement(user));
        log.info("user age: {}", updater.getAndAdd(user, 20));
        log.info("user age:{}", updater.get(user));
    }

    @Test
    public void testAtomicStampedReference() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);

        AtomicStampedReference<Integer> ref = new AtomicStampedReference<>(1, 0);

        ThreadUtil.getMixedTargetThreadPool().submit(() -> {
            boolean success = false;
            int stamp = ref.getStamp();
            log.info("value:{},stamp:{}", ref.getReference(), stamp);
            ThreadUtil.sleepMillis(500);
            success = ref.compareAndSet(1, 10, stamp, stamp + 1);
            log.info("success:{},value:{},stamp:{}", success, ref.getReference(), stamp);

            stamp++;
            success = ref.compareAndSet(10, 1, stamp, stamp + 1);
            log.info("success:{},value:{},stamp:{}", success, ref.getReference(), stamp);

            latch.countDown();
        });

        ThreadUtil.getMixedTargetThreadPool().submit(() -> {
            boolean success = false;
            int stamp = ref.getStamp();
            log.info("value:{},stamp:{}", ref.getReference(), stamp);
            ThreadUtil.sleepMillis(1000);
            success = ref.compareAndSet(1, 20, stamp, stamp + 1);
            log.info("success:{},value:{},stamp:{}", success, ref.getReference(), stamp);

            stamp++;
            success = ref.compareAndSet(10, 1, stamp, stamp + 1);
            log.info("success:{},value:{},stamp:{}", success, ref.getReference(), stamp);

            latch.countDown();
        });

        latch.await();
    }

    @Test
    public void testAtomicMarkableReference() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);

        AtomicMarkableReference<Integer> atomicRef = new AtomicMarkableReference<>(1, false);

        ThreadUtil.getMixedTargetThreadPool().submit(() -> {
            boolean success = false;
            int value = atomicRef.getReference();
            boolean mark = atomicRef.isMarked();
            log.info("value:{},mark:{}", value, mark);

            ThreadUtil.sleepMillis(500);
            success = atomicRef.compareAndSet(1, 10, mark, !mark);
            log.info("success:{},value:{},mark:{}", success, atomicRef.getReference(), mark);
            latch.countDown();
        });

        ThreadUtil.getMixedTargetThreadPool().submit(() -> {
            boolean success = false;
            int value = atomicRef.getReference();
            boolean mark = atomicRef.isMarked();
            log.info("value:{},mark:{}", value, mark);

            ThreadUtil.sleepSeconds(1);

            log.info("after 1 sec, value:{},mark:{}", value, mark);
            success = atomicRef.compareAndSet(value, 20, mark, !mark);
            log.info("success:{},value:{},mark:{}", success, value, mark);
            latch.countDown();
        });
        latch.await();
    }

    final int TURNS = 1 << 30;

    @Test
    public void testAtomicLong() throws InterruptedException {
        final int TASK_AMOUNT = 10;

        ExecutorService threadPool = ThreadUtil.getCPUIntenseTargetThreadPool();
        AtomicLong atomicLong = new AtomicLong(0);

        CountDownLatch latch = new CountDownLatch(TASK_AMOUNT);
        long start = System.currentTimeMillis();
        for (int i = 0; i < TASK_AMOUNT; i++) {
            int finalI = i;
            threadPool.submit(() -> {
                for (int j = 0; j < TURNS; j++) {
                    atomicLong.incrementAndGet();
                }
                latch.countDown();
            });
        }

        latch.await();

        float time = (System.currentTimeMillis() - start) / 1000f;
        log.info("time: {}, value: {}", time, atomicLong.get());
    }

    @Test
    public void testLongAddr() throws InterruptedException {
        final int TASK_COUNT = 10;

        ExecutorService threadPool = ThreadUtil.getCPUIntenseTargetThreadPool();
        LongAdder longAdder = new LongAdder();

        CountDownLatch latch = new CountDownLatch(TASK_COUNT);
        long start = System.currentTimeMillis();
        for (int i = 0; i < TASK_COUNT; i++) {
            final int finalI = i;
            threadPool.submit(() -> {
                for (int j = 0; j < TURNS; j++) {
                    longAdder.increment();
                }
                log.info("task: {}, value: {}", finalI, longAdder.longValue());
                latch.countDown();
            });
        }

        latch.await();

        float time = (System.currentTimeMillis() - start) / 1000f;

        log.info("time: {}", time);
        log.info("value: {}", longAdder.longValue());
    }
}
