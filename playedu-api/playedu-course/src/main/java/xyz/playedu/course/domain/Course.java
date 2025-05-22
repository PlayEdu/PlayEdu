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
package xyz.playedu.course.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName courses
 */
@TableName(value = "courses")
@Data
public class Course implements Serializable {
    /** */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 课程标题 */
    private String title;

    /** 课程封面 */
    private Integer thumb;

    /** 课程价格(分) */
    private Integer charge;

    /** 课程简介 */
    @JsonProperty("short_desc")
    private String shortDesc;

    /** 1:必修,0:选修 */
    @JsonProperty("is_required")
    private Integer isRequired;

    /** 课时数 */
    @JsonProperty("class_hour")
    private Integer classHour;

    /** 显示[1:是,0:否] */
    @JsonProperty("is_show")
    private Integer isShow;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("sort_at")
    private Date sortAt;

    /** 其它规则[课程设置] */
    private String extra;

    /** 管理员ID */
    @JsonProperty("admin_id")
    private Integer adminId;

    @JsonIgnore private Date updatedAt;

    @JsonIgnore private Date deletedAt;

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
        Course other = (Course) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getTitle() == null
                        ? other.getTitle() == null
                        : this.getTitle().equals(other.getTitle()))
                && (this.getThumb() == null
                        ? other.getThumb() == null
                        : this.getThumb().equals(other.getThumb()))
                && (this.getCharge() == null
                        ? other.getCharge() == null
                        : this.getCharge().equals(other.getCharge()))
                && (this.getClassHour() == null
                        ? other.getClassHour() == null
                        : this.getClassHour().equals(other.getClassHour()))
                && (this.getIsShow() == null
                        ? other.getIsShow() == null
                        : this.getIsShow().equals(other.getIsShow()))
                && (this.getIsRequired() == null
                        ? other.getIsRequired() == null
                        : this.getIsRequired().equals(other.getIsRequired()))
                && (this.getCreatedAt() == null
                        ? other.getCreatedAt() == null
                        : this.getCreatedAt().equals(other.getCreatedAt()))
                && (this.getSortAt() == null
                        ? other.getSortAt() == null
                        : this.getSortAt().equals(other.getSortAt()))
                && (this.getUpdatedAt() == null
                        ? other.getUpdatedAt() == null
                        : this.getUpdatedAt().equals(other.getUpdatedAt()))
                && (this.getDeletedAt() == null
                        ? other.getDeletedAt() == null
                        : this.getDeletedAt().equals(other.getDeletedAt()))
                && (this.getShortDesc() == null
                        ? other.getShortDesc() == null
                        : this.getShortDesc().equals(other.getShortDesc()))
                && (this.getSortAt() == null
                        ? other.getSortAt() == null
                        : this.getSortAt().equals(other.getSortAt()))
                && (this.getExtra() == null
                        ? other.getExtra() == null
                        : this.getExtra().equals(other.getExtra()))
                && (this.getAdminId() == null
                        ? other.getAdminId() == null
                        : this.getAdminId().equals(other.getAdminId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getThumb() == null) ? 0 : getThumb().hashCode());
        result = prime * result + ((getCharge() == null) ? 0 : getCharge().hashCode());
        result = prime * result + ((getShortDesc() == null) ? 0 : getShortDesc().hashCode());
        result = prime * result + ((getClassHour() == null) ? 0 : getClassHour().hashCode());
        result = prime * result + ((getIsShow() == null) ? 0 : getIsShow().hashCode());
        result = prime * result + ((getIsRequired() == null) ? 0 : getIsRequired().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getSortAt() == null) ? 0 : getSortAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        result = prime * result + ((getDeletedAt() == null) ? 0 : getDeletedAt().hashCode());
        result = prime * result + ((getSortAt() == null) ? 0 : getSortAt().hashCode());
        result = prime * result + ((getExtra() == null) ? 0 : getExtra().hashCode());
        result = prime * result + ((getAdminId() == null) ? 0 : getAdminId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", title=").append(title);
        sb.append(", thumb=").append(thumb);
        sb.append(", charge=").append(charge);
        sb.append(", shortDesc=").append(shortDesc);
        sb.append(", classHour=").append(classHour);
        sb.append(", isShow=").append(isShow);
        sb.append(", isRequired=").append(isRequired);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", sortAt=").append(sortAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", deletedAt=").append(deletedAt);
        sb.append(", sortAt=").append(sortAt);
        sb.append(", extra=").append(extra);
        sb.append(", adminId=").append(adminId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
