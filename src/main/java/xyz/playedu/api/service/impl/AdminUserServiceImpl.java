package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import xyz.playedu.api.domain.AdminUser;
import xyz.playedu.api.domain.AdminUserRole;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.exception.ServiceException;
import xyz.playedu.api.service.AdminUserRoleService;
import xyz.playedu.api.service.AdminUserService;
import xyz.playedu.api.mapper.AdminUserMapper;
import org.springframework.stereotype.Service;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.types.paginate.AdminUserPaginateFilter;
import xyz.playedu.api.types.paginate.PaginationResult;
import xyz.playedu.api.util.HelperUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser> implements AdminUserService {

    @Autowired
    private AdminUserRoleService userRoleService;

    public PaginationResult<AdminUser> paginate(int page, int size, AdminUserPaginateFilter filter) {
        QueryWrapper<AdminUser> wrapper = query().getWrapper().eq("1", "1");

        if (filter.getName() != null) {
            wrapper.like("name", "%" + filter.getName() + "%");
        }

        IPage<AdminUser> pageObj = new Page<>(page, size);
        pageObj = this.getBaseMapper().selectPage(pageObj, wrapper);

        PaginationResult<AdminUser> pageResult = new PaginationResult<>();
        pageResult.setData(pageObj.getRecords());
        pageResult.setTotal(pageObj.getTotal());

        return pageResult;
    }

    @Override
    public AdminUser findByEmail(String email) {
        return getOne(query().getWrapper().eq("email", email));
    }

    @Override
    public AdminUser findById(Integer id) {
        return getOne((query().getWrapper().eq("id", id)));
    }

    @Override
    public AdminUser findOrFail(Integer id) throws NotFoundException {
        AdminUser user = getOne(query().getWrapper().eq("id", id));
        if (user == null) {
            throw new NotFoundException("管理员不存在");
        }
        return user;
    }

    @Override
    public Boolean emailExists(String email) {
        AdminUser user = findByEmail(email);
        return user != null;
    }

    @Override
    @Transactional
    public void createWithRoleIds(String name, String email, String password, Integer isBanLogin, Integer[] roleIds) throws ServiceException {
        if (emailExists(email)) {
            throw new ServiceException("邮箱已存在");
        }

        String salt = HelperUtil.randomString(6);

        AdminUser adminUser = new AdminUser();
        adminUser.setName(name);
        adminUser.setEmail(email);
        adminUser.setSalt(salt);
        adminUser.setPassword(HelperUtil.MD5(password + salt));
        adminUser.setIsBanLogin(isBanLogin);
        adminUser.setCreatedAt(new Date());
        adminUser.setUpdatedAt(new Date());

        save(adminUser);

        relateRoles(adminUser, roleIds);
    }

    @Override
    public void relateRoles(AdminUser user, Integer[] roleIds) {
        if (roleIds == null || roleIds.length == 0) {
            return;
        }
        List<AdminUserRole> userRoles = new ArrayList<>();
        for (int i = 0; i < roleIds.length; i++) {
            AdminUserRole userRole = new AdminUserRole();
            userRole.setAdminId(user.getId());
            userRole.setRoleId(roleIds[i]);
            userRoles.add(userRole);
        }
        userRoleService.saveBatch(userRoles);
    }

    @Override
    public void resetRelateRoles(AdminUser user, Integer[] roleIds) {
        userRoleService.removeByUserId(user.getId());
        relateRoles(user, roleIds);
    }

    @Override
    public List<Integer> getRoleIdsByUserId(Integer userId) {
        return userRoleService.getRoleIdsByUserId(userId);
    }

    @Override
    @Transactional
    public void updateWithRoleIds(AdminUser user, String name, String email, String password, Integer isBanLogin, Integer[] roleIds) throws ServiceException {
        AdminUser updateAdminUser = new AdminUser();
        updateAdminUser.setId(user.getId());

        if (!user.getEmail().equals(email)) {//更换了邮箱
            if (emailExists(email)) {
                throw new ServiceException("邮箱已存在");
            }
            updateAdminUser.setEmail(email);
        }

        if (password != null && password.length() > 0) {//更换了密码
            updateAdminUser.setPassword(HelperUtil.MD5(password + user.getSalt()));
        }

        if (!user.getName().equals(name)) {//更换了姓名
            updateAdminUser.setName(name);
        }

        if (!user.getIsBanLogin().equals(isBanLogin)) {
            updateAdminUser.setIsBanLogin(isBanLogin);
        }

        updateById(updateAdminUser);

        resetRelateRoles(user, roleIds);
    }

    @Override
    @Transactional
    public void removeWithRoleIds(Integer userId) {
        userRoleService.removeByUserId(userId);
        removeById(userId);
    }
}




