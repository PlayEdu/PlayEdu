/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
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
