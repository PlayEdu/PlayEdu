package xyz.playedu.api.request.backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/23 13:56
 */
@Data
public class UserRequest {

    @NotNull(message = "请输入邮箱")
    @Email(message = "请输入合法邮箱")
    private String email;

    @NotNull(message = "请输入昵称")
    private String nickname;

    @Length(min = 1, max = 20, message = "姓名长度在1-20个字符之间")
    private String name;

    @NotNull(message = "请上传头像")
    private String avatar;

    @NotNull(message = "请输入密码")
    private String password;

    @JsonProperty("id_card")
    private String idCard;

    private Integer credit1;

    @JsonProperty("is_active")
    private Integer isActive;

    @JsonProperty("is_lock")
    private Integer isLock;

    @JsonProperty("is_verify")
    private Integer isVerify;

    @JsonProperty("verify_at")
    private Date verifyAt;

    @JsonProperty("is_set_password")
    private Integer isSetPassword;

    @JsonProperty("dep_ids")
    private Integer[] depIds;

}
