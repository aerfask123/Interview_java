package com.interview.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ThreadUtil {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    private static final int IO_MAX = Math.max(2, CPU_COUNT * 2);

    private static final int CPU_MAX = CPU_COUNT;

    /**
     * 混合线程池
     */
    private static final int MIXED_MAX = 128;  //最大线程数

    private static final String MIXED_THREAD_AMOUNT = "mixed.thread.amount";

    private static final int KEEP_ALIVE_SECONDS = 30;

    private static final int QUEUE_SIZE = 128;

    private static class CustomThreadFactory implements ThreadFactory {
        //线程池数量
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;

        //线程数量
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String threadTag;

        public CustomThreadFactory(String threadTag) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            this.threadTag = "apppool-" + poolNumber.getAndIncrement() + "-" + threadTag + "-";
        }

        @Override
        public Thread newThread(Runnable target) {
            Thread t = new Thread(group, target,
                    threadTag + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }


    public static ThreadPoolExecutor getIOIntenseTargetThreadPool() {
        return IOIntenseTargetThreadPoolLazyHolder.EXECUTOR;
    }

    public static ThreadPoolExecutor getCPUIntenseTargetThreadPool() {
        return CPUIntenseTargetThreadPoolLazyHolder.EXECUTOR;
    }

    public static ThreadPoolExecutor getMixedTargetThreadPool() {
        return MixedTargetThreadPoolLazyHolder.EXECUTOR;
    }

    private static class IOIntenseTargetThreadPoolLazyHolder {
        private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(
                IO_MAX, IO_MAX, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(QUEUE_SIZE),
                new CustomizableThreadFactory("io-intense-target-thread-pool-")
        );

        static {
            IOIntenseTargetThreadPoolLazyHolder.EXECUTOR.allowCoreThreadTimeOut(true);

            Runtime.getRuntime().addShutdownHook(new ShutdownHookThread("钩子方法关闭IO线程池", new Callable() {
                @Override
                public Object call() throws Exception {
                    shutdownThreadPoolGracefully(EXECUTOR);
                    return null;
                }
            }));
        }
    }

    private static class CPUIntenseTargetThreadPoolLazyHolder {
        private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(
                CPU_MAX, CPU_MAX, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(QUEUE_SIZE),
                new CustomizableThreadFactory("cpu-intense-target-thread-pool-")
        );

        static {
            EXECUTOR.allowCoreThreadTimeOut(true);

            Runtime.getRuntime().addShutdownHook(new ShutdownHookThread("钩子方法关闭CPU线程池", new Callable() {
                @Override
                public Object call() throws Exception {
                    shutdownThreadPoolGracefully(EXECUTOR);
                    return null;
                }
            }));
        }
    }

    private static class MixedTargetThreadPoolLazyHolder {
        //首先从环境变量 mixed.thread.amount 中获取预先配置的线程数
        //如果没有对 mixed.thread.amount 做配置，则使用常量 MIXED_MAX 作为线程数
        private static final int max = (null != System.getProperty(MIXED_THREAD_AMOUNT)) ?
                Integer.parseInt(System.getProperty(MIXED_THREAD_AMOUNT)) : MIXED_MAX;
        //线程池： 用于混合型任务
        private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(
                max,
                max,
                KEEP_ALIVE_SECONDS,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue(QUEUE_SIZE),
                new CustomThreadFactory("mixed"));

        public static ThreadPoolExecutor getInnerExecutor() {
            return EXECUTOR;
        }

        static {

            log.info("线程池已经初始化");


            EXECUTOR.allowCoreThreadTimeOut(true);
            //JVM关闭时的钩子函数
            Runtime.getRuntime().addShutdownHook(new ShutdownHookThread("混合型任务线程池", new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    //优雅关闭线程池
                    shutdownThreadPoolGracefully(EXECUTOR);
                    return null;
                }
            }));
        }
    }


    public static void shutdownThreadPoolGracefully(ExecutorService threadPool) {
        if (!(threadPool instanceof ExecutorService) || threadPool.isTerminated()) {
            return;
        }
        try {
            threadPool.shutdown();
        } catch (SecurityException e) {
            return;
        } catch (NullPointerException e) {
            return;
        }

        try {
            if (!threadPool.awaitTermination(6, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();

                if (!threadPool.awaitTermination(6, TimeUnit.SECONDS)) {
                    log.error("线程池任务未正常执行完毕");
                }
            }
        } catch (InterruptedException ie) {
            threadPool.shutdownNow();
        }

        if (!threadPool.isTerminated()) {
            try {
                for (int i = 0; i < 1000; i++) {
                    if (threadPool.awaitTermination(10, TimeUnit.MILLISECONDS)) {
                        break;
                    }
                    threadPool.shutdownNow();
                }
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            } catch (Throwable e) {
                log.error(e.getMessage());
            }
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    static class ShutdownHookThread extends Thread {

        private FutureTask futureTask;

        public ShutdownHookThread(String name, Callable callable) {
            super(name);
            this.futureTask = new FutureTask(callable);
        }

        @Override
        public void run() {
            try {
                futureTask.run();
                futureTask.get();
                log.info("{} 钩子方法关闭线程池成功", Thread.currentThread().getName());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void sleepSeconds(int i) {
        try {
            Thread.sleep(i * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleepMillis(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void printThread(String... name) {
        log.info("当前线程：{}", Thread.currentThread().getName());
    }
}
