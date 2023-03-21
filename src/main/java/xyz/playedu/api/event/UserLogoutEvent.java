package xyz.playedu.api.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Date;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/3/21 14:31
 */
@Setter
@Getter
public class UserLogoutEvent extends ApplicationEvent {
    private Integer userId;
    private String jti;
    private Date createdAt;

    public UserLogoutEvent(Object source, Integer userId, String jti) {
        super(source);
        this.userId = userId;
        this.jti = jti;
        this.createdAt = new Date();
    }
}
