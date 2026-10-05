package com.interview.generic;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomerProfile extends UserProfile<String, Integer> {

    public CustomerProfile(User user) {
        super(user);
    }

    @Override
    public void login(String username, Integer password) {
        log.info("Customer {} login,password:{}", username, password);
    }
}
