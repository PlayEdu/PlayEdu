/*
 * Copyright (C) 2023 杭州白书科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.playedu.common.util;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.multipart.MultipartFile;

import xyz.playedu.common.exception.ServiceException;
import xyz.playedu.common.types.config.S3Config;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class S3Util {

    private S3Config defaultConfig;

    public S3Config getS3Config() {
        return defaultConfig;
    }

    public S3Util(S3Config s3Config) {
        defaultConfig = s3Config;
    }

    public S3Util setConfig(S3Config config) {
        defaultConfig = config;
        return this;
    }

    public boolean configIsEmpty() {
        return defaultConfig == null
                || StringUtil.isEmpty(defaultConfig.getDomain())
                || StringUtil.isEmpty(defaultConfig.getEndpoint())
                || StringUtil.isEmpty(defaultConfig.getAccessKey())
                || StringUtil.isEmpty(defaultConfig.getSecretKey());
    }

    @SneakyThrows
    private AmazonS3 getClient() {
        if (defaultConfig == null) {
            throw new ServiceException("存储服务未配置");
        }
        AWSCredentials credentials =
                new BasicAWSCredentials(defaultConfig.getAccessKey(), defaultConfig.getSecretKey());

        AwsClientBuilder.EndpointConfiguration endpointConfiguration =
                new AwsClientBuilder.EndpointConfiguration(
                        defaultConfig.getEndpoint(), defaultConfig.getRegion());

        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(endpointConfiguration)
                .build();
    }

    @SneakyThrows
    public String saveFile(MultipartFile file, String savePath, String contentType) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        objectMetadata.setContentLength(file.getInputStream().available());
        getClient()
                .putObject(
                        defaultConfig.getBucket(), savePath, file.getInputStream(), objectMetadata);
        return generateEndpointPreSignUrl(savePath);
    }

    @SneakyThrows
    public String saveBytes(byte[] file, String savePath, String contentType) {
        InputStream inputStream = new ByteArrayInputStream(file);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        objectMetadata.setContentLength(inputStream.available());
        getClient().putObject(defaultConfig.getBucket(), savePath, inputStream, objectMetadata);
        return generateEndpointPreSignUrl(savePath);
    }

    public String uploadId(String path) {
        InitiateMultipartUploadRequest request =
                new InitiateMultipartUploadRequest(defaultConfig.getBucket(), path);
        InitiateMultipartUploadResult result = getClient().initiateMultipartUpload(request);
        return result.getUploadId();
    }

    public String generatePartUploadPreSignUrl(
            String filename, String partNumber, String uploadId) {
        GeneratePresignedUrlRequest request =
                new GeneratePresignedUrlRequest(
                        defaultConfig.getBucket(), filename, HttpMethod.PUT);
        request.setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000)); // 一个小时有效期
        request.addRequestParameter("partNumber", partNumber); // 分块索引
        request.addRequestParameter("uploadId", uploadId); // uploadId
        return getClient().generatePresignedUrl(request).toString();
    }

    @SneakyThrows
    public String merge(String filename, String uploadId) {
        AmazonS3 client = getClient();

        ListPartsRequest listPartsRequest =
                new ListPartsRequest(defaultConfig.getBucket(), filename, uploadId);
        PartListing parts = client.listParts(listPartsRequest);
        if (parts.getParts().isEmpty()) {
            throw new ServiceException("没有已上传的分片文件");
        }

        List<PartETag> eTags = new ArrayList<>();
        parts.getParts()
                .forEach(
                        item -> {
                            eTags.add(new PartETag(item.getPartNumber(), item.getETag()));
                        });

        CompleteMultipartUploadRequest request = new CompleteMultipartUploadRequest();
        request.setBucketName(defaultConfig.getBucket());
        request.setKey(filename);
        request.setUploadId(uploadId);
        request.setPartETags(eTags);

        client.completeMultipartUpload(request);

        return generateEndpointPreSignUrl(filename);
    }

    public void removeByPath(String path) {
        DeleteObjectRequest request = new DeleteObjectRequest(defaultConfig.getBucket(), path);
        getClient().deleteObject(request);
    }

    public boolean exists(String path) {
        return getClient().doesObjectExist(defaultConfig.getBucket(), path);
    }

    @SneakyThrows
    public String getContent(String path) {
        S3Object s3Object = getClient().getObject(defaultConfig.getBucket(), path);
        return new String(s3Object.getObjectContent().readAllBytes(), StandardCharsets.UTF_8);
    }

    public String generateEndpointPreSignUrl(String path) {
        if (defaultConfig.getService().equals("minio")) {
            return defaultConfig.getDomain() + "/" + path;
        }
        return "";
    }
}
