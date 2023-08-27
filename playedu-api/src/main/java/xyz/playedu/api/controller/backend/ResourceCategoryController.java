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

import xyz.playedu.api.event.ResourceCategoryDestroyEvent;
import xyz.playedu.api.request.backend.ResourceCategoryParentRequest;
import xyz.playedu.api.request.backend.ResourceCategoryRequest;
import xyz.playedu.api.request.backend.ResourceCategorySortRequest;
import xyz.playedu.common.annotation.BackendPermission;
import xyz.playedu.common.annotation.Log;
import xyz.playedu.common.constant.BPermissionConstant;
import xyz.playedu.common.constant.BackendConstant;
import xyz.playedu.common.constant.BusinessTypeConstant;
import xyz.playedu.common.context.BCtx;
import xyz.playedu.common.domain.Category;
import xyz.playedu.common.exception.NotFoundException;
import xyz.playedu.common.service.CategoryService;
import xyz.playedu.common.types.JsonResponse;
import xyz.playedu.course.service.CourseCategoryService;
import xyz.playedu.course.service.CourseService;
import xyz.playedu.resource.domain.Resource;
import xyz.playedu.resource.service.ResourceCategoryService;
import xyz.playedu.resource.service.ResourceService;

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

    @Autowired private CategoryService categoryService;

    @Autowired private CourseService courseService;

    @Autowired private ResourceService resourceService;

    @Autowired private ResourceCategoryService resourceCategoryService;

    @Autowired private CourseCategoryService courseCategoryService;

    @Autowired private ApplicationContext ctx;

    @GetMapping("/index")
    @Log(title = "资源-分类-列表", businessType = BusinessTypeConstant.GET)
    public JsonResponse index() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("categories", categoryService.groupByParent());
        return JsonResponse.data(data);
    }

    @GetMapping("/categories")
    @Log(title = "资源-分类-全部分类", businessType = BusinessTypeConstant.GET)
    public JsonResponse index(
            @RequestParam(name = "parent_id", defaultValue = "0") Integer parentId) {
        List<Category> categories = categoryService.listByParentId(parentId);
        return JsonResponse.data(categories);
    }

    @GetMapping("/create")
    @Log(title = "资源-分类-新建", businessType = BusinessTypeConstant.GET)
    public JsonResponse create() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("categories", categoryService.groupByParent());
        return JsonResponse.data(data);
    }

    @BackendPermission(slug = BPermissionConstant.RESOURCE_CATEGORY)
    @PostMapping("/create")
    @Log(title = "资源-分类-新建", businessType = BusinessTypeConstant.INSERT)
    public JsonResponse store(@RequestBody @Validated ResourceCategoryRequest req)
            throws NotFoundException {
        categoryService.create(req.getName(), req.getParentId(), req.getSort());
        return JsonResponse.success();
    }

    @BackendPermission(slug = BPermissionConstant.RESOURCE_CATEGORY)
    @GetMapping("/{id}")
    @Log(title = "资源-分类-编辑", businessType = BusinessTypeConstant.GET)
    public JsonResponse edit(@PathVariable Integer id) throws NotFoundException {
        Category category = categoryService.findOrFail(id);
        return JsonResponse.data(category);
    }

    @BackendPermission(slug = BPermissionConstant.RESOURCE_CATEGORY)
    @PutMapping("/{id}")
    @Log(title = "资源-分类-编辑", businessType = BusinessTypeConstant.UPDATE)
    public JsonResponse update(@PathVariable Integer id, @RequestBody ResourceCategoryRequest req)
            throws NotFoundException {
        Category category = categoryService.findOrFail(id);
        categoryService.update(category, req.getName(), req.getParentId(), req.getSort());
        return JsonResponse.success();
    }

    @BackendPermission(slug = BPermissionConstant.RESOURCE_CATEGORY)
    @GetMapping("/{id}/destroy")
    @Log(title = "资源-分类-批量删除", businessType = BusinessTypeConstant.DELETE)
    public JsonResponse preDestroy(@PathVariable Integer id) {
        List<Integer> courseIds = courseCategoryService.getCourseIdsByCategoryId(id);
        List<Integer> rids = resourceCategoryService.getRidsByCategoryId(id);

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

    @BackendPermission(slug = BPermissionConstant.RESOURCE_CATEGORY)
    @DeleteMapping("/{id}")
    @Log(title = "资源-分类-删除", businessType = BusinessTypeConstant.DELETE)
    public JsonResponse destroy(@PathVariable Integer id) throws NotFoundException {
        Category category = categoryService.findOrFail(id);
        categoryService.deleteById(category.getId());
        ctx.publishEvent(new ResourceCategoryDestroyEvent(this, BCtx.getId(), category.getId()));
        return JsonResponse.success();
    }

    @PutMapping("/update/sort")
    @Log(title = "资源-分类-更新排序", businessType = BusinessTypeConstant.UPDATE)
    public JsonResponse resort(@RequestBody @Validated ResourceCategorySortRequest req) {
        categoryService.resetSort(req.getIds());
        return JsonResponse.success();
    }

    @PutMapping("/update/parent")
    @Log(title = "资源-分类-更新父级", businessType = BusinessTypeConstant.UPDATE)
    public JsonResponse updateParent(@RequestBody @Validated ResourceCategoryParentRequest req)
            throws NotFoundException {
        categoryService.changeParent(req.getId(), req.getParentId(), req.getIds());
        return JsonResponse.success();
    }
}
