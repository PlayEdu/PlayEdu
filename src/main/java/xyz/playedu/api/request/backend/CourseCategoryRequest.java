package xyz.playedu.api.request.backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/24 13:56
 */
@Data
public class CourseCategoryRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "name参数不存在")
    @NotBlank(message = "请输入分类名")
    @Length(min = 1, max = 20, message = "分类名长度在1-20个字符之间")
    private String name;

    @JsonProperty("parent_id")
    @NotNull(message = "parent_id参数不存在")
    private Integer parentId;

    @NotNull(message = "sort参数不存在")
    private Integer sort;

}
