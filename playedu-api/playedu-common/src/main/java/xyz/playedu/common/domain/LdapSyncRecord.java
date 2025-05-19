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
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
@TableName(value = "ldap_sync_record")
public class LdapSyncRecord implements Serializable {
    /** */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 执行同步的管理员ID，0表示系统自动执行 */
    @JsonProperty("admin_id")
    private Integer adminId;

    /** 状态：0-进行中，1-成功，2-失败 */
    private Integer status;

    /** S3存储中的文件路径 */
    @JsonProperty("s3_file_path")
    private String s3FilePath;

    /** 总部门数量 */
    @JsonProperty("total_department_count")
    private Integer totalDepartmentCount;

    /** 新增部门数量 */
    @JsonProperty("created_department_count")
    private Integer createdDepartmentCount;

    /** 更新部门数量 */
    @JsonProperty("updated_department_count")
    private Integer updatedDepartmentCount;

    /** 删除部门数量 */
    @JsonProperty("deleted_department_count")
    private Integer deletedDepartmentCount;

    /** 总用户数量 */
    @JsonProperty("total_user_count")
    private Integer totalUserCount;

    /** 新增用户数量 */
    @JsonProperty("created_user_count")
    private Integer createdUserCount;

    /** 更新用户数量 */
    @JsonProperty("updated_user_count")
    private Integer updatedUserCount;

    /** 删除用户数量 */
    @JsonProperty("deleted_user_count")
    private Integer deletedUserCount;

    /** 被禁止的用户数量 */
    @JsonProperty("banned_user_count")
    private Integer bannedUserCount;

    /** 错误信息 */
    @JsonProperty("error_message")
    private String errorMessage;

    /** */
    @JsonProperty("created_at")
    private Date createdAt;

    /** */
    @JsonProperty("updated_at")
    private Date updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
