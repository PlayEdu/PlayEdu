/*
 * Copyright (C) 2023 杭州白书科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.playedu.common.bus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import xyz.playedu.common.context.BCtx;
import xyz.playedu.common.constant.BackendConstant;
import xyz.playedu.common.domain.AdminRole;
import xyz.playedu.common.service.AdminPermissionService;
import xyz.playedu.common.service.AdminRoleService;
import xyz.playedu.common.service.AdminUserService;
import xyz.playedu.common.util.PrivacyUtil;

import java.util.HashMap;
import java.util.List;

@Component
public class BackendBus {

    @Autowired private AdminPermissionService permissionService;

    @Autowired private AdminRoleService adminRoleService;

    @Autowired private AdminUserService adminUserService;

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

        if (roleIds.contains(superRole.getId())) { // 包含超级管理角色的话返回全部权限
            permissionIds = permissionService.allIds();
        } else { // 根据相应的roleIds读取权限
            permissionIds = adminRoleService.getPermissionIdsByRoleIds(roleIds);
            if (permissionIds.size() == 0) {
                return permissions;
            }
        }

        return permissionService.getSlugsByIds(permissionIds);
    }

    public static String valueHidden(String permissionSlug, String type, String value) {
        if (BCtx.isNull() || value == null) { // 非后管环境直接返回 || 值为null不需要处理
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
