package xyz.playedu.api.request.backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/23 10:52
 */
@Data
public class ResourceRequest {

    @NotNull(message = "category_id参数不存在")
    @JsonProperty("category_id")
    private Integer categoryId;

    @NotNull(message = "name参数不存在")
    @NotBlank(message = "请输入资源名")
    @Length(min = 1, max = 254, message = "资源名长度在1-254个字符之间")
    private String name;

    @NotNull(message = "extension参数不存在")
    @NotBlank(message = "请输入资源扩展")
    @Length(min = 1, max = 254, message = "资源扩展长度在1-20个字符之间")
    private String extension;

    @NotNull(message = "size参数不存在")
    private Long size;

    @NotNull(message = "disk参数不存在")
    @NotBlank(message = "disk值不能为空")
    private String disk;

    @NotNull(message = "file_id参数不存在")
    @JsonProperty("file_id")
    private String fileId;

    @NotNull(message = "path参数不存在")
    @NotBlank(message = "path值不能为空")
    private String path;

    @NotNull(message = "url参数不存在")
    private String url;

    private Integer duration;

}
