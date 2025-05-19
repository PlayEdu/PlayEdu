/*
 * Copyright (C) 2023 杭州白书科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.playedu.common.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/** LDAP部门同步详情实体 */
@Data
@TableName("ldap_sync_department_detail")
public class LdapSyncDepartmentDetail implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("record_id")
    private Integer recordId;

    @TableField("department_id")
    private Integer departmentId;

    @TableField("uuid")
    private String uuid;

    @TableField("dn")
    private String dn;

    @TableField("name")
    private String name;

    @TableField("action")
    private Integer action; // 1-新增，2-更新，3-删除，4-无变化

    @TableField("created_at")
    private Date createdAt;

    private static final long serialVersionUID = 1L;
}
