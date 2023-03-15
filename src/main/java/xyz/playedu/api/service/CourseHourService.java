package xyz.playedu.api.service;

import xyz.playedu.api.domain.CourseHour;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.playedu.api.exception.NotFoundException;

import java.util.List;

/**
* @author tengteng
* @description 针对表【course_hour】的数据库操作Service
* @createDate 2023-03-15 10:16:45
*/
public interface CourseHourService extends IService<CourseHour> {

    CourseHour findOrFail(Integer id, Integer courseId) throws NotFoundException;

    void update(CourseHour courseHour, Integer chapterId, Integer sort, String title, Integer duration);

    List<CourseHour> getHoursByCourseId(Integer courseId);

    CourseHour create(Integer courseId, Integer chapterId, Integer sort, String title, String type, Integer rid, Integer duration);

    Integer getCourseClassHourByCourseId(Integer courseId);

    void remove(Integer courseId, Integer chapterId);
}
