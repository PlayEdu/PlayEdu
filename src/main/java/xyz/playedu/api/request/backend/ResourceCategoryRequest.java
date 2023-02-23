package xyz.playedu.api.request.backend;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/23 09:56
 */
@Data
public class ResourceCategoryRequest {

    @NotNull(message = "资源类型不能为空")
    private String type;

    @NotNull(message = "分类名不能为空")
    @Length(min = 1, max = 12, message = "分类名长度在1-12个字符之间")
    private String name;

    @NotNull(message = "请输入排序值")
    private Integer sort;

}
