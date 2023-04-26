package com.cobin.homecloud.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * 获取 Mybatis-plus 构造器对象
 *
 * @Author 1_bit
 * @Date 2023/3/14 21:31
 */
public class WrapperUtils {
    public static <T> QueryWrapper<T> getQueryWrapper() {
        return new QueryWrapper<>();
    }
}
