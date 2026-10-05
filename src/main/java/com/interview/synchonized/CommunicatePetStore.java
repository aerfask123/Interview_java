package com.interview.synchonized;

import com.interview.util.JvmUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class CommunicatePetStore {

    public static final int MAX_AMOUNT = 10;

    static class DataBuffer<T> {
        private List<T> dataList = new LinkedList<>();

        private Integer amount = 0;

        private final Object LOCK_OBJECT = new Object();
        private final Object NOT_FULL = new Object();
        private final Object NOT_EMPTY = new Object();

        public void add(T element) throws InterruptedException {
            while (amount >= MAX_AMOUNT) {
                synchronized (NOT_FULL) {
                    log.info("数据已满，请稍候");
                    NOT_FULL.wait();
                }
            }
            synchronized (LOCK_OBJECT) {
                dataList.add(element);
                amount++;
            }
            synchronized (NOT_EMPTY) {
                NOT_EMPTY.notify();
            }
        }

        public T fetch() throws InterruptedException {
            while (amount <= 0) {
                synchronized (NOT_EMPTY) {
                    log.info("数据已空，请稍候");
                    NOT_EMPTY.wait();
                }
            }
            T element = null;
            synchronized (LOCK_OBJECT) {
                element = dataList.remove(0);
                amount--;
            }
            synchronized (NOT_FULL) {
                NOT_FULL.notify();
            }
            return element;
        }
    }

    public static void main(String[] args) {
        log.info("d当前进程的ID是" + JvmUtil.getProcessID());
        System.setErr(System.out);

        DataBuffer<IGoods> dataBuffer = new DataBuffer<>();

        Callable<IGoods> produceAction = () -> {
            IGoods goods = Goods.produceOne();

            dataBuffer.add(goods);
            return goods;
        };

        Callable<IGoods> consumeAction = () -> {
            IGoods goods = dataBuffer.fetch();
            log.info("消费了：" + goods);
            return goods;
        };

        final int THREAD_TOTAL = 20;

        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_TOTAL);

        final int CONSUMER_TOTAL = 11;
        final int PRODUCER_TOTAL = 1;

        for (int i = 0; i < PRODUCER_TOTAL; i++) {
            threadPool.submit(new Producer(produceAction, 50));
        }
        for (int i = 0; i < CONSUMER_TOTAL; i++) {
            threadPool.submit(new Consumer(consumeAction, 10));
        }
    }
}
