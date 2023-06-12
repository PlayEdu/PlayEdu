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

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName user_login_records
 */
@TableName(value = "user_login_records")
@Data
public class UserLoginRecord implements Serializable {
    /** */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** */
    private Integer userId;

    /** JTI */
    private String jti;

    /** 登录ip */
    private String ip;

    /** Ip解析区域 */
    private String ipArea;

    /** 浏览器 */
    private String browser;

    /** 浏览器版本 */
    private String browserVersion;

    /** 操作系统 */
    private String os;

    /** 过期时间 */
    private Long expired;

    /** 是否注销 */
    private Integer isLogout;

    /** 创建时间 */
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
        UserLoginRecord other = (UserLoginRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getUserId() == null
                        ? other.getUserId() == null
                        : this.getUserId().equals(other.getUserId()))
                && (this.getJti() == null
                        ? other.getJti() == null
                        : this.getJti().equals(other.getJti()))
                && (this.getIp() == null
                        ? other.getIp() == null
                        : this.getIp().equals(other.getIp()))
                && (this.getIpArea() == null
                        ? other.getIpArea() == null
                        : this.getIpArea().equals(other.getIpArea()))
                && (this.getBrowser() == null
                        ? other.getBrowser() == null
                        : this.getBrowser().equals(other.getBrowser()))
                && (this.getBrowserVersion() == null
                        ? other.getBrowserVersion() == null
                        : this.getBrowserVersion().equals(other.getBrowserVersion()))
                && (this.getOs() == null
                        ? other.getOs() == null
                        : this.getOs().equals(other.getOs()))
                && (this.getExpired() == null
                        ? other.getExpired() == null
                        : this.getExpired().equals(other.getExpired()))
                && (this.getIsLogout() == null
                        ? other.getIsLogout() == null
                        : this.getIsLogout().equals(other.getIsLogout()))
                && (this.getCreatedAt() == null
                        ? other.getCreatedAt() == null
                        : this.getCreatedAt().equals(other.getCreatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getJti() == null) ? 0 : getJti().hashCode());
        result = prime * result + ((getIp() == null) ? 0 : getIp().hashCode());
        result = prime * result + ((getIpArea() == null) ? 0 : getIpArea().hashCode());
        result = prime * result + ((getBrowser() == null) ? 0 : getBrowser().hashCode());
        result =
                prime * result
                        + ((getBrowserVersion() == null) ? 0 : getBrowserVersion().hashCode());
        result = prime * result + ((getOs() == null) ? 0 : getOs().hashCode());
        result = prime * result + ((getExpired() == null) ? 0 : getExpired().hashCode());
        result = prime * result + ((getIsLogout() == null) ? 0 : getIsLogout().hashCode());
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
        sb.append(", userId=").append(userId);
        sb.append(", jti=").append(jti);
        sb.append(", ip=").append(ip);
        sb.append(", ipArea=").append(ipArea);
        sb.append(", browser=").append(browser);
        sb.append(", browserVersion=").append(browserVersion);
        sb.append(", os=").append(os);
        sb.append(", expired=").append(expired);
        sb.append(", isLogout=").append(isLogout);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
