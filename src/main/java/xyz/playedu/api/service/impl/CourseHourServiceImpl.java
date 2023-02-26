package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.playedu.api.domain.CourseHour;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.service.CourseHourService;
import xyz.playedu.api.mapper.CourseHourMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author tengteng
 * @description 针对表【course_hour】的数据库操作Service实现
 * @createDate 2023-02-26 17:48:12
 */
@Service
public class CourseHourServiceImpl extends ServiceImpl<CourseHourMapper, CourseHour> implements CourseHourService {

    @Override
    public List<CourseHour> getHoursByCourseId(Integer courseId) {
        return list(query().getWrapper().eq("course_id", courseId).orderByAsc("published_at"));
    }

    @Override
    public CourseHour create(Integer courseId, Integer chapterId, String title, String type, Integer duration, Date publishedAt) {
        CourseHour courseHour = new CourseHour();
        courseHour.setCourseId(courseId);
        courseHour.setChapterId(chapterId);
        courseHour.setTitle(title);
        courseHour.setType(type);
        courseHour.setDuration(duration);
        courseHour.setPublishedAt(publishedAt);
        courseHour.setCreatedAt(new Date());
        courseHour.setUpdatedAt(new Date());

        save(courseHour);

        return courseHour;
    }

    @Override
    public void update(CourseHour courseHour, Integer chapterId, String title, Integer duration, Date publishedAt) {
        CourseHour newCourseHour = new CourseHour();
        newCourseHour.setId(courseHour.getId());
        newCourseHour.setChapterId(chapterId);
        newCourseHour.setTitle(title);
        newCourseHour.setDuration(duration);
        newCourseHour.setPublishedAt(publishedAt);
        newCourseHour.setCreatedAt(new Date());
        newCourseHour.setUpdatedAt(new Date());

        updateById(newCourseHour);
    }

    @Override
    public CourseHour findOrFail(Integer id) throws NotFoundException {
        CourseHour courseHour = getOne(query().getWrapper().eq("id", id));
        if (courseHour == null) {
            throw new NotFoundException("课时不存在");
        }
        return courseHour;
    }

    @Override
    public CourseHour findOrFail(Integer id, Integer courseId) throws NotFoundException {
        CourseHour courseHour = getOne(query().getWrapper().eq("id", id).eq("course_id", courseId));
        if (courseHour == null) {
            throw new NotFoundException("课时不存在");
        }
        return courseHour;
    }

    @Override
    public Integer getCourseClassHourByCourseId(Integer courseId) {
        return Math.toIntExact(count(query().getWrapper().eq("course_id", courseId)));
    }

    @Override
    public void resetChapterIdByCourseIdAndChapterId(Integer courseId, Integer chapterId) {
        update(update().getWrapper().eq("course_id", courseId).eq("chapter_id", chapterId).set("chapter_id", 0));
    }
}




