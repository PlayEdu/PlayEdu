/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
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
 * @TableName user_course_records
 */
@TableName(value = "user_course_records")
@Data
public class UserCourseRecord implements Serializable {
    /** */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** */
    @JsonProperty("user_id")
    private Integer userId;

    /** */
    @JsonProperty("course_id")
    private Integer courseId;

    /** 课时数量 */
    @JsonProperty("hour_count")
    private Integer hourCount;

    /** 已完成课时数 */
    @JsonProperty("finished_count")
    private Integer finishedCount;

    /** 进度 */
    private Integer progress;

    /** 看完[1:是,0:否] */
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
        UserCourseRecord other = (UserCourseRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getUserId() == null
                        ? other.getUserId() == null
                        : this.getUserId().equals(other.getUserId()))
                && (this.getCourseId() == null
                        ? other.getCourseId() == null
                        : this.getCourseId().equals(other.getCourseId()))
                && (this.getHourCount() == null
                        ? other.getHourCount() == null
                        : this.getHourCount().equals(other.getHourCount()))
                && (this.getFinishedCount() == null
                        ? other.getFinishedCount() == null
                        : this.getFinishedCount().equals(other.getFinishedCount()))
                && (this.getProgress() == null
                        ? other.getProgress() == null
                        : this.getProgress().equals(other.getProgress()))
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
        result = prime * result + ((getHourCount() == null) ? 0 : getHourCount().hashCode());
        result =
                prime * result + ((getFinishedCount() == null) ? 0 : getFinishedCount().hashCode());
        result = prime * result + ((getProgress() == null) ? 0 : getProgress().hashCode());
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
        sb.append(", hourCount=").append(hourCount);
        sb.append(", finishedCount=").append(finishedCount);
        sb.append(", progress=").append(progress);
        sb.append(", isFinished=").append(isFinished);
        sb.append(", finishedAt=").append(finishedAt);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
