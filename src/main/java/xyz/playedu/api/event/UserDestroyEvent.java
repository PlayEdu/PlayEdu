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
 * @create 2023/2/23 13:51
 */
@Getter
@Setter
public class UserDestroyEvent extends ApplicationEvent {

    private Integer userId;
    private Date createdAt;

    public UserDestroyEvent(Object source, Integer userId) {
        super(source);
        this.userId = userId;
        this.createdAt = new Date();
    }
}
