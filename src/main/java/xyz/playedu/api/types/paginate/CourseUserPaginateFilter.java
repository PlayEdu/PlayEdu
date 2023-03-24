package xyz.playedu.api.types.paginate;

import lombok.Data;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/24 16:10
 */
@Data
public class CourseUserPaginateFilter {
    private Integer courseId;
    private String email;
    private String name;
    private String idCard;
    private String sortField;
    private String sortAlgo;
    private Integer pageStart;
    private Integer pageSize;
}
