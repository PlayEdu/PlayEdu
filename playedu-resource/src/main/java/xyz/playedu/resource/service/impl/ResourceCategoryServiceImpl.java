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
package xyz.playedu.resource.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

import xyz.playedu.resource.domain.ResourceCategory;
import xyz.playedu.resource.mapper.ResourceCategoryMapper;
import xyz.playedu.resource.service.ResourceCategoryService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResourceCategoryServiceImpl
        extends ServiceImpl<ResourceCategoryMapper, ResourceCategory>
        implements ResourceCategoryService {
    @Override
    public void rebuild(Integer resourceId, List<Integer> categoryIds) {
        remove(query().getWrapper().eq("rid", resourceId));

        List<ResourceCategory> data = new ArrayList<>();
        categoryIds.forEach(
                categoryId -> {
                    data.add(
                            new ResourceCategory() {
                                {
                                    setCid(categoryId);
                                    setRid(resourceId);
                                }
                            });
                });

        saveBatch(data);
    }

    @Override
    public List<Integer> getRidsByCategoryId(Integer id) {
        return list(query().getWrapper().in("cid", id)).stream()
                .map(ResourceCategory::getRid)
                .toList();
    }
}
