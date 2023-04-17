/*
 * Copyright 2023 杭州白书科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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
 * @TableName user_course_hour_records
 */
@TableName(value = "user_course_hour_records")
@Data
public class UserCourseHourRecord implements Serializable {
    /** */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** */
    @JsonProperty("user_id")
    private Integer userId;

    /** */
    @JsonProperty("course_id")
    private Integer courseId;

    /** */
    @JsonProperty("hour_id")
    private Integer hourId;

    /** 总时长 */
    @JsonProperty("total_duration")
    private Integer totalDuration;

    /** 已完成时长 */
    @JsonProperty("finished_duration")
    private Integer finishedDuration;

    /** 实际观看时长 */
    @JsonProperty("real_duration")
    private Integer realDuration;

    /** 是否看完[1:是,0:否] */
    @JsonProperty("is_finished")
    private Integer isFinished;

    /** 看完时间 */
    @JsonProperty("finished_at")
    private Date finishedAt;

    /** */
    @JsonProperty("created_at")
    private Date createdAt;

    /** */
    @JsonProperty("updated_at")
    private Date updatedAt;

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
        UserCourseHourRecord other = (UserCourseHourRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getUserId() == null
                        ? other.getUserId() == null
                        : this.getUserId().equals(other.getUserId()))
                && (this.getCourseId() == null
                        ? other.getCourseId() == null
                        : this.getCourseId().equals(other.getCourseId()))
                && (this.getHourId() == null
                        ? other.getHourId() == null
                        : this.getHourId().equals(other.getHourId()))
                && (this.getTotalDuration() == null
                        ? other.getTotalDuration() == null
                        : this.getTotalDuration().equals(other.getTotalDuration()))
                && (this.getFinishedDuration() == null
                        ? other.getFinishedDuration() == null
                        : this.getFinishedDuration().equals(other.getFinishedDuration()))
                && (this.getRealDuration() == null
                        ? other.getRealDuration() == null
                        : this.getRealDuration().equals(other.getRealDuration()))
                && (this.getIsFinished() == null
                        ? other.getIsFinished() == null
                        : this.getIsFinished().equals(other.getIsFinished()))
                && (this.getFinishedAt() == null
                        ? other.getFinishedAt() == null
                        : this.getFinishedAt().equals(other.getFinishedAt()))
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
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getCourseId() == null) ? 0 : getCourseId().hashCode());
        result = prime * result + ((getHourId() == null) ? 0 : getHourId().hashCode());
        result =
                prime * result + ((getTotalDuration() == null) ? 0 : getTotalDuration().hashCode());
        result =
                prime * result
                        + ((getFinishedDuration() == null) ? 0 : getFinishedDuration().hashCode());
        result = prime * result + ((getRealDuration() == null) ? 0 : getRealDuration().hashCode());
        result = prime * result + ((getIsFinished() == null) ? 0 : getIsFinished().hashCode());
        result = prime * result + ((getFinishedAt() == null) ? 0 : getFinishedAt().hashCode());
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
        sb.append(", userId=").append(userId);
        sb.append(", courseId=").append(courseId);
        sb.append(", hourId=").append(hourId);
        sb.append(", totalDuration=").append(totalDuration);
        sb.append(", finishedDuration=").append(finishedDuration);
        sb.append(", realDuration=").append(realDuration);
        sb.append(", isFinished=").append(isFinished);
        sb.append(", finishedAt=").append(finishedAt);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
