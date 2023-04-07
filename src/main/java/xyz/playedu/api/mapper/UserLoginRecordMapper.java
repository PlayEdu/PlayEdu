/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

import xyz.playedu.api.domain.UserLoginRecord;

/**
 * @author tengteng
 * @description 针对表【user_login_records】的数据库操作Mapper
 * @createDate 2023-03-10 14:06:55 @Entity xyz.playedu.api.domain.UserLoginRecord
 */
@Mapper
public interface UserLoginRecordMapper extends BaseMapper<UserLoginRecord> {}
