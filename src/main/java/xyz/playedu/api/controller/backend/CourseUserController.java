/*
 * Copyright 2023 杭州白书科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.playedu.api.controller.backend;

import lombok.SneakyThrows;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import xyz.playedu.api.constant.BPermissionConstant;
import xyz.playedu.api.domain.User;
import xyz.playedu.api.domain.UserCourseRecord;
import xyz.playedu.api.event.UserCourseRecordDestroyEvent;
import xyz.playedu.api.middleware.BackendPermissionMiddleware;
import xyz.playedu.api.request.backend.CourseUserDestroyRequest;
import xyz.playedu.api.service.CourseService;
import xyz.playedu.api.service.UserCourseHourRecordService;
import xyz.playedu.api.service.UserCourseRecordService;
import xyz.playedu.api.service.UserService;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.types.mapper.UserCourseHourRecordUserFirstCreatedAtMapper;
import xyz.playedu.api.types.paginate.PaginationResult;
import xyz.playedu.api.types.paginate.UserPaginateFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/3/24 16:08
 */
@RestController
@RequestMapping("/backend/v1/course/{courseId}/user")
public class CourseUserController {

    @Autowired private CourseService courseService;

    @Autowired private UserCourseRecordService userCourseRecordService;

    @Autowired private UserCourseHourRecordService userCourseHourRecordService;

    @Autowired private UserService userService;

    @Autowired private ApplicationContext ctx;

    @BackendPermissionMiddleware(slug = BPermissionConstant.COURSE_USER)
    @GetMapping("/index")
    @SneakyThrows
    public JsonResponse index(
            @PathVariable(name = "courseId") Integer courseId,
            @RequestParam HashMap<String, Object> params) {
        Integer page = MapUtils.getInteger(params, "page", 1);
        Integer size = MapUtils.getInteger(params, "size", 10);
        String sortField = MapUtils.getString(params, "sort_field");
        String sortAlgo = MapUtils.getString(params, "sort_algo");
        String name = MapUtils.getString(params, "name");
        String email = MapUtils.getString(params, "email");
        String idCard = MapUtils.getString(params, "id_card");

        UserPaginateFilter filter = new UserPaginateFilter();
        filter.setName(name);
        filter.setEmail(email);
        filter.setSortAlgo(sortAlgo);
        filter.setSortField(sortField);
        filter.setIdCard(idCard);

        // 所属部门
        List<Integer> depIds = courseService.getDepIdsByCourseId(courseId);
        if (depIds != null && depIds.size() > 0) {
            filter.setDepIds(
                    depIds.stream().map(String::valueOf).collect(Collectors.joining(",", "", "")));
        }

        PaginationResult<User> result = userService.paginate(page, size, filter);

        List<Integer> userIds = result.getData().stream().map(User::getId).toList();

        HashMap<String, Object> data = new HashMap<>();
        data.put("data", result.getData());
        data.put("total", result.getTotal());
        data.put(
                "user_course_records",
                userCourseRecordService
                        .chunk(
                                userIds,
                                new ArrayList<>() {
                                    {
                                        add(courseId);
                                    }
                                })
                        .stream()
                        .collect(Collectors.toMap(UserCourseRecord::getUserId, e -> e)));
        data.put(
                "user_course_hour_user_first_at",
                userCourseHourRecordService
                        .getUserCourseHourUserFirstCreatedAt(courseId, userIds)
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        UserCourseHourRecordUserFirstCreatedAtMapper::getUserId,
                                        UserCourseHourRecordUserFirstCreatedAtMapper
                                                ::getCreatedAt)));
        data.put("course", courseService.findOrFail(courseId));

        return JsonResponse.data(data);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.COURSE_USER_DESTROY)
    @PostMapping("/destroy")
    public JsonResponse destroy(
            @PathVariable(name = "courseId") Integer courseId,
            @RequestBody @Validated CourseUserDestroyRequest req) {
        if (req.getIds().size() == 0) {
            return JsonResponse.error("请选择需要删除的数据");
        }
        List<UserCourseRecord> records =
                userCourseRecordService.chunks(
                        req.getIds(),
                        new ArrayList<>() {
                            {
                                add("user_id");
                                add("id");
                            }
                        });
        for (UserCourseRecord record : records) {
            userCourseRecordService.removeById(record);
            ctx.publishEvent(new UserCourseRecordDestroyEvent(this, record.getUserId(), courseId));
        }
        return JsonResponse.success();
    }
}
