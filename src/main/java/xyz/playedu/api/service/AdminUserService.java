package xyz.playedu.api.service;

import xyz.playedu.api.domain.AdminUser;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.exception.ServiceException;
import xyz.playedu.api.types.paginate.AdminUserPaginateFilter;
import xyz.playedu.api.types.paginate.PaginationResult;

import java.util.List;

/**
 * @author tengteng
 * @description 针对表【admin_users】的数据库操作Service
 * @createDate 2023-02-11 10:58:52
 */
public interface AdminUserService extends IService<AdminUser> {
    PaginationResult<AdminUser> paginate(int page, int size, AdminUserPaginateFilter filter);

    AdminUser findByEmail(String email);

    Boolean emailExists(String email);

    AdminUser findById(Integer id);

    AdminUser findOrFail(Integer id) throws NotFoundException;

    void createWithRoleIds(String name, String email, String password, Integer isBanLogin, Integer[] roleIds) throws ServiceException;

    void relateRoles(AdminUser user, Integer[] roleIds);

    void resetRelateRoles(AdminUser user, Integer[] roleIds);

    List<Integer> getRoleIdsByUserId(Integer userId);

    void updateWithRoleIds(AdminUser user, String name, String email, String password, Integer isBanLogin, Integer[] roleIds) throws ServiceException;

    void removeWithRoleIds(Integer userId);

    void removeRelateRolesByUserId(Integer userId);

    void passwordChange(AdminUser user, String password);

    List<AdminUser> chunks(List<Integer> ids);
}
