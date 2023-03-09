package xyz.playedu.api.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import xyz.playedu.api.event.CourseDestroyEvent;
import xyz.playedu.api.service.CourseDepartmentService;
import xyz.playedu.api.service.internal.ResourceCourseCategoryService;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/24 17:19
 */
@Component

public class CourseDestroyListener {

    @Autowired
    private CourseDepartmentService courseDepartmentService;

    @Autowired
    private ResourceCourseCategoryService courseCategoryService;

    @EventListener
    public void departmentRelateRemove(CourseDestroyEvent event) {
        courseDepartmentService.removeByCourseId(event.getCourseId());
    }

    @EventListener
    public void categoryRelateRemove(CourseDestroyEvent event) {
        courseCategoryService.removeByCourseId(event.getCourseId());
    }

}
