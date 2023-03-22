package xyz.playedu.api.service;

import xyz.playedu.api.domain.UserLearnDurationStats;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author tengteng
 * @description 针对表【user_learn_duration_stats】的数据库操作Service
 * @createDate 2023-03-22 13:55:29
 */
public interface UserLearnDurationStatsService extends IService<UserLearnDurationStats> {
    void storeOrUpdate(Integer userId, Long startTime, Long endTime);
}
