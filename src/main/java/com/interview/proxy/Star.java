package com.interview.proxy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Star implements StarInterface {

    private String name;

    @Override
    public void sing(String name) {
        System.out.println(this.name + "表演唱歌:" + name);
    }

    @Override
    public String dance() {
        System.out.println(this.name + "表演跳舞");
        return "谢谢";
    }
}
