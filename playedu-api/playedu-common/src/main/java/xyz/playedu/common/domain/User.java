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

import lombok.Data;

import xyz.playedu.common.bus.BackendBus;
import xyz.playedu.common.constant.BPermissionConstant;
import xyz.playedu.common.constant.BackendConstant;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName users
 */
@TableName(value = "users")
@Data
public class User implements Serializable {
    /** */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 邮件 */
    private String email;

    /** 真实姓名 */
    private String name;

    /** 头像 */
    private String avatar;

    /** 密码 */
    @JsonIgnore private String password;

    /** salt */
    @JsonIgnore private String salt;

    /** 身份证号 */
    @JsonProperty("id_card")
    private String idCard;

    /** 学分 */
    private Integer credit1;

    /** 注册Ip */
    @JsonProperty("create_ip")
    private String createIp;

    /** 注册城市 */
    @JsonProperty("create_city")
    private String createCity;

    /** 激活[1:是,0:否] */
    @JsonProperty("is_active")
    private Integer isActive;

    /** 锁定[1:是,0:否] */
    @JsonProperty("is_lock")
    private Integer isLock;

    /** 实名认证[1:是,0:否] */
    @JsonProperty("is_verify")
    private Integer isVerify;

    /** 实名认证时间 */
    @JsonProperty("verify_at")
    private Date verifyAt;

    /** 设置密码[1:是,0:否] */
    @JsonProperty("is_set_password")
    private Integer isSetPassword;

    /** 登录时间 */
    @JsonProperty("login_at")
    private Date loginAt;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("updated_at")
    private Date updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @JsonGetter("name")
    public String transformName() {
        return BackendBus.valueHidden(
                BPermissionConstant.DATA_USER_NAME,
                BackendConstant.PRIVACY_FIELD_TYPE_NAME,
                getName());
    }

    @JsonGetter("email")
    public String transformEmail() {
        return BackendBus.valueHidden(
                BPermissionConstant.DATA_USER_EMAIL,
                BackendConstant.PRIVACY_FIELD_TYPE_EMAIL,
                getEmail());
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
        User other = (User) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getEmail() == null
                        ? other.getEmail() == null
                        : this.getEmail().equals(other.getEmail()))
                && (this.getName() == null
                        ? other.getName() == null
                        : this.getName().equals(other.getName()))
                && (this.getAvatar() == null
                        ? other.getAvatar() == null
                        : this.getAvatar().equals(other.getAvatar()))
                && (this.getPassword() == null
                        ? other.getPassword() == null
                        : this.getPassword().equals(other.getPassword()))
                && (this.getSalt() == null
                        ? other.getSalt() == null
                        : this.getSalt().equals(other.getSalt()))
                && (this.getIdCard() == null
                        ? other.getIdCard() == null
                        : this.getIdCard().equals(other.getIdCard()))
                && (this.getCredit1() == null
                        ? other.getCredit1() == null
                        : this.getCredit1().equals(other.getCredit1()))
                && (this.getCreateIp() == null
                        ? other.getCreateIp() == null
                        : this.getCreateIp().equals(other.getCreateIp()))
                && (this.getCreateCity() == null
                        ? other.getCreateCity() == null
                        : this.getCreateCity().equals(other.getCreateCity()))
                && (this.getIsActive() == null
                        ? other.getIsActive() == null
                        : this.getIsActive().equals(other.getIsActive()))
                && (this.getIsLock() == null
                        ? other.getIsLock() == null
                        : this.getIsLock().equals(other.getIsLock()))
                && (this.getIsVerify() == null
                        ? other.getIsVerify() == null
                        : this.getIsVerify().equals(other.getIsVerify()))
                && (this.getVerifyAt() == null
                        ? other.getVerifyAt() == null
                        : this.getVerifyAt().equals(other.getVerifyAt()))
                && (this.getIsSetPassword() == null
                        ? other.getIsSetPassword() == null
                        : this.getIsSetPassword().equals(other.getIsSetPassword()))
                && (this.getLoginAt() == null
                        ? other.getLoginAt() == null
                        : this.getLoginAt().equals(other.getLoginAt()))
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
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getAvatar() == null) ? 0 : getAvatar().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        result = prime * result + ((getSalt() == null) ? 0 : getSalt().hashCode());
        result = prime * result + ((getIdCard() == null) ? 0 : getIdCard().hashCode());
        result = prime * result + ((getCredit1() == null) ? 0 : getCredit1().hashCode());
        result = prime * result + ((getCreateIp() == null) ? 0 : getCreateIp().hashCode());
        result = prime * result + ((getCreateCity() == null) ? 0 : getCreateCity().hashCode());
        result = prime * result + ((getIsActive() == null) ? 0 : getIsActive().hashCode());
        result = prime * result + ((getIsLock() == null) ? 0 : getIsLock().hashCode());
        result = prime * result + ((getIsVerify() == null) ? 0 : getIsVerify().hashCode());
        result = prime * result + ((getVerifyAt() == null) ? 0 : getVerifyAt().hashCode());
        result =
                prime * result + ((getIsSetPassword() == null) ? 0 : getIsSetPassword().hashCode());
        result = prime * result + ((getLoginAt() == null) ? 0 : getLoginAt().hashCode());
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
        sb.append(", email=").append(email);
        sb.append(", name=").append(name);
        sb.append(", avatar=").append(avatar);
        sb.append(", password=").append(password);
        sb.append(", salt=").append(salt);
        sb.append(", idCard=").append(idCard);
        sb.append(", credit1=").append(credit1);
        sb.append(", createIp=").append(createIp);
        sb.append(", createCity=").append(createCity);
        sb.append(", isActive=").append(isActive);
        sb.append(", isLock=").append(isLock);
        sb.append(", isVerify=").append(isVerify);
        sb.append(", verifyAt=").append(verifyAt);
        sb.append(", isSetPassword=").append(isSetPassword);
        sb.append(", loginAt=").append(loginAt);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
