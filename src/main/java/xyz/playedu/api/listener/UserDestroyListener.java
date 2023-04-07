/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.listener;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import xyz.playedu.api.event.UserDestroyEvent;
import xyz.playedu.api.service.UserService;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/2/23 15:18
 */
@Component
@Slf4j
public class UserDestroyListener {

    @Autowired private UserService userService;

    @EventListener
    public void updateLoginInfo(UserDestroyEvent event) {
        userService.removeRelateDepartmentsByUserId(event.getUserId());
    }
}
