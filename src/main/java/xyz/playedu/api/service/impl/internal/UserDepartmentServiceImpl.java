package xyz.playedu.api.service.impl.internal;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.playedu.api.domain.UserDepartment;
import xyz.playedu.api.service.internal.UserDepartmentService;
import xyz.playedu.api.mapper.UserDepartmentMapper;
import org.springframework.stereotype.Service;

/**
 * @author tengteng
 * @description 针对表【user_department】的数据库操作Service实现
 * @createDate 2023-02-23 15:08:38
 */
@Service
public class UserDepartmentServiceImpl extends ServiceImpl<UserDepartmentMapper, UserDepartment>
        implements UserDepartmentService {
}




