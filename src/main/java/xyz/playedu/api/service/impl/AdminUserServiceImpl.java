package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.playedu.api.domain.AdminUser;
import xyz.playedu.api.service.AdminUserService;
import xyz.playedu.api.mapper.AdminUserMapper;
import org.springframework.stereotype.Service;
import xyz.playedu.api.types.paginate.PaginationResult;

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

    public AdminUser findByEmail(String email) {
        return getOne(query().getWrapper().eq("email", email));
    }

    @Override
    public AdminUser findById(Integer id) {
        return getOne((query().getWrapper().eq("id", id)));
    }
}




