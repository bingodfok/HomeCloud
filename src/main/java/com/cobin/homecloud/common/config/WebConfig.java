package com.cobin.homecloud.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 通用配置
 *
 * @Author 1_bit
 * @Date 2023/3/16 22:57
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    /**
     * 跨域配置
     *
     * @param registry 登记
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowedOriginPatterns("*")
                .exposedHeaders("*");
    }
}
