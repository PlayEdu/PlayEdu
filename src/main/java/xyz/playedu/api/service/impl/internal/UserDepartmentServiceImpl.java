package xyz.playedu.api.service.impl.internal;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.playedu.api.domain.UserDepartment;
import xyz.playedu.api.service.internal.UserDepartmentService;
import xyz.playedu.api.mapper.UserDepartmentMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author tengteng
 * @description 针对表【user_department】的数据库操作Service实现
 * @createDate 2023-02-23 15:08:38
 */
@Service
public class UserDepartmentServiceImpl extends ServiceImpl<UserDepartmentMapper, UserDepartment>
        implements UserDepartmentService {

    @Override
    public List<Integer> getUserIdsByDepIds(Integer[] depIds) {
        List<Integer> ids = new ArrayList<>();
        List<UserDepartment> userDepartments = list(query().getWrapper().in("dep_id", Arrays.asList(depIds)));
        if (userDepartments.size() > 0) {
            for (UserDepartment userDepartment : userDepartments) {
                ids.add(userDepartment.getUserId());
            }
        }
        return ids;
    }
}




