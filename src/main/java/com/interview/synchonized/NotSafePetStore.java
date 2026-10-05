package com.interview.synchonized;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotSafePetStore {

    private static NotSafeDataBuffer<IGoods> notSafeDataBuffer = new NotSafeDataBuffer<>();

    static Callable<IGoods> produceAction = () ->
    {
        IGoods goods = Goods.produceOne();
        try {
            notSafeDataBuffer.put(goods);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return goods;
    };

    static Callable<IGoods> consumeAction = () ->
    {
        IGoods goods = null;
        try {
            goods = notSafeDataBuffer.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return goods;
    };

    public static void main(String[] args) {
        System.setErr(System.out);

        final int THREAD_TOTAL = 20;

        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_TOTAL);

        for (int i = 0; i < 5; i++) {
            threadPool.submit(new Producer(produceAction, 500));
            threadPool.submit(new Consumer(consumeAction, 1500));
        }
    }
}
