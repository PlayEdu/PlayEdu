/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

import xyz.playedu.api.domain.CategoryCourse;

/**
 * @author tengteng
 * @description 针对表【category_course】的数据库操作Mapper
 * @createDate 2023-02-24 14:48:26 @Entity xyz.playedu.api.domain.CategoryCourse
 */
@Mapper
public interface CategoryCourseMapper extends BaseMapper<CategoryCourse> {}
