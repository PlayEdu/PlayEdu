package xyz.playedu.api.request.backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Author 杭州白书科技有限公司
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
