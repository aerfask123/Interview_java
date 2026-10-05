package com.interview.generic;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LawyerProfile extends UserProfile<String, Integer> {
    public LawyerProfile(User user) {
        super(user);
    }

    @Override
    public void login(String username, Integer password) {
        log.info("Lawyer {} login,password:{}", username, password);
    }
}
