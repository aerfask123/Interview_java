package com.interview.generic;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfile<T, R> implements Profile<T, R> {

    private User user;

    @Override
    public void login(T username, R password) {
        return;
    }
}
