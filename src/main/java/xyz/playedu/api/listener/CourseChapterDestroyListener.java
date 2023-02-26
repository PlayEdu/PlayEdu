package xyz.playedu.api.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import xyz.playedu.api.event.CourseChapterDestroyEvent;
import xyz.playedu.api.service.CourseHourService;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/26 18:25
 */
@Component
public class CourseChapterDestroyListener {

    @Autowired
    private CourseHourService hourService;

    @EventListener
    public void resetCourseHourChapterId(CourseChapterDestroyEvent event) {
        hourService.resetChapterIdByCourseIdAndChapterId(event.getCourseId(), event.getChapterId());
    }

}
