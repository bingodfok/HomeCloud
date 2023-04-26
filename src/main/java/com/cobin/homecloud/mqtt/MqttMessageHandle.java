package com.cobin.homecloud.mqtt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 接收消息处理
 * @Author 1_bit
 * @Date 2023/4/8 22:25
 */
@Component
public class MqttMessageHandle implements MessageHandler {

    @Autowired
    MqttMappingHandle mqttMappingHandle;
    //接收消息处理
    @Override
    public void handleMessage(Message<?> message) throws MessagingException {

        //获取消息来源 Topic
        String mqtt_receivedTopic = message.getHeaders().get("mqtt_receivedTopic", String.class);
        AtomicReference<MqttHandleMethod> method = new AtomicReference<>();
        mqttMappingHandle.getHandlerMethods().keySet().forEach(topicInfo -> {
            if(topicInfo.getTopic().equals(mqtt_receivedTopic)){
                method.set(mqttMappingHandle.getHandlerMethods().get(topicInfo));
            }
        });

        try {
            method.get().invoke(message);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
