package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import xyz.playedu.api.domain.UserLearnDurationStats;
import xyz.playedu.api.service.UserLearnDurationStatsService;
import xyz.playedu.api.mapper.UserLearnDurationStatsMapper;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author tengteng
 * @description 针对表【user_learn_duration_stats】的数据库操作Service实现
 * @createDate 2023-03-22 13:55:29
 */
@Service
public class UserLearnDurationStatsServiceImpl extends ServiceImpl<UserLearnDurationStatsMapper, UserLearnDurationStats>
        implements UserLearnDurationStatsService {

    @Override
    @SneakyThrows
    public void storeOrUpdate(Integer userId, Long startTime, Long endTime) {
        // 处理日期
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date(endTime));
        // duration
        Long duration = endTime - startTime;

        UserLearnDurationStats stats = getOne(query().getWrapper().eq("user_id", userId).eq("created_date", date));
        if (stats == null) {
            UserLearnDurationStats newStats = new UserLearnDurationStats();
            newStats.setUserId(userId);
            newStats.setDuration(Math.toIntExact(duration));
            newStats.setCreatedDate(simpleDateFormat.parse(date));
            save(newStats);
            return;
        }

        UserLearnDurationStats newStats = new UserLearnDurationStats();
        newStats.setId(stats.getId());
        newStats.setDuration((int) (stats.getDuration() + duration));
        updateById(newStats);
    }
}




