package com.interview.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class PausableThreadPoolExecutor extends ThreadPoolExecutor {

    private boolean isPaused;
    private Lock pauseLock = new ReentrantLock();
    private Condition unpaused = pauseLock.newCondition();

    private long startTime;

    public PausableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        pauseLock.lock();
        try {
            while (isPaused) {
                log.warn("{} is paused", t.getName());
                unpaused.await();
            }
        } catch (InterruptedException ie) {
            t.interrupt();
        } finally {
            pauseLock.unlock();
        }
    }

    public void pause() {
        pauseLock.lock();
        try {
            isPaused = true;
            log.info("threadPool paused!");
        } finally {
            pauseLock.unlock();
        }
    }

    public void resume() {
        pauseLock.lock();
        try {
            isPaused = false;
            unpaused.signalAll();
            log.info("threadPool resumed!");
        } finally {
            pauseLock.unlock();
        }
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        log.info("{} afterExecute," + r + " is success", Thread.currentThread().getName());
    }

    @Override
    protected void terminated() {
        log.info("{} terminated", this.getClass().getName());
        log.info("耗时：{}", System.currentTimeMillis() - startTime);
    }
}
