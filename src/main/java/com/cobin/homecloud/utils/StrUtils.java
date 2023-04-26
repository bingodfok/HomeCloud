package com.cobin.homecloud.utils;

/**
 * 自定义 StringUtils
 *
 * @Author 1_bit
 * @Date 2023/3/13 22:27
 */
public class StrUtils {

    public static boolean notEmpty(String str) {
        if (str == null) return false;
        else return str.length() > 0;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNull(String str) {
        return str == null;
    }

}
