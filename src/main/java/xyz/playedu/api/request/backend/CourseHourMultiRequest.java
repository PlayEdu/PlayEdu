package xyz.playedu.api.request.backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/21 16:33
 */
@Data
public class CourseHourMultiRequest {
    @Data
    public static class HourItem {
        @JsonProperty("chapter_id")
        private Integer chapterId;
        private String title;
        private Integer duration;
        private Integer sort;
        private String type;
        private Integer rid;
    }

    @NotNull(message = "hours参数不存在")
    private List<HourItem> hours;
}
