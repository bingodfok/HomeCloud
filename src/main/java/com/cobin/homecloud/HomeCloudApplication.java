package com.cobin.homecloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@MapperScan("com.cobin.homecloud.mapper")
//@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@SpringBootApplication
@EnableConfigurationProperties
public class HomeCloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomeCloudApplication.class, args);
    }

}
