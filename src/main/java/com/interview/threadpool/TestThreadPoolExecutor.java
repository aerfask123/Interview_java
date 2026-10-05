package com.interview.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class TestThreadPoolExecutor {

    public static void main(String[] args) {
        AtomicInteger c = new AtomicInteger(1);
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(50);

//        LinkedBlockingDeque linkedBlockingDeque = new LinkedBlockingDeque();
//        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
//                2, 3, 0L, TimeUnit.MILLISECONDS
//                , queue, r -> new Thread(r, "MyThread-" + c.getAndIncrement()),
//                new ThreadPoolExecutor.DiscardOldestPolicy());
//        showState(queue, threadPool);

        long startTime = System.currentTimeMillis();

        run(queue, c);

        log.info("耗时：{}", System.currentTimeMillis() - startTime);

//        threadPool.submit(new MyTask("task1", 3600000));
//        showState(queue, threadPool);
//        threadPool.submit(new MyTask("task2", 3600000));
//        showState(queue, threadPool);
//        threadPool.submit(new MyTask("task3", 3600000));
//        showState(queue, threadPool);
//        threadPool.submit(new MyTask("task4", 3600000));
//        showState(queue, threadPool);
//        threadPool.submit(new MyTask("task5", 3600000));
//        showState(queue, threadPool);
//        threadPool.submit(new MyTask("task6", 0));
//        showState(queue, threadPool);
    }

    private static void run(ArrayBlockingQueue<Runnable> queue, AtomicInteger c) {
        PausableThreadPoolExecutor threadPool = new PausableThreadPoolExecutor(2, 3,
                1000L, TimeUnit.MILLISECONDS, queue, r -> new Thread(r, "MyThread-" + c.getAndIncrement()));

        for (int i = 0; i < 50; i++) {
            threadPool.submit(new MyTask("task" + i, 5000));
            showState(queue, threadPool);
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadPool.pause();

        threadPool.setMaximumPoolSize(20);
        threadPool.setCorePoolSize(16);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        threadPool.resume();

        threadPool.shutdown();

        try {
            if (!threadPool.awaitTermination(100, TimeUnit.MILLISECONDS)) {
                List<Runnable> runnables = threadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
        }
    }

    private static void showState(ArrayBlockingQueue<Runnable> queue, ThreadPoolExecutor threadPool) {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<Object> tasks = new ArrayList<>();
        for (Runnable runnable : queue) {
            try {
                Field field = FutureTask.class.getDeclaredField("callable");
                field.setAccessible(true);
                Object adapter = field.get(runnable);
                Class<?> clazz = Class.forName("java.util.concurrent.Executors$RunnableAdapter");
                Field task = clazz.getDeclaredField("task");
                task.setAccessible(true);
                Object o = task.get(adapter);
                tasks.add(o);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        log.info("pool size:{}, queue:{}", threadPool.getPoolSize(), tasks);
    }

    static class MyTask implements Runnable {
        private final String name;
        private final long duration;

        public MyTask(String name) {
            this.name = name;
            this.duration = 0;
        }

        public MyTask(String name, long duration) {
            this.name = name;
            this.duration = duration;
        }

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + " is running " + this);
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String toString() {
            return "【MyTask:" + name + "】";
        }
    }
}
