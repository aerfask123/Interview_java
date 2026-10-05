package com.interview.generic;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AdminProfile extends UserProfile<String, Integer> {

    public AdminProfile(User user) {
        super(user);
    }

    @Override
    public void login(String username, Integer password) {
        log.info("Admin {} login,password:{}", username, password);
    }
}
