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
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.playedu.api.annotation.Log;
import xyz.playedu.api.constant.BusinessType;
import xyz.playedu.api.domain.*;
import xyz.playedu.api.service.*;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.types.paginate.CourseAttachmentDownloadLogPaginateFiler;
import xyz.playedu.api.types.paginate.PaginationResult;

import java.util.*;

@RestController
@Slf4j
@RequestMapping("/backend/v1/course/attachment/download/log")
public class CourseAttachmentDownloadLogController {

    @Autowired private CourseAttachmentDownloadLogService courseAttachmentDownloadLogService;

    @GetMapping("/index")
    @Log(title = "学员下载课件记录-列表", businessType = BusinessType.GET)
    public JsonResponse index(@RequestParam HashMap<String, Object> params) {
        Integer page = MapUtils.getInteger(params, "page", 1);
        Integer size = MapUtils.getInteger(params, "size", 10);
        String sortField = MapUtils.getString(params, "sort_field");
        String sortAlgo = MapUtils.getString(params, "sort_algo");

        Integer userId = MapUtils.getInteger(params, "user_id");
        Integer courseId = MapUtils.getInteger(params, "course_id");
        String title = MapUtils.getString(params, "title");
        Integer courserAttachmentId = MapUtils.getInteger(params, "courser_attachment_id");
        Integer rid = MapUtils.getInteger(params, "rid");

        CourseAttachmentDownloadLogPaginateFiler filter = new CourseAttachmentDownloadLogPaginateFiler();
        filter.setUserId(userId);
        filter.setCourseId(courseId);
        filter.setTitle(title);
        filter.setCourserAttachmentId(courserAttachmentId);
        filter.setRid(rid);
        filter.setSortField(sortField);
        filter.setSortAlgo(sortAlgo);

        PaginationResult<CourseAttachmentDownloadLog> result = courseAttachmentDownloadLogService.paginate(page, size, filter);

        HashMap<String, Object> data = new HashMap<>();
        data.put("data", result.getData());
        data.put("total", result.getTotal());

        return JsonResponse.data(data);
    }
}
