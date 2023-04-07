/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.event;

import cn.hutool.http.useragent.UserAgent;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.ApplicationEvent;

import java.util.Date;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/3/10 13:22
 */
@Setter
@Getter
public class UserLoginEvent extends ApplicationEvent {

    private Integer userId;

    private String email;

    private Date loginAt;

    private String token;

    private String ip;

    private UserAgent userAgent;

    public UserLoginEvent(
            Object source,
            Integer userId,
            String email,
            String token,
            String ip,
            UserAgent userAgent) {
        super(source);
        this.userId = userId;
        this.email = email;
        this.token = token;
        this.ip = ip;
        this.userAgent = userAgent;
        this.loginAt = new Date();
    }
}
