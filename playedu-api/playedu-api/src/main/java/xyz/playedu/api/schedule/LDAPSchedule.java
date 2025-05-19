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
package xyz.playedu.api.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import xyz.playedu.common.bus.LDAPBus;

@Component
@Slf4j
public class LDAPSchedule {

    @Autowired private LDAPBus ldapBus;

    private int times;

    @Scheduled(fixedRate = 3600000)
    public void sync() {
        // 系统刚启动不执行
        if (times == 0) {
            times++;
            return;
        }

        if (!ldapBus.enabledLDAP()) {
            log.info("未配置LDAP服务");
            return;
        }

        try {
            // 使用新的同步记录功能
            ldapBus.syncAndRecord(0); // 0表示系统自动执行
            log.info("LDAP同步成功");
        } catch (Exception e) {
            log.error("LDAP同步失败", e);
        }
    }
}
