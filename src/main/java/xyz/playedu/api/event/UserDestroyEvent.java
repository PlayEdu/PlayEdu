package xyz.playedu.api.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Date;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/23 13:51
 */
@Getter
@Setter
public class UserDestroyEvent extends ApplicationEvent {

    private Integer userId;
    private Date at;

    public UserDestroyEvent(Object source, Integer userId, Date date) {
        super(source);
        this.userId = userId;
        this.at = date;
    }
}
