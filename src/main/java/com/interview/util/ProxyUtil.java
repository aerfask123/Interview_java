package com.interview.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;

@Slf4j
public class ProxyUtil {

    public static <T> T getProxyInstance(T obj) {
        return (T) Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), (proxy, method, args) -> {
            long startTime = System.currentTimeMillis();
            try {
                String methodName = method.getName();
                log.info("{}开始执行", methodName);
                Object result = method.invoke(obj, args);
                return result;
            } finally {
                log.info("{}执行完毕,耗时：{}", obj.getClass().getName(), (System.currentTimeMillis() - startTime));
            }
        });
    }
}
