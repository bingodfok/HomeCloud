package com.cobin.homecloud.cloud_resource.minio;

import com.cobin.homecloud.utils.SpringUtils;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * minio工具
 *
 * @Author 1_bit
 * @Date 2023/5/27 23:35
 */
@Configuration
public class MinIOUtils implements InitializingBean {

    private static MinioClient minioClient;

    @Override
    public void afterPropertiesSet() throws Exception {
        minioClient = SpringUtils.getBean(MinioClient.class);
    }

    /**
     * 获取桶tags
     * @param bucketName 桶名
     * @return tags Map
     */
    public static Map<String, String> getBucketTags(String bucketName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return minioClient.getBucketTags(GetBucketTagsArgs.builder().bucket(bucketName).build()).get();
    }

    public static GetObjectResponse getObject(String bucketName,String objName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objName).build());
    }

    /**
     * 获取对象 列表
     * @param bucketName 桶名
     * @param prefix 对象前缀 若获取根目录全部对象则 prefix = ""
     * @return 对象迭代列表
     */
    public static Iterable<Result<Item>> getObjects(String bucketName,String prefix){
       return minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).maxKeys(100).prefix(prefix).build());
    }

    /**
     * 获取根路劲下的对象 列表
     * @param bucketName 桶名
     * @return 对象迭代列表
     */
    public static Iterable<Result<Item>> getRootObjects(String bucketName){
        return getObjects(bucketName,"");
    }

}
