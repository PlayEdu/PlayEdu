package xyz.playedu.api.caches;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.playedu.api.domain.Course;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.service.CourseService;
import xyz.playedu.api.util.RedisUtil;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/20 17:57
 */
@Component
public class CourseCache {

    @Autowired
    private CourseService courseService;

    private final static String keyTemplate = "course:%d";

    private final static int expire = 3600;//s

    public Course findOrFail(Integer id) throws NotFoundException {
        String keyName = key(id);
        if (RedisUtil.exists(keyName)) {
            return (Course) RedisUtil.get(keyName);
        }
        Course course = courseService.findOrFail(id);
        put(course);
        return course;
    }

    public void put(Course course) {
        RedisUtil.set(key(course.getId()), course, expire);
    }

    private String key(Integer courseId) {
        return String.format(keyTemplate, courseId);
    }

}
