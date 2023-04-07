/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.types.mapper;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/3/29 10:01
 */
@Data
public class UserCourseHourRecordCountMapper {
    @JsonProperty("course_id")
    private Integer courseId;

    private Integer total;
}
