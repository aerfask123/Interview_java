package com.interview.synchonized;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Random;

@Data
@AllArgsConstructor
public class Goods implements IGoods {

    private String name;

    static String names[] = {"苹果", "香蕉", "橘子", "西瓜", "芒果"};

    public static IGoods produceOne() {
        Random random = new Random();
        return new Goods(names[random.nextInt(names.length)]);
    }
}
