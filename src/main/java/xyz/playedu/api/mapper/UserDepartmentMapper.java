/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

import xyz.playedu.api.domain.UserDepartment;

/**
 * @author tengteng
 * @description 针对表【user_department】的数据库操作Mapper
 * @createDate 2023-02-23 15:08:38 @Entity xyz.playedu.api.domain.UserDepartment
 */
@Mapper
public interface UserDepartmentMapper extends BaseMapper<UserDepartment> {}
