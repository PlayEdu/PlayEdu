package xyz.playedu.api.controller.backend;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.playedu.api.constant.BPermissionConstant;
import xyz.playedu.api.domain.AdminRole;
import xyz.playedu.api.domain.AdminUser;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.exception.ServiceException;
import xyz.playedu.api.middleware.BackendPermissionMiddleware;
import xyz.playedu.api.request.backend.AdminUserRequest;
import xyz.playedu.api.service.AdminRoleService;
import xyz.playedu.api.service.AdminUserService;
import xyz.playedu.api.types.paginate.AdminUserPaginateFilter;
import xyz.playedu.api.types.paginate.PaginationResult;
import xyz.playedu.api.types.JsonResponse;

import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/backend/v1/admin-user")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private AdminRoleService roleService;

    @BackendPermissionMiddleware(slug = BPermissionConstant.ADMIN_USER_INDEX)
    @GetMapping("/index")
    public JsonResponse Index(@RequestParam HashMap<String, Object> params) {
        Integer page = MapUtils.getInteger(params, "page", 1);
        Integer size = MapUtils.getInteger(params, "size", 10);
        String name = MapUtils.getString(params, "name");

        AdminUserPaginateFilter filter = new AdminUserPaginateFilter();
        if (name != null && name.length() > 0) {
            filter.setName(name);
        }

        PaginationResult<AdminUser> result = adminUserService.paginate(page, size, filter);

        return JsonResponse.data(result);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.ADMIN_USER_STORE)
    @GetMapping("/create")
    public JsonResponse create() {
        List<AdminRole> roles = roleService.list();

        HashMap<String, Object> data = new HashMap<>();
        data.put("roles", roles);

        return JsonResponse.data(data);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.ADMIN_USER_STORE)
    @PostMapping("/create")
    public JsonResponse store(@RequestBody @Validated AdminUserRequest req) throws ServiceException {
        if (req.getPassword() == null || req.getPassword().length() == 0) {
            return JsonResponse.error("请输入密码");
        }

        adminUserService.createWithRoleIds(req.getName(), req.getEmail(), req.getPassword(), req.getIsBanLogin(), req.getRoleIds());

        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.ADMIN_USER_UPDATE)
    @GetMapping("/{id}")
    public JsonResponse edit(@PathVariable Integer id) throws NotFoundException {
        AdminUser adminUser = adminUserService.findOrFail(id);
        List<Integer> roleIds = adminUserService.getRoleIdsByUserId(adminUser.getId());

        HashMap<String, Object> data = new HashMap<>();
        data.put("user", adminUser);
        data.put("role_ids", roleIds);

        return JsonResponse.data(data);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.ADMIN_USER_UPDATE)
    @PutMapping("/{id}")
    public JsonResponse update(@PathVariable Integer id, @RequestBody @Validated AdminUserRequest req) throws NotFoundException, ServiceException {
        AdminUser adminUser = adminUserService.findOrFail(id);
        adminUserService.updateWithRoleIds(adminUser, req.getName(), req.getEmail(), req.getPassword(), req.getIsBanLogin(), req.getRoleIds());
        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.ADMIN_USER_DESTROY)
    @DeleteMapping("/{id}")
    public JsonResponse destroy(@PathVariable Integer id) {
        adminUserService.removeWithRoleIds(id);
        return JsonResponse.success();
    }

}
