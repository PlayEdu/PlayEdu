package xyz.playedu.api.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Date;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/4/4 10:12
 */
@Getter
@Setter
public class UserCourseRecordDestroyEvent extends ApplicationEvent {

    private Integer userId;
    private Integer courseId;
    private Date createdAt;

    public UserCourseRecordDestroyEvent(Object source, Integer userId, Integer courseId) {
        super(source);
        this.userId = userId;
        this.courseId = courseId;
        this.createdAt = new Date();
    }
}
