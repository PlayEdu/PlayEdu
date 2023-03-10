package xyz.playedu.api.service;

import xyz.playedu.api.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.playedu.api.exception.NotFoundException;
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

    User findOrFail(Integer id) throws NotFoundException;

    User find(Integer id);

    User find(String email);

    User createWithDepIds(String email, String name, String avatar, String password, String idCard, Integer[] depIds);

    User updateWithDepIds(User user, String email, String nickname, String name, String avatar, String password, String idCard, Integer[] depIds);

    List<Integer> getDepIdsByUserId(Integer userId);
}
