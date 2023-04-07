/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.request.frontend;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/3/20 17:12
 */
@Data
public class CourseHourRecordRequest {
    @NotNull(message = "duration参数不存在")
    private Integer duration;
}
