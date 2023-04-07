/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.listener;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import xyz.playedu.api.event.UserCourseHourFinishedEvent;
import xyz.playedu.api.service.CourseHourService;
import xyz.playedu.api.service.UserCourseHourRecordService;
import xyz.playedu.api.service.UserCourseRecordService;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/3/20 17:41
 */
@Component
@Slf4j
public class UserCourseHourFinishedListener {

    @Autowired private UserCourseRecordService userCourseRecordService;

    @Autowired private UserCourseHourRecordService userCourseHourRecordService;

    @Autowired private CourseHourService hourService;

    @Async
    @EventListener
    public void userCourseProgressUpdate(UserCourseHourFinishedEvent evt) {
        Integer hourCount = hourService.getCountByCourseId(evt.getCourseId());
        Integer finishedCount =
                userCourseHourRecordService.getFinishedHourCount(
                        evt.getUserId(), evt.getCourseId());
        log.info(
                "UserCourseHourFinishedListener courseId={} userId={} hourCount={}"
                        + " finishedCount={}",
                evt.getCourseId(),
                evt.getUserId(),
                hourCount,
                finishedCount);
        userCourseRecordService.storeOrUpdate(
                evt.getUserId(), evt.getCourseId(), hourCount, finishedCount);
    }
}
