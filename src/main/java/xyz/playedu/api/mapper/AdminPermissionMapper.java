package xyz.playedu.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.playedu.api.domain.AdminPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author tengteng
 * @description 针对表【admin_permissions】的数据库操作Mapper
 * @createDate 2023-02-20 14:27:50
 * @Entity xyz.playedu.api.domain.AdminPermission
 */
@Mapper
public interface AdminPermissionMapper extends BaseMapper<AdminPermission> {

}




