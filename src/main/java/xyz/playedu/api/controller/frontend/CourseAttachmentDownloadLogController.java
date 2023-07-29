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

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.playedu.api.FCtx;
import xyz.playedu.api.domain.*;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.service.*;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.util.IpUtil;

import java.util.Date;

@RestController
@Slf4j
@RequestMapping("/api/v1/course/{courseId}/attachment/{attachmentId}/resource")
public class CourseAttachmentDownloadLogController {

    @Autowired private CourseService courseService;

    @Autowired private CourseAttachmentDownloadLogService courseAttachmentDownloadLogService;

    @PostMapping("/{id}")
    public JsonResponse store(@PathVariable(name = "courseId") Integer courseId,
                             @PathVariable(name = "attachmentId") Integer attachmentId,
                             @PathVariable(name = "id") Integer id) throws NotFoundException {
        Course course = courseService.findOrFail(courseId);

        CourseAttachmentDownloadLog courseAttachmentDownloadLog =
                new CourseAttachmentDownloadLog() {
                    {
                        setUserId(FCtx.getId());
                        setCourseId(course.getId());
                        setTitle(course.getTitle());
                        setCourserAttachmentId(attachmentId);
                        setRid(id);
                        setIp(IpUtil.getIpAddress());
                        setCreatedAt(new Date());
                    }
                };
        courseAttachmentDownloadLogService.save(courseAttachmentDownloadLog);
        return JsonResponse.success();
    }
}
