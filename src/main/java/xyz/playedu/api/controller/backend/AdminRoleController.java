package xyz.playedu.api.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.playedu.api.constant.BPermissionConstant;
import xyz.playedu.api.constant.BackendConstant;
import xyz.playedu.api.domain.AdminPermission;
import xyz.playedu.api.domain.AdminRole;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.middleware.BackendPermissionMiddleware;
import xyz.playedu.api.request.backend.AdminRoleRequest;
import xyz.playedu.api.service.AdminPermissionService;
import xyz.playedu.api.service.AdminRoleService;
import xyz.playedu.api.types.JsonResponse;

import java.util.HashMap;
import java.util.List;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/21 15:56
 */
@RestController
@RequestMapping("/backend/v1/admin-role")
public class AdminRoleController {

    @Autowired
    private AdminRoleService roleService;

    @Autowired
    private AdminPermissionService permissionService;

    @BackendPermissionMiddleware(slug = BPermissionConstant.ADMIN_ROLE)
    @GetMapping("/index")
    public JsonResponse index() {
        List<AdminRole> data = roleService.list();
        return JsonResponse.data(data);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.ADMIN_ROLE)
    @GetMapping("/create")
    public JsonResponse create() {
        List<AdminPermission> permissions = permissionService.listOrderBySortAsc();
        HashMap<String, Object> data = new HashMap<>();
        data.put("permissions", permissions);
        return JsonResponse.data(data);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.ADMIN_ROLE)
    @PostMapping("/create")
    public JsonResponse store(@RequestBody @Validated AdminRoleRequest request) {
        roleService.createWithPermissionIds(request.getName(), request.getPermissionIds());
        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.ADMIN_ROLE)
    @GetMapping("/{id}")
    public JsonResponse edit(@PathVariable(name = "id") Integer id) throws NotFoundException {
        AdminRole role = roleService.findOrFail(id);
        return JsonResponse.data(role);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.ADMIN_ROLE)
    @PutMapping("/{id}")
    public JsonResponse update(@PathVariable(name = "id") Integer id, @RequestBody @Validated AdminRoleRequest request) throws NotFoundException {
        AdminRole role = roleService.findOrFail(id);
        if (role.getSlug().equals(BackendConstant.SUPER_ADMIN_ROLE)) {
            return JsonResponse.error("超级管理权限无法编辑");
        }

        roleService.updateWithPermissionIds(role, request.getName(), request.getPermissionIds());

        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.ADMIN_ROLE)
    @DeleteMapping("/{id}")
    public JsonResponse destroy(@PathVariable(name = "id") Integer id) throws NotFoundException {
        AdminRole role = roleService.findOrFail(id);

        if (role.getSlug().equals(BackendConstant.SUPER_ADMIN_ROLE)) {
            return JsonResponse.error("超级管理角色无法删除");
        }

        roleService.removeWithPermissions(role);

        return JsonResponse.success();
    }

}
