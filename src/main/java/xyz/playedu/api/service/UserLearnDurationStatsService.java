/*
 * Copyright 2023 杭州白书科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
