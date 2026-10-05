package com.interview.concurrent;

import com.interview.util.ThreadUtil;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantCommunicationTest {

    static Lock lock = new ReentrantLock();

    static private Condition producerCondition = lock.newCondition();

    static private Condition consumerCondition = lock.newCondition();

    static ArrayBlockingQueue arrayQueue = new ArrayBlockingQueue(10);

    static SynchronousQueue syncQueue = new SynchronousQueue();

    static Random rn = new Random();


    static class WaitTarget implements Runnable {

        @Override
        public void run() {
//            lock.lock();
            try {
                System.out.println("WaitTarget is starting");
                while (true) {
                    ThreadUtil.sleepSeconds(1);
                    Integer produce = rn.nextInt();
                    System.out.println("生产者生产了:" + produce);
                    syncQueue.offer(produce);
//                    consumerCondition.signal();
//                    producerCondition.await();
                }

            } finally {
                System.out.println("WaitTarget is ending");
//                lock.unlock();
            }
        }
    }

    static class NotifyTarget implements Runnable {
        @Override
        public void run() {
//            lock.lock();
            try {
                System.out.println("NotifyTarget is starting");
                while (true) {
                    Integer take = (Integer) syncQueue.take();
                    System.out.println("消费到了：" + take);
//                    producerCondition.signal();
//                    consumerCondition.await();

                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                System.out.println("NotifyTarget is ending");
//                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        Thread waitThread = new Thread(new WaitTarget(), "WaitThread");

        waitThread.start();

        Thread notifyThread = new Thread(new NotifyTarget(), "NotifyThread");
        notifyThread.start();

        ThreadUtil.sleepSeconds(1);
    }
}
