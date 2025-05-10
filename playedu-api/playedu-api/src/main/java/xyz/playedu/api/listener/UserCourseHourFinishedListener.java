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
import xyz.playedu.api.event.UserCourseHourFinishedEvent;
import xyz.playedu.course.service.CourseHourService;
import xyz.playedu.course.service.UserCourseHourRecordService;
import xyz.playedu.course.service.UserCourseRecordService;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/3/20 17:41
 */
@Component
public class UserCourseHourFinishedListener {

    @Autowired private UserCourseRecordService userCourseRecordService;

    @Autowired private UserCourseHourRecordService userCourseHourRecordService;

    @Autowired private CourseHourService hourService;

    @EventListener
    public void userCourseProgressUpdate(UserCourseHourFinishedEvent evt) {
        Integer hourCount = hourService.getCountByCourseId(evt.getCourseId());
        Integer finishedCount =
                userCourseHourRecordService.getFinishedHourCount(
                        evt.getUserId(), evt.getCourseId());
        userCourseRecordService.storeOrUpdate(
                evt.getUserId(), evt.getCourseId(), hourCount, finishedCount);
    }
}
