package xyz.playedu.api.request.backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

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

    @NotNull(message = "dep_ids参数不存在")
    @JsonProperty("dep_ids")
    private Integer[] depIds;

    @NotNull(message = "category_ids参数不存在")
    @JsonProperty("category_ids")
    private Integer[] categoryIds;

    // 格式
    // [
    //   '章节名' => [
    //     [
    //       'name' => '课时名',
    //       'type' => '课时类型', // 可选值[VIDEO]
    //       'duration' => 时长, // 单位[秒]
    //       'rid' => 资源ID, // 如果是type=VIDEO的话则对应视频的id
    //     ]...
    //   ]...
    // ]
    @NotNull(message = "chapters参数不存在")
    private Map<String, Map<String, Object>[]> chapters;

    // 格式
    // [
    //   [
    //     'name' => '课时名',
    //     'type' => '课时类型',
    //     'duration' => '时长',
    //     'rid' => '资源id',
    //   ]...
    // ]
    @NotNull(message = "hours参数不存在")
    private Map<String, Object>[] hours;
}
