package xyz.playedu.api.request.backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/2 15:41
 */
@Data
public class PasswordChangeRequest {

    @JsonProperty("old_password")
    @NotNull(message = "old_password参数不存在")
    @NotBlank(message = "请输入原密码")
    private String oldPassword;

    @JsonProperty("new_password")
    @NotNull(message = "new_password参数不存在")
    @NotBlank(message = "请输入新密码")
    private String newPassword;

}
