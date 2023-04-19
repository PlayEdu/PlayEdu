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

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import xyz.playedu.api.BCtx;
import xyz.playedu.api.constant.BPermissionConstant;
import xyz.playedu.api.domain.Course;
import xyz.playedu.api.domain.Department;
import xyz.playedu.api.domain.User;
import xyz.playedu.api.domain.UserCourseRecord;
import xyz.playedu.api.event.DepartmentDestroyEvent;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.middleware.BackendPermissionMiddleware;
import xyz.playedu.api.request.backend.*;
import xyz.playedu.api.service.CourseService;
import xyz.playedu.api.service.DepartmentService;
import xyz.playedu.api.service.UserCourseRecordService;
import xyz.playedu.api.service.UserService;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.types.paginate.PaginationResult;
import xyz.playedu.api.types.paginate.UserPaginateFilter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/2/19 10:33
 */
@RestController
@Slf4j
@RequestMapping("/backend/v1/department")
public class DepartmentController {

    @Autowired private DepartmentService departmentService;

    @Autowired private UserService userService;

    @Autowired private CourseService courseService;

    @Autowired private UserCourseRecordService userCourseRecordService;

    @Autowired private ApplicationContext ctx;

    @GetMapping("/index")
    public JsonResponse index() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("departments", departmentService.groupByParent());
        data.put("dep_user_count", departmentService.getDepartmentsUserCount());
        return JsonResponse.data(data);
    }

    @GetMapping("/departments")
    public JsonResponse index(
            @RequestParam(name = "parent_id", defaultValue = "0") Integer parentId) {
        List<Department> departments = departmentService.listByParentId(parentId);
        return JsonResponse.data(departments);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.DEPARTMENT_CUD)
    @GetMapping("/create")
    public JsonResponse create() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("departments", departmentService.groupByParent());
        return JsonResponse.data(data);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.DEPARTMENT_CUD)
    @PostMapping("/create")
    public JsonResponse store(@RequestBody @Validated DepartmentRequest req)
            throws NotFoundException {
        departmentService.create(req.getName(), req.getParentId(), req.getSort());
        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.DEPARTMENT_CUD)
    @GetMapping("/{id}")
    public JsonResponse edit(@PathVariable Integer id) throws NotFoundException {
        Department department = departmentService.findOrFail(id);
        return JsonResponse.data(department);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.DEPARTMENT_CUD)
    @PutMapping("/{id}")
    public JsonResponse update(@PathVariable Integer id, @RequestBody DepartmentRequest req)
            throws NotFoundException {
        Department department = departmentService.findOrFail(id);
        departmentService.update(department, req.getName(), req.getParentId(), req.getSort());
        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.DEPARTMENT_CUD)
    @GetMapping("/{id}/destroy")
    public JsonResponse preDestroy(@PathVariable Integer id) {
        List<Integer> courseIds = departmentService.getCourseIdsByDepId(id);
        List<Integer> userIds = departmentService.getUserIdsByDepId(id);

        HashMap<String, Object> data = new HashMap<>();
        data.put("courses", new ArrayList<>());
        data.put("users", new ArrayList<>());
        data.put("children", departmentService.listByParentId(id));

        if (courseIds != null && courseIds.size() > 0) {
            data.put(
                    "courses",
                    courseService.chunks(
                            courseIds,
                            new ArrayList<>() {
                                {
                                    add("id");
                                    add("title");
                                }
                            }));
        }
        if (userIds != null && userIds.size() > 0) {
            data.put(
                    "users",
                    userService.chunks(
                            userIds,
                            new ArrayList<>() {
                                {
                                    add("id");
                                    add("name");
                                    add("avatar");
                                }
                            }));
        }

        return JsonResponse.data(data);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.DEPARTMENT_CUD)
    @DeleteMapping("/{id}")
    public JsonResponse destroy(@PathVariable Integer id) throws NotFoundException {
        Department department = departmentService.findOrFail(id);
        departmentService.destroy(department.getId());
        ctx.publishEvent(new DepartmentDestroyEvent(this, BCtx.getId(), department.getId()));
        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.DEPARTMENT_CUD)
    @PutMapping("/update/sort")
    public JsonResponse resort(@RequestBody @Validated DepartmentSortRequest req) {
        departmentService.resetSort(req.getIds());
        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.DEPARTMENT_CUD)
    @PutMapping("/update/parent")
    public JsonResponse updateParent(@RequestBody @Validated DepartmentParentRequest req)
            throws NotFoundException {
        departmentService.changeParent(req.getId(), req.getParentId(), req.getIds());
        return JsonResponse.success();
    }

    @GetMapping("/{id}/users")
    public JsonResponse users(
            @PathVariable(name = "id") Integer id, @RequestParam HashMap<String, Object> params) {
        Integer page = MapUtils.getInteger(params, "page", 1);
        Integer size = MapUtils.getInteger(params, "size", 10);
        String sortField = MapUtils.getString(params, "sort_field");
        String sortAlgo = MapUtils.getString(params, "sort_algo");

        String name = MapUtils.getString(params, "name");
        String email = MapUtils.getString(params, "email");
        String idCard = MapUtils.getString(params, "id_card");
        String depIds = String.valueOf(id);

        UserPaginateFilter filter =
                new UserPaginateFilter() {
                    {
                        setName(name);
                        setEmail(email);
                        setIdCard(idCard);
                        setDepIds(depIds);
                        setSortAlgo(sortAlgo);
                        setSortField(sortField);
                    }
                };

        PaginationResult<User> users = userService.paginate(page, size, filter);

        // 部门关联线上课
        List<Course> courses =
                courseService.getDepCoursesAndShow(
                        new ArrayList<>() {
                            {
                                add(id);
                            }
                        });

        // 学员的课程学习进度
        Map<Integer, List<UserCourseRecord>> userCourseRecords =
                userCourseRecordService
                        .chunk(
                                users.getData().stream().map(User::getId).toList(),
                                courses.stream().map(Course::getId).toList())
                        .stream()
                        .collect(Collectors.groupingBy(UserCourseRecord::getUserId));
        Map<Integer, Map<Integer, UserCourseRecord>> userCourseRecordsMap = new HashMap<>();
        userCourseRecords.forEach(
                (userId, records) -> {
                    userCourseRecordsMap.put(
                            userId,
                            records.stream()
                                    .collect(
                                            Collectors.toMap(
                                                    UserCourseRecord::getCourseId, e -> e)));
                });

        HashMap<String, Object> data = new HashMap<>();
        data.put("data", users.getData());
        data.put("total", users.getTotal());
        data.put("courses", courses.stream().collect(Collectors.groupingBy(Course::getId)));
        data.put("user_course_records", userCourseRecordsMap);

        return JsonResponse.data(data);
    }
}
