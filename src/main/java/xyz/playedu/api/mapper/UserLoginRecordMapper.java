package xyz.playedu.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.playedu.api.domain.UserLoginRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author tengteng
* @description 针对表【user_login_records】的数据库操作Mapper
* @createDate 2023-03-10 14:06:55
* @Entity xyz.playedu.api.domain.UserLoginRecord
*/
@Mapper
public interface UserLoginRecordMapper extends BaseMapper<UserLoginRecord> {

}




