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

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import xyz.playedu.api.constant.BackendLogConstant;
import xyz.playedu.api.domain.AdminLog;
import xyz.playedu.api.domain.AdminUser;
import xyz.playedu.api.event.AdminUserLoginEvent;
import xyz.playedu.api.service.AdminLogService;
import xyz.playedu.api.service.AdminUserService;
import xyz.playedu.api.util.IpUtil;

import java.util.Date;

@Component
@Slf4j
public class AdminUserLoginListener {

    @Autowired private AdminUserService adminUserService;

    @Autowired private AdminLogService adminLogService;

    @EventListener
    public void updateLoginInfo(AdminUserLoginEvent event) {
        AdminUser adminUser = new AdminUser();

        adminUser.setId(event.getAdminId());
        adminUser.setLoginAt(event.getLoginAt());
        adminUser.setLoginTimes(event.getLoginTimes() + 1);
        adminUser.setLoginIp(event.getIp());

        adminUserService.updateById(adminUser);
    }

    @Async
    @EventListener
    public void log(AdminUserLoginEvent event) {
        String area = IpUtil.getRealAddressByIP(event.getIp());

        AdminLog adminLog = new AdminLog();
        adminLog.setAdminId(event.getAdminId());
        adminLog.setModule(BackendLogConstant.MODULE_LOGIN);
        adminLog.setOpt(BackendLogConstant.OPT_LOGIN);
        adminLog.setIp(event.getIp());
        adminLog.setIpArea(area);
        adminLog.setCreatedAt(new Date());

        adminLogService.save(adminLog);
    }
}
