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
package xyz.playedu.api.event;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.ApplicationEvent;

import java.util.Date;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/2/26 17:10
 */
@Getter
@Setter
public class ResourceCategoryDestroyEvent extends ApplicationEvent {

    private Integer adminId;
    private Integer categoryId;
    private Date createdAt;

    public ResourceCategoryDestroyEvent(Object source, Integer adminId, Integer categoryId) {
        super(source);
        this.adminId = adminId;
        this.categoryId = categoryId;
        this.createdAt = new Date();
    }
}
