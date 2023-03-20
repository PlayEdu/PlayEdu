package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.playedu.api.domain.UserCourseRecord;
import xyz.playedu.api.service.UserCourseRecordService;
import xyz.playedu.api.mapper.UserCourseRecordMapper;
import org.springframework.stereotype.Service;

/**
* @author tengteng
* @description 针对表【user_course_records】的数据库操作Service实现
* @createDate 2023-03-20 16:41:04
*/
@Service
public class UserCourseRecordServiceImpl extends ServiceImpl<UserCourseRecordMapper, UserCourseRecord>
    implements UserCourseRecordService{

}




