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

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import xyz.playedu.api.event.UserLearnCourseUpdateEvent;
import xyz.playedu.course.service.UserLearnDurationRecordService;
import xyz.playedu.course.service.UserLearnDurationStatsService;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/3/22 14:18
 */
@Component
@Slf4j
public class UserLearnCourseUpdateListener {

    @Autowired private UserLearnDurationRecordService userLearnDurationRecordService;

    @Autowired private UserLearnDurationStatsService userLearnDurationStatsService;

    @EventListener
    public void storeLearnDuration(UserLearnCourseUpdateEvent event) {
        // 观看时长统计
        userLearnDurationStatsService.storeOrUpdate(
                event.getUserId(), event.getStartAt(), event.getEndAt());
        // 观看记录
        userLearnDurationRecordService.store(
                event.getUserId(),
                event.getCourseId(),
                event.getHourId(),
                event.getStartAt(),
                event.getEndAt());
    }
}
