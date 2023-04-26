package com.cobin.homecloud.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义MqttReqMapping
 * @Author 1_bit
 * @Date 2023/4/11 10:50
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MqttReqMapping {
    String topic() default "";

    int qos() default 0;
}
