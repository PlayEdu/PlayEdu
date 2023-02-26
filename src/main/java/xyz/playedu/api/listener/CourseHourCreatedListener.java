package xyz.playedu.api.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import xyz.playedu.api.event.CourseHourCreatedEvent;
import xyz.playedu.api.service.CourseHourService;
import xyz.playedu.api.service.CourseService;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/26 18:22
 */
@Component
public class CourseHourCreatedListener {

    @Autowired
    private CourseHourService hourService;

    @Autowired
    private CourseService courseService;

    @EventListener
    public void courseClassHourUpdate(CourseHourCreatedEvent event) {
        Integer classHour = hourService.getCourseClassHourByCourseId(event.getCourseId());
        courseService.updateClassHour(event.getCourseId(), classHour);
    }

}
