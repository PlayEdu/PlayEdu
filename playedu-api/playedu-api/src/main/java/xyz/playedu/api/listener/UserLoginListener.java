/*
 * Copyright (C) 2023 杭州白书科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.playedu.api.listener;

import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import xyz.playedu.api.event.UserLoginEvent;
import xyz.playedu.common.service.FrontendAuthService;
import xyz.playedu.common.service.UserLoginRecordService;
import xyz.playedu.common.util.IpUtil;

@Component
@Slf4j
public class UserLoginListener {

    @Autowired private UserLoginRecordService loginRecordService;

    @Autowired private FrontendAuthService authService;

    @Async
    @EventListener
    public void updateLoginInfo(UserLoginEvent event) {
        String ipArea = IpUtil.getRealAddressByIP(event.getIp());

        HashMap<String, String> tokenData = authService.parse(event.getToken());
        String jti = tokenData.get("jti");
        Long exp = Long.parseLong(tokenData.get("exp"));

        loginRecordService.store(
                event.getUserId(),
                jti,
                exp,
                event.getIp(),
                ipArea,
                event.getUserAgent().getBrowser().toString(),
                event.getUserAgent().getVersion(),
                event.getUserAgent().getOs().toString());
    }
}
