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
import xyz.playedu.api.exception.ServiceException;
import xyz.playedu.api.request.frontend.ChangePasswordRequest;
import xyz.playedu.api.service.CourseService;
import xyz.playedu.api.service.DepartmentService;
import xyz.playedu.api.service.UserService;
import xyz.playedu.api.types.JsonResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

        // 读取部门课
        List<Course> depCourses = courseService.getDepCoursesAndShow(new ArrayList<>() {{
            add(depId);
        }});

        // 公开课
        List<Course> openCourses = courseService.getOpenCoursesAndShow(200);

        HashMap<String, Object> data = new HashMap<>();
        data.put("open", openCourses.stream().collect(Collectors.groupingBy(Course::getIsRequired)));
        data.put("department", depCourses.stream().collect(Collectors.groupingBy(Course::getIsRequired)));

        return JsonResponse.data(data);
    }


}