package xyz.playedu.api.service;

import xyz.playedu.api.domain.AdminUser;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.playedu.api.types.paginate.AdminUserPaginateFilter;
import xyz.playedu.api.types.paginate.PaginationResult;

/**
 * @author tengteng
 * @description 针对表【admin_users】的数据库操作Service
 * @createDate 2023-02-11 10:58:52
 */
public interface AdminUserService extends IService<AdminUser> {
    PaginationResult<AdminUser> paginate(int page, int size, AdminUserPaginateFilter filter);

    AdminUser findByEmail(String email);

    AdminUser findById(Integer id);
}
