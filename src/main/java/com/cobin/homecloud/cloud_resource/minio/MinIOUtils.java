package com.cobin.homecloud.cloud_resource.minio;

import com.cobin.homecloud.utils.SpringUtils;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.List;
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
     *
     * @param bucketName 桶名
     * @return tags Map
     */
    public static Map<String, String> getBucketTags(String bucketName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return minioClient.getBucketTags(GetBucketTagsArgs.builder().bucket(bucketName).build()).get();
    }

    public static GetObjectResponse getObject(String bucketName, String objName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objName).build());
    }

    /**
     * 获取对象 列表
     *
     * @param bucketName 桶名
     * @param prefix     对象前缀 若获取根目录全部对象则 prefix = ""
     * @return 对象迭代列表
     */
    public static Iterable<Result<Item>> getObjects(String bucketName, String prefix) {
        return minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).maxKeys(100).prefix(prefix).build());
    }

    /**
     * 获取根路劲下的对象 列表
     *
     * @param bucketName 桶名
     * @return 对象迭代列表
     */
    public static Iterable<Result<Item>> getRootObjects(String bucketName) {
        return getObjects(bucketName, "");
    }

    /**
     * 检查桶是否存在
     */
    public static boolean bucketExists(String bucketName) {
        boolean re = false;
        try {
            re = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return re;
    }

    public static void removeObject(String bucketName, String objName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().object(objName).bucket(bucketName).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 合并文件对象
     *
     * @param list       分片文件信息
     * @param resultName 期望结果文件名
     */
    public static ObjectWriteResponse composeObject(List<ComposeSource> list, String bucket, String resultName) {
        ObjectWriteResponse response = null;
        try {
            response = minioClient.composeObject(ComposeObjectArgs.builder().sources(list).object(resultName).bucket(bucket).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static Map<String, String> getPresignedPostFormData(String objName, String bucketName, int expirySec) {
        PostPolicy policy = new PostPolicy(bucketName, ZonedDateTime.now().plusSeconds(expirySec));
        policy.addEqualsCondition("key", objName);
        Map<String, String> from = null;
        try {
            from = minioClient.getPresignedPostFormData(policy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return from;
    }

    /**
     * 读取临时文件对象上传路径
     */
    public static String getPresignedPutObjectUrl(String objName, String bucketName, int expiry) {
        String url = null;
        try {
            url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .object(objName).method(Method.GET)
                    .bucket(bucketName)
                    .expiry(expiry)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * 向 minio 上传文件对象
     * @return 文件对象响应
     */
    public static ObjectWriteResponse putObject() throws IOException {
        ObjectWriteResponse response = null;
        try{
            response = minioClient.putObject(PutObjectArgs.builder().build());
        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }
}
