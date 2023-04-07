/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.io.Serializable;

/**
 * @TableName admin_role_permission
 */
@TableName(value = "admin_role_permission")
@Data
public class AdminRolePermission implements Serializable {
    @JsonProperty("role_id")
    private Integer roleId;

    @JsonProperty("perm_id")
    private Integer permId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

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
        AdminRolePermission other = (AdminRolePermission) that;
        return (this.getRoleId() == null
                        ? other.getRoleId() == null
                        : this.getRoleId().equals(other.getRoleId()))
                && (this.getPermId() == null
                        ? other.getPermId() == null
                        : this.getPermId().equals(other.getPermId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRoleId() == null) ? 0 : getRoleId().hashCode());
        result = prime * result + ((getPermId() == null) ? 0 : getPermId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", roleId=").append(roleId);
        sb.append(", permId=").append(permId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
