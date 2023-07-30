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
package xyz.playedu.api.controller.frontend;

import lombok.SneakyThrows;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import xyz.playedu.api.FCtx;
import xyz.playedu.api.domain.*;
import xyz.playedu.api.service.*;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.types.paginate.CoursePaginateFiler;
import xyz.playedu.api.types.paginate.PaginationResult;
import xyz.playedu.api.util.IpUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/3/13 16:25
 */
@RestController
@RequestMapping("/api/v1/course")
public class CourseController {

    @Autowired private CourseService courseService;

    @Autowired private CourseChapterService chapterService;

    @Autowired private CourseHourService hourService;

    @Autowired private CourseAttachmentService attachmentService;

    @Autowired private ResourceService resourceService;

    @Autowired private UserCourseRecordService userCourseRecordService;

    @Autowired private UserCourseHourRecordService userCourseHourRecordService;

    @Autowired private CourseAttachmentDownloadLogService courseAttachmentDownloadLogService;

    @GetMapping("/index")
    public JsonResponse index(@RequestParam HashMap<String, Object> params) {
        Integer page = MapUtils.getInteger(params, "page", 1);
        Integer size = MapUtils.getInteger(params, "size", 10);
        String categoryIds = MapUtils.getString(params, "category_ids");

        CoursePaginateFiler filer = new CoursePaginateFiler();
        filer.setIsShow(1);
        filer.setCategoryIds(categoryIds);

        PaginationResult<Course> result = courseService.paginate(page, size, filer);

        return JsonResponse.data(result);
    }

    @GetMapping("/{id}")
    @SneakyThrows
    public JsonResponse detail(@PathVariable(name = "id") Integer id) {
        Course course = courseService.findOrFail(id);

        List<CourseHour> courseHours = hourService.getHoursByCourseId(course.getId());

        List<CourseAttachment> attachments =
                attachmentService.getAttachmentsByCourseId(course.getId());
        if (null != attachments && attachments.size() > 0) {
            Map<Integer, Resource> resourceMap =
                    resourceService
                            .chunks(attachments.stream().map(CourseAttachment::getRid).toList())
                            .stream()
                            .collect(Collectors.toMap(Resource::getId, Function.identity()));
            attachments.forEach(
                    courseAttachment -> {
                        Resource resource = resourceMap.get(courseAttachment.getRid());
                        if(null != resource){
                            courseAttachment.setExt(resource.getExtension());
                        }
                    });
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("course", course);
        data.put("chapters", chapterService.getChaptersByCourseId(course.getId()));
        data.put(
                "hours",
                courseHours.stream().collect(Collectors.groupingBy(CourseHour::getChapterId)));
        data.put("learn_record", userCourseRecordService.find(FCtx.getId(), course.getId()));
        data.put(
                "learn_hour_records",
                userCourseHourRecordService.getRecords(FCtx.getId(), course.getId()).stream()
                        .collect(Collectors.toMap(UserCourseHourRecord::getHourId, e -> e)));
        data.put("attachments", attachments);
        return JsonResponse.data(data);
    }

    @GetMapping("/{courseId}/attach/{id}/download")
    @SneakyThrows
    public JsonResponse attachmentDownload(
            @PathVariable(name = "courseId") Integer courseId,
            @PathVariable(name = "id") Integer id) {
        CourseAttachment attachment = attachmentService.findOrFail(id, courseId);
        Resource resource = resourceService.findOrFail(attachment.getRid());

        HashMap<String, Object> data = new HashMap<>();
        data.put("download_url", resource.getUrl());

        courseAttachmentDownloadLogService.save(
                new CourseAttachmentDownloadLog() {
                    {
                        setUserId(FCtx.getId());
                        setCourseId(attachment.getCourseId());
                        setCourserAttachmentId(attachment.getId());
                        setRid(resource.getId());
                        setTitle(attachment.getTitle());
                        setIp(IpUtil.getIpAddress());
                        setCreatedAt(new Date());
                    }
                });

        return JsonResponse.data(data);
    }
}
