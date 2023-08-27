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
package xyz.playedu.resource.service;

import com.baomidou.mybatisplus.extension.service.IService;

import xyz.playedu.common.exception.NotFoundException;
import xyz.playedu.common.types.paginate.PaginationResult;
import xyz.playedu.common.types.paginate.ResourcePaginateFilter;
import xyz.playedu.resource.domain.Resource;

import java.util.List;

public interface ResourceService extends IService<Resource> {

    PaginationResult<Resource> paginate(int page, int size, ResourcePaginateFilter filter);

    List<String> paginateType(ResourcePaginateFilter filter);

    Resource create(
            Integer adminId,
            String categoryIds,
            String type,
            String filename,
            String ext,
            Long size,
            String disk,
            String fileId,
            String path,
            String url);

    Resource findOrFail(Integer id) throws NotFoundException;

    void changeParentId(Integer id, Integer parentId);

    void storeResourceVideo(Integer rid, Integer duration, String poster);

    List<Resource> chunks(List<Integer> ids);

    List<Resource> chunks(List<Integer> ids, List<String> fields);

    Integer total(String type);

    Integer duration(Integer id);

    void updateNameAndCategoryId(Integer id, String name, Integer categoryId);

    List<Integer> categoryIds(Integer resourceId);
}
