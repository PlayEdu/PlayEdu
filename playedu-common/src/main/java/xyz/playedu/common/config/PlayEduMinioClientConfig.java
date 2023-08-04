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
package xyz.playedu.common.config;

import io.minio.CreateMultipartUploadResponse;
import io.minio.ListPartsResponse;
import io.minio.MinioAsyncClient;
import io.minio.messages.Part;
import lombok.SneakyThrows;

import java.util.List;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/3/6 16:12
 */
public class PlayEduMinioClientConfig extends MinioAsyncClient {
    public PlayEduMinioClientConfig(MinioAsyncClient client) {
        super(client);
    }

    @SneakyThrows
    public String uploadId(String bucket, String filename) {
        CreateMultipartUploadResponse response =
                super.createMultipartUpload(bucket, null, filename, null, null);
        return response.result().uploadId();
    }

    @SneakyThrows
    public void merge(String bucketName, String objectName, String uploadId) {
        ListPartsResponse listPartsResponse =
                super.listParts(bucketName, null, objectName, 10000, 0, uploadId, null, null);
        List<Part> partList = listPartsResponse.result().partList();
        Part[] parts = new Part[10000];
        int partNumber = 1;
        for (Part part : partList) {
            parts[partNumber - 1] = new Part(partNumber, part.etag());
            partNumber++;
        }
        super.completeMultipartUpload(bucketName, null, objectName, uploadId, parts, null, null);
    }
}
