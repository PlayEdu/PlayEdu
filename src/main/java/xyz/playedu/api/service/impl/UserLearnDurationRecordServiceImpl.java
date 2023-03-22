package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import xyz.playedu.api.domain.UserLearnDurationRecord;
import xyz.playedu.api.service.UserLearnDurationRecordService;
import xyz.playedu.api.mapper.UserLearnDurationRecordMapper;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author tengteng
 * @description 针对表【user_learn_duration_records】的数据库操作Service实现
 * @createDate 2023-03-20 16:41:12
 */
@Service
public class UserLearnDurationRecordServiceImpl extends ServiceImpl<UserLearnDurationRecordMapper, UserLearnDurationRecord>
        implements UserLearnDurationRecordService {

    @Override
    @SneakyThrows
    public void store(Integer userId, Integer courseId, Integer hourId, Long startTime, Long endTime) {
        // 处理日期
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date(endTime));

        UserLearnDurationRecord record = new UserLearnDurationRecord();
        record.setUserId(userId);
        record.setCourseId(courseId);
        record.setHourId(hourId);
        record.setStartAt(new Date(startTime));
        record.setEndAt(new Date(endTime));
        record.setDuration((int) (endTime - startTime));
        record.setCreatedDate(simpleDateFormat.parse(date));

        save(record);
    }
}




