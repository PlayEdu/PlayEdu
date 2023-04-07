/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

import xyz.playedu.api.domain.AdminUser;

@Mapper
/**
 * @author tengteng
 * @description 针对表【admin_users】的数据库操作Mapper
 * @createDate 2023-02-11 10:58:52 @Entity xyz.playedu.api.domain.AdminUser
 */
public interface AdminUserMapper extends BaseMapper<AdminUser> {}
