package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import xyz.playedu.api.constant.SystemConstant;
import xyz.playedu.api.domain.User;
import xyz.playedu.api.domain.UserDepartment;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.exception.ServiceException;
import xyz.playedu.api.service.internal.UserDepartmentService;
import xyz.playedu.api.service.UserService;
import xyz.playedu.api.mapper.UserMapper;
import org.springframework.stereotype.Service;
import xyz.playedu.api.types.paginate.PaginationResult;
import xyz.playedu.api.types.paginate.UserPaginateFilter;
import xyz.playedu.api.util.HelperUtil;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
        filter.setPageStart((page - 1) * size);
        filter.setPageSize(size);

        PaginationResult<User> pageResult = new PaginationResult<>();
        pageResult.setData(getBaseMapper().paginate(filter));
        pageResult.setTotal(getBaseMapper().paginateCount(filter));

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
    public User updateWithDepIds(User user, String email, String name, String avatar, String password, String idCard, Integer[] depIds) {
        User newUser = new User();
        newUser.setId(user.getId());
        newUser.setEmail(email);
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

    @Override
    public void passwordChange(User user, String oldPassword, String newPassword) throws ServiceException {
        if (!HelperUtil.MD5(oldPassword + user.getSalt()).equals(user.getPassword())) {
            throw new ServiceException("原密码不正确");
        }
        updateById(new User() {{
            setId(user.getId());
            setPassword(HelperUtil.MD5(newPassword + user.getSalt()));
        }});
    }

    @Override
    public List<User> chunks(List<Integer> ids, List<String> fields) {
        if (ids == null || ids.size() == 0) {
            return new ArrayList<>();
        }
        return list(query().getWrapper().in("id", ids).select(fields));
    }

    @Override
    public Long total() {
        return count();
    }

    @Override
    @SneakyThrows
    public Long todayCount() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        String todayDate = simpleDateFormat.format(now);
        return count(query().getWrapper().between("created_at", simpleDateFormat.parse(todayDate), now));
    }

    @Override
    @SneakyThrows
    public Long yesterdayCount() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = simpleDateFormat.format(new Date());
        String yesterdayDate = simpleDateFormat.format(new Date(System.currentTimeMillis() - 86400000));
        return count(query().getWrapper().between("created_at", simpleDateFormat.parse(yesterdayDate), simpleDateFormat.parse(todayDate)));
    }

    @Override
    public Map<Integer, List<Integer>> getDepIdsGroup(List<Integer> userIds) {
        if (userIds == null || userIds.size() == 0) {
            return null;
        }
        Map<Integer, List<UserDepartment>> data = userDepartmentService
                .list(userDepartmentService.query().getWrapper().in("user_id", userIds))
                .stream()
                .collect(Collectors.groupingBy(UserDepartment::getUserId));
        Map<Integer, List<Integer>> result = new HashMap<>();
        data.forEach((userId, records) -> {
            result.put(userId, records.stream().map(UserDepartment::getDepId).toList());
        });
        return result;
    }
}




