package xyz.playedu.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @TableName courses
 */
@TableName(value = "courses")
@Data
public class Course implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 课程标题
     */
    private String title;

    /**
     * 课程封面
     */
    private String thumb;

    /**
     * 课程价格(分)
     */
    private Integer charge;

    /**
     * 课时数
     */
    @JsonProperty("class_hour")
    private Integer classHour;

    /**
     * 显示[1:是,0:否]
     */
    @JsonProperty("is_show")
    private Integer isShow;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonIgnore
    private Date updatedAt;

    @JsonIgnore
    private Date deletedAt;

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
                && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
                && (this.getThumb() == null ? other.getThumb() == null : this.getThumb().equals(other.getThumb()))
                && (this.getCharge() == null ? other.getCharge() == null : this.getCharge().equals(other.getCharge()))
                && (this.getClassHour() == null ? other.getClassHour() == null : this.getClassHour().equals(other.getClassHour()))
                && (this.getIsShow() == null ? other.getIsShow() == null : this.getIsShow().equals(other.getIsShow()))
                && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
                && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()))
                && (this.getDeletedAt() == null ? other.getDeletedAt() == null : this.getDeletedAt().equals(other.getDeletedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getThumb() == null) ? 0 : getThumb().hashCode());
        result = prime * result + ((getCharge() == null) ? 0 : getCharge().hashCode());
        result = prime * result + ((getClassHour() == null) ? 0 : getClassHour().hashCode());
        result = prime * result + ((getIsShow() == null) ? 0 : getIsShow().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        result = prime * result + ((getDeletedAt() == null) ? 0 : getDeletedAt().hashCode());
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
        sb.append(", classHour=").append(classHour);
        sb.append(", isShow=").append(isShow);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", deletedAt=").append(deletedAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}