/*
 * Copyright (C) 2023 杭州白书科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.playedu.api.request.backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/2/19 09:43
 */
@Data
public class AdminUserRequest implements Serializable {

    @Serial private static final long serialVersionUID = 1L;

    @NotBlank(message = "请输入管理员姓名")
    @Length(min = 1, max = 12, message = "管理员姓名长度在1-12个字符之间")
    private String name;

    @NotBlank(message = "请输入管理员邮箱")
    @Email(message = "请输入合法邮箱")
    private String email;

    @NotNull(message = "password参数不存在")
    private String password;

    @JsonProperty("is_ban_login")
    @NotNull(message = "is_ban_login参数不存在")
    private Integer isBanLogin;

    @JsonProperty("role_ids")
    @NotNull(message = "role_ids参数不存在")
    private Integer[] roleIds;
}
