package com.cobin.homecloud.mqtt;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * Mqtt发送消息
 * @Author 1_bit
 * @Date 2023/4/8 18:12
 */
@Component
@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttGatWay {

    void send(String msg);

    void send(@Header(MqttHeaders.TOPIC) int Qos, String msg);

    void send(@Header(MqttHeaders.TOPIC) String Topic, @Header(MqttHeaders.QOS) int Qos, String msg);
}
