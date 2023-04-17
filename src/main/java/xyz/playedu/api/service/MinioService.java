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
package xyz.playedu.api.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/3/7 13:29
 */
public interface MinioService {

    String url(String path);

    String saveFile(MultipartFile file, String savePath, String contentType);

    String saveBytes(byte[] file, String savePath, String contentType);

    String uploadId(String path);

    String chunkPreSignUrl(String filename, String partNumber, String uploadId);

    String merge(String filename, String uploadId);

    void removeByPath(String path);
}
