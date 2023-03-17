package xyz.playedu.api.types.paginate;

import lombok.Data;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/23 11:18
 */
@Data
public class ResourcePaginateFilter {

    private String name;

    private String extension;

    private String disk;

    private String sortField;

    private String sortAlgo;

    private String categoryIds;

    private String type;

    private Integer adminId;

    private Integer pageStart;

    private Integer pageSize;

}
