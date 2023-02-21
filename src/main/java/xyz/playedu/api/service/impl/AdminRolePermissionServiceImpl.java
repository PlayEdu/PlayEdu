package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import xyz.playedu.api.domain.AdminRole;
import xyz.playedu.api.domain.AdminRolePermission;
import xyz.playedu.api.service.AdminRolePermissionService;
import xyz.playedu.api.mapper.AdminRolePermissionMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tengteng
 * @description 针对表【admin_role_permission】的数据库操作Service实现
 * @createDate 2023-02-21 16:07:01
 */
@Service
public class AdminRolePermissionServiceImpl extends ServiceImpl<AdminRolePermissionMapper, AdminRolePermission> implements AdminRolePermissionService {
    @Override
    public void removeRolePermissionsByRoleId(Integer roleId) {
        remove(query().getWrapper().eq("role_id", roleId));
    }

    @Override
    public List<Integer> getPermissionIdsByRoleIds(List<Integer> roleIds) {
        List<AdminRolePermission> rolePermissions = list(query().getWrapper().in("role_id", roleIds));
        List<Integer> ids = new ArrayList<>();
        for (AdminRolePermission rolePermission : rolePermissions) {
            ids.add(rolePermission.getPermId());
        }
        return ids;
    }
}




