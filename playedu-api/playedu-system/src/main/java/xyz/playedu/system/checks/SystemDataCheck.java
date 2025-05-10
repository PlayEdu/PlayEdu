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
package xyz.playedu.system.checks;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import xyz.playedu.common.constant.BackendConstant;
import xyz.playedu.common.domain.AdminRole;
import xyz.playedu.common.service.AdminRoleService;
import xyz.playedu.common.service.AdminUserService;

@Component
@Slf4j
@Order(12)
public class SystemDataCheck implements CommandLineRunner {

    @Autowired private AdminRoleService adminRoleService;

    @Autowired private AdminUserService adminUserService;

    @Override
    public void run(String... args) throws Exception {
        adminInit();
    }

    private void adminInit() {
        try {
            AdminRole superRole = adminRoleService.getBySlug(BackendConstant.SUPER_ADMIN_ROLE);
            if (superRole != null) {
                return;
            }
            Integer roleId = adminRoleService.initSuperAdminRole();
            adminUserService.createWithRoleIds(
                    "超级管理员", "admin@playedu.xyz", "playedu", 0, new Integer[] {roleId});
        } catch (Exception e) {
            log.error("超级管理员初始化失败,错误信息:{}", e.getMessage());
        }
    }
}
