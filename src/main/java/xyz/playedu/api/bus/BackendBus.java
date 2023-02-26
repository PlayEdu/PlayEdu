package xyz.playedu.api.bus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.playedu.api.constant.BackendConstant;
import xyz.playedu.api.domain.AdminRole;
import xyz.playedu.api.service.AdminPermissionService;
import xyz.playedu.api.service.AdminRoleService;
import xyz.playedu.api.service.AdminUserService;

import java.util.HashMap;
import java.util.List;

@Component
public class BackendBus {

    @Autowired
    private AdminPermissionService permissionService;

    @Autowired
    private AdminRoleService adminRoleService;

    @Autowired
    private AdminUserService adminUserService;

    public static boolean inUnAuthWhitelist(String uri) {
        for (int i = 0; i < BackendConstant.UN_AUTH_URI_WHITELIST.length; i++) {
            if (uri.equals(BackendConstant.UN_AUTH_URI_WHITELIST[i])) {
                return true;
            }
        }
        return false;
    }

    public HashMap<String, Boolean> adminUserPermissions(Integer userId) {
        // 读取超级管理角色
        AdminRole superRole = adminRoleService.getBySlug(BackendConstant.SUPER_ADMIN_ROLE);

        HashMap<String, Boolean> permissions = new HashMap<>();
        List<Integer> roleIds = adminUserService.getRoleIdsByUserId(userId);
        if (roleIds.size() == 0) {
            return permissions;
        }

        List<Integer> permissionIds;

        if (roleIds.contains(superRole.getId())) {//包含超级管理角色的话返回全部权限
            permissionIds = permissionService.allIds();
        } else {//根据相应的roleIds读取权限
            permissionIds = adminRoleService.getPermissionIdsByRoleIds(roleIds);
            if (permissionIds.size() == 0) {
                return permissions;
            }
        }

        return permissionService.getSlugsByIds(permissionIds);
    }

}
