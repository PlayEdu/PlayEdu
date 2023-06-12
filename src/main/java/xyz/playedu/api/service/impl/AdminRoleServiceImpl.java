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
package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xyz.playedu.api.domain.AdminRole;
import xyz.playedu.api.domain.AdminRolePermission;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.mapper.AdminRoleMapper;
import xyz.playedu.api.service.AdminRoleService;
import xyz.playedu.api.service.internal.AdminRolePermissionService;
import xyz.playedu.api.service.internal.AdminUserRoleService;
import xyz.playedu.api.util.HelperUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author tengteng
 * @description 针对表【admin_roles】的数据库操作Service实现
 * @createDate 2023-02-21 15:53:27
 */
@Service
public class AdminRoleServiceImpl extends ServiceImpl<AdminRoleMapper, AdminRole>
        implements AdminRoleService {

    @Autowired private AdminRolePermissionService rolePermissionService;

    @Autowired private AdminUserRoleService userRoleService;

    @Override
    public AdminRole getBySlug(String slug) {
        return getOne(query().getWrapper().eq("slug", slug));
    }

    @Override
    @Transactional
    public void createWithPermissionIds(String name, Integer[] permissionIds) {
        AdminRole role = new AdminRole();

        role.setName(name);
        role.setSlug(HelperUtil.randomString(12));
        role.setCreatedAt(new Date());
        role.setUpdatedAt(new Date());

        save(role);

        relatePermissions(role, permissionIds);
    }

    @Override
    public void relatePermissions(AdminRole role, Integer[] permissionIds) {
        if (permissionIds == null || permissionIds.length == 0) {
            return;
        }

        List<AdminRolePermission> rolePermissions = new ArrayList<>();
        for (int i = 0; i < permissionIds.length; i++) {
            AdminRolePermission rolePermission = new AdminRolePermission();
            rolePermission.setRoleId(role.getId());
            rolePermission.setPermId(permissionIds[i]);
            rolePermissions.add(rolePermission);
        }
        rolePermissionService.saveBatch(rolePermissions);
    }

    @Override
    public void resetRelatePermissions(AdminRole role, Integer[] permissionIds) {
        removeRelatePermissionByRoleId(role.getId());
        relatePermissions(role, permissionIds);
    }

    @Override
    @Transactional
    public void updateWithPermissionIds(AdminRole role, String name, Integer[] permissionIds) {
        if (!role.getName().equals(name)) {
            AdminRole newRole = new AdminRole();
            newRole.setId(role.getId());
            newRole.setName(name);
            updateById(newRole);
        }

        resetRelatePermissions(role, permissionIds);
    }

    @Override
    public AdminRole findOrFail(Integer id) throws NotFoundException {
        AdminRole role = getOne(query().getWrapper().eq("id", id));
        if (role == null) {
            throw new NotFoundException("管理角色不存在");
        }
        return role;
    }

    @Override
    @Transactional
    public void removeWithPermissions(AdminRole role) {
        removeRelatePermissionByRoleId(role.getId());
        removeById(role.getId());
        userRoleService.remove(userRoleService.query().getWrapper().eq("role_id", role.getId()));
    }

    @Override
    public List<Integer> getPermissionIdsByRoleId(Integer roleId) {
        QueryWrapper<AdminRolePermission> wrapper =
                rolePermissionService.query().getWrapper().eq("role_id", roleId);
        List<AdminRolePermission> rolePermissions = rolePermissionService.list(wrapper);
        List<Integer> ids = new ArrayList<>();
        for (AdminRolePermission rolePermission : rolePermissions) {
            ids.add(rolePermission.getPermId());
        }
        return ids;
    }

    @Override
    public List<Integer> getPermissionIdsByRoleIds(List<Integer> roleIds) {
        QueryWrapper<AdminRolePermission> wrapper =
                rolePermissionService.query().getWrapper().in("role_id", roleIds);
        List<AdminRolePermission> rolePermissions = rolePermissionService.list(wrapper);
        List<Integer> ids = new ArrayList<>();
        for (AdminRolePermission rolePermission : rolePermissions) {
            ids.add(rolePermission.getPermId());
        }
        return ids;
    }

    @Override
    public void removeRelatePermissionByRoleId(Integer roleId) {
        QueryWrapper<AdminRolePermission> wrapper =
                rolePermissionService.query().getWrapper().eq("role_id", roleId);
        rolePermissionService.remove(wrapper);
    }
}
