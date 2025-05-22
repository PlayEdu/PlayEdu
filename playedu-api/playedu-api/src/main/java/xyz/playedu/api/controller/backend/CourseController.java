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

import java.text.ParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.playedu.api.event.CourseDestroyEvent;
import xyz.playedu.api.request.backend.CourseRequest;
import xyz.playedu.common.annotation.BackendPermission;
import xyz.playedu.common.annotation.Log;
import xyz.playedu.common.bus.BackendBus;
import xyz.playedu.common.constant.BPermissionConstant;
import xyz.playedu.common.constant.BusinessTypeConstant;
import xyz.playedu.common.context.BCtx;
import xyz.playedu.common.domain.AdminUser;
import xyz.playedu.common.domain.Category;
import xyz.playedu.common.domain.Department;
import xyz.playedu.common.exception.NotFoundException;
import xyz.playedu.common.service.*;
import xyz.playedu.common.types.JsonResponse;
import xyz.playedu.common.types.paginate.CoursePaginateFiler;
import xyz.playedu.common.types.paginate.PaginationResult;
import xyz.playedu.common.util.StringUtil;
import xyz.playedu.course.domain.*;
import xyz.playedu.course.service.CourseAttachmentService;
import xyz.playedu.course.service.CourseChapterService;
import xyz.playedu.course.service.CourseHourService;
import xyz.playedu.course.service.CourseService;
import xyz.playedu.resource.domain.Resource;
import xyz.playedu.resource.service.ResourceService;

@RestController
@Slf4j
@RequestMapping("/backend/v1/course")
public class CourseController {

    @Autowired private CourseService courseService;

    @Autowired private CategoryService categoryService;

    @Autowired private CourseChapterService chapterService;

    @Autowired private CourseHourService hourService;

    @Autowired private CourseAttachmentService attachmentService;

    @Autowired private ResourceService resourceService;

    @Autowired private DepartmentService departmentService;

    @Autowired private AdminUserService adminUserService;

    @Autowired private ApplicationContext ctx;

    @Autowired private BackendBus backendBus;

    @BackendPermission(slug = BPermissionConstant.COURSE)
    @GetMapping("/index")
    @Log(title = "线上课-列表", businessType = BusinessTypeConstant.GET)
    public JsonResponse index(@RequestParam HashMap<String, Object> params) {
        Integer page = MapUtils.getInteger(params, "page", 1);
        Integer size = MapUtils.getInteger(params, "size", 10);
        String sortField = MapUtils.getString(params, "sort_field");
        String sortAlgo = MapUtils.getString(params, "sort_algo");

        String title = MapUtils.getString(params, "title");
        String depIds = MapUtils.getString(params, "dep_ids");
        String categoryIds = MapUtils.getString(params, "category_ids");
        Integer isRequired = MapUtils.getInteger(params, "is_required");

        // 获取所有子部门
        Set<Integer> alldepIdsSet = new HashSet<>();
        if (StringUtil.isNotEmpty(depIds)) {
            String[] depIdArr = depIds.split(",");
            if (StringUtil.isNotEmpty(depIdArr)) {
                for (String depIdStr : depIdArr) {
                    Integer depId = Integer.parseInt(depIdStr);
                    alldepIdsSet.add(depId);
                    // 查询所有的子部门
                    List<Department> departmentList =
                            departmentService.getChildDepartmentsByParentId(depId);
                    if (StringUtil.isNotEmpty(departmentList)) {
                        for (Department department : departmentList) {
                            alldepIdsSet.add(department.getId());
                        }
                    }
                }
            }
        }
        List<Integer> alldepIds = new ArrayList<>();
        if ("0".equals(depIds)) {
            alldepIds.add(0);
        }
        if (StringUtil.isNotEmpty(alldepIdsSet)) {
            alldepIds.addAll(alldepIdsSet);
        }

        // 获取所有子类
        Set<Integer> allCategoryIdsSet = new HashSet<>();
        if (StringUtil.isNotEmpty(categoryIds)) {
            String[] categoryIdArr = categoryIds.split(",");
            if (StringUtil.isNotEmpty(categoryIdArr)) {
                for (String categoryIdStr : categoryIdArr) {
                    Integer categoryId = Integer.parseInt(categoryIdStr);
                    allCategoryIdsSet.add(categoryId);
                    // 查询所有的子分类
                    List<Category> categoryList =
                            categoryService.getChildCategorysByParentId(categoryId);
                    if (StringUtil.isNotEmpty(categoryList)) {
                        for (Category category : categoryList) {
                            allCategoryIdsSet.add(category.getId());
                        }
                    }
                }
            }
        }
        List<Integer> allCategoryIds = new ArrayList<>();
        if ("0".equals(categoryIds)) {
            allCategoryIds.add(0);
        }
        if (StringUtil.isNotEmpty(allCategoryIdsSet)) {
            allCategoryIds.addAll(allCategoryIdsSet);
        }

        CoursePaginateFiler filter = new CoursePaginateFiler();
        filter.setTitle(title);
        filter.setSortField(sortField);
        filter.setSortAlgo(sortAlgo);
        filter.setCategoryIds(allCategoryIds);
        filter.setDepIds(alldepIds);
        filter.setIsRequired(isRequired);

        if (!backendBus.isSuperAdmin()) {
            filter.setAdminId(BCtx.getId());
        }

        PaginationResult<Course> result = courseService.paginate(page, size, filter);

        HashMap<String, Object> data = new HashMap<>();
        data.put("data", result.getData());
        data.put("total", result.getTotal());

        List<Integer> courseIds = result.getData().stream().map(Course::getId).toList();
        data.put("course_category_ids", courseService.getCategoryIdsGroup(courseIds));
        data.put("course_dep_ids", courseService.getDepIdsGroup(courseIds));
        data.put("categories", categoryService.id2name());
        data.put("departments", departmentService.id2name());

        // 操作人
        data.put("admin_users", new HashMap<>());
        if (!result.getData().isEmpty()) {
            Map<Integer, String> adminUsers =
                    adminUserService
                            .chunks(result.getData().stream().map(Course::getAdminId).toList())
                            .stream()
                            .collect(Collectors.toMap(AdminUser::getId, AdminUser::getName));
            data.put("admin_users", adminUsers);
        }

        // 课程封面资源ID
        data.put(
                "resource_url",
                resourceService.chunksPreSignUrlByIds(
                        result.getData().stream().map(Course::getThumb).toList()));

        return JsonResponse.data(data);
    }

