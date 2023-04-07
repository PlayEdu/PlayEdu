/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.service;

import com.baomidou.mybatisplus.extension.service.IService;

import xyz.playedu.api.domain.UserLearnDurationStats;

import java.util.List;

/**
 * @author tengteng
 * @description 针对表【user_learn_duration_stats】的数据库操作Service
 * @createDate 2023-03-22 13:55:29
 */
public interface UserLearnDurationStatsService extends IService<UserLearnDurationStats> {
    void storeOrUpdate(Integer userId, Long startTime, Long endTime);

    Long todayTotal();

    Long yesterdayTotal();

    List<UserLearnDurationStats> top10();

    Long todayUserDuration(Integer userId);

    Long userDuration(Integer userId);
}
