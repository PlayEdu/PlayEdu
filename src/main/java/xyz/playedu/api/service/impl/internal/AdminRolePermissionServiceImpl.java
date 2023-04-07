/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.service.impl.internal;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

import xyz.playedu.api.domain.AdminRolePermission;
import xyz.playedu.api.mapper.AdminRolePermissionMapper;
import xyz.playedu.api.service.internal.AdminRolePermissionService;

/**
 * @author tengteng
 * @description 针对表【admin_role_permission】的数据库操作Service实现
 * @createDate 2023-02-21 16:07:01
 */
@Service
public class AdminRolePermissionServiceImpl
        extends ServiceImpl<AdminRolePermissionMapper, AdminRolePermission>
        implements AdminRolePermissionService {}
