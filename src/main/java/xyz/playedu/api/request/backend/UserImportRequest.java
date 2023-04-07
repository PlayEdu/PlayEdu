/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.request.backend;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

import java.util.List;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/2/23 16:12
 */
@Data
public class UserImportRequest {

    @Data
    public static class UserItem {
        private String email;
        private String name;
        private String password;

        @JsonProperty("id_card")
        private String idCard;

        @JsonProperty("dep_ids")
        private String depIds;
    }

    @NotNull(message = "请导入数据")
    private List<UserItem> users;

    @NotNull(message = "起始行")
    @JsonProperty("start_line")
    private Integer startLine;
}
