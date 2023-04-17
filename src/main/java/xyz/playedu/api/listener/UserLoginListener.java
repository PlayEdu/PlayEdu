/*
 * Copyright 2023 杭州白书科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.playedu.api.listener;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import xyz.playedu.api.constant.SystemConstant;
import xyz.playedu.api.event.UserLoginEvent;
import xyz.playedu.api.exception.JwtLogoutException;
import xyz.playedu.api.service.JWTService;
import xyz.playedu.api.service.UserLoginRecordService;
import xyz.playedu.api.types.JWTPayload;
import xyz.playedu.api.util.IpUtil;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/3/10 13:45
 */
@Component
@Slf4j
public class UserLoginListener {

    @Autowired private UserLoginRecordService loginRecordService;

    @Autowired private JWTService jwtService;

    @Async
    @EventListener
    public void updateLoginInfo(UserLoginEvent event) throws JwtLogoutException {
        String ipArea = IpUtil.getRealAddressByIP(event.getIp());
        JWTPayload payload = jwtService.parse(event.getToken(), SystemConstant.JWT_PRV_USER);
        loginRecordService.store(
                event.getUserId(),
                payload.getJti(),
                payload.getExp(),
                event.getIp(),
                ipArea,
                event.getUserAgent().getBrowser().toString(),
                event.getUserAgent().getVersion(),
                event.getUserAgent().getOs().toString());
    }
}
