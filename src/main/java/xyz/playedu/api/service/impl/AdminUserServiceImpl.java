package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import xyz.playedu.api.domain.AdminUser;
import xyz.playedu.api.service.AdminUserService;
import xyz.playedu.api.mapper.AdminUserMapper;
import org.springframework.stereotype.Service;
import xyz.playedu.api.types.PaginationResult;

/**
 * @author tengteng
 * @description 针对表【admin_users】的数据库操作Service实现
 * @createDate 2023-01-30 16:22:00
 */
@Service
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser> implements AdminUserService {

    public PaginationResult<AdminUser> paginate(int page, int size, Wrapper<AdminUser> queryWrapper) {
        IPage<AdminUser> userPage = new Page<>(page, size);
        userPage = this.getBaseMapper().selectPage(userPage, queryWrapper);

        PaginationResult<AdminUser> pageResult = new PaginationResult<>();
        pageResult.setData(userPage.getRecords());
        pageResult.setTotal(userPage.getTotal());

        return pageResult;
    }

}




