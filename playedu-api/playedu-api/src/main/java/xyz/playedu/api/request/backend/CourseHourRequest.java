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

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/2/26 18:00
 */
@Data
public class CourseHourRequest {

    @NotNull(message = "chapter_id参数不存在")
    @JsonProperty("chapter_id")
    private Integer chapterId;

    @NotBlank(message = "请输入课时标题")
    private String title;

    @NotNull(message = "duration参数不存在")
    private Integer duration;

    @NotNull(message = "sort参数不存在")
    private Integer sort;

    @NotBlank(message = "请选择课时类型")
    private String type;

    @NotNull(message = "rid参数不存在")
    private Integer rid;
}
