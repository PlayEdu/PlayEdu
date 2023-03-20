package xyz.playedu.api.controller.frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.playedu.api.PlayEduFCtx;
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

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/13 09:21
 */
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private CourseService courseService;

    @GetMapping("/detail")
    public JsonResponse detail() {
        User user = PlayEduFCtx.getUser();
        List<Department> departments = departmentService.listByIds(userService.getDepIdsByUserId(user.getId()));

        HashMap<String, Object> data = new HashMap<>();
        data.put("user", user);
        data.put("departments", departments);

        return JsonResponse.data(data);
    }

    @PutMapping("/password")
    public JsonResponse changePassword(@RequestBody @Validated ChangePasswordRequest req) throws ServiceException {
        userService.passwordChange(PlayEduFCtx.getUser(), req.getOldPassword(), req.getNewPassword());
        return JsonResponse.success();
    }

    @GetMapping("/courses")
    public JsonResponse courses() {
        // 公开课
        List<Course> openCourses = courseService.openCoursesAndShow(200);

        // 部门课
        List<Course> depCourses = new ArrayList<>();
        List<Integer> userJoinDepIds = userService.getDepIdsByUserId(PlayEduFCtx.getUserId());
        if (userJoinDepIds != null && userJoinDepIds.size() > 0) {
            depCourses = courseService.depCoursesAndShow(userJoinDepIds);
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("open", openCourses);
        data.put("department", depCourses);

        return JsonResponse.data(data);
    }


}