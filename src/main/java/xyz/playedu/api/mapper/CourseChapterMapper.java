/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

import xyz.playedu.api.domain.CourseChapter;

/**
 * @author tengteng
 * @description 针对表【course_chapters】的数据库操作Mapper
 * @createDate 2023-02-26 17:34:01 @Entity xyz.playedu.api.domain.CourseChapter
 */
@Mapper
public interface CourseChapterMapper extends BaseMapper<CourseChapter> {}
