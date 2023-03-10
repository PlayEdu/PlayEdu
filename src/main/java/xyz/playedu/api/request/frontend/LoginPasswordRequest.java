package xyz.playedu.api.request.frontend;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/10 13:13
 */
@Data
public class LoginPasswordRequest {

    @NotBlank(message = "请输入邮箱")
    private String email;

    @NotBlank(message = "请输入密码")
    private String password;

    @NotBlank(message = "请输入验证码")
    @JsonProperty("captcha_key")
    private String captchaKey;

    @NotBlank(message = "请输入验证码")
    @JsonProperty("captcha_val")
    private String captchaVal;

}
