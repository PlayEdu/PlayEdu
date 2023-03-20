package xyz.playedu.api.bus;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.playedu.api.domain.Course;
import xyz.playedu.api.domain.CourseHour;
import xyz.playedu.api.domain.User;
import xyz.playedu.api.service.CourseService;
import xyz.playedu.api.service.UserService;

import java.util.List;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/20 14:56
 */
@Component
public class UserBus {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    public boolean canSeeCourse(User user, Course course) {
        List<Integer> courseDepIds = courseService.getDepIdsByCourseId(course.getId());
        if (courseDepIds == null || courseDepIds.size() == 0) {
            //线上课无所属部门=>公开课=>任何学员都可以学习
            return true;
        }
        List<Integer> userDepIds = userService.getDepIdsByUserId(user.getId());
        if (userDepIds == null || userDepIds.size() == 0) {
            return false;
        }
        return CollectionUtils.intersection(courseDepIds, userDepIds).size() > 0;
    }

}
