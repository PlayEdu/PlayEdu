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
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import xyz.playedu.common.bus.BackendBus;
import xyz.playedu.common.constant.BPermissionConstant;
import xyz.playedu.common.constant.BackendConstant;

/**
 * @TableName admin_users
 */
@TableName(value = "admin_users")
@Data
@Slf4j
public class AdminUser implements Serializable {
    /** */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 姓名 */
    private String name;

    /** 邮箱 */
    private String email;

    /** 密码 */
    @JsonIgnore private String password;

    /** Salt */
    @JsonIgnore private String salt;

    /** 登录IP */
    @JsonProperty("login_ip")
    private String loginIp;

    /** 登录时间 */
    @JsonProperty("login_at")
    private Date loginAt;

    /** 1禁止登录,0否 */
    @JsonProperty("is_ban_login")
    private Integer isBanLogin;

    /** 登录次数 */
    @JsonProperty("login_times")
    private Integer loginTimes;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("updated_at")
    private Date updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @JsonGetter("email")
    public String transformEmail() {
        return BackendBus.valueHidden(
                BPermissionConstant.DATA_ADMIN_EMAIL,
                BackendConstant.PRIVACY_FIELD_TYPE_EMAIL,
                email);
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
        AdminUser other = (AdminUser) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getName() == null
                        ? other.getName() == null
                        : this.getName().equals(other.getName()))
                && (this.getEmail() == null
                        ? other.getEmail() == null
                        : this.getEmail().equals(other.getEmail()))
                && (this.getPassword() == null
                        ? other.getPassword() == null
                        : this.getPassword().equals(other.getPassword()))
                && (this.getSalt() == null
                        ? other.getSalt() == null
                        : this.getSalt().equals(other.getSalt()))
                && (this.getLoginIp() == null
                        ? other.getLoginIp() == null
                        : this.getLoginIp().equals(other.getLoginIp()))
                && (this.getLoginAt() == null
                        ? other.getLoginAt() == null
                        : this.getLoginAt().equals(other.getLoginAt()))
                && (this.getIsBanLogin() == null
                        ? other.getIsBanLogin() == null
                        : this.getIsBanLogin().equals(other.getIsBanLogin()))
                && (this.getLoginTimes() == null
                        ? other.getLoginTimes() == null
                        : this.getLoginTimes().equals(other.getLoginTimes()))
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
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        result = prime * result + ((getSalt() == null) ? 0 : getSalt().hashCode());
        result = prime * result + ((getLoginIp() == null) ? 0 : getLoginIp().hashCode());
        result = prime * result + ((getLoginAt() == null) ? 0 : getLoginAt().hashCode());
        result = prime * result + ((getIsBanLogin() == null) ? 0 : getIsBanLogin().hashCode());
        result = prime * result + ((getLoginTimes() == null) ? 0 : getLoginTimes().hashCode());
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
        sb.append(", name=").append(name);
        sb.append(", email=").append(email);
        sb.append(", password=").append(password);
        sb.append(", salt=").append(salt);
        sb.append(", loginIp=").append(loginIp);
        sb.append(", loginAt=").append(loginAt);
        sb.append(", isBanLogin=").append(isBanLogin);
        sb.append(", loginTimes=").append(loginTimes);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
