package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.playedu.api.domain.User;
import xyz.playedu.api.domain.UserDepartment;
import xyz.playedu.api.service.internal.UserDepartmentService;
import xyz.playedu.api.service.UserService;
import xyz.playedu.api.mapper.UserMapper;
import org.springframework.stereotype.Service;
import xyz.playedu.api.types.paginate.PaginationResult;
import xyz.playedu.api.types.paginate.UserPaginateFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tengteng
 * @description 针对表【users】的数据库操作Service实现
 * @createDate 2023-02-23 13:50:58
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserDepartmentService userDepartmentService;

    @Override
    public boolean emailIsExists(String email) {
        User user = getOne(query().getWrapper().eq("email", email));
        return user != null;
    }

    @Override
    public PaginationResult<User> paginate(int page, int size, UserPaginateFilter filter) {
        QueryWrapper<User> wrapper = query().getWrapper().eq("1", "1");

        if (filter != null) {
            if (filter.getEmail() != null) {
                wrapper.eq("email", filter.getEmail());
            }
            if (filter.getName() != null) {
                wrapper.eq("name", filter.getName());
            }
            if (filter.getNickname() != null) {
                wrapper.eq("nickname", filter.getNickname());
            }
            if (filter.getIdCard() != null) {
                wrapper.eq("id_card", filter.getIdCard());
            }
            if (filter.getIsActive() != null) {
                wrapper.eq("is_active", filter.getIsActive());
            }
            if (filter.getIsLock() != null) {
                wrapper.eq("is_lock", filter.getIsLock());
            }
            if (filter.getIsVerify() != null) {
                wrapper.eq("is_verify", filter.getIsVerify());
            }
            if (filter.getIsSetPassword() != null) {
                wrapper.eq("is_set_password", filter.getIsSetPassword());
            }
            if (filter.getCreatedAt() != null && filter.getCreatedAt().length == 2) {
                wrapper.between("created_at", filter.getCreatedAt()[0], filter.getCreatedAt()[1]);
            }
            if (filter.getSortField() != null && filter.getSortAlgo() != null) {
                wrapper.orderBy(true, filter.getSortAlgo().toUpperCase() == "ASC", filter.getSortField());
            }
        }

        IPage<User> userPage = new Page<>(page, size);
        userPage = page(userPage, wrapper);

        PaginationResult<User> pageResult = new PaginationResult<>();
        pageResult.setData(userPage.getRecords());
        pageResult.setTotal(userPage.getTotal());

        return pageResult;
    }

    @Override
    public List<String> existsEmailsByEmails(List<String> emails) {
        List<User> users = list(query().getWrapper().in("email", emails).select("id", "email"));
        List<String> existsEmails = new ArrayList<>();
        for (User user : users) {
            existsEmails.add(user.getEmail());
        }
        return existsEmails;
    }

    @Override
    public void removeRelateDepartmentsByUserId(Integer userId) {
        QueryWrapper<UserDepartment> wrapper = userDepartmentService.query().getWrapper().eq("user_id", userId);
        userDepartmentService.remove(wrapper);
    }
}




