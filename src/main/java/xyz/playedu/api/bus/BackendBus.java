package xyz.playedu.api.bus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.playedu.api.BCtx;
import xyz.playedu.api.constant.BackendConstant;
import xyz.playedu.api.domain.AdminRole;
import xyz.playedu.api.service.AdminPermissionService;
import xyz.playedu.api.service.AdminRoleService;
import xyz.playedu.api.service.AdminUserService;
import xyz.playedu.api.util.PrivacyUtil;

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
        return BackendConstant.UN_AUTH_URI_WHITELIST.contains(uri);
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

    public static String valueHidden(String permissionSlug, String type, String value) {
        if (BCtx.isNull()) {//非后管环境返回原值
            return value;
        }
        HashMap<String, Boolean> permissions = BCtx.getAdminPer();
        if (permissions.get(permissionSlug) != null) {
            return value;
        }
        if (BackendConstant.PRIVACY_FIELD_TYPE_EMAIL.equals(type)) {
            return PrivacyUtil.hideEmail(value);
        } else if (BackendConstant.PRIVACY_FIELD_TYPE_PHONE.equals(type)) {
            return PrivacyUtil.hidePhone(value);
        } else if (BackendConstant.PRIVACY_FIELD_TYPE_NAME.equals(type)) {
            return PrivacyUtil.hideChineseName(value);
        } else if (BackendConstant.PRIVACY_FIELD_TYPE_ID_CARD.equals(type)) {
            return PrivacyUtil.hideIDCard(value);
        }
        return PrivacyUtil.desValue(value, 1, 0, "*");
    }

    public boolean isSuperAdmin() {
        AdminRole superRole = adminRoleService.getBySlug(BackendConstant.SUPER_ADMIN_ROLE);
        if (superRole == null) {
            return false;
        }
        List<Integer> roleIds = adminUserService.getRoleIdsByUserId(BCtx.getId());
        if (roleIds.size() == 0) {
            return false;
        }
        return roleIds.contains(superRole.getId());
    }

}
