/*
 * Copyright 2023 杭州白书科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.playedu.api.service.impl.internal;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

import xyz.playedu.api.domain.AdminUserRole;
import xyz.playedu.api.mapper.AdminUserRoleMapper;
import xyz.playedu.api.service.internal.AdminUserRoleService;

import java.util.List;

/**
 * @author tengteng
 * @description 针对表【admin_user_role】的数据库操作Service实现
 * @createDate 2023-02-21 16:25:43
 */
@Service
public class AdminUserRoleServiceImpl extends ServiceImpl<AdminUserRoleMapper, AdminUserRole>
        implements AdminUserRoleService {
    @Override
    public List<Integer> getAdminUserIds(Integer roleId) {
        return list(query().getWrapper().eq("role_id", roleId)).stream()
                .map(AdminUserRole::getAdminId)
                .toList();
    }
}
