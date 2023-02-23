package xyz.playedu.api.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import xyz.playedu.api.event.DepartmentDestroyEvent;
import xyz.playedu.api.service.UserDepartmentService;

/**
 * @Author 杭州白书科技有限公司
 * @create 2023/2/23 15:28
 */
@Component
@Slf4j
public class DepartmentDestroyListener {
    @Autowired
    private UserDepartmentService userDepartmentService;

    @Order(1)
    @EventListener
    public void updateLoginInfo(DepartmentDestroyEvent event) {
        userDepartmentService.removeByDepId(event.getDepId());
    }
}
