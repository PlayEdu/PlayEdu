package xyz.playedu.api.request.backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "请输入邮箱")
    @NotNull(message = "email参数不存在")
    @Email(message = "请输入正确的邮箱")
    private String email;

    @NotNull(message = "name参数不存在")
    @NotBlank(message = "请输入姓名")
    @Length(min = 1, max = 20, message = "姓名长度在1-20个字符之间")
    private String name;

    @Length(min = 1, max = 20, message = "昵称长度在1-20个字符之间")
    private String nickname;

    @NotNull(message = "avatar参数不存在")
    @NotBlank(message = "请上传头像")
    private String avatar;

    @NotNull(message = "password参数不存在")
    @Length(min = 6, max = 16, message = "密码长度在6-16个字符之间")
    private String password;

    @JsonProperty("id_card")
    private String idCard;

    @JsonProperty("dep_ids")
    private Integer[] depIds;

}
