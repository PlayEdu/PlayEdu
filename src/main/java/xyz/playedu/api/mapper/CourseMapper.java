package xyz.playedu.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.playedu.api.domain.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author tengteng
* @description 针对表【courses】的数据库操作Mapper
* @createDate 2023-02-24 14:48:38
* @Entity xyz.playedu.api.domain.Course
*/
@Mapper
public interface CourseMapper extends BaseMapper<Course> {

}




