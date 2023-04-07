/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import xyz.playedu.api.event.CourseCategoryDestroyEvent;
import xyz.playedu.api.service.CourseService;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/2/24 14:07
 */
@Component
public class CourseCategoryDestroyListener {

    @Autowired private CourseService courseService;

    @EventListener
    public void resetRelateCourseCategoryId(CourseCategoryDestroyEvent event) {
        courseService.removeCategoryIdRelate(event.getCategoryId());
    }
}
