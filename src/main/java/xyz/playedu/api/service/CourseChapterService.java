package xyz.playedu.api.service;

import xyz.playedu.api.domain.CourseChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.playedu.api.exception.NotFoundException;

import java.util.List;

/**
 * @author tengteng
 * @description 针对表【course_chapters】的数据库操作Service
 * @createDate 2023-02-26 17:30:19
 */
public interface CourseChapterService extends IService<CourseChapter> {

    List<CourseChapter> getChaptersByCourseId(Integer courseId);

    void create(Integer courseId, String name, Integer sort);

    void update(CourseChapter chapter, String name, Integer sort);

    CourseChapter findOrFail(Integer id) throws NotFoundException;

    CourseChapter findOrFail(Integer id, Integer courseId) throws NotFoundException;

}
