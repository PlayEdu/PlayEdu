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
 * @create 2023/3/13 10:41
 */
@Data
public class ResourceDestroyMultiRequest {

    private List<Integer> ids;
}
