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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/2/23 10:52
 */
@Data
public class ResourceRequest {

    @NotNull(message = "category_id参数不存在")
    @JsonProperty("category_id")
    private Integer categoryId;

    @NotBlank(message = "请输入资源名")
    @Length(min = 1, max = 254, message = "资源名长度在1-254个字符之间")
    private String name;

    @NotBlank(message = "请输入资源扩展")
    @Length(min = 1, max = 254, message = "资源扩展长度在1-20个字符之间")
    private String extension;

    @NotNull(message = "size参数不存在")
    private Long size;

    @NotBlank(message = "disk值不能为空")
    private String disk;

    @NotNull(message = "file_id参数不存在")
    @JsonProperty("file_id")
    private String fileId;

    @NotBlank(message = "path值不能为空")
    private String path;

    @NotNull(message = "url参数不存在")
    private String url;

    private Integer duration;

    private String poster;

    private Integer parentId;
}
