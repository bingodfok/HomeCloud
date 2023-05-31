package com.cobin.homecloud.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
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

    /**
     * 设置一个hash 并设置过期时间
     */
    public static void setMapValueTimeout(String key, Map<String, Object> mapValue, Long timeout) {
        template.opsForHash().putAll(key, mapValue);
        if (Boolean.FALSE.equals(template.expire(key, timeout, TimeUnit.SECONDS))) {
            template.expire(key, timeout, TimeUnit.SECONDS);
        }
    }

    /**
     * 根据key获取hashMap
     */
    public static Map<Object, Object> getMapValue(String key) {
        return template.opsForHash().entries(key);
    }

    /**
     * hashValue 自减
     *
     * @return delta 步长
     */
    public static long subtractionValue(String key, String hashKey, int delta) {
        return template.opsForHash().increment(key, hashKey, -delta);
    }

}
