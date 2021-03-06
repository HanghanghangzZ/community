package com.hang.myselfcommunity.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

/**
 * 阿里云OOS的工具类
 */
public class AliyunOOSUtil {
    public static String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
    public static String accessKeyId = "****";
    public static String accessKeySecret = "*****";
    public static String bucketName = "hanghanghangzz";
    private static OSS ossClient = null;

    public static void buildOOSClient() {
        if (ossClient == null) {
            ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        }
    }

    public static void destroy() {
        ossClient.shutdown();
        ossClient = null;
    }

    /**
     * 上传文件至阿里云OOS，并返回有效期为1000年的url
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String uploadFile(MultipartFile file) throws IOException {
        UUID uuid = UUID.randomUUID();
        String UUIDName = uuid.toString().replace("-", "");

        InputStream inputStream = file.getInputStream();
        String fileKey = UUIDName;

        PutObjectResult putObjectResult = ossClient.putObject(AliyunOOSUtil.bucketName, fileKey, inputStream);
        /* 3600l * 1000 * 24 * 365 * 1000 设置url过期时间为1000年 */
        URL url = ossClient.generatePresignedUrl(AliyunOOSUtil.bucketName, UUIDName, new Date(new Date().getTime() + 3600L * 1000 * 24 * 365 * 1000));
        System.out.println(url);
        System.out.println("Object：" + fileKey + "存入OSS成功。");

        return url.toString();
    }
}