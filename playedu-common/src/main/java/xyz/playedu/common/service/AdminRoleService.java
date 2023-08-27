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
package xyz.playedu.common.service;

import com.baomidou.mybatisplus.extension.service.IService;

import xyz.playedu.common.domain.AdminRole;
import xyz.playedu.common.exception.NotFoundException;

import java.util.List;

/**
 * @author tengteng
 * @description 针对表【admin_roles】的数据库操作Service
 * @createDate 2023-02-21 15:53:27
 */
public interface AdminRoleService extends IService<AdminRole> {

    AdminRole getBySlug(String slug);

    Integer initSuperAdminRole();

    void createWithPermissionIds(String name, Integer[] permissionIds);

    void relatePermissions(AdminRole role, Integer[] permissionIds);

    void resetRelatePermissions(AdminRole role, Integer[] permissionIds);

    void updateWithPermissionIds(AdminRole role, String name, Integer[] permissionIds);

    AdminRole findOrFail(Integer id) throws NotFoundException;

    void removeWithPermissions(AdminRole role);

    List<Integer> getPermissionIdsByRoleId(Integer roleId);

    List<Integer> getPermissionIdsByRoleIds(List<Integer> roleIds);

    void removeRelatePermissionByRoleId(Integer roleId);
}
