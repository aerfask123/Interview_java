package com.interview.completablefuture;

import com.interview.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class DrinkTea {

    private static final int SLEEP_GAP = 3;

    public static void main(String[] args) {
        Random rn = new Random();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        CompletableFuture<Boolean> hotJob = CompletableFuture.supplyAsync(() -> {
            log.info("洗好水壶");
            log.info("烧开水");

            ThreadUtil.sleepSeconds(2);

            log.info("水开了");
            return true;
        });

        CompletableFuture<Boolean> washJob = CompletableFuture.supplyAsync(() -> {
            log.info("洗茶杯");
            ThreadUtil.sleepSeconds(SLEEP_GAP);
            log.info("洗完了");

            return rn.nextBoolean();
        });

        CompletableFuture<String> drinkJob = hotJob.thenCombineAsync(washJob, (hot, wash) -> {
            if (hot && wash) {
                log.info("泡茶喝，茶喝完");
                return "茶喝完了";
            }
            throw new RuntimeException("茶没泡好，喝不了");
        }).handle((result, t) -> {
            if (t != null) {
                log.info("{}", t.getMessage());
            } else {
                log.info("{}", result);
            }
            return result;
        });

        drinkJob.join();
        stopWatch.stop();
        log.info("耗时：{}", stopWatch.getTotalTimeMillis());
        log.info(stopWatch.prettyPrint());
    }
}
