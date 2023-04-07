/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.request.backend;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

import java.util.List;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/3/24 16:22
 */
@Data
public class CourseUserDestroyRequest {
    @NotNull(message = "ids参数不存在")
    private List<Integer> ids;
}
