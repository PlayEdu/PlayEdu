package xyz.playedu.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.playedu.api.domain.UserLearnDurationRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author tengteng
* @description 针对表【user_learn_duration_records】的数据库操作Mapper
* @createDate 2023-03-20 16:41:12
* @Entity xyz.playedu.api.domain.UserLearnDurationRecord
*/
@Mapper
public interface UserLearnDurationRecordMapper extends BaseMapper<UserLearnDurationRecord> {

}




