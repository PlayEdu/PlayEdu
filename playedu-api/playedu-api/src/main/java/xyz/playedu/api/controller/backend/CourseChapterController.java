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
import xyz.playedu.api.event.CourseChapterDestroyEvent;
import xyz.playedu.api.request.backend.CourseChapterRequest;
import xyz.playedu.api.request.backend.CourseChapterSortRequest;
import xyz.playedu.common.annotation.BackendPermission;
import xyz.playedu.common.annotation.Log;
import xyz.playedu.common.constant.BPermissionConstant;
import xyz.playedu.common.constant.BusinessTypeConstant;
import xyz.playedu.common.context.BCtx;
import xyz.playedu.common.exception.NotFoundException;
import xyz.playedu.common.types.JsonResponse;
import xyz.playedu.course.domain.CourseChapter;
import xyz.playedu.course.service.CourseChapterService;
import xyz.playedu.course.service.CourseHourService;

@RestController
@RequestMapping("/backend/v1/course/{courseId}/chapter")
public class CourseChapterController {

    @Autowired private CourseChapterService chapterService;

    @Autowired private CourseHourService hourService;

    @Autowired private ApplicationContext ctx;

    @BackendPermission(slug = BPermissionConstant.COURSE)
    @PostMapping("/create")
    @Log(title = "线上课-章节-新建", businessType = BusinessTypeConstant.GET)
    public JsonResponse store(
            @PathVariable(name = "courseId") Integer courseId,
            @RequestBody @Validated CourseChapterRequest req) {
        chapterService.create(courseId, req.getName(), req.getSort());
        return JsonResponse.success();
    }

    @BackendPermission(slug = BPermissionConstant.COURSE)
    @GetMapping("/{id}")
    @Log(title = "线上课-章节-编辑", businessType = BusinessTypeConstant.GET)
    public JsonResponse edit(
            @PathVariable(name = "courseId") Integer courseId,
            @PathVariable(name = "id") Integer id)
            throws NotFoundException {
        CourseChapter chapter = chapterService.findOrFail(id, courseId);
        return JsonResponse.data(chapter);
    }

    @BackendPermission(slug = BPermissionConstant.COURSE)
    @PutMapping("/{id}")
    @Log(title = "线上课-章节-编辑", businessType = BusinessTypeConstant.UPDATE)
    public JsonResponse update(
            @PathVariable(name = "courseId") Integer courseId,
            @PathVariable(name = "id") Integer id,
            @RequestBody @Validated CourseChapterRequest req)
            throws NotFoundException {
        CourseChapter chapter = chapterService.findOrFail(id, courseId);
        chapterService.update(chapter, req.getName(), req.getSort());
        return JsonResponse.success();
    }

    @BackendPermission(slug = BPermissionConstant.COURSE)
    @DeleteMapping("/{id}")
    @Log(title = "线上课-章节-删除", businessType = BusinessTypeConstant.DELETE)
    public JsonResponse destroy(
            @PathVariable(name = "courseId") Integer courseId,
            @PathVariable(name = "id") Integer id)
            throws NotFoundException {
        CourseChapter chapter = chapterService.findOrFail(id, courseId);
        if (hourService.getCountByChapterId(chapter.getId()) > 0) {
            return JsonResponse.error("当前章节下面存在课时无法删除");
        }
        chapterService.removeById(chapter.getId());
        ctx.publishEvent(
                new CourseChapterDestroyEvent(
                        this, BCtx.getId(), chapter.getCourseId(), chapter.getId()));
        return JsonResponse.success();
    }

    @PutMapping("/update/sort")
    @Log(title = "线上课-章节-更新排序", businessType = BusinessTypeConstant.UPDATE)
    public JsonResponse updateSort(
            @PathVariable(name = "courseId") Integer courseId,
            @RequestBody @Validated CourseChapterSortRequest req) {
        chapterService.updateSort(req.getIds(), courseId);
        return JsonResponse.success();
    }
}
