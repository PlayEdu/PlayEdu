package xyz.playedu.api.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import xyz.playedu.api.types.ImageCaptchaRequestInterface;

import java.io.Serializable;

@Data
public class LoginRequest implements Serializable, ImageCaptchaRequestInterface {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "请输入邮箱")
    public String email;

    @NotNull(message = "请输入密码")
    public String password;

    @NotNull(message = "请输入图形验证码")
    public String captchaValue;

    @NotNull(message = "captchaKey参数为空")
    public String captchaKey;
}
