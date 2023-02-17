package xyz.playedu.api.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Date;

@Getter
@Setter
public class AdminUserLoginEvent extends ApplicationEvent {

    private Integer adminId;

    private Date loginAt;

    private String token;

    private String ip;

    private Integer loginTimes;

    public AdminUserLoginEvent(Object source, Integer adminId, Date loginAt, String token, String ip, Integer loginTimes) {
        super(source);
        this.adminId = adminId;
        this.loginAt = loginAt;
        this.token = token;
        this.ip = ip;
        this.loginTimes = loginTimes;
    }
}
