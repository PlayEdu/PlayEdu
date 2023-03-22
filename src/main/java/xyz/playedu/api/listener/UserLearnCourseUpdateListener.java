package xyz.playedu.api.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import xyz.playedu.api.event.UserLearnCourseUpdateEvent;
import xyz.playedu.api.service.UserLearnDurationRecordService;
import xyz.playedu.api.service.UserLearnDurationStatsService;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/22 14:18
 */
@Component
@Slf4j
public class UserLearnCourseUpdateListener {

    @Autowired
    private UserLearnDurationRecordService userLearnDurationRecordService;

    @Autowired
    private UserLearnDurationStatsService userLearnDurationStatsService;

    @Async
    @EventListener
    public void storeLearnDuration(UserLearnCourseUpdateEvent event) {
        // 观看时长统计
        userLearnDurationStatsService.storeOrUpdate(event.getUserId(), event.getStartAt(), event.getEndAt());
        // 观看记录
        userLearnDurationRecordService.store(event.getUserId(), event.getCourseId(), event.getHourId(), event.getStartAt(), event.getEndAt());
    }

}
