package xyz.playedu.api.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Date;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/24 14:31
 */
@Getter
@Setter
public class CourseDestroyEvent extends ApplicationEvent {

    private Integer courseId;
    private Date at;
    private Integer adminId;

    public CourseDestroyEvent(Object source, Integer adminId, Integer courseId, Date date) {
        super(source);
        this.courseId = courseId;
        this.at = date;
        this.adminId = adminId;
    }

}
