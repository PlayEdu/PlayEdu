package xyz.playedu.api.request.backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/19 09:43
 */
@Data
public class AdminUserRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "请输入管理员姓名")
    @Length(min = 1, max = 12, message = "管理员姓名长度在1-12个字符之间")
    private String name;

    @NotNull(message = "请输入管理员邮箱")
    @Email(message = "请输入合法邮箱")
    private String email;

    private String password;

    @JsonProperty("is_ban_login")
    @NotNull(message = "is_ban_login参数不存在")
    private Integer isBanLogin;

    @JsonProperty("role_ids")
    @NotNull(message = "role_ids参数不存在")
    private Integer[] roleIds;
}
