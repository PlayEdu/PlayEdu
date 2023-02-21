package xyz.playedu.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.playedu.api.domain.AdminRolePermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author tengteng
* @description 针对表【admin_role_permission】的数据库操作Mapper
* @createDate 2023-02-21 16:07:01
* @Entity xyz.playedu.api.domain.AdminRolePermission
*/
@Mapper
public interface AdminRolePermissionMapper extends BaseMapper<AdminRolePermission> {

}




