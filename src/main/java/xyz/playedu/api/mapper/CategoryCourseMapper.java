package xyz.playedu.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.playedu.api.domain.CategoryCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author tengteng
* @description 针对表【category_course】的数据库操作Mapper
* @createDate 2023-02-24 14:48:26
* @Entity xyz.playedu.api.domain.CategoryCourse
*/
@Mapper
public interface CategoryCourseMapper extends BaseMapper<CategoryCourse> {

}




