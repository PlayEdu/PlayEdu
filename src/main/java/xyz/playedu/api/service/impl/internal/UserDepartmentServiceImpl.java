package xyz.playedu.api.service.impl.internal;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
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
public class UserDepartmentServiceImpl extends ServiceImpl<UserDepartmentMapper, UserDepartment> implements UserDepartmentService {

    @Override
    public List<Integer> getUserIdsByDepIds(List<Integer> depIds) {
        return list(query().getWrapper().in("dep_id", depIds)).stream().map(UserDepartment::getUserId).toList();
    }

    @Override
    public void storeDepIds(Integer userId, Integer[] depIds) {
        if (depIds == null) {
            return;
        }
        List<UserDepartment> userDepartments = new ArrayList<>();
        for (int i = 0; i < depIds.length; i++) {
            Integer depId = depIds[i];
            userDepartments.add(new UserDepartment() {{
                setUserId(userId);
                setDepId(depId);
            }});
        }
        saveBatch(userDepartments);
    }

    @Override
    public void resetStoreDepIds(Integer userId, Integer[] depIds) {
        remove(query().getWrapper().eq("user_id", userId));
        storeDepIds(userId, depIds);
    }
}




