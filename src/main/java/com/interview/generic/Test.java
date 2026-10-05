package com.interview.generic;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<CustomerProfile> customers = new ArrayList<>();
        customers.add(new CustomerProfile(new User("王芳", 12)));
        customers.add(new CustomerProfile(new User("张雨绮", 123)));
        login(customers);

        List<LawyerProfile> lawyers = new ArrayList<>();
        lawyers.add(new LawyerProfile(new User("章若楠", 1234)));
        lawyers.add(new LawyerProfile(new User("刘亦菲", 123)));
        login(lawyers);

        List<AdminProfile> admins = new ArrayList<>();
        admins.add(new AdminProfile(new User("杨颖", 12345)));
        admins.add(new AdminProfile(new User("秦岚", 123456)));
        login(admins);
    }

    public static void login(List<? extends UserProfile> users) {
        for (UserProfile user : users) {
            System.out.println(user.getClass().getName());
            user.login(user.getUser().getUsername(), user.getUser().getPassword());
        }
    }
}
