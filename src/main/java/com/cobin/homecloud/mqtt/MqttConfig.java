package com.cobin.homecloud.mqtt;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class MqttConfig {
    @Autowired
    MqttProperties properties;

    @Autowired
    MqttMessageHandle messageHandle;


    //客户端工厂
    @Bean
    public MqttPahoClientFactory mqttPahoClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        //mqtt连接条件创建
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setConnectionTimeout(properties.getTimeout());
        mqttConnectOptions.setServerURIs(new String[]{properties.getBroker()});
        mqttConnectOptions.setCleanSession(properties.isClearSession());
        factory.setConnectionOptions(mqttConnectOptions);
        return factory;
    }

    @Bean
    public ThreadPoolTaskExecutor mqttThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 最大可创建的线程数
        executor.setMaxPoolSize(200);
        // 核心线程池大小
        executor.setCorePoolSize(5);
        // 队列最大长度
        executor.setQueueCapacity(1000);
        // 线程池维护线程所允许的空闲时间
        executor.setKeepAliveSeconds(300);
        // 线程池对拒绝任务(无线程可用)的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 线程名前缀
        executor.setThreadNamePrefix("mqtt.Thread.pool-");
        return executor;
    }

    // 线程池通道
    // 入站消息通道
    @Bean
    public MessageChannel mqttInboundChannel() {
        // 用线程池
        return new ExecutorChannel(mqttThreadPoolTaskExecutor());
    }

    //出站消息通道
    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    // 通道适配器
    // 根据Spring IOC容器为单例模式 可以利用 注入的Adapter addTopic() 实现动态订阅主题
    @Bean
    public MqttPahoMessageDrivenChannelAdapter adapter(MqttPahoClientFactory factory) {
        //Topic 在Controller中定义
        return new MqttPahoMessageDrivenChannelAdapter(properties.getInClientId(), factory);
    }

    // 接收,处理来自mqtt的消息
    @Bean
    public IntegrationFlow mqttInbound(MqttPahoMessageDrivenChannelAdapter adapter) {
        adapter.setCompletionTimeout(5000);
        //在Controller中定义
//        adapter.setQos(1);
        return IntegrationFlows.from(adapter)
                .channel(mqttInboundChannel())
                .handle(messageHandle)
                .get();
    }

    // 出站处理器 (向 mqtt 发送消息)
    @Bean
    public IntegrationFlow mqttOutboundFlow(MqttPahoClientFactory factory) {

        MqttPahoMessageHandler handler = new MqttPahoMessageHandler(properties.getOutClientId(), factory);
        handler.setAsync(true);
        handler.setConverter(new DefaultPahoMessageConverter());
        handler.setDefaultTopic(properties.getDefaultTopic());
        return IntegrationFlows.from("mqttOutboundChannel")
                .handle(handler)
                .get();
    }
}
