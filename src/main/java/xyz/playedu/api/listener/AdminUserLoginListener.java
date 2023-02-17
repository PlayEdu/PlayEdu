package xyz.playedu.api.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import xyz.playedu.api.event.AdminUserLoginEvent;
import xyz.playedu.api.service.AdminUserService;

@Component
@Slf4j
public class AdminUserLoginListener {

    @Autowired
    private AdminUserService adminUserService;

    @Order(1)
    @EventListener
    public void updateLoginAtAndTimes(AdminUserLoginEvent event) {
        adminUserService.updateLoginTimesAndLoginAt(event.getAdminId(), event.getLoginAt(), event.getLoginTimes() + 1);
    }

    @Order(10)
    @EventListener
    public void recordLoginIp(AdminUserLoginEvent event) {
        log.info("我执行了:recordLoginIp");
    }

}
