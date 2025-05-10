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
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName app_config
 */
@TableName(value = "app_config")
@Data
public class AppConfig implements Serializable {
    /** */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 分组 */
    @JsonProperty("group_name")
    private String groupName;

    /** 名称 */
    private String name;

    /** 升序 */
    private Integer sort;

    /** */
    @JsonProperty("field_type")
    private String fieldType;

    /** 键 */
    @JsonProperty("key_name")
    private String keyName;

    /** 值 */
    @JsonProperty("key_value")
    private String keyValue;

    /** 可选值 */
    @JsonProperty("option_value")
    private String optionValue;

    /** 是否私密信息 */
    @JsonProperty("is_private")
    private Integer isPrivate;

    /** 帮助信息 */
    private String help;

    /** */
    @JsonIgnore private Date createdAt;

    /** 1显示,0否 */
    @JsonIgnore private Integer isHidden;

    @JsonGetter("key_value")
    public String transformKeyValue() {
        return isHidden == 1 ? "******" : keyValue;
    }

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
        AppConfig other = (AppConfig) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getGroupName() == null
                        ? other.getGroupName() == null
                        : this.getGroupName().equals(other.getGroupName()))
                && (this.getName() == null
                        ? other.getName() == null
                        : this.getName().equals(other.getName()))
                && (this.getSort() == null
                        ? other.getSort() == null
                        : this.getSort().equals(other.getSort()))
                && (this.getFieldType() == null
                        ? other.getFieldType() == null
                        : this.getFieldType().equals(other.getFieldType()))
                && (this.getKeyName() == null
                        ? other.getKeyName() == null
                        : this.getKeyName().equals(other.getKeyName()))
                && (this.getKeyValue() == null
                        ? other.getKeyValue() == null
                        : this.getKeyValue().equals(other.getKeyValue()))
                && (this.getOptionValue() == null
                        ? other.getOptionValue() == null
                        : this.getOptionValue().equals(other.getOptionValue()))
                && (this.getIsPrivate() == null
                        ? other.getIsPrivate() == null
                        : this.getIsPrivate().equals(other.getIsPrivate()))
                && (this.getHelp() == null
                        ? other.getHelp() == null
                        : this.getHelp().equals(other.getHelp()))
                && (this.getCreatedAt() == null
                        ? other.getCreatedAt() == null
                        : this.getCreatedAt().equals(other.getCreatedAt()))
                && (this.getIsHidden() == null
                        ? other.getIsHidden() == null
                        : this.getIsHidden().equals(other.getIsHidden()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getGroupName() == null) ? 0 : getGroupName().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getSort() == null) ? 0 : getSort().hashCode());
        result = prime * result + ((getFieldType() == null) ? 0 : getFieldType().hashCode());
        result = prime * result + ((getKeyName() == null) ? 0 : getKeyName().hashCode());
        result = prime * result + ((getKeyValue() == null) ? 0 : getKeyValue().hashCode());
        result = prime * result + ((getOptionValue() == null) ? 0 : getOptionValue().hashCode());
        result = prime * result + ((getIsPrivate() == null) ? 0 : getIsPrivate().hashCode());
        result = prime * result + ((getHelp() == null) ? 0 : getHelp().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
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
        sb.append(", groupName=").append(groupName);
        sb.append(", name=").append(name);
        sb.append(", sort=").append(sort);
        sb.append(", fieldType=").append(fieldType);
        sb.append(", keyName=").append(keyName);
        sb.append(", keyValue=").append(keyValue);
        sb.append(", optionValue=").append(optionValue);
        sb.append(", isPrivate=").append(isPrivate);
        sb.append(", help=").append(help);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", isHidden=").append(isHidden);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
