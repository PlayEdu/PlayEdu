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
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.playedu.common.exception.NotFoundException;
import xyz.playedu.common.service.AppConfigService;
import xyz.playedu.common.types.paginate.PaginationResult;
import xyz.playedu.common.types.paginate.ResourcePaginateFilter;
import xyz.playedu.common.util.S3Util;
import xyz.playedu.common.util.StringUtil;
import xyz.playedu.resource.domain.Resource;
import xyz.playedu.resource.domain.ResourceCategory;
import xyz.playedu.resource.domain.ResourceExtra;
import xyz.playedu.resource.mapper.ResourceMapper;
import xyz.playedu.resource.service.ResourceCategoryService;
import xyz.playedu.resource.service.ResourceExtraService;
import xyz.playedu.resource.service.ResourceService;

/**
 * @author tengteng
 * @description 针对表【resource】的数据库操作Service实现
 * @createDate 2023-02-23 10:50:26
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource>
        implements ResourceService {

    @Autowired private ResourceExtraService resourceExtraService;

    @Autowired private ResourceCategoryService relationService;

    @Autowired private AppConfigService appConfigService;

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
            String path,
            Integer parentId,
            Integer isHidden) {
        Resource resource = new Resource();
        resource.setAdminId(adminId);
        resource.setType(type);
        resource.setName(filename);
        resource.setExtension(ext);
        resource.setSize(size);
        resource.setDisk(disk);
        resource.setPath(path);
        resource.setCreatedAt(new Date());
        resource.setParentId(parentId);
        resource.setIsHidden(isHidden);
        save(resource);

        if (categoryIds != null && categoryIds.trim().length() > 0) {
            String[] idArray = categoryIds.split(",");
            List<ResourceCategory> relations = new ArrayList<>();
            for (String s : idArray) {
                int tmpId = Integer.parseInt(s);
                if (tmpId == 0) {
                    continue;
                }
                relations.add(
                        new ResourceCategory() {
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
    @Transactional
    public void update(
            Resource resource,
            Integer adminId,
            String categoryIds,
            String type,
            String filename,
            String ext,
            Long size,
            String disk,
            String path,
            Integer parentId,
            Integer isHidden) {
        resource.setAdminId(adminId);
        resource.setType(type);
        resource.setName(filename);
        resource.setExtension(ext);
        resource.setSize(size);
        resource.setDisk(disk);
        resource.setPath(path);
        resource.setCreatedAt(new Date());
        resource.setParentId(parentId);
        resource.setIsHidden(isHidden);
        updateById(resource);

        if (categoryIds != null && categoryIds.trim().length() > 0) {
            String[] idArray = categoryIds.split(",");
            List<ResourceCategory> relations = new ArrayList<>();
            for (String s : idArray) {
                int tmpId = Integer.parseInt(s);
                if (tmpId == 0) {
                    continue;
                }
                relations.add(
                        new ResourceCategory() {
                            {
                                setCid(tmpId);
                                setRid(resource.getId());
                            }
                        });
            }
            relationService.saveBatch(relations);
        }
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
        ResourceExtra resourceExtra =
                resourceExtraService.getOne(
                        resourceExtraService.query().getWrapper().eq("rid", id));
        if (resourceExtra == null) {
            return null;
        }
        return resourceExtra.getDuration();
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
                .map(ResourceCategory::getCid)
                .toList();
    }

    @Override
    public Integer total(List<String> types) {
        return Math.toIntExact(count(query().getWrapper().in("type", types).eq("is_hidden", 0)));
    }

    @Override
    public Map<Integer, String> chunksPreSignUrlByIds(List<Integer> ids) {
        if (StringUtil.isEmpty(ids)) {
            return new HashMap<>();
        }

        S3Util s3Util = new S3Util(appConfigService.getS3Config());
        Map<Integer, String> preSignUrlMap = new HashMap<>();
        List<Resource> resourceList = list(query().getWrapper().in("id", ids));
        if (StringUtil.isNotEmpty(resourceList)) {
            resourceList.forEach(
                    resource -> {
                        String path = resource.getPath();
                        try {
                            String url = s3Util.generateEndpointPreSignUrl(path, "");
                            if (StringUtil.isNotEmpty(url)) {
                                preSignUrlMap.put(resource.getId(), url);
                            }
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        }
                    });
        }
        return preSignUrlMap;
    }

    @Override
    public Map<Integer, String> downloadResById(Integer id) {
        Map<Integer, String> preSignUrlMap = new HashMap<>();
        Resource resource = getById(id);
        if (StringUtil.isNotNull(resource)) {
            String name = resource.getName() + "." + resource.getExtension();
            String url =
                    new S3Util(appConfigService.getS3Config())
                            .generateEndpointPreSignUrl(resource.getPath(), name);
            if (StringUtil.isNotEmpty(url)) {
                preSignUrlMap.put(resource.getId(), url);
            }
        }
        return preSignUrlMap;
    }
}
