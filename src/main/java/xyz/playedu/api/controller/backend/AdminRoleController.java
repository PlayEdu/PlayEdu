package xyz.playedu.api.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.playedu.api.constant.BPermissionConstant;
import xyz.playedu.api.domain.AdminPermission;
import xyz.playedu.api.domain.AdminRole;
import xyz.playedu.api.domain.AdminRolePermission;
import xyz.playedu.api.middleware.BackendPermissionMiddleware;
import xyz.playedu.api.request.backend.AdminRoleRequest;
import xyz.playedu.api.service.AdminPermissionService;
import xyz.playedu.api.service.AdminRolePermissionService;
import xyz.playedu.api.service.AdminRoleService;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.util.HelperUtil;

import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    private AdminRolePermissionService rolePermissionService;

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
    @Transactional
    public JsonResponse store(@RequestBody @Validated AdminRoleRequest request) {
        AdminRole role = new AdminRole();

        role.setName(request.getName());
        role.setSlug(HelperUtil.randomString(12));
        role.setCreatedAt(new Date());
        role.setUpdatedAt(new Date());

        roleService.save(role);

        if (request.getPermissionIds().length > 0) {
            List<AdminRolePermission> rolePermissions = new ArrayList<>();
            for (int i = 0; i < request.getPermissionIds().length; i++) {
                AdminRolePermission rolePermission = new AdminRolePermission();
                rolePermission.setRoleId(role.getId());
                rolePermission.setPermId(request.getPermissionIds()[i]);
                rolePermissions.add(rolePermission);
            }
            rolePermissionService.saveBatch(rolePermissions);
        }

        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.ADMIN_ROLE)
    @GetMapping("/{id}")
    public JsonResponse edit(@PathVariable(name = "id") Integer id) {
        AdminRole role = roleService.getById(id);
        if (role == null) {
            return JsonResponse.error("管理角色不存在");
        }
        return JsonResponse.data(role);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.ADMIN_ROLE)
    @PutMapping("/{id}")
    @Transactional
    public JsonResponse update(@PathVariable(name = "id") Integer id, @RequestBody @Validated AdminRoleRequest request) {
        AdminRole role = roleService.getById(id);
        if (role == null) {
            return JsonResponse.error("管理角色不存在");
        }

        AdminRole newRole = new AdminRole();
        newRole.setId(role.getId());
        newRole.setName(request.getName());

        roleService.updateById(newRole);

        // 先清空已有的权限
        rolePermissionService.removeRolePermissionsByRoleId(role.getId());

        if (request.getPermissionIds().length > 0) {
            // 重新关联权限
            List<AdminRolePermission> rolePermissions = new ArrayList<>();
            for (int i = 0; i < request.getPermissionIds().length; i++) {
                AdminRolePermission rolePermission = new AdminRolePermission();
                rolePermission.setRoleId(role.getId());
                rolePermission.setPermId(request.getPermissionIds()[i]);
                rolePermissions.add(rolePermission);
            }
            rolePermissionService.saveBatch(rolePermissions);
        }

        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.ADMIN_ROLE)
    @DeleteMapping("/{id}")
    @Transactional
    public JsonResponse destroy(@PathVariable(name = "id") Integer id) {
        rolePermissionService.removeRolePermissionsByRoleId(id);
        roleService.removeById(id);
        return JsonResponse.success();
    }

}
