package com.interview.methodhandle;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class MethodhandleTest {

    static class ClassA {
        public void println(String s) {
            System.out.println(s);
        }
    }

    public static void main(String[] args) throws Throwable {
        Object obj = System.currentTimeMillis() % 2 == 0 ? new ClassA() : System.out;

        getPrintInMH(obj).invokeExact("hello");
    }

    private static MethodHandle getPrintInMH(Object obj) throws NoSuchMethodException, IllegalAccessException {
        MethodType mt = MethodType.methodType(void.class, String.class);

        return MethodHandles.lookup().findVirtual(obj.getClass(), "println", mt).bindTo(obj);
    }
}
