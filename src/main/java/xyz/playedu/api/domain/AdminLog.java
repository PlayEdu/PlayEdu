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

    /** 管理员姓名 */
    @JsonProperty("admin_name")
    private String adminName;

    /** 模块 */
    private String module;

    /** 标题 */
    private String title;

    /** 操作指令 */
    private Integer opt;

    /** 请求方法 */
    private String method;

    /** 请求方式 POST,GET,PUT,DELETE */
    @JsonProperty("request_method")
    private String requestMethod;

    /** 请求URL */
    private String url;

    /** 请求参数 */
    private String param;

    /** 返回参数 */
    private String result;

    /** ip */
    private String ip;

    /** 地址 */
    @JsonProperty("ip_area")
    private String ipArea;

    /** 备注 */
    @JsonProperty("error_msg")
    private String errorMsg;

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
                && (this.getAdminName() == null
                        ? other.getAdminName() == null
                        : this.getAdminName().equals(other.getAdminName()))
                && (this.getModule() == null
                        ? other.getModule() == null
                        : this.getModule().equals(other.getModule()))
                && (this.getTitle() == null
                        ? other.getTitle() == null
                        : this.getTitle().equals(other.getTitle()))
                && (this.getOpt() == null
                        ? other.getOpt() == null
                        : this.getOpt().equals(other.getOpt()))
                && (this.getMethod() == null
                        ? other.getMethod() == null
                        : this.getMethod().equals(other.getMethod()))
                && (this.getRequestMethod() == null
                        ? other.getRequestMethod() == null
                        : this.getRequestMethod().equals(other.getRequestMethod()))
                && (this.getUrl() == null
                        ? other.getUrl() == null
                        : this.getUrl().equals(other.getUrl()))
                && (this.getParam() == null
                        ? other.getParam() == null
                        : this.getParam().equals(other.getParam()))
                && (this.getResult() == null
                        ? other.getResult() == null
                        : this.getResult().equals(other.getResult()))
                && (this.getIp() == null
                        ? other.getIp() == null
                        : this.getIp().equals(other.getIp()))
                && (this.getIpArea() == null
                        ? other.getIpArea() == null
                        : this.getIpArea().equals(other.getIpArea()))
                && (this.getErrorMsg() == null
                        ? other.getErrorMsg() == null
                        : this.getErrorMsg().equals(other.getErrorMsg()))
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
        result = prime * result + ((getAdminName() == null) ? 0 : getAdminName().hashCode());
        result = prime * result + ((getModule() == null) ? 0 : getModule().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getOpt() == null) ? 0 : getOpt().hashCode());
        result = prime * result + ((getMethod() == null) ? 0 : getMethod().hashCode());
        result =
                prime * result + ((getRequestMethod() == null) ? 0 : getRequestMethod().hashCode());
        result = prime * result + ((getUrl() == null) ? 0 : getUrl().hashCode());
        result = prime * result + ((getParam() == null) ? 0 : getParam().hashCode());
        result = prime * result + ((getResult() == null) ? 0 : getResult().hashCode());
        result = prime * result + ((getIp() == null) ? 0 : getIp().hashCode());
        result = prime * result + ((getIpArea() == null) ? 0 : getIpArea().hashCode());
        result = prime * result + ((getErrorMsg() == null) ? 0 : getErrorMsg().hashCode());
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
        sb.append(", adminName=").append(adminName);
        sb.append(", module=").append(module);
        sb.append(", title=").append(title);
        sb.append(", opt=").append(opt);
        sb.append(", method=").append(method);
        sb.append(", requestMethod=").append(requestMethod);
        sb.append(", url=").append(url);
        sb.append(", param=").append(param);
        sb.append(", result=").append(result);
        sb.append(", ip=").append(ip);
        sb.append(", ipArea=").append(ipArea);
        sb.append(", errorMsg=").append(errorMsg);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
