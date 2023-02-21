package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.playedu.api.domain.AdminRole;
import xyz.playedu.api.service.AdminRoleService;
import xyz.playedu.api.mapper.AdminRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author tengteng
* @description 针对表【admin_roles】的数据库操作Service实现
* @createDate 2023-02-21 15:53:27
*/
@Service
public class AdminRoleServiceImpl extends ServiceImpl<AdminRoleMapper, AdminRole>
    implements AdminRoleService{

}




