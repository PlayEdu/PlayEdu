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
package xyz.playedu.api.controller.backend;

import lombok.SneakyThrows;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import xyz.playedu.api.request.backend.ResourceDestroyMultiRequest;
import xyz.playedu.api.request.backend.ResourceUpdateRequest;
import xyz.playedu.common.annotation.Log;
import xyz.playedu.common.bus.BackendBus;
import xyz.playedu.common.constant.BackendConstant;
import xyz.playedu.common.constant.BusinessTypeConstant;
import xyz.playedu.common.context.BCtx;
import xyz.playedu.common.domain.AdminUser;
import xyz.playedu.common.exception.NotFoundException;
import xyz.playedu.common.exception.ServiceException;
import xyz.playedu.common.service.AdminUserService;
import xyz.playedu.common.service.MinioService;
import xyz.playedu.common.types.JsonResponse;
import xyz.playedu.common.types.paginate.PaginationResult;
import xyz.playedu.common.types.paginate.ResourcePaginateFilter;
import xyz.playedu.resource.domain.Resource;
import xyz.playedu.resource.domain.ResourceVideo;
import xyz.playedu.resource.service.ResourceService;
import xyz.playedu.resource.service.ResourceVideoService;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/backend/v1/resource")
public class ResourceController {

    @Autowired private AdminUserService adminUserService;

    @Autowired private ResourceService resourceService;

    @Autowired private ResourceVideoService resourceVideoService;

    @Autowired private MinioService minioService;

    @Autowired private BackendBus backendBus;

    @GetMapping("/index")
    @Log(title = "资源-列表", businessType = BusinessTypeConstant.GET)
    public JsonResponse index(@RequestParam HashMap<String, Object> params) {
        Integer page = MapUtils.getInteger(params, "page", 1);
        Integer size = MapUtils.getInteger(params, "size", 10);
        String sortField = MapUtils.getString(params, "sort_field");
        String sortAlgo = MapUtils.getString(params, "sort_algo");
        String name = MapUtils.getString(params, "name");
        String type = MapUtils.getString(params, "type");
        String categoryIds = MapUtils.getString(params, "category_ids");

        if (type == null || type.trim().isEmpty()) {
            return JsonResponse.error("请选择资源类型");
        }

        ResourcePaginateFilter filter = new ResourcePaginateFilter();
        filter.setSortAlgo(sortAlgo);
        filter.setSortField(sortField);
        filter.setType(type);
        filter.setCategoryIds(categoryIds);
        filter.setName(name);

        if (!backendBus.isSuperAdmin()) { // 非超管只能读取它自己上传的资源
            filter.setAdminId(BCtx.getId());
        }

        PaginationResult<Resource> result = resourceService.paginate(page, size, filter);

        HashMap<String, Object> data = new HashMap<>();
        data.put("result", result);

        if (type.equals(BackendConstant.RESOURCE_TYPE_VIDEO)) {
            List<ResourceVideo> resourceVideos =
                    resourceVideoService.chunksByRids(
                            result.getData().stream().map(Resource::getId).toList());
            Map<Integer, ResourceVideo> resourceVideosExtra =
                    resourceVideos.stream()
                            .collect(Collectors.toMap(ResourceVideo::getRid, e -> e));
            data.put("videos_extra", resourceVideosExtra);
        }

        // 操作人
        data.put("admin_users", new HashMap<>());
        if (!result.getData().isEmpty()) {
            Map<Integer, String> adminUsers =
                    adminUserService
                            .chunks(result.getData().stream().map(Resource::getAdminId).toList())
                            .stream()
                            .collect(Collectors.toMap(AdminUser::getId, AdminUser::getName));
            data.put("admin_users", adminUsers);
        }

        if (!type.equals(BackendConstant.RESOURCE_TYPE_VIDEO)
                && !type.equals(BackendConstant.RESOURCE_TYPE_IMAGE)) {
            filter.setType(BackendConstant.RESOURCE_TYPE_ATTACHMENT);
            data.put("existing_types", resourceService.paginateType(filter));
        }
        return JsonResponse.data(data);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @SneakyThrows
    @Log(title = "资源-删除", businessType = BusinessTypeConstant.DELETE)
    public JsonResponse destroy(@PathVariable(name = "id") Integer id) throws NotFoundException {
        Resource resource = resourceService.findOrFail(id);

        if (!backendBus.isSuperAdmin()) {
            if (!resource.getAdminId().equals(BCtx.getId())) {
                throw new ServiceException("无权限");
            }
        }

        // 删除文件
        minioService.removeByPath(resource.getPath());
        // 如果是视频资源文件则删除对应的时长关联记录
        if (BackendConstant.RESOURCE_TYPE_VIDEO.equals(resource.getType())) {
            resourceVideoService.removeByRid(resource.getId());
        }
        // 删除资源记录
        resourceService.removeById(resource.getId());
        return JsonResponse.success();
    }

    @PostMapping("/destroy-multi")
    @SneakyThrows
    @Log(title = "资源-批量列表", businessType = BusinessTypeConstant.DELETE)
    public JsonResponse multiDestroy(@RequestBody ResourceDestroyMultiRequest req) {
        if (req.getIds() == null || req.getIds().isEmpty()) {
            return JsonResponse.error("请选择需要删除的资源");
        }

        List<Resource> resources = resourceService.chunks(req.getIds());
        if (resources == null || resources.isEmpty()) {
            return JsonResponse.success();
        }

        for (Resource resourceItem : resources) {
            // 权限校验
            if (!backendBus.isSuperAdmin()) {
                if (!resourceItem.getAdminId().equals(BCtx.getId())) {
                    throw new ServiceException("无权限");
                }
            }

            // 删除资源源文件
            minioService.removeByPath(resourceItem.getPath());
            // 如果是视频资源的话还需要删除视频的关联资源，如: 封面截图
            if (BackendConstant.RESOURCE_TYPE_VIDEO.equals(resourceItem.getType())) {
                resourceVideoService.removeByRid(resourceItem.getId());
            }
            // 删除数据库的记录
            resourceService.removeById(resourceItem.getId());
        }
        return JsonResponse.success();
    }

    @GetMapping("/{id}")
    @SneakyThrows
    @Log(title = "资源-编辑", businessType = BusinessTypeConstant.GET)
    public JsonResponse edit(@PathVariable(name = "id") Integer id) {
        Resource resource = resourceService.findOrFail(id);

        if (!backendBus.isSuperAdmin()) {
            if (!resource.getAdminId().equals(BCtx.getId())) {
                throw new ServiceException("无权限");
            }
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("resources", resource);
        data.put("category_ids", resourceService.categoryIds(id));
        return JsonResponse.data(data);
    }

    @PutMapping("/{id}")
    @SneakyThrows
    @Log(title = "资源-编辑", businessType = BusinessTypeConstant.UPDATE)
    public JsonResponse update(
            @RequestBody @Validated ResourceUpdateRequest req,
            @PathVariable(name = "id") Integer id) {
        Resource resource = resourceService.findOrFail(id);

        if (!backendBus.isSuperAdmin()) {
            if (!resource.getAdminId().equals(BCtx.getId())) {
                throw new ServiceException("无权限");
            }
        }

        resourceService.updateNameAndCategoryId(
                resource.getId(), req.getName(), req.getCategoryId());
        return JsonResponse.success();
    }
}
