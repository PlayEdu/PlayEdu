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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import xyz.playedu.common.domain.AppConfig;
import xyz.playedu.common.service.AdminPermissionService;
import xyz.playedu.common.service.AppConfigService;

import java.util.ArrayList;

@Order(10000)
@Component
public class UpgradeCheck implements CommandLineRunner {

    @Autowired private AppConfigService appConfigService;

    @Autowired private AdminPermissionService permissionService;

    @Override
    public void run(String... args) throws Exception {
        upgrade_v1_beta7();
    }

    private void upgrade_v1_beta7() {
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
}
