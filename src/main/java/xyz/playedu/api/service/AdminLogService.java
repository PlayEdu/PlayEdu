/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.service;

import com.baomidou.mybatisplus.extension.service.IService;

import org.springframework.stereotype.Service;

import xyz.playedu.api.domain.AdminLog;

/**
 * @author tengteng
 * @description 针对表【admin_logs】的数据库操作Service
 * @createDate 2023-02-17 15:40:31
 */
@Service
public interface AdminLogService extends IService<AdminLog> {}
