package xyz.playedu.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.playedu.api.domain.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import xyz.playedu.api.types.mapper.CourseCategoryCountMapper;
import xyz.playedu.api.types.paginate.CoursePaginateFiler;

import java.util.List;

/**
* @author tengteng
* @description 针对表【courses】的数据库操作Mapper
* @createDate 2023-03-20 14:25:31
* @Entity xyz.playedu.api.domain.Course
*/
@Mapper
public interface CourseMapper extends BaseMapper<Course> {

    List<CourseCategoryCountMapper> getCategoryCount();

    List<Course> paginate(CoursePaginateFiler filer);

    Long paginateCount(CoursePaginateFiler filer);

    List<Course> openCoursesAndShow(Integer limit);
}




