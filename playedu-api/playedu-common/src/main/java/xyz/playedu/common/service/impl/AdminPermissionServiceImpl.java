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
package xyz.playedu.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Service;
import xyz.playedu.common.domain.AdminPermission;
import xyz.playedu.common.mapper.AdminPermissionMapper;
import xyz.playedu.common.service.AdminPermissionService;

/**
 * @author tengteng
 * @description 针对表【admin_permissions】的数据库操作Service实现
 * @createDate 2023-02-20 14:27:50
 */
@Service
public class AdminPermissionServiceImpl extends ServiceImpl<AdminPermissionMapper, AdminPermission>
        implements AdminPermissionService {

    @Override
    public HashMap<String, Integer> allSlugs() {
        List<AdminPermission> data = list();
        HashMap<String, Integer> map = new HashMap<>();
        for (AdminPermission permission : data) {
            map.put(permission.getSlug(), permission.getId());
        }
        return map;
    }

    @Override
    public List<AdminPermission> listOrderBySortAsc() {
        return list(query().getWrapper().orderByAsc("group_name", "sort"));
    }

    @Override
    public HashMap<String, Boolean> getSlugsByIds(List<Integer> ids) {
        List<AdminPermission> adminPermissions = list(query().getWrapper().in("id", ids));
        HashMap<String, Boolean> map = new HashMap<>();
        for (AdminPermission adminPermission : adminPermissions) {
            map.put(adminPermission.getSlug(), true);
        }
        return map;
    }

    @Override
    public List<Integer> allIds() {
        List<AdminPermission> permissions = list(query().getWrapper().eq("1", "1").select("id"));
        List<Integer> ids = new ArrayList<>();
        for (AdminPermission permission : permissions) {
            ids.add(permission.getId());
        }
        return ids;
    }

    @Override
    public List<AdminPermission> chunks(List<Integer> ids) {
        return list(query().getWrapper().in("id", ids));
    }
}
