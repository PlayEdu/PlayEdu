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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import xyz.playedu.common.annotation.Log;
import xyz.playedu.common.constant.BackendConstant;
import xyz.playedu.common.constant.BusinessTypeConstant;
import xyz.playedu.common.constant.SystemConstant;
import xyz.playedu.common.domain.User;
import xyz.playedu.common.service.*;
import xyz.playedu.common.types.JsonResponse;
import xyz.playedu.course.domain.UserLearnDurationStats;
import xyz.playedu.course.service.CourseService;
import xyz.playedu.course.service.UserLearnDurationStatsService;
import xyz.playedu.resource.service.ResourceService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/3/7 13:55
 */
@RestController
@RequestMapping("/backend/v1/dashboard")
public class DashboardController {

    @Autowired private AdminUserService adminUserService;

    @Autowired private CategoryService categoryService;

    @Autowired private UserService userService;

    @Autowired private CourseService courseService;

    @Autowired private DepartmentService departmentService;

    @Autowired private ResourceService resourceService;

    @Autowired private UserLearnDurationStatsService userLearnDurationStatsService;

    @GetMapping("/index")
    @Log(title = "主面板", businessType = BusinessTypeConstant.GET)
    public JsonResponse index() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("version", SystemConstant.VERSION);

        data.put("user_total", userService.total()); // 总学员数量
        data.put("user_today", userService.todayCount()); // 今日注册学员数量
        data.put("user_yesterday", userService.yesterdayCount()); // 昨日注册学员数量

        data.put("course_total", courseService.total()); // 线上课数量

        data.put("department_total", departmentService.total());
        data.put("resource_category_total", categoryService.total());
        data.put("admin_user_total", adminUserService.total());

        data.put(
                "resource_video_total", resourceService.total(BackendConstant.RESOURCE_TYPE_VIDEO));
        data.put(
                "resource_image_total", resourceService.total(BackendConstant.RESOURCE_TYPE_IMAGE));

        List<String> types = new ArrayList<>();
        types.add(BackendConstant.RESOURCE_TYPE_PDF);
        types.add(BackendConstant.RESOURCE_TYPE_WORD);
        types.add(BackendConstant.RESOURCE_TYPE_PPT);
        types.add(BackendConstant.RESOURCE_TYPE_EXCEL);
        types.add(BackendConstant.RESOURCE_TYPE_ZIP);
        types.add(BackendConstant.RESOURCE_TYPE_RAR);
        types.add(BackendConstant.RESOURCE_TYPE_TXT);
        data.put("resource_file_total", resourceService.total(types));

        data.put("user_learn_today", userLearnDurationStatsService.todayTotal());
        data.put("user_learn_yesterday", userLearnDurationStatsService.yesterdayTotal());

        List<UserLearnDurationStats> userLearnTop10 = userLearnDurationStatsService.top10();
        Map<Integer, User> top10Users =
                userService
                        .chunks(
                                userLearnTop10.stream()
                                        .map(UserLearnDurationStats::getUserId)
                                        .toList(),
                                new ArrayList<>() {
                                    {
                                        add("id");
                                        add("name");
                                        add("avatar");
                                        add("email");
                                    }
                                })
                        .stream()
                        .collect(Collectors.toMap(User::getId, e -> e));
        data.put("user_learn_top10", userLearnTop10);
        data.put("user_learn_top10_users", top10Users);

        return JsonResponse.data(data);
    }
}
