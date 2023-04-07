/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

import xyz.playedu.api.domain.CourseHour;

/**
 * @author tengteng
 * @description 针对表【course_hour】的数据库操作Mapper
 * @createDate 2023-03-15 10:16:45 @Entity xyz.playedu.api.domain.CourseHour
 */
@Mapper
public interface CourseHourMapper extends BaseMapper<CourseHour> {}
