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

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.playedu.common.context.FCtx;
import xyz.playedu.common.types.JsonResponse;
import xyz.playedu.common.util.IpUtil;
import xyz.playedu.course.domain.*;
import xyz.playedu.course.service.*;
import xyz.playedu.resource.domain.Resource;
import xyz.playedu.resource.service.ResourceService;

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

    @GetMapping("/{id}")
    @SneakyThrows
    public JsonResponse detail(@PathVariable(name = "id") Integer id) {
        Course course = courseService.findOrFail(id);

        List<CourseHour> courseHours = hourService.getHoursByCourseId(course.getId());

        List<Integer> rids = new ArrayList<>();
        rids.add(course.getThumb());

        List<CourseAttachment> attachments =
                attachmentService.getAttachmentsByCourseId(course.getId());
        if (null != attachments && !attachments.isEmpty()) {
            List<Integer> attachmentIds =
                    attachments.stream().map(CourseAttachment::getRid).toList();
            rids.addAll(attachmentIds);
            Map<Integer, Resource> resourceMap =
                    resourceService
                            .chunks(attachments.stream().map(CourseAttachment::getRid).toList())
                            .stream()
                            .collect(Collectors.toMap(Resource::getId, Function.identity()));
            attachments.forEach(
                    courseAttachment -> {
                        Resource resource = resourceMap.get(courseAttachment.getRid());
                        if (null != resource) {
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

        // 获取签名url
        data.put("resource_url", resourceService.chunksPreSignUrlByIds(rids));

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
        // 获取资源签名url
        data.put("resource_url", resourceService.downloadResById(attachment.getRid()));

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
