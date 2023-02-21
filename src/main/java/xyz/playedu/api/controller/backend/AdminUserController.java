package xyz.playedu.api.controller.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.playedu.api.domain.AdminRole;
import xyz.playedu.api.domain.AdminUser;
import xyz.playedu.api.domain.AdminUserRole;
import xyz.playedu.api.request.backend.AdminUserRequest;
import xyz.playedu.api.service.AdminRoleService;
import xyz.playedu.api.service.AdminUserRoleService;
import xyz.playedu.api.service.AdminUserService;
import xyz.playedu.api.types.PaginationResult;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.util.HelperUtil;

import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    private AdminUserRoleService userRoleService;

    @GetMapping("/index")
    public JsonResponse Index(@RequestParam(name = "page", defaultValue = "1") Integer page, @RequestParam(name = "size", defaultValue = "10") Integer size) {
        PaginationResult<AdminUser> result = adminUserService.paginate(page, size, null);

        ArrayList<AdminUser> data = new ArrayList<>();
        for (AdminUser adminUser : result.getData()) {
            adminUser.setPassword(null);
            adminUser.setSalt(null);
            data.add(adminUser);
        }
        result.setData(data);

        return JsonResponse.data(result);
    }

    @GetMapping("/create")
    public JsonResponse create() {
        List<AdminRole> roles = roleService.list();
        HashMap<String, Object> data = new HashMap<>();
        data.put("roles", roles);
        return JsonResponse.data(data);

    }

    @PostMapping("/create")
    @Transactional
    public JsonResponse store(@RequestBody @Validated AdminUserRequest request) {
        if (request.getPassword() == null || request.getPassword().length() == 0) {
            return JsonResponse.error("请输入密码");
        }

        if (adminUserService.findByEmail(request.getEmail()) != null) {
            return JsonResponse.error("邮箱已存在");
        }

        String salt = HelperUtil.randomString(6);

        AdminUser adminUser = new AdminUser();
        adminUser.setName(request.getName());
        adminUser.setEmail(request.getEmail());
        adminUser.setSalt(salt);
        adminUser.setPassword(HelperUtil.MD5(request.getPassword() + salt));
        adminUser.setIsBanLogin(request.getIsBanLogin());
        adminUser.setCreatedAt(new Date());
        adminUser.setUpdatedAt(new Date());

        if (!adminUserService.save(adminUser)) {
            return JsonResponse.error("添加管理员失败");
        }

        if (request.getRoleIds().length > 0) {
            List<AdminUserRole> userRoles = new ArrayList<>();
            for (int i = 0; i < request.getRoleIds().length; i++) {
                AdminUserRole userRole = new AdminUserRole();
                userRole.setAdminId(adminUser.getId());
                userRole.setRoleId(request.getRoleIds()[i]);
                userRoles.add(userRole);
            }
            userRoleService.saveBatch(userRoles);
        }


        return JsonResponse.success();
    }

    @GetMapping("/{id}")
    public JsonResponse edit(@PathVariable Integer id) {
        AdminUser adminUser = adminUserService.findById(id);
        if (adminUser == null) {
            return JsonResponse.error("管理员不存在");
        }
        adminUser.setPassword(null);
        adminUser.setSalt(null);
        return JsonResponse.data(adminUser);
    }

    @PutMapping("/{id}")
    @Transactional
    public JsonResponse update(@PathVariable Integer id, @RequestBody @Validated AdminUserRequest request) {
        AdminUser adminUser = adminUserService.findById(id);
        if (adminUser == null) {
            return JsonResponse.error("管理员不存在");
        }

        AdminUser updateAdminUser = new AdminUser();
        updateAdminUser.setId(adminUser.getId());

        if (!adminUser.getEmail().equals(request.getEmail())) {//更换了邮箱
            if (adminUserService.findByEmail(request.getEmail()) != null) {
                return JsonResponse.error("邮箱已存在");
            }
            updateAdminUser.setEmail(request.getEmail());
        }

        if (request.getPassword() != null && request.getPassword().length() > 0) {//更换了密码
            updateAdminUser.setPassword(HelperUtil.MD5(request.getPassword() + adminUser.getSalt()));
        }

        if (!request.getName().equals(adminUser.getName())) {//更换了姓名
            updateAdminUser.setName(request.getName());
        }

        if (!adminUserService.updateById(updateAdminUser)) {
            return JsonResponse.error("更新管理员资料失败");
        }

        if (request.getRoleIds().length > 0) {
            // 先删除管理员与权限的已有关联关系
            userRoleService.removeUserRolesByUserId(adminUser.getId());

            // 重新绑定关联关系
            List<AdminUserRole> userRoles = new ArrayList<>();
            for (int i = 0; i < request.getRoleIds().length; i++) {
                AdminUserRole userRole = new AdminUserRole();
                userRole.setAdminId(adminUser.getId());
                userRole.setRoleId(request.getRoleIds()[i]);
                userRoles.add(userRole);
            }
            userRoleService.saveBatch(userRoles);
        }

        return JsonResponse.success();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public JsonResponse destroy(@PathVariable Integer id) {
        if (!adminUserService.removeById(id)) {
            return JsonResponse.error("删除管理员失败");
        }
        userRoleService.removeUserRolesByUserId(id);
        return JsonResponse.success();
    }

}
