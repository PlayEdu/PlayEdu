/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.service;

import com.baomidou.mybatisplus.extension.service.IService;

import xyz.playedu.api.domain.UserLearnDurationRecord;

/**
 * @author tengteng
 * @description 针对表【user_learn_duration_records】的数据库操作Service
 * @createDate 2023-03-20 16:41:12
 */
public interface UserLearnDurationRecordService extends IService<UserLearnDurationRecord> {
    void store(Integer userId, Integer courseId, Integer hourId, Long startTime, Long endTime);
}
