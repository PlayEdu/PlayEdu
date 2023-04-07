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
 * @create 2023/3/14 11:02
 */
@Data
public class ResourceCategorySortRequest {

    @NotNull(message = "参数为空")
    private List<Integer> ids;
}
