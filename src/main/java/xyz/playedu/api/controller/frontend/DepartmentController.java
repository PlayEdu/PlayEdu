package xyz.playedu.api.controller.frontend;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.playedu.api.domain.Course;
import xyz.playedu.api.domain.Department;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.service.CourseService;
import xyz.playedu.api.service.DepartmentService;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.types.paginate.CoursePaginateFiler;
import xyz.playedu.api.types.paginate.PaginationResult;

import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/13 16:23
 */
@RestController
@RequestMapping("/api/v1/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private CourseService courseService;

    @GetMapping("/index")
    public JsonResponse index() {
        return JsonResponse.data(departmentService.all().stream().collect(Collectors.groupingBy(Department::getParentId)));
    }

    @GetMapping("/{id}/courses")
    public JsonResponse courses(@PathVariable(name = "id") Integer id, @RequestParam HashMap<String, Object> params) throws NotFoundException {
        Integer page = MapUtils.getInteger(params, "page", 1);
        Integer size = MapUtils.getInteger(params, "size", 10);

        Department department = departmentService.findOrFail(id);

        CoursePaginateFiler filer = new CoursePaginateFiler();
        filer.setIsShow(1);
        filer.setDepIds(department.getId() + "");

        PaginationResult<Course> result = courseService.paginate(page, size, filer);

        HashMap<String, Object> data = new HashMap<>();
        data.put("data", result.getData());
        data.put("total", result.getTotal());

        return JsonResponse.data(data);
    }

}