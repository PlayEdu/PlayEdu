package xyz.playedu.api.service;

import xyz.playedu.api.domain.CourseHour;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.playedu.api.exception.NotFoundException;

import java.util.Date;
import java.util.List;

/**
 * @author tengteng
 * @description 针对表【course_hour】的数据库操作Service
 * @createDate 2023-02-26 17:48:12
 */
public interface CourseHourService extends IService<CourseHour> {

    List<CourseHour> getHoursByCourseId(Integer courseId);

    CourseHour create(Integer courseId, Integer chapterId, String title, String type, Integer duration, Date publishedAt);

    void update(CourseHour courseHour, Integer chapterId, String title, Integer duration, Date publishedAt);

    CourseHour findOrFail(Integer id) throws NotFoundException;

    CourseHour findOrFail(Integer id, Integer courseId) throws NotFoundException;

    Integer getCourseClassHourByCourseId(Integer courseId);

    void resetChapterIdByCourseIdAndChapterId(Integer courseId,Integer chapterId);

}
