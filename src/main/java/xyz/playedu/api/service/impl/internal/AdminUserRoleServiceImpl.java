package xyz.playedu.api.service.impl.internal;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.playedu.api.domain.AdminUserRole;
import xyz.playedu.api.service.internal.AdminUserRoleService;
import xyz.playedu.api.mapper.AdminUserRoleMapper;
import org.springframework.stereotype.Service;

/**
 * @author tengteng
 * @description 针对表【admin_user_role】的数据库操作Service实现
 * @createDate 2023-02-21 16:25:43
 */
@Service
public class AdminUserRoleServiceImpl extends ServiceImpl<AdminUserRoleMapper, AdminUserRole> implements AdminUserRoleService {
}




