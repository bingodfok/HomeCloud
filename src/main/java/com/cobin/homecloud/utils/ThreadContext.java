package com.cobin.homecloud.utils;

/**
 * 线程缓存
 * @Author 1_bit
 * @Date 2023/4/26 0:04
 */
public class ThreadContext {
    public static ThreadLocal<Object> local = new ThreadLocal<>();

    public static void setContext(Object value) {
        local.set(value);
    }

    public static Object getContext() {
        return local.get();
    }

    public static void remove() {
        local.remove();
    }
}
