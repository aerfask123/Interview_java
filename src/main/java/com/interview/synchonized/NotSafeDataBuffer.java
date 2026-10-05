package com.interview.synchonized;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

@Slf4j
public class NotSafeDataBuffer<T> {

    private static final int MAX_AMOUNT = 10;
    private List<T> dataList = new LinkedList<>();

    private volatile int amount = 0;

    private AtomicIntegerFieldUpdater amountUpdater = AtomicIntegerFieldUpdater.newUpdater(NotSafeDataBuffer.class, "amount");

    public synchronized void put(T data) throws Exception {
        if (amount >= MAX_AMOUNT) {
            log.info("数据已满，请稍候");
            return;
        }

        dataList.add(data);
        log.info("put data:{}", data);
        amountUpdater.incrementAndGet(this);

        if (amount != dataList.size()) {
            throw new Exception(amount + "!=" + dataList.size());
        }
    }

    public synchronized T get() throws Exception {
        if (amount <= 0) {
            log.info("数据已空，请稍候");
            return null;
        }
        T data = dataList.remove(0);
        log.info("get data:{}", data);
        amountUpdater.decrementAndGet(this);
        if (amount != dataList.size()) {
            throw new Exception(amount + "!=" + dataList.size());
        }
        return data;
    }
}
