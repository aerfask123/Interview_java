package com.interview.generic;

public interface Profile<T, R> {

    void login(T username, R password);
}
