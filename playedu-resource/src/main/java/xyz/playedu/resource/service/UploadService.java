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
package xyz.playedu.resource.service;

import org.springframework.web.multipart.MultipartFile;

import xyz.playedu.resource.domain.Resource;
import xyz.playedu.common.domain.UserUploadImageLog;
import xyz.playedu.common.exception.ServiceException;
import xyz.playedu.common.types.UploadFileInfo;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/3/8 14:02
 */
public interface UploadService {

    UploadFileInfo upload(MultipartFile file, String dir) throws ServiceException;

    Resource storeMinio(Integer adminId, MultipartFile file, String categoryIds)
            throws ServiceException;

    Resource storeBase64Image(Integer adminId, String content, String categoryIds)
            throws ServiceException;

    UserUploadImageLog userAvatar(Integer userId, MultipartFile file, String typed, String scene);
}
