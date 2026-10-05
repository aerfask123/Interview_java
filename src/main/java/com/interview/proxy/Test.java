package com.interview.proxy;

import com.interview.util.ProxyUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Test {

    public static void main(String[] args) throws InterruptedException {
        Star star = new Star("章若楠");
        StarInterface proxy = ProxyUtil.getProxyInstance(star);
        proxy.sing("《我好想你》");
        System.out.println(proxy.dance());

        int coreSize = Runtime.getRuntime().availableProcessors();
        log.info("cpu核数：{}", coreSize);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("程序即将退出");
            try {
                Thread.sleep(1000000000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }));

        Thread.sleep(1000);
    }
}
