/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.request.backend;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

import org.hibernate.validator.constraints.Length;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/2/23 13:56
 */
@Data
public class UserRequest {

    @NotBlank(message = "请输入邮箱")
    @Email(message = "请输入正确的邮箱")
    private String email;

    @NotBlank(message = "请输入姓名")
    @Length(min = 1, max = 20, message = "姓名长度在1-20个字符之间")
    private String name;

    @NotBlank(message = "请上传头像")
    private String avatar;

    @NotNull(message = "password参数不存在")
    private String password;

    @JsonProperty("id_card")
    private String idCard;

    @JsonProperty("dep_ids")
    private Integer[] depIds;
}
