package xyz.playedu.api.request.backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Map;

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

    @NotNull(message = "is_required参数不存在")
    @JsonProperty("is_required")
    private Integer isRequired;

    @NotNull(message = "dep_ids参数不存在")
    @JsonProperty("dep_ids")
    private Integer[] depIds;

    @NotNull(message = "category_ids参数不存在")
    @JsonProperty("category_ids")
    private Integer[] categoryIds;

    @Data
    public static class HourItem {
        private String name;
        private String type;
        private Integer duration;
        private Integer rid;
    }

    @Data
    public static class ChapterItem {
        private String name;
        private List<HourItem> hours;
    }

    // 格式
    // [
    //   {
    //     'name' => '章节名',
    //     'hours' => [
    //       [
    //         'name' => '课时名',
    //         'type' => '课时类型',
    //         'duration' => '时长',
    //         'rid' => '资源id',
    //       ],...
    //     ],
    //   }...
    // ]
    @NotNull(message = "chapters参数不存在")
    private List<ChapterItem> chapters;

    // 格式
    // [
    //   {
    //     'name' => '课时名',
    //     'type' => '课时类型',
    //     'duration' => '时长',
    //     'rid' => '资源id',
    //   }...
    // ]
    @NotNull(message = "hours参数不存在")
    private List<HourItem> hours;
}
