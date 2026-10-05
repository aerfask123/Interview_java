package com.interview.cas;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class User implements Serializable {

    private String uid;

    private String nickName;

    public volatile int age;
}
