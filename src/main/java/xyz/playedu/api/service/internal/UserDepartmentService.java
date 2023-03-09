package xyz.playedu.api.service.internal;

import xyz.playedu.api.domain.UserDepartment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author tengteng
* @description 针对表【user_department】的数据库操作Service
* @createDate 2023-02-23 15:08:38
*/
public interface UserDepartmentService extends IService<UserDepartment> {
    List<Integer> getUserIdsByDepIds(List<Integer> depIds);

    void storeDepIds(Integer userId, Integer[] depIds);

    void resetStoreDepIds(Integer userId, Integer[] depIds);
}
