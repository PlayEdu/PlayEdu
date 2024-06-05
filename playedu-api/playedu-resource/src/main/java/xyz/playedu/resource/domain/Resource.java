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
package xyz.playedu.resource.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName resources
 */
@TableName(value = "resources")
@Data
public class Resource implements Serializable {
    /** */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** */
    @JsonProperty("admin_id")
    private Integer adminId;

    /** 类型 */
    private String type;

    /** 资源名 */
    private String name;

    /** 文件类型 */
    private String extension;

    /** 大小[字节] */
    private Long size;

    /** 存储磁盘 */
    private String disk;

    /** 文件id */
    @JsonProperty("file_id")
    private String fileId;

    /** 相对地址 */
    private String path;

    /** URL地址 */
    private String url;

    /** */
    @JsonProperty("created_at")
    private Date createdAt;

    /** 所属素材 */
    @JsonProperty("parent_id")
    private Integer parentId;

    /** 隐藏[0:否,1:是] */
    @JsonIgnore private Integer isHidden;

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
        Resource other = (Resource) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getAdminId() == null
                        ? other.getAdminId() == null
                        : this.getAdminId().equals(other.getAdminId()))
                && (this.getType() == null
                        ? other.getType() == null
                        : this.getType().equals(other.getType()))
                && (this.getName() == null
                        ? other.getName() == null
                        : this.getName().equals(other.getName()))
                && (this.getExtension() == null
                        ? other.getExtension() == null
                        : this.getExtension().equals(other.getExtension()))
                && (this.getSize() == null
                        ? other.getSize() == null
                        : this.getSize().equals(other.getSize()))
                && (this.getDisk() == null
                        ? other.getDisk() == null
                        : this.getDisk().equals(other.getDisk()))
                && (this.getFileId() == null
                        ? other.getFileId() == null
                        : this.getFileId().equals(other.getFileId()))
                && (this.getPath() == null
                        ? other.getPath() == null
                        : this.getPath().equals(other.getPath()))
                && (this.getUrl() == null
                        ? other.getUrl() == null
                        : this.getUrl().equals(other.getUrl()))
                && (this.getCreatedAt() == null
                        ? other.getCreatedAt() == null
                        : this.getCreatedAt().equals(other.getCreatedAt()))
                && (this.getParentId() == null
                        ? other.getParentId() == null
                        : this.getParentId().equals(other.getParentId()))
                && (this.getIsHidden() == null
                        ? other.getIsHidden() == null
                        : this.getIsHidden().equals(other.getIsHidden()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getAdminId() == null) ? 0 : getAdminId().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getExtension() == null) ? 0 : getExtension().hashCode());
        result = prime * result + ((getSize() == null) ? 0 : getSize().hashCode());
        result = prime * result + ((getDisk() == null) ? 0 : getDisk().hashCode());
        result = prime * result + ((getFileId() == null) ? 0 : getFileId().hashCode());
        result = prime * result + ((getPath() == null) ? 0 : getPath().hashCode());
        result = prime * result + ((getUrl() == null) ? 0 : getUrl().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getParentId() == null) ? 0 : getParentId().hashCode());
        result = prime * result + ((getIsHidden() == null) ? 0 : getIsHidden().hashCode());
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
        sb.append(", type=").append(type);
        sb.append(", name=").append(name);
        sb.append(", extension=").append(extension);
        sb.append(", size=").append(size);
        sb.append(", disk=").append(disk);
        sb.append(", fileId=").append(fileId);
        sb.append(", path=").append(path);
        sb.append(", url=").append(url);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", parentId=").append(parentId);
        sb.append(", isHidden=").append(isHidden);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
