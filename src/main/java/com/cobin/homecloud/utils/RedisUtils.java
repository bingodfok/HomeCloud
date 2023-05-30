package com.cobin.homecloud.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @Author 1_bit
 * @Date 2023/3/14 22:47
 */
@Configuration
public class RedisUtils {

    private static RedisTemplate<Object, Object> template;

    @Autowired
    public RedisUtils(RedisTemplate<Object, Object> template) {
        RedisUtils.template = template;
    }

    public static <K> Object getValue(K key) {
        return template.opsForValue().get(key);
    }

    public static <K, V> void setValue(K key, V value) {
        template.opsForValue().set(key, value);
    }

    public static <K, V> void setValueTimeout(K key, V value, Long timeout) {
        template.opsForValue().set(key, value, timeout, TimeUnit.MINUTES);
    }

    public static <K> void deleteValue(K key) {
        template.opsForValue().getOperations().delete(key);
    }



}
