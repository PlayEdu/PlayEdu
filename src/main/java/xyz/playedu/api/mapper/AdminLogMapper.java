package xyz.playedu.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.playedu.api.domain.AdminLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author tengteng
 * @description 针对表【admin_logs】的数据库操作Mapper
 * @createDate 2023-02-17 15:40:31
 * @Entity xyz.playedu.api.domain.AdminLog
 */
@Mapper
public interface AdminLogMapper extends BaseMapper<AdminLog> {

}




