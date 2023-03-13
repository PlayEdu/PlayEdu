package xyz.playedu.api.controller.frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.playedu.api.domain.Department;
import xyz.playedu.api.service.DepartmentService;
import xyz.playedu.api.types.JsonResponse;

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

    @GetMapping("/index")
    public JsonResponse index() {
        return JsonResponse.data(departmentService.all().stream().collect(Collectors.groupingBy(Department::getParentId)));
    }

}
