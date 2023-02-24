package xyz.playedu.api.service;

import xyz.playedu.api.domain.AdminRole;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.playedu.api.exception.NotFoundException;

/**
 * @author tengteng
 * @description 针对表【admin_roles】的数据库操作Service
 * @createDate 2023-02-21 15:53:27
 */
public interface AdminRoleService extends IService<AdminRole> {

    AdminRole getBySlug(String slug);

    AdminRole createWithPermissionIds(String name, Integer[] permissionIds);

    void relatePermissions(AdminRole role, Integer[] permissionIds);

    void resetRelatePermissions(AdminRole role, Integer[] permissionIds);

    void updateWithPermissionIds(AdminRole role, String name, Integer[] permissionIds);

    AdminRole findOrFail(Integer id) throws NotFoundException;

    void removeWithPermissions(AdminRole role);

}
