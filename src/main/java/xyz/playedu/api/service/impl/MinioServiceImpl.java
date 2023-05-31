/*
 * Copyright 2023 杭州白书科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.playedu.api.service.impl;

import io.minio.*;
import io.minio.http.Method;

import lombok.SneakyThrows;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import xyz.playedu.api.exception.ServiceException;
import xyz.playedu.api.service.AppConfigService;
import xyz.playedu.api.service.MinioService;
import xyz.playedu.api.types.config.MinioConfig;
import xyz.playedu.api.vendor.PlayEduMinioClient;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class MinioServiceImpl implements MinioService {

    private MinioConfig minioConfig = null;

    @Autowired private AppConfigService appConfigService;

    @SneakyThrows
    private MinioConfig getMinioConfig() {
        if (minioConfig == null) {
            minioConfig = appConfigService.getMinioConfig();
            if (minioConfig.getAccessKey().isBlank()
                    || minioConfig.getSecretKey().isBlank()
                    || minioConfig.getBucket().isBlank()
                    || minioConfig.getDomain().isBlank()
                    || minioConfig.getEndpoint().isBlank()) {
                throw new ServiceException("MinIO服务未配置");
            }
        }
        return minioConfig;
    }

    private String bucket() {
        return getMinioConfig().getBucket();
    }

    public MinioClient getMinioClient() {
        MinioConfig c = getMinioConfig();

        return MinioClient.builder()
                .endpoint(c.getEndpoint())
                .credentials(c.getAccessKey(), c.getSecretKey())
                .build();
    }

    public PlayEduMinioClient getPlayEduMinioClient() {
        MinioConfig c = getMinioConfig();

        MinioAsyncClient client =
                PlayEduMinioClient.builder()
                        .endpoint(c.getEndpoint())
                        .credentials(c.getAccessKey(), c.getSecretKey())
                        .build();

        return new PlayEduMinioClient(client);
    }

    @Override
    public String url(String path) {
        MinioConfig c = getMinioConfig();

        return c.getDomain()
                + (c.getDomain().endsWith("/") ? "" : "/")
                + c.getBucket()
                + "/"
                + path;
    }

    @Override
    @SneakyThrows
    public String saveFile(MultipartFile file, String savePath, String contentType) {
        PutObjectArgs objectArgs =
                PutObjectArgs.builder().bucket(bucket()).object(savePath).stream(
                                file.getInputStream(), file.getSize(), -1)
                        .contentType(contentType)
                        .build();
        getMinioClient().putObject(objectArgs);

        return url(savePath);
    }

    @Override
    public String uploadId(String path) {
        return getPlayEduMinioClient().uploadId(bucket(), path);
    }

    @Override
    @SneakyThrows
    public String chunkPreSignUrl(String filename, String partNumber, String uploadId) {
        Map<String, String> extraQueryParams = new HashMap<>();
        extraQueryParams.put("partNumber", partNumber);
        extraQueryParams.put("uploadId", uploadId);

        return getMinioClient()
                .getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .bucket(bucket())
                                .object(filename)
                                .method(Method.PUT)
                                .expiry(60 * 60 * 24)
                                .extraQueryParams(extraQueryParams)
                                .build());
    }

    @Override
    public String merge(String filename, String uploadId) {
        getPlayEduMinioClient().merge(bucket(), filename, uploadId);
        return url(filename);
    }

    @Override
    @SneakyThrows
    public void removeByPath(String path) {
        getMinioClient()
                .removeObject(RemoveObjectArgs.builder().bucket(bucket()).object(path).build());
    }

    @Override
    @SneakyThrows
    public String saveBytes(byte[] file, String savePath, String contentType) {
        InputStream inputStream = new ByteArrayInputStream(file);

        PutObjectArgs objectArgs =
                PutObjectArgs.builder().bucket(bucket()).object(savePath).stream(
                                inputStream, file.length, -1)
                        .contentType(contentType)
                        .build();

        getMinioClient().putObject(objectArgs);

        return url(savePath);
    }
}
