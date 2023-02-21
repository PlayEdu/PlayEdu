package xyz.playedu.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.playedu.api.domain.AdminUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author tengteng
* @description 针对表【admin_user_role】的数据库操作Mapper
* @createDate 2023-02-21 16:25:43
* @Entity xyz.playedu.api.domain.AdminUserRole
*/
@Mapper
public interface AdminUserRoleMapper extends BaseMapper<AdminUserRole> {

}




