package xyz.playedu.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.playedu.api.domain.CourseChapter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author tengteng
* @description 针对表【course_chapters】的数据库操作Mapper
* @createDate 2023-02-26 17:34:01
* @Entity xyz.playedu.api.domain.CourseChapter
*/
@Mapper
public interface CourseChapterMapper extends BaseMapper<CourseChapter> {

}




