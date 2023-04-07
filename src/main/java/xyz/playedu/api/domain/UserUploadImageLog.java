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
 * @TableName user_upload_image_logs
 */
@TableName(value = "user_upload_image_logs")
@Data
public class UserUploadImageLog implements Serializable {
    /** */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** */
    @JsonProperty("user_id")
    private Integer userId;

    /** 图片类型 */
    private String typed;

    /** 上传场景 */
    private String scene;

    /** 驱动 */
    private String driver;

    /** 相对路径 */
    private String path;

    /** 访问地址 */
    private String url;

    /** 大小,单位:字节 */
    private Long size;

    /** 文件名 */
    private String name;

    /** */
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
        UserUploadImageLog other = (UserUploadImageLog) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getUserId() == null
                        ? other.getUserId() == null
                        : this.getUserId().equals(other.getUserId()))
                && (this.getTyped() == null
                        ? other.getTyped() == null
                        : this.getTyped().equals(other.getTyped()))
                && (this.getScene() == null
                        ? other.getScene() == null
                        : this.getScene().equals(other.getScene()))
                && (this.getDriver() == null
                        ? other.getDriver() == null
                        : this.getDriver().equals(other.getDriver()))
                && (this.getPath() == null
                        ? other.getPath() == null
                        : this.getPath().equals(other.getPath()))
                && (this.getUrl() == null
                        ? other.getUrl() == null
                        : this.getUrl().equals(other.getUrl()))
                && (this.getSize() == null
                        ? other.getSize() == null
                        : this.getSize().equals(other.getSize()))
                && (this.getName() == null
                        ? other.getName() == null
                        : this.getName().equals(other.getName()))
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
        result = prime * result + ((getTyped() == null) ? 0 : getTyped().hashCode());
        result = prime * result + ((getScene() == null) ? 0 : getScene().hashCode());
        result = prime * result + ((getDriver() == null) ? 0 : getDriver().hashCode());
        result = prime * result + ((getPath() == null) ? 0 : getPath().hashCode());
        result = prime * result + ((getUrl() == null) ? 0 : getUrl().hashCode());
        result = prime * result + ((getSize() == null) ? 0 : getSize().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
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
        sb.append(", typed=").append(typed);
        sb.append(", scene=").append(scene);
        sb.append(", driver=").append(driver);
        sb.append(", path=").append(path);
        sb.append(", url=").append(url);
        sb.append(", size=").append(size);
        sb.append(", name=").append(name);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
