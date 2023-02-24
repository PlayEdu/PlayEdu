package xyz.playedu.api.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Date;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/24 14:01
 */
@Setter
@Getter
public class CourseCategoryDestroyEvent extends ApplicationEvent {

    private Integer categoryId;
    private Date at;

    public CourseCategoryDestroyEvent(Object source, Integer categoryId, Date date) {
        super(source);
        this.categoryId = categoryId;
        this.at = date;
    }

}
