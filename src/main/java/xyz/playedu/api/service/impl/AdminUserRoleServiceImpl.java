package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.playedu.api.domain.AdminUserRole;
import xyz.playedu.api.service.AdminUserRoleService;
import xyz.playedu.api.mapper.AdminUserRoleMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tengteng
 * @description 针对表【admin_user_role】的数据库操作Service实现
 * @createDate 2023-02-21 16:25:43
 */
@Service
public class AdminUserRoleServiceImpl extends ServiceImpl<AdminUserRoleMapper, AdminUserRole> implements AdminUserRoleService {

    @Override
    public void removeByUserId(Integer userId) {
        remove(query().getWrapper().eq("admin_id", userId));
    }

    @Override
    public List<Integer> getRoleIdsByUserId(Integer userId) {
        List<AdminUserRole> userRoles = list(query().getWrapper().eq("admin_id", userId));
        List<Integer> ids = new ArrayList<>();
        for (AdminUserRole userRole : userRoles) {
            ids.add(userRole.getRoleId());
        }
        return ids;
    }
}




