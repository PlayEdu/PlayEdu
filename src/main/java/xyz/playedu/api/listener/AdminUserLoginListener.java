package xyz.playedu.api.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import xyz.playedu.api.domain.AdminUser;
import xyz.playedu.api.event.AdminUserLoginEvent;
import xyz.playedu.api.service.AdminUserService;
import xyz.playedu.api.util.IpUtil;

@Component
@Slf4j
public class AdminUserLoginListener {

    @Autowired
    private AdminUserService adminUserService;

    @Order(1)
    @EventListener
    public void updateLoginInfo(AdminUserLoginEvent event) {
        AdminUser adminUser = new AdminUser();

        adminUser.setId(event.getAdminId());
        adminUser.setLoginAt(event.getLoginAt());
        adminUser.setLoginTimes(event.getLoginTimes() + 1);
        adminUser.setLoginIp(event.getIp());

        adminUserService.updateById(adminUser);
    }

    @Order(10)
    @EventListener
    public void recordLoginIp(AdminUserLoginEvent event) {
        String area = IpUtil.getRealAddressByIP(event.getIp());
        log.info("地区:" + area);
    }


}
