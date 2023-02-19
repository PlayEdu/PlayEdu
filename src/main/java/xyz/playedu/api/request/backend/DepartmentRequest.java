package xyz.playedu.api.request.backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/19 10:42
 */
@Data
public class DepartmentRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "请输入部门名称")
    @Length(min = 1, max = 20, message = "部门名称长度在1-20个字符之间")
    private String name;

    @JsonProperty("parent_id")
    @NotNull(message = "请选择上级部门")
    private Integer parentId;

    @NotNull(message = "请输入排序值")
    private Integer sort;

}
