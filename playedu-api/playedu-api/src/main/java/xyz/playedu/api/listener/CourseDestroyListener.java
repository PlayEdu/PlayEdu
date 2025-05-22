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
package xyz.playedu.api.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import xyz.playedu.api.event.CourseDestroyEvent;
import xyz.playedu.course.service.CourseAttachmentService;
import xyz.playedu.course.service.CourseCategoryService;
import xyz.playedu.course.service.CourseDepartmentUserService;
import xyz.playedu.course.service.UserCourseHourRecordService;
import xyz.playedu.course.service.UserCourseRecordService;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/2/24 17:19
 */
@Component
public class CourseDestroyListener {

    @Autowired private CourseDepartmentUserService courseDepartmentUserService;

    @Autowired private CourseCategoryService courseCategoryService;

    @Autowired private UserCourseRecordService userCourseRecordService;

    @Autowired private UserCourseHourRecordService userCourseHourRecordService;

    @Autowired private CourseAttachmentService courseAttachmentService;

    @EventListener
    public void departmentRelateRemove(CourseDestroyEvent event) {
        courseDepartmentUserService.removeByCourseId(event.getCourseId());
    }

    @EventListener
    public void categoryRelateRemove(CourseDestroyEvent event) {
        courseCategoryService.removeByCourseId(event.getCourseId());
    }

    @EventListener
    public void attachmentRelateRemove(CourseDestroyEvent event) {
        courseAttachmentService.remove(event.getCourseId());
    }

    @EventListener
    public void removeUserRecords(CourseDestroyEvent event) {
        userCourseRecordService.removeByCourseId(event.getCourseId());
        userCourseHourRecordService.removeByCourseId(event.getCourseId());
    }
}
