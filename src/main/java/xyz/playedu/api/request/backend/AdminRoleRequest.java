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
 * @create 2023/2/21 16:00
 */
@Data
public class AdminRoleRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "请输入管理角色名")
    @Length(min = 1, max = 12, message = "管理角色名长度在1-16个字符之间")
    private String name;

    @JsonProperty("permission_ids")
    @NotNull(message = "permission_ids参数不存在")
    private Integer[] permissionIds;

}
