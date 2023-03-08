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
 *
 * @TableName resource_videos
 */
@TableName(value ="resource_videos")
@Data
public class ResourceVideo implements Serializable {
    /**
     *
     */
    private Integer rid;

    /**
     * 封面
     */
    private String poster;

    /**
     * 视频时长[s]
     */
    private Integer duration;

    /**
     * 创建时间
     */
    @JsonIgnore
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
        ResourceVideo other = (ResourceVideo) that;
        return (this.getRid() == null ? other.getRid() == null : this.getRid().equals(other.getRid()))
            && (this.getPoster() == null ? other.getPoster() == null : this.getPoster().equals(other.getPoster()))
            && (this.getDuration() == null ? other.getDuration() == null : this.getDuration().equals(other.getDuration()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRid() == null) ? 0 : getRid().hashCode());
        result = prime * result + ((getPoster() == null) ? 0 : getPoster().hashCode());
        result = prime * result + ((getDuration() == null) ? 0 : getDuration().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", rid=").append(rid);
        sb.append(", poster=").append(poster);
        sb.append(", duration=").append(duration);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}