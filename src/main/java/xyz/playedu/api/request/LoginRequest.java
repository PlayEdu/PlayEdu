package xyz.playedu.api.request;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class LoginRequest  implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "请输入邮箱")
    public String email;

    @NotNull(message = "请输入密码")
    public String password;

    @NotNull(message = "请输入图形验证码")
    public String captchaValue;

    @NotNull(message = "captchaKey参数为空")
    public String captchaKey;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCaptchaValue() {
        return captchaValue;
    }

    public void setCaptchaValue(String captchaValue) {
        this.captchaValue = captchaValue;
    }

    public String getCaptchaKey() {
        return captchaKey;
    }

    public void setCaptchaKey(String captchaKey) {
        this.captchaKey = captchaKey;
    }
}
