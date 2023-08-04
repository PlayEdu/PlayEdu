/*
 * Copyright (C) 2023 杭州白书科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.playedu.course.bus;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.playedu.common.domain.User;
import xyz.playedu.common.service.UserService;
import xyz.playedu.course.domain.Course;
import xyz.playedu.course.service.CourseService;

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

    // 注意，调用该方法需要考虑到并发写入问题
 /*   public void userLearnDurationRecord(User user, Course course, CourseHour hour) {
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
    }*/
}
