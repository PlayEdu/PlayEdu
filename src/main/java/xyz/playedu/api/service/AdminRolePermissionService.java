package xyz.playedu.api.service;

import org.springframework.stereotype.Service;
import xyz.playedu.api.domain.AdminRolePermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.HashMap;
import java.util.List;

/**
 * @author tengteng
 * @description 针对表【admin_role_permission】的数据库操作Service
 * @createDate 2023-02-21 16:07:01
 */

public interface AdminRolePermissionService extends IService<AdminRolePermission> {

    void removeByRoleId(Integer roleId);

    List<Integer> getPermissionIdsByRoleIds(List<Integer> roleIds);
}
