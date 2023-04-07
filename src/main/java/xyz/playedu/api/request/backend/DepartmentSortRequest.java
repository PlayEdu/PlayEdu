/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.request.backend;

import lombok.Data;

import java.util.List;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/3/14 11:11
 */
@Data
public class DepartmentSortRequest {
    private List<Integer> ids;
}
