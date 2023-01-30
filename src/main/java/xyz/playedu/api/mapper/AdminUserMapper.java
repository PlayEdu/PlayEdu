package xyz.playedu.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.playedu.api.domain.AdminUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author tengteng
* @description 针对表【admin_users】的数据库操作Mapper
* @createDate 2023-01-30 16:22:00
* @Entity xyz.playedu.api.domain.AdminUser
*/
@Mapper
public interface AdminUserMapper extends BaseMapper<AdminUser> {

}




