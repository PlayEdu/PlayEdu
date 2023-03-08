package xyz.playedu.api.request.backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/8 14:49
 */
@Data
public class UploadVideoMergeRequest {

    @NotBlank(message = "请输入课程标题")
    private String filename;

    @JsonProperty("upload_id")
    @NotBlank(message = "请输入upload_id")
    private String uploadId;

    @JsonProperty("original_filename")
    @NotBlank(message = "请输入original_filename")
    private String originalFilename;

    @NotNull(message = "请输入size")
    private Long size;

    @NotNull(message = "请输入duration")
    private Integer duration;

    @NotBlank(message = "请输入extension")
    private String extension;

    @JsonProperty("category_ids")
    private String categoryIds;

    @NotNull(message = "请上传视频封面")
    private String poster;

}
