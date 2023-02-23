package xyz.playedu.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.playedu.api.domain.UserDepartment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author tengteng
* @description 针对表【user_department】的数据库操作Mapper
* @createDate 2023-02-23 15:08:38
* @Entity xyz.playedu.api.domain.UserDepartment
*/
@Mapper
public interface UserDepartmentMapper extends BaseMapper<UserDepartment> {

}




