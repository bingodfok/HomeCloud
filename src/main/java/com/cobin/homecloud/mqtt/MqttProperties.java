package com.cobin.homecloud.mqtt;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class MqttProperties {
    /**
     * 连接地址
     */
    @Value("${mqtt.broker}")
    private String broker;
    /**
     * 进-客户Id
     */
    @Value("${mqtt.in_client_id}")
    private String inClientId;

    /**
     * 出-客户Id
     */
    @Value("${mqtt.out_client_id}")
    private String outClientId;

    /**
     * 客户Id
     */
    @Value("${mqtt.client_id}")
    private String clientId;

    /**
     * 默认连接话题
     */
    @Value("${mqtt.default_topic}")
    private String defaultTopic;

    /**
     * 超时时间
     */
    @Value("${mqtt.timeout}")
    private int timeout;

    /**
     * 保持连接数
     */
    @Value("${mqtt.keepalive}")
    private int keepalive;

    /**
     * 是否清除session
     */
    @Value("${mqtt.clearSession}")
    private boolean clearSession;

}

