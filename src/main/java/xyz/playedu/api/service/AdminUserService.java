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
package xyz.playedu.api.service;

import com.baomidou.mybatisplus.extension.service.IService;

import xyz.playedu.api.domain.AdminUser;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.exception.ServiceException;
import xyz.playedu.api.types.paginate.AdminUserPaginateFilter;
import xyz.playedu.api.types.paginate.PaginationResult;

import java.util.List;
import java.util.Map;

/**
 * @author tengteng
 * @description 针对表【admin_users】的数据库操作Service
 * @createDate 2023-02-11 10:58:52
 */
public interface AdminUserService extends IService<AdminUser> {
    PaginationResult<AdminUser> paginate(int page, int size, AdminUserPaginateFilter filter);

    AdminUser findByEmail(String email);

    Boolean emailExists(String email);

    AdminUser findById(Integer id);

    AdminUser findOrFail(Integer id) throws NotFoundException;

    void createWithRoleIds(
            String name, String email, String password, Integer isBanLogin, Integer[] roleIds)
            throws ServiceException;

    void relateRoles(AdminUser user, Integer[] roleIds);

    void resetRelateRoles(AdminUser user, Integer[] roleIds);

    List<Integer> getRoleIdsByUserId(Integer userId);

    void updateWithRoleIds(
            AdminUser user,
            String name,
            String email,
            String password,
            Integer isBanLogin,
            Integer[] roleIds)
            throws ServiceException;

    void removeWithRoleIds(Integer userId);

    void removeRelateRolesByUserId(Integer userId);

    void passwordChange(AdminUser user, String password);

    List<AdminUser> chunks(List<Integer> ids);

    Long total();

    Map<Integer, List<Integer>> getAdminUserRoleIds(List<Integer> userIds);
}
