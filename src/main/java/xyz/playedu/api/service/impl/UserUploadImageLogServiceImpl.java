/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
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
