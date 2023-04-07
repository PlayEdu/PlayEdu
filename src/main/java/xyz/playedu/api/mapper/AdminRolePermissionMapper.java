/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

import xyz.playedu.api.domain.AdminRolePermission;

/**
 * @author tengteng
 * @description 针对表【admin_role_permission】的数据库操作Mapper
 * @createDate 2023-02-21 16:07:01 @Entity xyz.playedu.api.domain.AdminRolePermission
 */
@Mapper
public interface AdminRolePermissionMapper extends BaseMapper<AdminRolePermission> {}
