/**
 * This file is part of the PlayEdu.
 * (c) 杭州白书科技有限公司
 */
package xyz.playedu.api.listener;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import xyz.playedu.api.event.UserLogoutEvent;
import xyz.playedu.api.service.UserLoginRecordService;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/3/21 14:51
 */
@Component
@Slf4j
public class UserLogoutListener {

    @Autowired private UserLoginRecordService userLoginRecordService;

    @Async
    @EventListener
    public void updateLoginRecord(UserLogoutEvent event) {
        userLoginRecordService.logout(event.getUserId(), event.getJti());
    }
}
