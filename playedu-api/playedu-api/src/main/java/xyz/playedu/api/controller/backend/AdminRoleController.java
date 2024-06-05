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
package xyz.playedu.api.controller.backend;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import xyz.playedu.api.request.backend.AdminRoleRequest;
import xyz.playedu.common.annotation.BackendPermission;
import xyz.playedu.common.annotation.Log;
import xyz.playedu.common.constant.BPermissionConstant;
import xyz.playedu.common.constant.BackendConstant;
import xyz.playedu.common.constant.BusinessTypeConstant;
import xyz.playedu.common.domain.AdminPermission;
import xyz.playedu.common.domain.AdminRole;
import xyz.playedu.common.exception.NotFoundException;
import xyz.playedu.common.service.AdminPermissionService;
import xyz.playedu.common.service.AdminRoleService;
import xyz.playedu.common.types.JsonResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/backend/v1/admin-role")
@Slf4j
public class AdminRoleController {

    @Autowired private AdminRoleService roleService;

    @Autowired private AdminPermissionService permissionService;

    @GetMapping("/index")
    @Log(title = "管理员角色-列表", businessType = BusinessTypeConstant.GET)
    public JsonResponse index() {
        List<AdminRole> data = roleService.list();
        return JsonResponse.data(data);
    }

    @BackendPermission(slug = BPermissionConstant.ADMIN_ROLE)
    @GetMapping("/create")
    @Log(title = "管理员角色-新建", businessType = BusinessTypeConstant.GET)
    public JsonResponse create() {
        List<AdminPermission> permissions = permissionService.listOrderBySortAsc();

        HashMap<String, Object> data = new HashMap<>();
        data.put(
                "perm_action",
                permissions.stream().collect(Collectors.groupingBy(AdminPermission::getType)));

        return JsonResponse.data(data);
    }

    @BackendPermission(slug = BPermissionConstant.ADMIN_ROLE)
    @PostMapping("/create")
    @Log(title = "管理员角色-新建", businessType = BusinessTypeConstant.INSERT)
    public JsonResponse store(@RequestBody @Validated AdminRoleRequest request) {
        roleService.createWithPermissionIds(request.getName(), request.getPermissionIds());
        return JsonResponse.success();
    }

    @BackendPermission(slug = BPermissionConstant.ADMIN_ROLE)
    @GetMapping("/{id}")
    @Log(title = "管理员角色-编辑", businessType = BusinessTypeConstant.GET)
    public JsonResponse edit(@PathVariable(name = "id") Integer id) throws NotFoundException {
        AdminRole role = roleService.findOrFail(id);

        // 关联的权限
        List<Integer> permissionIds = roleService.getPermissionIdsByRoleId(role.getId());
        List<Integer> permAction = new ArrayList<>();
        List<Integer> permData = new ArrayList<>();
        if (permissionIds != null && permissionIds.size() > 0) {
            List<AdminPermission> permissions = permissionService.chunks(permissionIds);
            Map<String, List<AdminPermission>> permissionsGroup =
                    permissions.stream().collect(Collectors.groupingBy(AdminPermission::getType));
            if (permissionsGroup.get("action") != null) {
                permAction =
                        permissionsGroup.get("action").stream()
                                .map(AdminPermission::getId)
                                .toList();
            }
            if (permissionsGroup.get("data") != null) {
                permData =
                        permissionsGroup.get("data").stream().map(AdminPermission::getId).toList();
            }
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("role", role);
        data.put("perm_action", permAction);
        data.put("perm_data", permData);

        return JsonResponse.data(data);
    }

    @BackendPermission(slug = BPermissionConstant.ADMIN_ROLE)
    @PutMapping("/{id}")
    @Log(title = "管理员角色-编辑", businessType = BusinessTypeConstant.UPDATE)
    public JsonResponse update(
            @PathVariable(name = "id") Integer id, @RequestBody @Validated AdminRoleRequest request)
            throws NotFoundException {
        AdminRole role = roleService.findOrFail(id);
        if (role.getSlug().equals(BackendConstant.SUPER_ADMIN_ROLE)) {
            return JsonResponse.error("超级管理权限无法编辑");
        }

        roleService.updateWithPermissionIds(role, request.getName(), request.getPermissionIds());

        return JsonResponse.success();
    }

    @BackendPermission(slug = BPermissionConstant.ADMIN_ROLE)
    @DeleteMapping("/{id}")
    @Log(title = "管理员角色-删除", businessType = BusinessTypeConstant.DELETE)
    public JsonResponse destroy(@PathVariable(name = "id") Integer id) throws NotFoundException {
        AdminRole role = roleService.findOrFail(id);

        if (role.getSlug().equals(BackendConstant.SUPER_ADMIN_ROLE)) {
            return JsonResponse.error("超级管理角色无法删除");
        }

        roleService.removeWithPermissions(role);

        return JsonResponse.success();
    }
}
