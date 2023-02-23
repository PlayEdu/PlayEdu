package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.playedu.api.domain.AdminPermission;
import xyz.playedu.api.service.AdminPermissionService;
import xyz.playedu.api.mapper.AdminPermissionMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author tengteng
 * @description 针对表【admin_permissions】的数据库操作Service实现
 * @createDate 2023-02-20 14:27:50
 */
@Service
public class AdminPermissionServiceImpl extends ServiceImpl<AdminPermissionMapper, AdminPermission> implements AdminPermissionService {

    @Override
    public HashMap<String, Boolean> allSlugs() {
        List<AdminPermission> data = list();
        HashMap<String, Boolean> map = new HashMap<>();
        for (AdminPermission permission : data) {
            map.put(permission.getSlug(), true);
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
}




