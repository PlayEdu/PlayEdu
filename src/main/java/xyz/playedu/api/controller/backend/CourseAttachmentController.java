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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import xyz.playedu.api.constant.BPermissionConstant;
import xyz.playedu.api.constant.BackendConstant;
import xyz.playedu.api.domain.CourseAttachment;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.middleware.BackendPermissionMiddleware;
import xyz.playedu.api.request.backend.CourseAttachmentMultiRequest;
import xyz.playedu.api.request.backend.CourseAttachmentRequest;
import xyz.playedu.api.request.backend.CourseAttachmentSortRequest;
import xyz.playedu.api.service.CourseAttachmentService;
import xyz.playedu.api.types.JsonResponse;

import java.util.*;

@RestController
@Slf4j
@RequestMapping("/backend/v1/course/{courseId}/attachment")
public class CourseAttachmentController {

    @Autowired private CourseAttachmentService attachmentService;

    @BackendPermissionMiddleware(slug = BPermissionConstant.COURSE)
    @PostMapping("/create")
    public JsonResponse store(
            @PathVariable(name = "courseId") Integer courseId,
            @RequestBody @Validated CourseAttachmentRequest req)
            throws NotFoundException {
        // 附件类型校验
        String type = req.getType();
        if (!BackendConstant.RESOURCE_TYPE_ATTACHMENT.contains(type)) {
            return JsonResponse.error("附件类型不支持");
        }

        // 课时重复添加校验
        List<Integer> existsRids = attachmentService.getRidsByCourseId(courseId);
        if (existsRids != null) {
            if (existsRids.contains(req.getRid())) {
                return JsonResponse.error("附件已存在");
            }
        }

        CourseAttachment courseAttachment =
                attachmentService.create(
                        courseId, req.getSort(), req.getTitle(), type, req.getRid());
        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.COURSE)
    @PostMapping("/create-batch")
    @Transactional
    public JsonResponse storeMulti(
            @PathVariable(name = "courseId") Integer courseId,
            @RequestBody @Validated CourseAttachmentMultiRequest req) {
        if (req.getAttachments().size() == 0) {
            return JsonResponse.error("参数为空");
        }

        List<Integer> existsRids = attachmentService.getRidsByCourseId(courseId);

        List<CourseAttachment> attachments = new ArrayList<>();
        Date now = new Date();

        for (CourseAttachmentMultiRequest.AttachmentItem item : req.getAttachments()) {
            if (existsRids.contains(item.getRid())) {
                return JsonResponse.error("附件《" + item.getTitle() + "》已存在");
            }

            attachments.add(
                    new CourseAttachment() {
                        {
                            setCourseId(courseId);
                            setSort(item.getSort());
                            setType(item.getType());
                            setRid(item.getRid());
                            setTitle(item.getTitle());
                            setCreatedAt(now);
                        }
                    });
        }

        attachmentService.saveBatch(attachments);
        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.COURSE)
    @GetMapping("/{id}")
    public JsonResponse edit(
            @PathVariable(name = "courseId") Integer courseId,
            @PathVariable(name = "id") Integer id)
            throws NotFoundException {
        CourseAttachment courseAttachment = attachmentService.findOrFail(id, courseId);
        return JsonResponse.data(courseAttachment);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.COURSE)
    @PutMapping("/{id}")
    public JsonResponse update(
            @PathVariable(name = "courseId") Integer courseId,
            @PathVariable(name = "id") Integer id,
            @RequestBody @Validated CourseAttachmentRequest req)
            throws NotFoundException {
        CourseAttachment courseAttachment = attachmentService.findOrFail(id, courseId);
        attachmentService.update(courseAttachment, req.getSort(), req.getTitle());
        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.COURSE)
    @DeleteMapping("/{id}")
    public JsonResponse destroy(
            @PathVariable(name = "courseId") Integer courseId,
            @PathVariable(name = "id") Integer id)
            throws NotFoundException {
        CourseAttachment courseAttachment = attachmentService.findOrFail(id, courseId);
        attachmentService.removeById(courseAttachment.getId());
        return JsonResponse.success();
    }

    @PutMapping("/update/sort")
    public JsonResponse updateSort(
            @PathVariable(name = "courseId") Integer courseId,
            @RequestBody @Validated CourseAttachmentSortRequest req) {
        attachmentService.updateSort(req.getIds(), courseId);
        return JsonResponse.success();
    }
}
