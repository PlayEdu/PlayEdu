package xyz.playedu.api.bus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.playedu.api.constant.BackendConstant;
import xyz.playedu.api.service.AdminPermissionService;
import xyz.playedu.api.service.AdminRolePermissionService;
import xyz.playedu.api.service.AdminUserRoleService;

import java.util.HashMap;
import java.util.List;

@Component
public class BackendBus {

    @Autowired
    private AdminUserRoleService adminUserRoleService;

    @Autowired
    private AdminRolePermissionService rolePermissionService;

    @Autowired
    private AdminPermissionService permissionService;

    public static boolean inUnAuthWhitelist(String uri) {
        for (int i = 0; i < BackendConstant.UN_AUTH_URI_WHITELIST.length; i++) {
            if (uri.equals(BackendConstant.UN_AUTH_URI_WHITELIST[i])) {
                return true;
            }
        }
        return false;
    }

    public HashMap<String, Boolean> adminUserPermissions(Integer userId) {
        HashMap<String, Boolean> permissons = new HashMap<>();
        List<Integer> roleIds = adminUserRoleService.getRoleIdsByUserId(userId);
        if (roleIds.size() == 0) {
            return permissons;
        }
        List<Integer> permissionIds = rolePermissionService.getPermissionIdsByRoleIds(roleIds);
        if (permissionIds.size() == 0) {
            return permissons;
        }
        return permissionService.getSlugsByIds(permissionIds);
    }

}
