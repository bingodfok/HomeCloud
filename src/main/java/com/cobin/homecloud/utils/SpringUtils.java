package com.cobin.homecloud.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * Spring工具 在非Spring管理的环境中获取Bean
 *
 * @Author 1_bit
 * @Date 2023/3/13 23:53
 */
@Configuration
public class SpringUtils implements ApplicationContextAware, BeanFactoryPostProcessor {

    private static ApplicationContext applicationContext;

    private static BeanFactory beanFactory;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtils.applicationContext = applicationContext;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        SpringUtils.beanFactory = beanFactory;
    }

    /**
     * 根据Class类型获得一个注入的Bean
     *
     * @return Class 对应的Bean Object
     */
    public static <T> T getBean(Class<T> aClass) {
        return beanFactory.getBean(aClass);
    }

    /**
     * 根据类名获得一个注入的Bean
     *
     * @param name 类名
     * @return Bean Object
     */
    public static Object getBean(String name) {
        return beanFactory.getBean(name);
    }

    public static Map<String, Object> getBeanByAnnotation(Class<? extends Annotation> Clazz){
       return applicationContext.getBeansWithAnnotation(Clazz);
    }


}
