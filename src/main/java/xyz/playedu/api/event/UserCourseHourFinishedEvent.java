package xyz.playedu.api.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Date;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/20 17:32
 */
@Getter
@Setter
public class UserCourseHourFinishedEvent extends ApplicationEvent {
    private Integer userId;
    private Integer courseId;
    private Integer hourId;
    private Date createdAt;

    public UserCourseHourFinishedEvent(Object source, Integer userId, Integer courseId, Integer hourId) {
        super(source);
        this.userId = userId;
        this.courseId = courseId;
        this.hourId = hourId;
        this.createdAt = new Date();
    }
}
