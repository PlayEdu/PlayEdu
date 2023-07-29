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

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import xyz.playedu.api.BCtx;
import xyz.playedu.api.annotation.Log;
import xyz.playedu.api.constant.BPermissionConstant;
import xyz.playedu.api.constant.BackendConstant;
import xyz.playedu.api.constant.BusinessType;
import xyz.playedu.api.domain.CourseChapter;
import xyz.playedu.api.domain.CourseHour;
import xyz.playedu.api.event.CourseHourCreatedEvent;
import xyz.playedu.api.event.CourseHourDestroyEvent;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.middleware.BackendPermissionMiddleware;
import xyz.playedu.api.request.backend.CourseHourMultiRequest;
import xyz.playedu.api.request.backend.CourseHourRequest;
import xyz.playedu.api.request.backend.CourseHourSortRequest;
import xyz.playedu.api.service.CourseChapterService;
import xyz.playedu.api.service.CourseHourService;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.types.SelectOption;

import java.util.*;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/2/26 17:50
 */
@RestController
@Slf4j
@RequestMapping("/backend/v1/course/{courseId}/hour")
public class CourseHourController {

    @Autowired private CourseHourService hourService;

    @Autowired private CourseChapterService chapterService;

    @Autowired private ApplicationContext ctx;

    @BackendPermissionMiddleware(slug = BPermissionConstant.COURSE)
    @GetMapping("/create")
    @Log(title = "线上课-课时-新建", businessType = BusinessType.GET)
    public JsonResponse create(@PathVariable(name = "courseId") Integer courseId) {
        // 课时类型
        List<SelectOption<String>> typeItems = new ArrayList<>();
        for (int i = 0; i < BackendConstant.COURSE_HOUR_TYPE_WHITELIST.length; i++) {
            SelectOption<String> tmpTypeItem = new SelectOption<>();
            tmpTypeItem.setKey(BackendConstant.COURSE_HOUR_TYPE_WHITELIST[i]);
            tmpTypeItem.setValue(BackendConstant.COURSE_HOUR_TYPE_WHITELIST_TEXT[i]);

            typeItems.add(tmpTypeItem);
        }

        // 读取课程下的章节
        List<CourseChapter> chapters = chapterService.getChaptersByCourseId(courseId);

        HashMap<String, Object> data = new HashMap<>();
        data.put("types", typeItems);
        data.put("chapters", chapters);

        return JsonResponse.data(data);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.COURSE)
    @PostMapping("/create")
    @Log(title = "线上课-课时-新建", businessType = BusinessType.INSERT)
    public JsonResponse store(
            @PathVariable(name = "courseId") Integer courseId,
            @RequestBody @Validated CourseHourRequest req)
            throws NotFoundException {
        // 课时类型校验
        String type = req.getType();
        if (!Arrays.asList(BackendConstant.COURSE_HOUR_TYPE_WHITELIST).contains(type)) {
            return JsonResponse.error("课时类型不支持");
        }
        // 章节id校验
        Integer chapterId = req.getChapterId();
        chapterService.findOrFail(chapterId, courseId);

        // 课时重复添加校验
        List<Integer> existsRids =
                hourService.getRidsByCourseId(courseId, BackendConstant.RESOURCE_TYPE_VIDEO);
        if (existsRids != null) {
            if (existsRids.contains(req.getRid())) {
                return JsonResponse.error("课时已存在");
            }
        }

        CourseHour courseHour =
                hourService.create(
                        courseId,
                        chapterId,
                        req.getSort(),
                        req.getTitle(),
                        type,
                        req.getRid(),
                        req.getDuration());
        ctx.publishEvent(
                new CourseHourCreatedEvent(
                        this,
                        BCtx.getId(),
                        courseHour.getCourseId(),
                        courseHour.getChapterId(),
                        courseHour.getId()));
        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.COURSE)
    @PostMapping("/create-batch")
    @Transactional
    @Log(title = "线上课-课时-批量导入", businessType = BusinessType.INSERT)
    public JsonResponse storeMulti(
            @PathVariable(name = "courseId") Integer courseId,
            @RequestBody @Validated CourseHourMultiRequest req) {
        if (req.getHours().size() == 0) {
            return JsonResponse.error("参数为空");
        }

        List<Integer> existsRids =
                hourService.getRidsByCourseId(courseId, BackendConstant.RESOURCE_TYPE_VIDEO);

        List<CourseHour> hours = new ArrayList<>();
        Date now = new Date();

        for (CourseHourMultiRequest.HourItem item : req.getHours()) {
            if (existsRids.contains(item.getRid())) {
                return JsonResponse.error("课时《" + item.getTitle() + "》已存在");
            }

            hours.add(
                    new CourseHour() {
                        {
                            setCourseId(courseId);
                            setChapterId(item.getChapterId());
                            setSort(item.getSort());
                            setType(item.getType());
                            setRid(item.getRid());
                            setTitle(item.getTitle());
                            setDuration(item.getDuration());
                            setCreatedAt(now);
                        }
                    });
        }

        hourService.saveBatch(hours);

        // 只需要发布一次event就可以了
        CourseHour firstHour = hours.get(0);
        ctx.publishEvent(
                new CourseHourCreatedEvent(
                        this,
                        BCtx.getId(),
                        firstHour.getCourseId(),
                        firstHour.getChapterId(),
                        firstHour.getId()));

        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.COURSE)
    @GetMapping("/{id}")
    @Log(title = "线上课-课时-编辑", businessType = BusinessType.GET)
    public JsonResponse edit(
            @PathVariable(name = "courseId") Integer courseId,
            @PathVariable(name = "id") Integer id)
            throws NotFoundException {
        CourseHour courseHour = hourService.findOrFail(id, courseId);
        return JsonResponse.data(courseHour);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.COURSE)
    @PutMapping("/{id}")
    @Log(title = "线上课-课时-编辑", businessType = BusinessType.UPDATE)
    public JsonResponse update(
            @PathVariable(name = "courseId") Integer courseId,
            @PathVariable(name = "id") Integer id,
            @RequestBody @Validated CourseHourRequest req)
            throws NotFoundException {
        CourseHour courseHour = hourService.findOrFail(id, courseId);
        // 章节id校验
        Integer chapterId = req.getChapterId();
        chapterService.findOrFail(chapterId, courseId);

        hourService.update(courseHour, chapterId, req.getSort(), req.getTitle(), req.getDuration());
        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.COURSE)
    @DeleteMapping("/{id}")
    @Log(title = "线上课-课时-删除", businessType = BusinessType.DELETE)
    public JsonResponse destroy(
            @PathVariable(name = "courseId") Integer courseId,
            @PathVariable(name = "id") Integer id)
            throws NotFoundException {
        CourseHour courseHour = hourService.findOrFail(id, courseId);
        hourService.removeById(courseHour.getId());
        ctx.publishEvent(
                new CourseHourDestroyEvent(
                        this,
                        BCtx.getId(),
                        courseHour.getCourseId(),
                        courseHour.getChapterId(),
                        courseHour.getId()));
        return JsonResponse.success();
    }

    @PutMapping("/update/sort")
    @Log(title = "线上课-课时-更新排序", businessType = BusinessType.UPDATE)
    public JsonResponse updateSort(
            @PathVariable(name = "courseId") Integer courseId,
            @RequestBody @Validated CourseHourSortRequest req) {
        hourService.updateSort(req.getIds(), courseId);
        return JsonResponse.success();
    }
}
