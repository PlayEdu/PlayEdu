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
        if (filter.getDepIds() != null && filter.getDepIds().length > 0) {
            List<Integer> userIds = userDepartmentService.getUserIdsByDepIds(filter.getDepIds());
            if (userIds.size() == 0) {
                userIds.add(0);
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

        if (!user.getEmail().equals(email)) {
            newUser.setEmail(email);
        }
        if (!user.getNickname().equals(nickname)) {
            newUser.setNickname(nickname);
        }
        if (!user.getName().equals(name)) {
            newUser.setName(name);
        }
        if (!user.getAvatar().equals(avatar)) {
            newUser.setAvatar(avatar);
        }
        if (password != null && password.length() > 0) {
            newUser.setPassword(HelperUtil.MD5(password + user.getSalt()));
        }
        if (!user.getIdCard().equals(idCard)) {
            newUser.setIdCard(idCard);
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
}




