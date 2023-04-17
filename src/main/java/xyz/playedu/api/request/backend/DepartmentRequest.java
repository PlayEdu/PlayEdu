/*
 * Copyright 2023 杭州白书科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.playedu.api.request.backend;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/2/19 10:42
 */
@Data
public class DepartmentRequest implements Serializable {
    @Serial private static final long serialVersionUID = 1L;

    @NotBlank(message = "请输入部门名称")
    @Length(min = 1, max = 20, message = "部门名称长度在1-20个字符之间")
    private String name;

    @JsonProperty("parent_id")
    @NotNull(message = "parent_id参数不存在")
    private Integer parentId;

    @NotNull(message = "sort参数不存在")
    private Integer sort;
}
