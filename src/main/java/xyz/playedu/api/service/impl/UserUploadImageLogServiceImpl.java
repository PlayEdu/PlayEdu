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
package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

import xyz.playedu.api.domain.UserUploadImageLog;
import xyz.playedu.api.mapper.UserUploadImageLogMapper;
import xyz.playedu.api.service.UserUploadImageLogService;

/**
 * @author tengteng
 * @description 针对表【user_upload_image_logs】的数据库操作Service实现
 * @createDate 2023-03-24 14:32:48
 */
@Service
public class UserUploadImageLogServiceImpl
        extends ServiceImpl<UserUploadImageLogMapper, UserUploadImageLog>
        implements UserUploadImageLogService {}
