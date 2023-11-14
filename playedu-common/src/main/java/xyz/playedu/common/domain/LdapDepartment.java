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

@TableName(value = "ldap_department")
public class LdapDepartment implements Serializable {
    /** */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 唯一特征值 */
    private String uuid;

    /** 部门ID */
    @JsonProperty("department_id")
    private Integer departmentId;

    /** dn */
    private String dn;

    /** */
    @JsonProperty("created_at")
    private Date createdAt;

    /** */
    @JsonProperty("updated_at")
    private Date updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** */
    public Integer getId() {
        return id;
    }

    /** */
    public void setId(Integer id) {
        this.id = id;
    }

    /** 唯一特征值 */
    public String getUuid() {
        return uuid;
    }

    /** 唯一特征值 */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /** 部门ID */
    public Integer getDepartmentId() {
        return departmentId;
    }

    /** 部门ID */
    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    /** dn */
    public String getDn() {
        return dn;
    }

    /** dn */
    public void setDn(String dn) {
        this.dn = dn;
    }

    /** */
    public Date getCreatedAt() {
        return createdAt;
    }

    /** */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /** */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /** */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        LdapDepartment other = (LdapDepartment) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getUuid() == null
                        ? other.getUuid() == null
                        : this.getUuid().equals(other.getUuid()))
                && (this.getDepartmentId() == null
                        ? other.getDepartmentId() == null
                        : this.getDepartmentId().equals(other.getDepartmentId()))
                && (this.getDn() == null
                        ? other.getDn() == null
                        : this.getDn().equals(other.getDn()))
                && (this.getCreatedAt() == null
                        ? other.getCreatedAt() == null
                        : this.getCreatedAt().equals(other.getCreatedAt()))
                && (this.getUpdatedAt() == null
                        ? other.getUpdatedAt() == null
                        : this.getUpdatedAt().equals(other.getUpdatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUuid() == null) ? 0 : getUuid().hashCode());
        result = prime * result + ((getDepartmentId() == null) ? 0 : getDepartmentId().hashCode());
        result = prime * result + ((getDn() == null) ? 0 : getDn().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", uuid=").append(uuid);
        sb.append(", departmentId=").append(departmentId);
        sb.append(", dn=").append(dn);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
