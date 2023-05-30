package com.cobin.homecloud.cloud_resource.minio;//


import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinIOConfig {

    @Autowired
    MinIOProperties minIoProperties;

    @Bean(name = "minioClient")
    public MinioClient minioClient() {
        return MinioClient.builder().endpoint(minIoProperties.getUrl(), minIoProperties.getPort(), minIoProperties.isSecure()) // 无SSl安全证书
                .credentials(minIoProperties.getAccessKey(), minIoProperties.getSecretKey())
                .build();
    }
}
