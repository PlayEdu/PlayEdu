/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.bus;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import xyz.playedu.api.FCtx;
import xyz.playedu.api.caches.UserLastLearnTimeCache;
import xyz.playedu.api.domain.Course;
import xyz.playedu.api.domain.CourseHour;
import xyz.playedu.api.domain.User;
import xyz.playedu.api.event.UserLearnCourseUpdateEvent;
import xyz.playedu.api.service.CourseService;
import xyz.playedu.api.service.UserService;

import java.util.List;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/3/20 14:56
 */
@Component
public class UserBus {

    @Autowired private CourseService courseService;

    @Autowired private UserService userService;

    @Autowired private UserLastLearnTimeCache userLastLearnTimeCache;

    @Autowired private ApplicationContext ctx;

    public boolean canSeeCourse(User user, Course course) {
        List<Integer> courseDepIds = courseService.getDepIdsByCourseId(course.getId());
        if (courseDepIds == null || courseDepIds.size() == 0) {
            // 线上课无所属部门=>任何学员都可以学习
            return true;
        }
        List<Integer> userDepIds = userService.getDepIdsByUserId(user.getId());
        if (userDepIds == null || userDepIds.size() == 0) {
            return false;
        }
        return CollectionUtils.intersection(courseDepIds, userDepIds).size() > 0;
    }

    public void userLearnDurationRecord(User user, Course course, CourseHour hour) {
        Long curTime = System.currentTimeMillis();

        // 最近一次学习时间
        Long lastTime = userLastLearnTimeCache.get(FCtx.getId());
        // 最大周期为10s+0.5s的网络延迟
        if (lastTime == null || curTime - lastTime > 10500) {
            lastTime = curTime - 10000;
        }

        userLastLearnTimeCache.put(user.getId(), curTime);

        ctx.publishEvent(
                new UserLearnCourseUpdateEvent(
                        this, user.getId(), course.getId(), hour.getId(), lastTime, curTime));
    }
}
