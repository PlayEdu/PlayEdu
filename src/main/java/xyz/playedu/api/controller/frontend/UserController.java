package xyz.playedu.api.controller.frontend;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.playedu.api.FCtx;
import xyz.playedu.api.domain.Course;
import xyz.playedu.api.domain.Department;
import xyz.playedu.api.domain.User;
import xyz.playedu.api.domain.UserCourseRecord;
import xyz.playedu.api.exception.ServiceException;
import xyz.playedu.api.request.frontend.ChangePasswordRequest;
import xyz.playedu.api.service.CourseService;
import xyz.playedu.api.service.DepartmentService;
import xyz.playedu.api.service.UserCourseRecordService;
import xyz.playedu.api.service.UserService;
import xyz.playedu.api.types.JsonResponse;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/13 09:21
 */
@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserCourseRecordService userCourseRecordService;

    @GetMapping("/detail")
    public JsonResponse detail() {
        User user = FCtx.getUser();
        List<Department> departments = departmentService.listByIds(userService.getDepIdsByUserId(user.getId()));

        HashMap<String, Object> data = new HashMap<>();
        data.put("user", user);
        data.put("departments", departments);

        return JsonResponse.data(data);
    }

    @PutMapping("/avatar")
    public JsonResponse changeAvatar() {
        return JsonResponse.success();
    }

    @PutMapping("/password")
    public JsonResponse changePassword(@RequestBody @Validated ChangePasswordRequest req) throws ServiceException {
        userService.passwordChange(FCtx.getUser(), req.getOldPassword(), req.getNewPassword());
        return JsonResponse.success();
    }

    @GetMapping("/courses")
    public JsonResponse courses(@RequestParam HashMap<String, Object> params) {
        Integer depId = MapUtils.getInteger(params, "dep_id");
        if (depId == null || depId == 0) {
            return JsonResponse.error("请选择部门");
        }

        List<Integer> userJoinDepIds = userService.getDepIdsByUserId(FCtx.getUserId());
        if (userJoinDepIds == null) {
            return JsonResponse.error("当前学员未加入任何部门");
        }
        if (!userJoinDepIds.contains(depId)) {
            return JsonResponse.error("当前学员未加入所选择部门");
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("learn_course_records", new HashMap<>());

        // -------- 读取当前学员可以参加的课程 ----------
        List<Course> courses = new ArrayList<>();
        // 读取部门课
        List<Course> depCourses = courseService.getDepCoursesAndShow(new ArrayList<>() {{
            add(depId);
        }});
        // 全部部门课
        List<Course> openCourses = courseService.getOpenCoursesAndShow(500);
        if (depCourses != null && depCourses.size() > 0) {
            courses.addAll(depCourses);
        }
        if (openCourses != null && openCourses.size() > 0) {
            courses.addAll(openCourses);
        }

        if (courses.size() > 0) {
            courses = courses.stream().sorted(Comparator.comparing(Course::getId).reversed()).toList();
        }

        data.put("courses", courses);

        // -------- 读取学习进度 ----------
        Map<Integer, UserCourseRecord> learnCourseRecords = new HashMap<>();
        if (courses.size() > 0) {
            learnCourseRecords = userCourseRecordService
                    .chunk(FCtx.getUserId(), courses.stream().map(Course::getId).toList())
                    .stream()
                    .collect(Collectors.toMap(UserCourseRecord::getCourseId, e -> e));
        }
        data.put("learn_course_records", learnCourseRecords);

        return JsonResponse.data(data);
    }

}