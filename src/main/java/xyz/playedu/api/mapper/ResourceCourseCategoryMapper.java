package xyz.playedu.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.playedu.api.domain.ResourceCourseCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author tengteng
* @description 针对表【resource_course_category】的数据库操作Mapper
* @createDate 2023-03-09 09:54:22
* @Entity xyz.playedu.api.domain.ResourceCourseCategory
*/
@Mapper
public interface ResourceCourseCategoryMapper extends BaseMapper<ResourceCourseCategory> {

}




