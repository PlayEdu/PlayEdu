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
package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xyz.playedu.api.domain.Resource;
import xyz.playedu.api.domain.ResourceCategoryRelation;
import xyz.playedu.api.domain.ResourceVideo;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.mapper.ResourceMapper;
import xyz.playedu.api.service.ResourceService;
import xyz.playedu.api.service.ResourceVideoService;
import xyz.playedu.api.service.internal.ResourceCategoryRelationService;
import xyz.playedu.api.types.paginate.PaginationResult;
import xyz.playedu.api.types.paginate.ResourcePaginateFilter;

import java.util.*;

/**
 * @author tengteng
 * @description 针对表【resources】的数据库操作Service实现
 * @createDate 2023-02-23 10:50:26
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource>
        implements ResourceService {

    @Autowired private ResourceVideoService resourceVideoService;

    @Autowired private ResourceCategoryRelationService relationService;

    @Override
    public PaginationResult<Resource> paginate(int page, int size, ResourcePaginateFilter filter) {
        PaginationResult<Resource> pageResult = new PaginationResult<>();

        filter.setPageStart((page - 1) * size);
        filter.setPageSize(size);

        pageResult.setData(getBaseMapper().paginate(filter));
        pageResult.setTotal(getBaseMapper().paginateCount(filter));

        return pageResult;
    }

    @Override
    public List<String> paginateType(ResourcePaginateFilter filter) {
        return getBaseMapper().paginateType(filter);
    }

    @Override
    @Transactional
    public Resource create(
            Integer adminId,
            String categoryIds,
            String type,
            String filename,
            String ext,
            Long size,
            String disk,
            String fileId,
            String path,
            String url) {
        Resource resource = new Resource();
        resource.setAdminId(adminId);
        resource.setType(type);
        resource.setName(filename);
        resource.setExtension(ext);
        resource.setSize(size);
        resource.setDisk(disk);
        resource.setFileId(fileId);
        resource.setPath(path);
        resource.setUrl(url);
        resource.setCreatedAt(new Date());
        save(resource);

        if (categoryIds != null && categoryIds.trim().length() > 0) {
            String[] idArray = categoryIds.split(",");
            List<ResourceCategoryRelation> relations = new ArrayList<>();
            for (String s : idArray) {
                int tmpId = Integer.parseInt(s);
                if (tmpId == 0) {
                    continue;
                }
                relations.add(
                        new ResourceCategoryRelation() {
                            {
                                setCid(tmpId);
                                setRid(resource.getId());
                            }
                        });
            }
            relationService.saveBatch(relations);
        }
        return resource;
    }

    @Override
    public Resource findOrFail(Integer id) throws NotFoundException {
        Resource resource = getById(id);
        if (resource == null) {
            throw new NotFoundException("资源不存在");
        }
        return resource;
    }

    @Override
    public void changeParentId(Integer id, Integer parentId) {
        Resource resource = new Resource();
        resource.setId(id);
        resource.setParentId(parentId);
        resource.setIsHidden(1);
        updateById(resource);
    }

    @Override
    public void storeResourceVideo(Integer rid, Integer duration, String poster) {
        resourceVideoService.create(rid, duration, poster);
    }

    @Override
    public List<Resource> chunks(List<Integer> ids) {
        return list(query().getWrapper().in("id", ids));
    }

    @Override
    public List<Resource> chunks(List<Integer> ids, List<String> fields) {
        return list(query().getWrapper().in("id", ids).select(fields));
    }

    @Override
    public Integer total(String type) {
        return Math.toIntExact(count(query().getWrapper().eq("type", type).eq("is_hidden", 0)));
    }

    @Override
    public Integer duration(Integer id) {
        ResourceVideo resourceVideo =
                resourceVideoService.getOne(
                        resourceVideoService.query().getWrapper().eq("rid", id));
        if (resourceVideo == null) {
            return null;
        }
        return resourceVideo.getDuration();
    }

    @Override
    @Transactional
    public void updateNameAndCategoryId(Integer id, String name, Integer categoryId) {
        Resource resource = new Resource();
        resource.setId(id);
        resource.setName(name);
        updateById(resource);

        relationService.rebuild(
                id,
                new ArrayList<>() {
                    {
                        add(categoryId);
                    }
                });
    }

    @Override
    public List<Integer> categoryIds(Integer resourceId) {
        return relationService
                .list(relationService.query().getWrapper().eq("rid", resourceId))
                .stream()
                .map(ResourceCategoryRelation::getCid)
                .toList();
    }
}
