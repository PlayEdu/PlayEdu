package xyz.playedu.api.service;

import xyz.playedu.api.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.playedu.api.types.paginate.PaginationResult;
import xyz.playedu.api.types.paginate.UserPaginateFilter;

import java.util.List;

/**
 * @author tengteng
 * @description 针对表【users】的数据库操作Service
 * @createDate 2023-02-23 13:50:58
 */
public interface UserService extends IService<User> {
    boolean emailIsExists(String email);

    PaginationResult<User> paginate(int page, int size, UserPaginateFilter filter);

    List<String> existsEmailsByEmails(List<String> emails);

    void removeRelateDepartmentsByUserId(Integer userId);
}
