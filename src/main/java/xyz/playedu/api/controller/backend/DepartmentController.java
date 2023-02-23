package xyz.playedu.api.controller.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.playedu.api.bus.DepartmentBus;
import xyz.playedu.api.constant.BPermissionConstant;
import xyz.playedu.api.domain.Department;
import xyz.playedu.api.event.DepartmentDestroyEvent;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.middleware.BackendPermissionMiddleware;
import xyz.playedu.api.request.backend.DepartmentRequest;
import xyz.playedu.api.service.DepartmentService;
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
    private DepartmentBus departmentBus;

    @Autowired
    private ApplicationContext ctx;

    @BackendPermissionMiddleware(slug = BPermissionConstant.DEPARTMENT_INDEX)
    @GetMapping("/index")
    public JsonResponse index() {
        Map<Integer, List<Department>> departments = departmentService.all().stream().collect(Collectors.groupingBy(Department::getParentId));

        HashMap<String, Object> data = new HashMap<>();
        data.put("departments", departments);

        return JsonResponse.data(data);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.DEPARTMENT_STORE)
    @GetMapping("/create")
    public JsonResponse create(@RequestParam(name = "parent_id", defaultValue = "0") Integer parentId) {
        List<Department> data = departmentService.listByParentId(parentId);
        return JsonResponse.data(data);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.DEPARTMENT_STORE)
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

    @BackendPermissionMiddleware(slug = BPermissionConstant.DEPARTMENT_UPDATE)
    @GetMapping("/{id}")
    public JsonResponse edit(@PathVariable Integer id) throws NotFoundException {
        Department department = departmentService.findOrFail(id);
        return JsonResponse.data(department);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.DEPARTMENT_UPDATE)
    @PutMapping("/{id}")
    public JsonResponse update(@PathVariable Integer id, @RequestBody DepartmentRequest request) throws NotFoundException {
        Department department = departmentService.findOrFail(id);
        departmentService.update(department, request.getName(), request.getParentId(), request.getSort());
        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.DEPARTMENT_DESTROY)
    @DeleteMapping("/{id}")
    public JsonResponse destroy(@PathVariable Integer id) throws NotFoundException {
        Department department = departmentService.findOrFail(id);
        departmentService.deleteById(department.getId());

        ctx.publishEvent(new DepartmentDestroyEvent(this, id, new Date()));

        return JsonResponse.success();
    }

}
