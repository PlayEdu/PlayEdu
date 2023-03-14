package xyz.playedu.api.request.backend;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/14 11:07
 */
@Data
public class ResourceCategoryParentRequest {
    @NotNull(message = "参数为空")
    private List<Integer> ids;

    @NotNull(message = "参数为空")
    private Integer id;

    @NotNull(message = "参数为空")
    private Integer parentId;
}
