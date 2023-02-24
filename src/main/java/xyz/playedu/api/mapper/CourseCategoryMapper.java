package xyz.playedu.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.playedu.api.domain.CourseCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author tengteng
* @description 针对表【course_categories】的数据库操作Mapper
* @createDate 2023-02-24 13:55:19
* @Entity xyz.playedu.api.domain.CourseCategory
*/
@Mapper
public interface CourseCategoryMapper extends BaseMapper<CourseCategory> {

}




