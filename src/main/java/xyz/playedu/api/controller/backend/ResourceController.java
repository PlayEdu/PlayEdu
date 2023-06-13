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

import xyz.playedu.api.BCtx;
import xyz.playedu.api.bus.BackendBus;
import xyz.playedu.api.constant.BPermissionConstant;
import xyz.playedu.api.constant.BackendConstant;
import xyz.playedu.api.domain.AdminUser;
import xyz.playedu.api.domain.Resource;
import xyz.playedu.api.domain.ResourceVideo;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.middleware.BackendPermissionMiddleware;
import xyz.playedu.api.request.backend.ResourceDestroyMultiRequest;
import xyz.playedu.api.request.backend.ResourceUpdateRequest;
import xyz.playedu.api.service.AdminUserService;
import xyz.playedu.api.service.MinioService;
import xyz.playedu.api.service.ResourceService;
import xyz.playedu.api.service.ResourceVideoService;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.types.paginate.PaginationResult;
import xyz.playedu.api.types.paginate.ResourcePaginateFilter;

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
    public JsonResponse index(@RequestParam HashMap<String, Object> params) {
        Integer page = MapUtils.getInteger(params, "page", 1);
        Integer size = MapUtils.getInteger(params, "size", 10);
        String sortField = MapUtils.getString(params, "sort_field");
        String sortAlgo = MapUtils.getString(params, "sort_algo");
        String name = MapUtils.getString(params, "name");
        String type = MapUtils.getString(params, "type");
        String categoryIds = MapUtils.getString(params, "category_ids");

        if (type == null || type.trim().length() == 0) {
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
        if (result.getData().size() > 0) {
            Map<Integer, String> adminUsers =
                    adminUserService
                            .chunks(result.getData().stream().map(Resource::getAdminId).toList())
                            .stream()
                            .collect(Collectors.toMap(AdminUser::getId, AdminUser::getName));
            data.put("admin_users", adminUsers);
        }

        return JsonResponse.data(data);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.RESOURCE_DESTROY)
    @DeleteMapping("/{id}")
    @Transactional
    public JsonResponse destroy(@PathVariable(name = "id") Integer id) throws NotFoundException {
        Resource resource = resourceService.findOrFail(id);
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

    @BackendPermissionMiddleware(slug = BPermissionConstant.RESOURCE_DESTROY)
    @PostMapping("/destroy-multi")
    @Transactional
    public JsonResponse multiDestroy(@RequestBody ResourceDestroyMultiRequest req) {
        if (req.getIds() == null || req.getIds().size() == 0) {
            return JsonResponse.error("请选择需要删除的资源");
        }
        List<Resource> resources = resourceService.chunks(req.getIds());
        if (resources == null || resources.size() == 0) {
            return JsonResponse.success();
        }
        for (Resource resourceItem : resources) {
            minioService.removeByPath(resourceItem.getPath());
            if (BackendConstant.RESOURCE_TYPE_VIDEO.equals(resourceItem.getType())) {
                resourceVideoService.removeByRid(resourceItem.getId());
            }
            resourceService.removeById(resourceItem.getId());
        }
        return JsonResponse.success();
    }

    @PutMapping("/{id}")
    @SneakyThrows
    public JsonResponse update(
            @RequestBody @Validated ResourceUpdateRequest req,
            @PathVariable(name = "id") Integer id) {
        Resource resource = resourceService.findOrFail(id);
        resourceService.updateNameAndCategoryId(
                resource.getId(), req.getName(), req.getCategoryId());
        return JsonResponse.success();
    }
}
