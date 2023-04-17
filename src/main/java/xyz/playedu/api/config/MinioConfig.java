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
package xyz.playedu.api.config;

import io.minio.MinioAsyncClient;
import io.minio.MinioClient;

import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import xyz.playedu.api.vendor.PlayEduMinioClient;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/2/28 16:38
 */
@Data
@Configuration
public class MinioConfig {

    @Value("${minio.domain}")
    private String domain;

    @Value("${minio.bucket}")
    private String bucket;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Value("${minio.end-point}")
    private String endPoint;

    @Bean
    public MinioClient getMinioClient() {
        return MinioClient.builder()
                .endpoint(this.endPoint)
                .credentials(this.accessKey, this.secretKey)
                .build();
    }

    @Bean
    public PlayEduMinioClient getPlayEduMinioClient() {
        MinioAsyncClient client =
                PlayEduMinioClient.builder()
                        .endpoint(this.endPoint)
                        .credentials(this.accessKey, this.secretKey)
                        .build();
        return new PlayEduMinioClient(client);
    }
}
