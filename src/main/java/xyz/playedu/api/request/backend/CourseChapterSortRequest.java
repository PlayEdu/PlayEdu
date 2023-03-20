package xyz.playedu.api.request.backend;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/20 10:42
 */
@Data
public class CourseChapterSortRequest {
    @NotNull(message = "ids参数不存在")
    private List<Integer> ids;
}
