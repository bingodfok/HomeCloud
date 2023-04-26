package com.cobin.homecloud.utils;

import com.cobin.homecloud.common.annotation.Anonymous;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * 获取自定义匿名访问路径
 *
 * @Author 1_bit
 * @Date 2023/4/8 16:21
 */
@Configuration
public class AnonymousUrlContext implements ApplicationContextAware, InitializingBean {

    //使用Set集合防止出现重复路径
    private final Set<String> AnonymousUrl = new HashSet<>();
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {

        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        // RequestMappingInfo Request信息 ;HandlerMethod 处理方法
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();
        handlerMethods.keySet().forEach(info -> {

            //判断Method上是否有Anonymous注解 如果 method 不为空则获取 Mapping 的路径
            Anonymous method = handlerMethods.get(info).getMethodAnnotation(Anonymous.class);
            Optional.ofNullable(method).ifPresent(anonymous ->
                    AnonymousUrl.addAll(info.getPatternValues())
            );

            //判断Method所在类上是否有Anonymous注解 如果BeanType不为空则获取 Mapping 的路径
            Anonymous BeanType = handlerMethods.get(info).getBeanType().getAnnotation(Anonymous.class);
            Optional.ofNullable(BeanType).ifPresent(anonymous ->
                    AnonymousUrl.addAll(info.getDirectPaths())
            );
        });
    }

    public Set<String> getAnonymousUrl() {
        return this.AnonymousUrl;
    }
}
