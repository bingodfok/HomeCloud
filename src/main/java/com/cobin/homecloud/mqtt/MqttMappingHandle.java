package com.cobin.homecloud.mqtt;


import com.cobin.homecloud.common.annotation.MqttController;
import com.cobin.homecloud.common.annotation.MqttReqMapping;
import com.cobin.homecloud.utils.SpringUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
/**
 * 自定义MQTT映射路径处理
 * @Author 1_bit
 * @Date 2023/4/8 22:32
 */
@Component
public class MqttMappingHandle implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(MqttMappingHandle.class);
    private final ConcurrentMap<TopicInfo, MqttHandleMethod> handelMap = new ConcurrentHashMap<>();

    @Override
    public void afterPropertiesSet(){

        Map<String, Object> bean = SpringUtils.getBeanByAnnotation(MqttController.class);
        bean.keySet().forEach(BeanName->{
            Class<?> Clazz = bean.get(BeanName).getClass();
            String baseurl = Clazz.getAnnotation(MqttController.class).value();
            for (Method method : Clazz.getDeclaredMethods()) {

                Optional.ofNullable(method.getAnnotation(MqttReqMapping.class)).ifPresent(pram->{
                    String url = baseurl + pram.topic();
                    logger.info("add topic ['{}'] Qos [{}]",url,pram.qos());
                    handelMap.put(new TopicInfo(url, pram.qos()),new MqttHandleMethod(method,bean.get(BeanName)));
                });
            }
        });
    }
    public ConcurrentMap<TopicInfo ,MqttHandleMethod> getHandlerMethods(){
        return this.handelMap;
    }
    @Data
    static class TopicInfo {
        private String topic;
        private int qos;
        public TopicInfo(String topic,int qos){
            this.qos = qos;
            this.topic = topic;
        }
        public TopicInfo(){};
    }
}

