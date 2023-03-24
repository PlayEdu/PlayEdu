package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.playedu.api.domain.UserCourseRecord;
import xyz.playedu.api.service.UserCourseRecordService;
import xyz.playedu.api.mapper.UserCourseRecordMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author tengteng
 * @description 针对表【user_course_records】的数据库操作Service实现
 * @createDate 2023-03-20 16:41:04
 */
@Service
public class UserCourseRecordServiceImpl extends ServiceImpl<UserCourseRecordMapper, UserCourseRecord>
        implements UserCourseRecordService {

    @Override
    public UserCourseRecord find(Integer userId, Integer courseId) {
        return getOne(query().getWrapper().eq("user_id", userId).eq("course_id", courseId));
    }

    @Override
    public void storeOrUpdate(Integer userId, Integer courseId, Integer hourCount, Integer finishedCount) {
        if (hourCount == 0) {
            return;
        }

        UserCourseRecord record = find(userId, courseId);

        // 已看完
        if (record != null && record.getIsFinished() == 1) {
            return;
        }

        boolean isFinished = finishedCount >= hourCount;
        Date finishedAt = isFinished ? new Date() : null;
        Integer progress = finishedCount * 100 / hourCount * 100;

        if (record == null) {
            UserCourseRecord insertRecord = new UserCourseRecord();
            insertRecord.setUserId(userId);
            insertRecord.setCourseId(courseId);
            insertRecord.setHourCount(hourCount);
            insertRecord.setFinishedCount(finishedCount);
            insertRecord.setFinishedAt(finishedAt);
            insertRecord.setIsFinished(isFinished ? 1 : 0);
            insertRecord.setProgress(progress);
            insertRecord.setCreatedAt(new Date());
            insertRecord.setUpdatedAt(new Date());

            save(insertRecord);
        } else {
            UserCourseRecord updateRecord = new UserCourseRecord();
            updateRecord.setId(record.getId());
            updateRecord.setHourCount(hourCount);
            updateRecord.setFinishedCount(finishedCount);
            updateRecord.setFinishedAt(finishedAt);
            updateRecord.setIsFinished(isFinished ? 1 : 0);
            updateRecord.setProgress(progress);

            updateById(updateRecord);
        }
    }

    @Override
    public List<UserCourseRecord> chunk(Integer userId, List<Integer> courseIds) {
        if (courseIds == null || courseIds.size() == 0) {
            return new ArrayList<>();
        }
        return list(query().getWrapper().eq("user_id", userId).in("course_id", courseIds));
    }
}




