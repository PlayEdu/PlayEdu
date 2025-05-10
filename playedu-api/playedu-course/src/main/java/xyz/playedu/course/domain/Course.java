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
import java.io.Serial;
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
    private String thumb;

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

    @JsonProperty("published_at")
    private Date publishedAt;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("admin_id")
    private Integer adminId;

    @JsonIgnore private Date updatedAt;

    @JsonIgnore private Date deletedAt;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
