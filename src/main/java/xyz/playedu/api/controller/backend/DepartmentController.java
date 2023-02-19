package xyz.playedu.api.controller.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.playedu.api.bus.DepartmentBus;
import xyz.playedu.api.domain.Department;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.request.backend.DepartmentRequest;
import xyz.playedu.api.service.DepartmentService;
import xyz.playedu.api.types.JsonResponse;

import java.util.Date;
import java.util.List;

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
    private DepartmentBus departmentBus;

    @GetMapping("/index")
    public JsonResponse index() {
        List<Department> data = departmentService.list();
        return JsonResponse.data(data);
    }

    @GetMapping("/create")
    public JsonResponse create(@RequestParam(name = "parent_id", defaultValue = "0") Integer parentId) {
        List<Department> data = departmentService.listByParentId(parentId);
        return JsonResponse.data(data);
    }

    @PostMapping("/create")
    public JsonResponse store(@RequestBody @Validated DepartmentRequest request) throws NotFoundException {
        String parentChain = "";
        if (request.getParentId() != 0) {
            parentChain = departmentBus.compParentChain(request.getParentId());
        }

        Department department = new Department();
        department.setName(request.getName());
        department.setParentId(request.getParentId());
        department.setParentChain(parentChain);
        department.setSort(request.getSort());
        department.setCreatedAt(new Date());
        department.setUpdatedAt(new Date());

        departmentService.save(department);

        return JsonResponse.success();
    }

    @GetMapping("/{id}")
    public JsonResponse edit(@PathVariable Integer id) throws NotFoundException {
        Department department = departmentService.findOrFail(id);
        return JsonResponse.data(department);
    }

    @PutMapping("/{id}")
    public JsonResponse update(@PathVariable Integer id, @RequestBody DepartmentRequest request) throws NotFoundException {
        Department department = departmentService.findOrFail(id);
        departmentService.update(department, request.getName(), request.getParentId(), request.getSort());
        return JsonResponse.success();
    }

    @DeleteMapping("/{id}")
    public JsonResponse destroy(@PathVariable Integer id) throws NotFoundException {
        Department department = departmentService.findOrFail(id);
        departmentService.deleteById(department.getId());
        return JsonResponse.success();
    }

}
