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
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.types.paginate.AdminUserPaginateFilter;
import xyz.playedu.api.types.paginate.PaginationResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/backend/v1/admin-user")
public class AdminUserController {

    @Autowired private AdminUserService adminUserService;

    @Autowired private AdminRoleService roleService;

    @BackendPermissionMiddleware(slug = BPermissionConstant.ADMIN_USER_INDEX)
    @GetMapping("/index")
    public JsonResponse Index(@RequestParam HashMap<String, Object> params) {
        Integer page = MapUtils.getInteger(params, "page", 1);
        Integer size = MapUtils.getInteger(params, "size", 10);
        String name = MapUtils.getString(params, "name");
        Integer roleId = MapUtils.getInteger(params, "role_id");

        AdminUserPaginateFilter filter = new AdminUserPaginateFilter();
        filter.setName(name);
        filter.setRoleId(roleId);

        PaginationResult<AdminUser> result = adminUserService.paginate(page, size, filter);

        Map<Integer, List<Integer>> userRoleIds = new HashMap<>();
        if (result.getData() != null && result.getData().size() > 0) {
            userRoleIds =
                    adminUserService.getAdminUserRoleIds(
                            result.getData().stream().map(AdminUser::getId).toList());
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("data", result.getData());
        data.put("total", result.getTotal());
        data.put("user_role_ids", userRoleIds);
        data.put(
                "roles",
                roleService.list().stream().collect(Collectors.groupingBy(AdminRole::getId)));

        return JsonResponse.data(data);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.ADMIN_USER_CUD)
    @GetMapping("/create")
    public JsonResponse create() {
        List<AdminRole> roles = roleService.list();

        HashMap<String, Object> data = new HashMap<>();
        data.put("roles", roles);

        return JsonResponse.data(data);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.ADMIN_USER_CUD)
    @PostMapping("/create")
    public JsonResponse store(@RequestBody @Validated AdminUserRequest req)
            throws ServiceException {
        if (req.getPassword() == null || req.getPassword().length() == 0) {
            return JsonResponse.error("请输入密码");
        }

        adminUserService.createWithRoleIds(
                req.getName(),
                req.getEmail(),
                req.getPassword(),
                req.getIsBanLogin(),
                req.getRoleIds());

        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.ADMIN_USER_CUD)
    @GetMapping("/{id}")
    public JsonResponse edit(@PathVariable Integer id) throws NotFoundException {
        AdminUser adminUser = adminUserService.findOrFail(id);
        List<Integer> roleIds = adminUserService.getRoleIdsByUserId(adminUser.getId());

        HashMap<String, Object> data = new HashMap<>();
        data.put("user", adminUser);
        data.put("role_ids", roleIds);

        return JsonResponse.data(data);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.ADMIN_USER_CUD)
    @PutMapping("/{id}")
    public JsonResponse update(
            @PathVariable Integer id, @RequestBody @Validated AdminUserRequest req)
            throws NotFoundException, ServiceException {
        AdminUser adminUser = adminUserService.findOrFail(id);
        adminUserService.updateWithRoleIds(
                adminUser,
                req.getName(),
                req.getEmail(),
                req.getPassword(),
                req.getIsBanLogin(),
                req.getRoleIds());
        return JsonResponse.success();
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.ADMIN_USER_CUD)
    @DeleteMapping("/{id}")
    public JsonResponse destroy(@PathVariable Integer id) {
        adminUserService.removeWithRoleIds(id);
        return JsonResponse.success();
    }
}
