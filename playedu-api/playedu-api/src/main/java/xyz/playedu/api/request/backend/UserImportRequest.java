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
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/2/23 16:12
 */
@Data
public class UserImportRequest {

    @Data
    public static class UserItem {
        private String deps;
        private String email;
        private String name;
        private String password;

        @JsonProperty("id_card")
        private String idCard;
    }

    @NotNull(message = "请导入数据")
    private List<UserItem> users;

    @NotNull(message = "起始行")
    @JsonProperty("start_line")
    private Integer startLine;
}
