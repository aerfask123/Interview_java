package com.interview.test;

import java.util.List;

public class FinalTest {

    private static final User user = new User();

    public static void main(String[] args) {
        user.age = 16;
        System.out.println(user);

        List<Integer> nums = List.of(1, 2, 4);
        nums.set(0, 9);
        System.out.println(nums);
    }
}

class User {
    int age = 6;

    int salary = 8;

    String name = "Jack";

    @Override
    public String toString() {
        return "User{" +
                "age=" + age +
                ", salary=" + salary +
                ", name='" + name + '\'' +
                '}';
    }
}