package xyz.playedu.api.request.backend;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/26 17:36
 */
@Data
public class CourseChapterRequest {

    @NotBlank(message = "请输入章节名")
    @NotNull(message = "name参数不存在")
    @Length(min = 1, max = 64, message = "章节名长度在1-64个字符之间")
    private String name;

    @NotNull(message = "sort参数不存在")
    private Integer sort;

}
