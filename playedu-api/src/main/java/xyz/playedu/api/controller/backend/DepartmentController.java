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

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import xyz.playedu.api.event.DepartmentDestroyEvent;
import xyz.playedu.api.request.backend.DepartmentParentRequest;
import xyz.playedu.api.request.backend.DepartmentRequest;
import xyz.playedu.api.request.backend.DepartmentSortRequest;
import xyz.playedu.common.annotation.BackendPermission;
import xyz.playedu.common.annotation.Log;
import xyz.playedu.common.bus.LDAPBus;
import xyz.playedu.common.constant.BPermissionConstant;
import xyz.playedu.common.constant.BusinessTypeConstant;
import xyz.playedu.common.context.BCtx;
import xyz.playedu.common.domain.Department;
import xyz.playedu.common.domain.User;
import xyz.playedu.common.exception.NotFoundException;
import xyz.playedu.common.service.DepartmentService;
import xyz.playedu.common.service.UserService;
import xyz.playedu.common.types.JsonResponse;
import xyz.playedu.common.types.paginate.PaginationResult;
import xyz.playedu.common.types.paginate.UserPaginateFilter;
import xyz.playedu.course.domain.Course;
import xyz.playedu.course.domain.UserCourseRecord;
import xyz.playedu.course.service.CourseDepartmentService;
import xyz.playedu.course.service.CourseService;
import xyz.playedu.course.service.UserCourseRecordService;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/backend/v1/department")
public class DepartmentController {

    @Autowired private DepartmentService departmentService;

    @Autowired private CourseDepartmentService courseDepartmentService;

    @Autowired private UserService userService;

    @Autowired private CourseService courseService;

    @Autowired private UserCourseRecordService userCourseRecordService;

    @Autowired private ApplicationContext ctx;

    @Autowired private LDAPBus ldapBus;

