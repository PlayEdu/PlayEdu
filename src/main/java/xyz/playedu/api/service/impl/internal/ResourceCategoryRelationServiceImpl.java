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
package xyz.playedu.api.service.impl.internal;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

import xyz.playedu.api.domain.ResourceCategoryRelation;
import xyz.playedu.api.mapper.ResourceCategoryRelationMapper;
import xyz.playedu.api.service.internal.ResourceCategoryRelationService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResourceCategoryRelationServiceImpl
        extends ServiceImpl<ResourceCategoryRelationMapper, ResourceCategoryRelation>
        implements ResourceCategoryRelationService {
    @Override
    public void rebuild(List<Integer> ids, Integer categoryId) {
        remove(query().getWrapper().in("rid", ids));
        List<ResourceCategoryRelation> data = new ArrayList<>();
        ids.forEach(
                (item) ->
                        data.add(
                                new ResourceCategoryRelation() {
                                    {
                                        setCid(categoryId);
                                        setRid(item);
                                    }
                                }));
        saveBatch(data);
    }
}
