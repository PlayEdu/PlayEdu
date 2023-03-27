package xyz.playedu.api.service;

import xyz.playedu.api.domain.UserCourseHourRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author tengteng
 * @description 针对表【user_course_hour_records】的数据库操作Service
 * @createDate 2023-03-20 16:41:08
 */
public interface UserCourseHourRecordService extends IService<UserCourseHourRecord> {
    UserCourseHourRecord find(Integer userId, Integer courseId, Integer hourId);

    UserCourseHourRecord storeOrUpdate(Integer userId, Integer courseId, Integer hourId, Integer duration, Integer totalDuration);

    Integer getFinishedHourCount(Integer userId, Integer courseId);

    List<UserCourseHourRecord> getRecords(Integer userId, Integer courseId);

    List<UserCourseHourRecord> getLatestCourseIds(Integer userId, Integer size);
}
