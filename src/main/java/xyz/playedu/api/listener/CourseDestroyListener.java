package xyz.playedu.api.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import xyz.playedu.api.event.CourseDestroyEvent;
import xyz.playedu.api.service.CategoryCourseService;
import xyz.playedu.api.service.CourseDepartmentService;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/24 17:19
 */
@Component

public class CourseDestroyListener {

    @Autowired
    private CourseDepartmentService courseDepartmentService;

    @Autowired
    private CategoryCourseService categoryCourseService;

    @Order(1)
    @EventListener
    public void departmentRelateRemove(CourseDestroyEvent event) {
        courseDepartmentService.removeByCourseId(event.getCourseId());
    }

    @Order(1)
    @EventListener
    public void categoryRelateRemove(CourseDestroyEvent event) {
        categoryCourseService.removeByCourseId(event.getCourseId());
    }

}
