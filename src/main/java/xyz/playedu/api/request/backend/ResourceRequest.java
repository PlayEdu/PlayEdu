package xyz.playedu.api.request.backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/23 10:52
 */
@Data
public class ResourceRequest {

    @NotNull(message = "请选择资源分类")
    @JsonProperty("category_id")
    private Integer categoryId;

    @NotNull(message = "资源名不能为空")
    @Length(min = 1, max = 254, message = "资源名长度在1-254个字符之间")
    private String name;

    @NotNull(message = "请输入资源扩展")
    @Length(min = 1, max = 254, message = "资源扩展长度在1-20个字符之间")
    private String extension;

    @NotNull(message = "请输入文件大小")
    private Long size;

    @NotNull(message = "请输入文件存储磁盘")
    private String disk;

    @NotNull(message = "请输入fileId")
    @JsonProperty("file_id")
    private String fileId;

    @NotNull(message = "请输入存储路径")
    private String path;

    @NotNull(message = "请输入访问URL")
    private String url;

}
