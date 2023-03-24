package xyz.playedu.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.playedu.api.domain.UserUploadImageLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author tengteng
* @description 针对表【user_upload_image_logs】的数据库操作Mapper
* @createDate 2023-03-24 14:32:48
* @Entity xyz.playedu.api.domain.UserUploadImageLog
*/
@Mapper
public interface UserUploadImageLogMapper extends BaseMapper<UserUploadImageLog> {

}




