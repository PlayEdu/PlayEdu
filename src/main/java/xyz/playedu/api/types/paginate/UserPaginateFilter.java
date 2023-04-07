/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.types.paginate;

import lombok.Data;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/2/23 14:23
 */
@Data
public class UserPaginateFilter {

    private String name;
    private String email;
    private String idCard;
    private Integer isActive;
    private Integer isLock;
    private Integer isVerify;
    private Integer isSetPassword;

    // 创建时间范围过滤
    private String[] createdAt;

    private String depIds;

    // 排序控制
    private String sortField;
    private String sortAlgo;

    private Integer pageStart;
    private Integer pageSize;
}
