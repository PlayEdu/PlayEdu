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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import xyz.playedu.api.BCtx;
import xyz.playedu.api.constant.BPermissionConstant;
import xyz.playedu.api.constant.BackendConstant;
import xyz.playedu.api.domain.Resource;
import xyz.playedu.api.domain.ResourceCategory;
import xyz.playedu.api.event.ResourceCategoryDestroyEvent;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.middleware.BackendPermissionMiddleware;
import xyz.playedu.api.request.backend.ResourceCategoryParentRequest;
import xyz.playedu.api.request.backend.ResourceCategoryRequest;
import xyz.playedu.api.request.backend.ResourceCategorySortRequest;
import xyz.playedu.api.service.CourseService;
import xyz.playedu.api.service.ResourceCategoryService;
import xyz.playedu.api.service.ResourceService;
import xyz.playedu.api.types.JsonResponse;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/2/23 09:46
 */
@RestController
@RequestMapping("/backend/v1/resource-category")
public class ResourceCategoryController {

    @Autowired private ResourceCategoryService categoryService;

    @Autowired private CourseService courseService;

    @Autowired private ResourceService resourceService;

    @Autowired private ApplicationContext ctx;

    @GetMapping("/index")
    public JsonResponse index() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("categories", categoryService.groupByParent());
        return JsonResponse.data(data);
    }

    @GetMapping("/categories")
    public JsonResponse index(
            @RequestParam(name = "parent_id", defaultValue = "0") Integer parentId) {
        List<ResourceCategory> categories = categoryService.listByParentId(parentId);
        return JsonResponse.data(categories);
    }

    @GetMapping("/create")
    public JsonResponse create() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("categories", categoryService.groupByParent());
        return JsonResponse.data(data);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.RESOURCE_CATEGORY)
    @PostMapping("/create")
    public JsonResponse store(@RequestBody @Validated ResourceCategoryRequest req)
            throws NotFoundException {
        categoryService.create(req.getName(), req.getParentId(), req.getSort());
        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.RESOURCE_CATEGORY)
    @GetMapping("/{id}")
    public JsonResponse edit(@PathVariable Integer id) throws NotFoundException {
        ResourceCategory category = categoryService.findOrFail(id);
        return JsonResponse.data(category);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.RESOURCE_CATEGORY)
    @PutMapping("/{id}")
    public JsonResponse update(@PathVariable Integer id, @RequestBody ResourceCategoryRequest req)
            throws NotFoundException {
        ResourceCategory category = categoryService.findOrFail(id);
        categoryService.update(category, req.getName(), req.getParentId(), req.getSort());
        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.RESOURCE_CATEGORY)
    @GetMapping("/{id}/destroy")
    public JsonResponse preDestroy(@PathVariable Integer id) {
        List<Integer> courseIds = categoryService.getCourseIdsById(id);
        List<Integer> rids = categoryService.getRidsById(id);

        HashMap<String, Object> data = new HashMap<>();
        data.put("children", categoryService.listByParentId(id));
        data.put("courses", new ArrayList<>());
        data.put("videos", new ArrayList<>());
        data.put("images", new ArrayList<>());

        if (courseIds != null && courseIds.size() > 0) {
            data.put(
                    "courses",
                    courseService.chunks(
                            courseIds,
                            new ArrayList<>() {
                                {
                                    add("id");
                                    add("title");
                                }
                            }));
        }

        if (rids != null && rids.size() > 0) {
            Map<String, List<Resource>> resources =
                    resourceService
                            .chunks(
                                    rids,
                                    new ArrayList<>() {
                                        {
                                            add("id");
                                            add("admin_id");
                                            add("type");
                                            add("name");
                                            add("url");
                                        }
                                    })
                            .stream()
                            .collect(Collectors.groupingBy(Resource::getType));
            data.put("videos", resources.get(BackendConstant.RESOURCE_TYPE_VIDEO));
            data.put("images", resources.get(BackendConstant.RESOURCE_TYPE_IMAGE));
        }

        return JsonResponse.data(data);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.RESOURCE_CATEGORY)
    @DeleteMapping("/{id}")
    public JsonResponse destroy(@PathVariable Integer id) throws NotFoundException {
        ResourceCategory category = categoryService.findOrFail(id);
        categoryService.deleteById(category.getId());
        ctx.publishEvent(new ResourceCategoryDestroyEvent(this, BCtx.getId(), category.getId()));
        return JsonResponse.success();
    }

    @PutMapping("/update/sort")
    public JsonResponse resort(@RequestBody @Validated ResourceCategorySortRequest req) {
        categoryService.resetSort(req.getIds());
        return JsonResponse.success();
    }

    @PutMapping("/update/parent")
    public JsonResponse updateParent(@RequestBody @Validated ResourceCategoryParentRequest req)
            throws NotFoundException {
        categoryService.changeParent(req.getId(), req.getParentId(), req.getIds());
        return JsonResponse.success();
    }
}
