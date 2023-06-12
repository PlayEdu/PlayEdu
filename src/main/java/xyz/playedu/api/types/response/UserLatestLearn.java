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
package xyz.playedu.api.types.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import xyz.playedu.api.domain.Course;
import xyz.playedu.api.domain.CourseHour;
import xyz.playedu.api.domain.UserCourseHourRecord;
import xyz.playedu.api.domain.UserCourseRecord;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/3/27 15:29
 */
@Data
public class UserLatestLearn {
    @JsonProperty("course")
    private Course course;

    @JsonProperty("record")
    private UserCourseRecord userCourseRecord;

    @JsonProperty("last_learn_hour")
    private CourseHour lastLearnHour;

    @JsonProperty("hour_record")
    private UserCourseHourRecord hourRecord;
}
