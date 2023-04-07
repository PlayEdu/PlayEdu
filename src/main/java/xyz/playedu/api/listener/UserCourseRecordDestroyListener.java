/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import xyz.playedu.api.event.UserCourseRecordDestroyEvent;
import xyz.playedu.api.service.UserCourseHourRecordService;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/4/4 10:16
 */
@Component
public class UserCourseRecordDestroyListener {

    @Autowired private UserCourseHourRecordService userCourseHourRecordService;

    @EventListener
    public void emptyUserCourseHourRecords(UserCourseRecordDestroyEvent event) {
        userCourseHourRecordService.remove(event.getUserId(), event.getCourseId());
    }
}