    @BackendPermission(slug = BPermissionConstant.COURSE)
    @GetMapping("/create")
    public JsonResponse create() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("categories", categoryService.groupByParent());
        return JsonResponse.data(data);
    }

    @BackendPermission(slug = BPermissionConstant.COURSE)
    @PostMapping("/create")
    @Transactional
    @Log(title = "线上课-新建", businessType = BusinessTypeConstant.INSERT)
    public JsonResponse store(@RequestBody @Validated CourseRequest req) throws ParseException {
        if (req.getShortDesc().length() > 200) {
            return JsonResponse.error("课程简短介绍不能超过200字");
        }
        Course course =
                courseService.createWithCategoryIdsAndDepIds(
                        req.getTitle(),
                        req.getThumb(),
                        req.getShortDesc(),
                        req.getIsRequired(),
                        req.getIsShow(),
                        req.getCategoryIds(),
                        req.getDepIds(),
                        BCtx.getId());

        Date now = new Date();
        int classHourCount = 0;

        if (!req.getHours().isEmpty()) { // 无章节课时配置
            List<CourseHour> insertHours = new ArrayList<>();
            final Integer[] chapterSort = {0};
            for (CourseRequest.HourItem hourItem : req.getHours()) {
                insertHours.add(
                        new CourseHour() {
                            {
                                setCourseId(course.getId());
                                setChapterId(0);
                                setSort(chapterSort[0]++);
                                setTitle(hourItem.getName());
                                setType(hourItem.getType());
                                setDuration(hourItem.getDuration());
                                setRid(hourItem.getRid());
                                setCreatedAt(now);
                            }
                        });
            }
            if (!insertHours.isEmpty()) {
                hourService.saveBatch(insertHours);
                classHourCount = insertHours.size();
            }
        } else {
            if (req.getChapters().isEmpty()) {
                return JsonResponse.error("请配置课时");
            }

            List<CourseHour> insertHours = new ArrayList<>();
            final Integer[] chapterSort = {0};

            for (CourseRequest.ChapterItem chapterItem : req.getChapters()) {
                CourseChapter tmpChapter =
                        new CourseChapter() {
                            {
                                setCourseId(course.getId());
                                setSort(chapterSort[0]++);
                                setName(chapterItem.getName());
                                setCreatedAt(now);
                                setUpdatedAt(now);
                            }
                        };

                chapterService.save(tmpChapter);

                final Integer[] hourSort = {0};
                for (CourseRequest.HourItem hourItem : chapterItem.getHours()) {
                    insertHours.add(
                            new CourseHour() {
                                {
                                    setChapterId(tmpChapter.getId());
                                    setCourseId(course.getId());
                                    setSort(hourSort[0]++);
                                    setTitle(hourItem.getName());
                                    setType(hourItem.getType());
                                    setDuration(hourItem.getDuration());
                                    setRid(hourItem.getRid());
                                    setCreatedAt(now);
                                }
                            });
                }
            }
            if (!insertHours.isEmpty()) {
                hourService.saveBatch(insertHours);
                classHourCount = insertHours.size();
            }
        }

        if (classHourCount > 0) {
            courseService.updateClassHour(course.getId(), classHourCount);
        }

        // 课程附件
        if (null != req.getAttachments() && !req.getAttachments().isEmpty()) {
            List<CourseAttachment> insertAttachments = new ArrayList<>();
            final Integer[] sort = {0};
            for (CourseRequest.AttachmentItem attachmentItem : req.getAttachments()) {
                insertAttachments.add(
                        new CourseAttachment() {
                            {
                                setCourseId(course.getId());
                                setSort(sort[0]++);
                                setTitle(attachmentItem.getName());
                                setType(attachmentItem.getType());
                                setRid(attachmentItem.getRid());
                                setCreatedAt(now);
                            }
                        });
            }
            if (!insertAttachments.isEmpty()) {
                attachmentService.saveBatch(insertAttachments);
            }
        }

        return JsonResponse.success();
    }

    @BackendPermission(slug = BPermissionConstant.COURSE)
    @GetMapping("/{id}")
    @Log(title = "线上课-编辑", businessType = BusinessTypeConstant.GET)
    public JsonResponse edit(@PathVariable(name = "id") Integer id) throws NotFoundException {
        Course course = courseService.findOrFail(id);
        if (!backendBus.isSuperAdmin() && !course.getAdminId().equals(BCtx.getId())) {
            return JsonResponse.error("无权限操作");
        }
        List<Integer> rids = new ArrayList<>();
        rids.add(course.getThumb());

        List<Integer> depIds = courseService.getDepIdsByCourseId(course.getId());
        List<Integer> categoryIds = courseService.getCategoryIdsByCourseId(course.getId());
        List<CourseChapter> chapters = chapterService.getChaptersByCourseId(course.getId());
        List<CourseHour> hours = hourService.getHoursByCourseId(course.getId());
        List<CourseAttachment> attachments =
                attachmentService.getAttachmentsByCourseId(course.getId());
        if (StringUtil.isNotEmpty(attachments)) {
            List<Integer> attachmentIds =
                    attachments.stream().map(CourseAttachment::getRid).toList();
            rids.addAll(attachmentIds);
            Map<Integer, Resource> resourceMap =
                    resourceService.chunks(attachmentIds).stream()
                            .collect(Collectors.toMap(Resource::getId, Function.identity()));
            attachments.forEach(
                    courseAttachment -> {
                        Resource resource = resourceMap.get(courseAttachment.getRid());
                        if (StringUtil.isNotNull(resource)) {
                            courseAttachment.setExt(resource.getExtension());
                        }
                    });
        }

        // 部门名称
        Map<Integer, String> deps = new HashMap<>();
        if (StringUtil.isNotEmpty(depIds)) {
            deps =
                    departmentService.chunk(depIds).stream()
                            .collect(Collectors.toMap(Department::getId, Department::getName));
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("course", course);
        data.put("dep_ids", depIds); // 已关联的部门
        data.put("category_ids", categoryIds); // 已关联的分类
        data.put("chapters", chapters);
        data.put("hours", hours.stream().collect(Collectors.groupingBy(CourseHour::getChapterId)));
        data.put("attachments", attachments);
        data.put("deps", deps);
        // 获取签名url
        data.put("resource_url", resourceService.chunksPreSignUrlByIds(rids));
        return JsonResponse.data(data);
    }

    @BackendPermission(slug = BPermissionConstant.COURSE)
    @PutMapping("/{id}")
    @Transactional
    @Log(title = "线上课-编辑", businessType = BusinessTypeConstant.UPDATE)
    public JsonResponse update(
            @PathVariable(name = "id") Integer id, @RequestBody @Validated CourseRequest req)
            throws NotFoundException {
        Course course = courseService.findOrFail(id);
        if (!backendBus.isSuperAdmin() && !course.getAdminId().equals(BCtx.getId())) {
            return JsonResponse.error("无权限操作");
        }

        courseService.updateWithCategoryIdsAndDepIds(
                course,
                req.getTitle(),
                req.getThumb(),
                req.getShortDesc(),
                req.getIsRequired(),
                req.getIsShow(),
                req.getSortAt(),
                req.getCategoryIds(),
                req.getDepIds());

        return JsonResponse.success();
    }

    @BackendPermission(slug = BPermissionConstant.COURSE)
    @DeleteMapping("/{id}")
    @Log(title = "线上课-删除", businessType = BusinessTypeConstant.DELETE)
    @SneakyThrows
    public JsonResponse destroy(@PathVariable(name = "id") Integer id) {
        Course course = courseService.findOrFail(id);
        if (!backendBus.isSuperAdmin() && !course.getAdminId().equals(BCtx.getId())) {
            return JsonResponse.error("无权限操作");
        }

        courseService.removeById(id);

        ctx.publishEvent(new CourseDestroyEvent(this, BCtx.getId(), id));

        return JsonResponse.success();
    }
}
