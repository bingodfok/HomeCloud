package com.cobin.homecloud.mqtt;

import lombok.Data;
import org.springframework.messaging.Message;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
@Data
public class MqttHandleMethod {
    private Method method;
    private Object beanType;

    public MqttHandleMethod(Method method, Object beanType){
        Assert.notNull(method,"Method 不能为空...");
        Assert.notNull(method,"BeanType 不能为空...");
        this.beanType = beanType;
        this.method = method;
    }

    public void invoke(Message<?> message) throws InvocationTargetException, IllegalAccessException {
        method.invoke(beanType,message);
    }
}
