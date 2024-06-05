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
package xyz.playedu.course.caches;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import xyz.playedu.common.exception.NotFoundException;
import xyz.playedu.common.util.RedisUtil;
import xyz.playedu.course.domain.Course;
import xyz.playedu.course.service.CourseService;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/3/20 17:57
 */
@Component
public class CourseCache {

    @Autowired private CourseService courseService;

    private static final String keyTemplate = "course:%d";

    private static final int expire = 3600; // s

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
