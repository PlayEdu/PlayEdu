/*
 * Copyright 2023 杭州白书科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
