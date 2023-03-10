package xyz.playedu.api.types.paginate;

import lombok.Data;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/24 15:53
 */
@Data
public class CoursePaginateFiler {

    private String title;

    private String depIds;

    private String categoryIds;

    private String sortField;

    private String sortAlgo;

}
