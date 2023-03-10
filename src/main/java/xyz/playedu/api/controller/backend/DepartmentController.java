package xyz.playedu.api.controller.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.playedu.api.PlayEduBContext;
import xyz.playedu.api.constant.BPermissionConstant;
import xyz.playedu.api.domain.Department;
import xyz.playedu.api.domain.User;
import xyz.playedu.api.event.DepartmentDestroyEvent;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.middleware.BackendPermissionMiddleware;
import xyz.playedu.api.request.backend.DepartmentRequest;
import xyz.playedu.api.service.CourseService;
import xyz.playedu.api.service.DepartmentService;
import xyz.playedu.api.service.UserService;
import xyz.playedu.api.types.JsonResponse;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/19 10:33
 */
@RestController
@Slf4j
@RequestMapping("/backend/v1/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ApplicationContext ctx;

    @GetMapping("/index")
    public JsonResponse index() {
        Map<Integer, List<Department>> departments = departmentService.all().stream().collect(Collectors.groupingBy(Department::getParentId));

        HashMap<String, Object> data = new HashMap<>();
        data.put("departments", departments);

        return JsonResponse.data(data);
    }

    @GetMapping("/departments")
    public JsonResponse index(@RequestParam(name = "parent_id", defaultValue = "0") Integer parentId) {
        List<Department> departments = departmentService.listByParentId(parentId);
        return JsonResponse.data(departments);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.DEPARTMENT_CUD)
    @GetMapping("/create")
    public JsonResponse create() {
        Map<Integer, List<Department>> departments = departmentService.all().stream().collect(Collectors.groupingBy(Department::getParentId));

        HashMap<String, Object> data = new HashMap<>();
        data.put("departments", departments);

        return JsonResponse.data(data);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.DEPARTMENT_CUD)
    @PostMapping("/create")
    public JsonResponse store(@RequestBody @Validated DepartmentRequest req) throws NotFoundException {
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
    public JsonResponse update(@PathVariable Integer id, @RequestBody DepartmentRequest req) throws NotFoundException {
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

        if (courseIds != null && courseIds.size() > 0) {
            data.put("courses", courseService.chunks(courseIds, new ArrayList<>() {{
                add("id");
                add("title");
            }}));
        }
        if (userIds != null && userIds.size() > 0) {
            data.put("users", userService.chunks(userIds, new ArrayList<>() {{
                add("id");
                add("nickname");
                add("name");
                add("avatar");
            }}));
        }

        return JsonResponse.data(data);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.DEPARTMENT_CUD)
    @DeleteMapping("/{id}")
    public JsonResponse destroy(@PathVariable Integer id) throws NotFoundException {
        Department department = departmentService.findOrFail(id);
        departmentService.deleteById(department.getId());
        ctx.publishEvent(new DepartmentDestroyEvent(this, PlayEduBContext.getAdminUserID(), department.getId(), new Date()));
        return JsonResponse.success();
    }

}
