package xyz.playedu.api.request.backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/23 16:12
 */
@Data
public class UserImportRequest {

    @NotNull(message = "请导入数据")
    private String[][] users;

    @NotNull(message = "起始行")
    @JsonProperty("start_line")
    private Integer startLine;

}
