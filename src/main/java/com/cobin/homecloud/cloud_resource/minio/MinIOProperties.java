package com.cobin.homecloud.cloud_resource.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinIOProperties {

    private String url;

    private String accessKey;

    private String secretKey;

    private Integer port;

    private boolean secure;

}
