package xyz.playedu.api.request.backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/24 14:38
 */
@Data
public class CourseRequest {

    @NotNull(message = "title参数不存在")
    @NotBlank(message = "请输入课程标题")
    private String title;

    @NotNull(message = "thumb参数不存在")
    @NotBlank(message = "请上传课程封面")
    private String thumb;

    @NotNull(message = "is_show参数不存在")
    @JsonProperty("is_show")
    private Integer isShow;

    @NotNull(message = "dep_ids参数不存在")
    @JsonProperty("dep_ids")
    private Integer[] depIds;

    @NotNull(message = "category_ids参数不存在")
    @JsonProperty("category_ids")
    private Integer[] categoryIds;
}
