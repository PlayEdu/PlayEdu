package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import xyz.playedu.api.constant.SystemConstant;
import xyz.playedu.api.domain.User;
import xyz.playedu.api.domain.UserDepartment;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.service.internal.UserDepartmentService;
import xyz.playedu.api.service.UserService;
import xyz.playedu.api.mapper.UserMapper;
import org.springframework.stereotype.Service;
import xyz.playedu.api.types.paginate.PaginationResult;
import xyz.playedu.api.types.paginate.UserPaginateFilter;
import xyz.playedu.api.util.HelperUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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

        if (filter.getEmail() != null && filter.getEmail().trim().length() > 0) {
            wrapper.eq("email", filter.getEmail());
        }
        if (filter.getName() != null && filter.getName().trim().length() > 0) {
            wrapper.eq("name", filter.getName());
        }
        if (filter.getNickname() != null && filter.getNickname().length() > 0) {
            wrapper.eq("nickname", filter.getNickname());
        }
        if (filter.getIdCard() != null && filter.getIdCard().trim().length() > 0) {
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
        if (filter.getCreatedAt() != null && filter.getCreatedAt().trim().length() > 0) {
            Date[] createdAt = Arrays.stream(filter.getCreatedAt().split(",")).map(Date::new).toArray(Date[]::new);
            wrapper.between("created_at", createdAt[0], createdAt[1]);
        }
        if (filter.getDepIds() != null && filter.getDepIds().trim().length() > 0) {
            List<Integer> depIds = Arrays.stream(filter.getDepIds().split(",")).map(Integer::valueOf).toList();
            List<Integer> userIds = userDepartmentService.getUserIdsByDepIds(depIds);
            if (userIds == null || userIds.size() == 0) {
                userIds = HelperUtil.zeroIntegerList();
            }
            wrapper.in("id", userIds);
        }

        String sortFiled = filter.getSortField();
        if (sortFiled == null || sortFiled.trim().length() == 0) {
            sortFiled = "id";
        }
        String sortAlgo = filter.getSortAlgo();
        if (sortAlgo == null || sortAlgo.trim().length() == 0) {
            sortAlgo = "desc";
        }
        if ("desc".equals(sortAlgo)) {
            wrapper.orderByDesc(sortFiled);
        } else {
            wrapper.orderByAsc(sortFiled);
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
        return list(query().getWrapper().in("email", emails).select("id", "email")).stream().map(User::getEmail).toList();
    }

    @Override
    public void removeRelateDepartmentsByUserId(Integer userId) {
        userDepartmentService.remove(userDepartmentService.query().getWrapper().eq("user_id", userId));
    }

    @Override
    public User findOrFail(Integer id) throws NotFoundException {
        User user = getOne(query().getWrapper().eq("id", id));
        if (user == null) {
            throw new NotFoundException("学员不存在");
        }
        return user;
    }

    @Override
    @Transactional
    public User createWithDepIds(String email, String name, String avatar, String password, String idCard, Integer[] depIds) {
        String salt = HelperUtil.randomString(6);
        String passwordHashed = HelperUtil.MD5(password + salt);

        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setNickname(name);
        user.setAvatar(avatar);
        user.setPassword(passwordHashed);
        user.setSalt(salt);
        user.setIdCard(idCard);
        user.setCredit1(0);
        user.setIsSetPassword(0);
        user.setIsActive(1);
        user.setIsLock(0);
        user.setCreateIp(SystemConstant.INTERNAL_IP);
        user.setCreateCity(SystemConstant.INTERNAL_IP_AREA);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        if (idCard != null && idCard.length() > 0) {
            user.setVerifyAt(new Date());
            user.setIsVerify(1);
        }

        save(user);
        userDepartmentService.storeDepIds(user.getId(), depIds);
        return user;
    }

    @Override
    @Transactional
    public User updateWithDepIds(User user, String email, String nickname, String name, String avatar, String password, String idCard, Integer[] depIds) {
        User newUser = new User();
        newUser.setId(user.getId());
        newUser.setEmail(email);
        newUser.setNickname(nickname);
        newUser.setName(name);
        newUser.setAvatar(avatar);
        newUser.setIdCard(idCard);

        if (password != null && password.length() > 0) {
            newUser.setPassword(HelperUtil.MD5(password + user.getSalt()));
        }

        if (newUser.getName() != null && newUser.getName().length() > 0 && newUser.getIdCard() != null && newUser.getIdCard().length() > 0) {
            if (user.getVerifyAt() == null) {
                newUser.setIsVerify(1);
                newUser.setVerifyAt(new Date());
            }
        } else {
            if (user.getIsVerify() == 1) {
                newUser.setIsVerify(0);
                newUser.setVerifyAt(null);
            }
        }

        updateById(newUser);
        userDepartmentService.resetStoreDepIds(newUser.getId(), depIds);
        return newUser;
    }

    @Override
    public List<Integer> getDepIdsByUserId(Integer userId) {
        return userDepartmentService.list(userDepartmentService.query().getWrapper().eq("user_id", userId)).stream().map(UserDepartment::getDepId).toList();
    }

    @Override
    public User find(Integer id) {
        return getOne(query().getWrapper().eq("id", id));
    }

    @Override
    public User find(String email) {
        return getOne(query().getWrapper().eq("email", email));
    }
}




