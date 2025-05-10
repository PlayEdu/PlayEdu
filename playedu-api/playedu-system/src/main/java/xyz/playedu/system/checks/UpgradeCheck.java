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

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import xyz.playedu.common.domain.AppConfig;
import xyz.playedu.common.service.AdminPermissionService;
import xyz.playedu.common.service.AppConfigService;

@Order(10000)
@Component
public class UpgradeCheck implements CommandLineRunner {

    @Autowired private AppConfigService appConfigService;

    @Autowired private AdminPermissionService permissionService;

    @Override
    public void run(String... args) throws Exception {
        upgrade_1_beta7();
        upgrade_1_4();
        upgrade_1_6();
    }

    private void upgrade_1_4() {
        appConfigService.remove(
                appConfigService
                        .query()
                        .getWrapper()
                        .in(
                                "key_name",
                                new ArrayList<>() {
                                    {
                                        add("ldap.user_dn_prefix");
                                    }
                                }));
    }

    private void upgrade_1_beta7() {
        appConfigService.update(
                new AppConfig() {
                    {
                        setIsPrivate(1);
                    }
                },
                appConfigService.query().getWrapper().eq("key_name", "minio.secret_key"));

        permissionService.remove(
                permissionService
                        .query()
                        .getWrapper()
                        .in(
                                "slug",
                                new ArrayList<>() {
                                    {
                                        add("resource-destroy");
                                    }
                                }));
    }

    private void upgrade_1_6() {
        permissionService.remove(
                permissionService
                        .query()
                        .getWrapper()
                        .in(
                                "slug",
                                new ArrayList<>() {
                                    {
                                        add("data-user-id-card");
                                    }
                                }));
    }
}
