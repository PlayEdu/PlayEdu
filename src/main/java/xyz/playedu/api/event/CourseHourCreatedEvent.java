package xyz.playedu.api.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Date;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/26 18:20
 */
@Setter
@Getter
public class CourseHourCreatedEvent extends ApplicationEvent {
    private Integer adminId;
    private Integer hourId;
    private Integer courseId;
    private Integer chapterId;
    private Date date;

    public CourseHourCreatedEvent(Object source, Integer adminId, Integer courseId, Integer chapterId, Integer hourId, Date date) {
        super(source);
        this.adminId = adminId;
        this.courseId = courseId;
        this.chapterId = chapterId;
        this.hourId = hourId;
        this.date = date;
    }
}
