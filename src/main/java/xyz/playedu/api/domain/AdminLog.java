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
package xyz.playedu.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName admin_logs
 */
@TableName(value = "admin_logs")
@Data
public class AdminLog implements Serializable {
    /** */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 管理员ID */
    @JsonProperty("admin_id")
    private Integer adminId;

    /** 模块 */
    private String module;

    /** 操作指令 */
    private String opt;

    /** 备注 */
    private String remark;

    /** ip */
    private String ip;

    /** 地址 */
    @JsonProperty("ip_area")
    private String ipArea;

    @JsonProperty("created_at")
    private Date createdAt;

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
        AdminLog other = (AdminLog) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getAdminId() == null
                        ? other.getAdminId() == null
                        : this.getAdminId().equals(other.getAdminId()))
                && (this.getModule() == null
                        ? other.getModule() == null
                        : this.getModule().equals(other.getModule()))
                && (this.getOpt() == null
                        ? other.getOpt() == null
                        : this.getOpt().equals(other.getOpt()))
                && (this.getRemark() == null
                        ? other.getRemark() == null
                        : this.getRemark().equals(other.getRemark()))
                && (this.getIp() == null
                        ? other.getIp() == null
                        : this.getIp().equals(other.getIp()))
                && (this.getIpArea() == null
                        ? other.getIpArea() == null
                        : this.getIpArea().equals(other.getIpArea()))
                && (this.getCreatedAt() == null
                        ? other.getCreatedAt() == null
                        : this.getCreatedAt().equals(other.getCreatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getAdminId() == null) ? 0 : getAdminId().hashCode());
        result = prime * result + ((getModule() == null) ? 0 : getModule().hashCode());
        result = prime * result + ((getOpt() == null) ? 0 : getOpt().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getIp() == null) ? 0 : getIp().hashCode());
        result = prime * result + ((getIpArea() == null) ? 0 : getIpArea().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", adminId=").append(adminId);
        sb.append(", module=").append(module);
        sb.append(", opt=").append(opt);
        sb.append(", remark=").append(remark);
        sb.append(", ip=").append(ip);
        sb.append(", ipArea=").append(ipArea);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
