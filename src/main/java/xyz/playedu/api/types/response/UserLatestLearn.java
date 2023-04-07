/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
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
