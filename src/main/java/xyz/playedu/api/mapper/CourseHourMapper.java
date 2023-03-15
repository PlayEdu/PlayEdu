package xyz.playedu.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.playedu.api.domain.CourseHour;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author tengteng
* @description 针对表【course_hour】的数据库操作Mapper
* @createDate 2023-03-15 10:16:45
* @Entity xyz.playedu.api.domain.CourseHour
*/
@Mapper
public interface CourseHourMapper extends BaseMapper<CourseHour> {

}




