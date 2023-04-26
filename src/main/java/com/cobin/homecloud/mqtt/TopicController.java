package com.cobin.homecloud.mqtt;

import com.cobin.homecloud.common.annotation.MqttController;
import com.cobin.homecloud.common.annotation.MqttReqMapping;
import org.springframework.messaging.Message;

/**
 * 仿 SpringMVC 自定义 MqttController
 * @Author 1_bit
 * @Date 2023/4/8 21:45
 */
@MqttController
public class TopicController {

    //方法
    @MqttReqMapping(topic = "bing",qos = 1)
    public void testMapping(Message<?> message){
        System.out.println(message.getPayload());
    }

    @MqttReqMapping(topic = "test")
    public void test(Message<?> message){
        System.out.println(message.getPayload());
    }

    public void test1(Message<?> message){
        System.out.println(message.getPayload());
    }
}
