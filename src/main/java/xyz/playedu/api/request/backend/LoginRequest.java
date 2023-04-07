/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.request.backend;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

import xyz.playedu.api.request.backend.types.ImageCaptchaRequestInterface;

import java.io.Serial;
import java.io.Serializable;

@Data
public class LoginRequest implements Serializable, ImageCaptchaRequestInterface {

    @Serial private static final long serialVersionUID = 1L;

    @NotNull(message = "请输入邮箱")
    public String email;

    @NotNull(message = "请输入密码")
    public String password;

    @NotNull(message = "请输入图形验证码")
    @JsonProperty("captcha_value")
    public String captchaValue;

    @NotNull(message = "captchaKey参数为空")
    @JsonProperty("captcha_key")
    public String captchaKey;
}
