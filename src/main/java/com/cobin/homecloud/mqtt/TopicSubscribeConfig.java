package com.cobin.homecloud.mqtt;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
/**
 * 主题订阅配置
 * @Author 1_bit
 * @Date 2023/4/8 22:54
 */
@Configuration
public class TopicSubscribeConfig implements InitializingBean {

    @Autowired
    MqttPahoMessageDrivenChannelAdapter adapter;
    @Autowired
    MqttMappingHandle mqttMappingHandle;

    @Override
    public void afterPropertiesSet() throws Exception {
        mqttMappingHandle.getHandlerMethods().keySet().forEach(info->{
            adapter.addTopic(info.getTopic(),info.getQos());
        });
    }
}
