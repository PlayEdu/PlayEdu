package xyz.playedu.api.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Date;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/26 17:42
 */
@Getter
@Setter
public class CourseChapterDestroyEvent extends ApplicationEvent {
    private Integer adminId;
    private Integer courseId;
    private Integer chapterId;
    private Date createdAt;

    public CourseChapterDestroyEvent(Object source, Integer adminId, Integer courseId, Integer chapterId) {
        super(source);
        this.adminId = adminId;
        this.courseId = courseId;
        this.chapterId = chapterId;
        this.createdAt = new Date();
    }
}
