package xyz.playedu.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.playedu.api.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import xyz.playedu.api.types.paginate.UserPaginateFilter;

import java.util.List;

/**
* @author tengteng
* @description 针对表【users】的数据库操作Mapper
* @createDate 2023-03-20 13:37:33
* @Entity xyz.playedu.api.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

    List<User> paginate(UserPaginateFilter filter);

    Long paginateCount(UserPaginateFilter filter);
}




