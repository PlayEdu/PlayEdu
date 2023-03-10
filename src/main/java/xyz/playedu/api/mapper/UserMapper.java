package xyz.playedu.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.playedu.api.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author tengteng
* @description 针对表【users】的数据库操作Mapper
* @createDate 2023-02-23 14:04:49
* @Entity xyz.playedu.api.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