    @GetMapping("/index")
    @Log(title = "部门-列表", businessType = BusinessTypeConstant.GET)
    public JsonResponse index() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("departments", departmentService.groupByParent());
        data.put("dep_user_count", departmentService.getDepartmentsUserCount());
        data.put("user_total", userService.total());
        return JsonResponse.data(data);
    }

    @GetMapping("/departments")
    @Log(title = "部门-全部部门", businessType = BusinessTypeConstant.GET)
    public JsonResponse index(
            @RequestParam(name = "parent_id", defaultValue = "0") Integer parentId) {
        List<Department> departments = departmentService.listByParentId(parentId);
        return JsonResponse.data(departments);
    }

    @BackendPermission(slug = BPermissionConstant.DEPARTMENT_CUD)
    @GetMapping("/create")
    @Log(title = "部门-新建", businessType = BusinessTypeConstant.GET)
    public JsonResponse create() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("departments", departmentService.groupByParent());
        return JsonResponse.data(data);
    }

    @BackendPermission(slug = BPermissionConstant.DEPARTMENT_CUD)
    @PostMapping("/create")
    @Log(title = "部门-新建", businessType = BusinessTypeConstant.INSERT)
    public JsonResponse store(@RequestBody @Validated DepartmentRequest req)
            throws NotFoundException {
        if (ldapBus.enabledLDAP()) {
            return JsonResponse.error("已启用LDAP服务，禁止添加部门");
        }
        departmentService.create(req.getName(), req.getParentId(), req.getSort());
        return JsonResponse.success();
    }

    @BackendPermission(slug = BPermissionConstant.DEPARTMENT_CUD)
    @GetMapping("/{id}")
    @Log(title = "部门-编辑", businessType = BusinessTypeConstant.GET)
    public JsonResponse edit(@PathVariable Integer id) throws NotFoundException {
        Department department = departmentService.findOrFail(id);
        return JsonResponse.data(department);
    }

    @BackendPermission(slug = BPermissionConstant.DEPARTMENT_CUD)
    @PutMapping("/{id}")
    @Log(title = "部门-编辑", businessType = BusinessTypeConstant.UPDATE)
    public JsonResponse update(@PathVariable Integer id, @RequestBody DepartmentRequest req)
            throws NotFoundException {
        if (ldapBus.enabledLDAP()) {
            return JsonResponse.error("已启用LDAP服务，禁止添加部门");
        }
        Department department = departmentService.findOrFail(id);
        departmentService.update(department, req.getName(), req.getParentId(), req.getSort());
        return JsonResponse.success();
    }

    @BackendPermission(slug = BPermissionConstant.DEPARTMENT_CUD)
    @GetMapping("/{id}/destroy")
    @Log(title = "部门-批量删除", businessType = BusinessTypeConstant.DELETE)
    public JsonResponse preDestroy(@PathVariable Integer id) {
        if (ldapBus.enabledLDAP()) {
            return JsonResponse.error("已启用LDAP服务，禁止添加部门");
        }
        List<Integer> courseIds = courseDepartmentService.getCourseIdsByDepId(id);
        List<Integer> userIds = departmentService.getUserIdsByDepId(id);

        HashMap<String, Object> data = new HashMap<>();
        data.put("courses", new ArrayList<>());
        data.put("users", new ArrayList<>());
        data.put("children", departmentService.listByParentId(id));

        if (courseIds != null && !courseIds.isEmpty()) {
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
        if (userIds != null && !userIds.isEmpty()) {
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

    @BackendPermission(slug = BPermissionConstant.DEPARTMENT_CUD)
    @DeleteMapping("/{id}")
    @Log(title = "部门-删除", businessType = BusinessTypeConstant.DELETE)
    public JsonResponse destroy(@PathVariable Integer id) throws NotFoundException {
        if (ldapBus.enabledLDAP()) {
            return JsonResponse.error("已启用LDAP服务，禁止添加部门");
        }
        Department department = departmentService.findOrFail(id);
        departmentService.destroy(department.getId());
        ctx.publishEvent(new DepartmentDestroyEvent(this, BCtx.getId(), department.getId()));
        return JsonResponse.success();
    }

    @BackendPermission(slug = BPermissionConstant.DEPARTMENT_CUD)
    @PutMapping("/update/sort")
    @Log(title = "部门-更新排序", businessType = BusinessTypeConstant.UPDATE)
    public JsonResponse resort(@RequestBody @Validated DepartmentSortRequest req) {
        departmentService.resetSort(req.getIds());
        return JsonResponse.success();
    }

    @BackendPermission(slug = BPermissionConstant.DEPARTMENT_CUD)
    @PutMapping("/update/parent")
    @Log(title = "部门-更新父级", businessType = BusinessTypeConstant.UPDATE)
    public JsonResponse updateParent(@RequestBody @Validated DepartmentParentRequest req)
            throws NotFoundException {
        if (ldapBus.enabledLDAP()) {
            return JsonResponse.error("已启用LDAP服务，禁止添加部门");
        }
        departmentService.changeParent(req.getId(), req.getParentId(), req.getIds());
        return JsonResponse.success();
    }

    @BackendPermission(slug = BPermissionConstant.DEPARTMENT_USER_LEARN)
    @GetMapping("/{id}/users")
    @Log(title = "部门-学员", businessType = BusinessTypeConstant.GET)
    public JsonResponse users(
            @PathVariable(name = "id") Integer id, @RequestParam HashMap<String, Object> params) {
        Integer page = MapUtils.getInteger(params, "page", 1);
        Integer size = MapUtils.getInteger(params, "size", 10);
        String sortField = MapUtils.getString(params, "sort_field");
        String sortAlgo = MapUtils.getString(params, "sort_algo");

        String name = MapUtils.getString(params, "name");
        String email = MapUtils.getString(params, "email");
        String idCard = MapUtils.getString(params, "id_card");
        List<Integer> depIds =
                new ArrayList<>() {
                    {
                        add(id);
                    }
                };

        String courseIdsStr = MapUtils.getString(params, "course_ids");
        String showMode = MapUtils.getString(params, "show_mode");

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

        List<Course> courses;
        if (courseIdsStr != null && !courseIdsStr.trim().isEmpty()) {
            // 指定了需要显示的线上课
            courses =
                    courseService.chunks(
                            Arrays.stream(courseIdsStr.split(",")).map(Integer::valueOf).toList());
        } else {
            if ("only_open".equals(showMode)) {
                // 公开(无关联部门)线上课
                courses = courseService.getOpenCoursesAndShow(10000);
            } else if ("only_dep".equals(showMode)) {
                // 部门关联线上课
                courses =
                        courseService.getDepCoursesAndShow(
                                new ArrayList<>() {
                                    {
                                        add(id);
                                    }
                                });
            } else {
                // 部门关联线上课
                courses =
                        courseService.getDepCoursesAndShow(
                                new ArrayList<>() {
                                    {
                                        add(id);
                                    }
                                });
                List<Course> openCourses = courseService.getOpenCoursesAndShow(10000);
                ;
                if (openCourses != null) {
                    courses.addAll(openCourses);
                }
            }
        }

        List<Integer> courseIds = courses.stream().map(Course::getId).toList();

        // 学员的课程学习进度
        Map<Integer, List<UserCourseRecord>> userCourseRecords =
                userCourseRecordService
                        .chunk(users.getData().stream().map(User::getId).toList(), courseIds)
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
        data.put("courses", courses.stream().collect(Collectors.toMap(Course::getId, e -> e)));
        data.put("user_course_records", userCourseRecordsMap);

        return JsonResponse.data(data);
    }

    @BackendPermission(slug = BPermissionConstant.DEPARTMENT_CUD)
    @PostMapping("/ldap-sync")
    @Log(title = "部门-LDAP同步", businessType = BusinessTypeConstant.INSERT)
    @SneakyThrows
    public JsonResponse ldapSync() {
        ldapBus.departmentSync();
        return JsonResponse.success();
    }
}
