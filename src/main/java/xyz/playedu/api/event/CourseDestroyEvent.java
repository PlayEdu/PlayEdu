/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.event;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.ApplicationEvent;

import java.util.Date;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/2/24 14:31
 */
@Getter
@Setter
public class CourseDestroyEvent extends ApplicationEvent {

    private Integer courseId;
    private Date createdAt;
    private Integer adminId;

    public CourseDestroyEvent(Object source, Integer adminId, Integer courseId) {
        super(source);
        this.courseId = courseId;
        this.createdAt = new Date();
        this.adminId = adminId;
    }
}
