package xyz.playedu.api.listener;

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
 * @create 2023/3/20 17:41
 */
@Component
public class UserCourseHourFinishedListener {

    @Autowired
    private UserCourseRecordService userCourseRecordService;

    @Autowired
    private UserCourseHourRecordService userCourseHourRecordService;

    @Autowired
    private CourseHourService hourService;

    @EventListener
    @Async
    public void userCourseProgressUpdate(UserCourseHourFinishedEvent evt) {
        Integer hourCount = hourService.getCountByCourseId(evt.getCourseId());
        Integer finishedCount = userCourseHourRecordService.getFinishedHourCount(evt.getUserId(), evt.getCourseId());
        userCourseRecordService.storeOrUpdate(evt.getUserId(), evt.getCourseId(), hourCount, finishedCount);
    }

}
