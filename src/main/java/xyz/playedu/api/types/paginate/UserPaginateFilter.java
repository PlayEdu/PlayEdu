package xyz.playedu.api.types.paginate;

import lombok.Data;

import java.util.Date;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/23 14:23
 */
@Data
public class UserPaginateFilter {

    private String nickname;

    private String name;

    private String email;

    private String idCard;

    private Integer isActive;
    private Integer isLock;
    private Integer isVerify;
    private Integer isSetPassword;

    // 创建时间范围过滤
    private Date[] createdAt;

    private Integer[] depIds;

    // 排序控制
    private String sortField;
    private String sortAlgo;

}
