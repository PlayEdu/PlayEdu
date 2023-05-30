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
package xyz.playedu.api.checks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import xyz.playedu.api.constant.BackendConstant;
import xyz.playedu.api.constant.CConfig;
import xyz.playedu.api.domain.AppConfig;
import xyz.playedu.api.service.AppConfigService;

import java.util.*;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/3/9 13:29
 */
@Component
public class AppConfigCheck implements ApplicationRunner {

    private static final HashMap<String, AppConfig[]> configs =
            new HashMap<>() {
                {
                    // 系统配置
                    put(
                            "系统",
                            new AppConfig[] {
                                new AppConfig() {
                                    {
                                        setName("网站名");
                                        setSort(10);
                                        setFieldType(BackendConstant.APP_CONFIG_FIELD_TYPE_INPUT);
                                        setKeyName(CConfig.SYSTEM_NAME);
                                        setKeyValue("");
                                        setHelp("请输入网站名");
                                    }
                                },
                                new AppConfig() {
                                    {
                                        setName("Logo");
                                        setSort(20);
                                        setFieldType(BackendConstant.APP_CONFIG_FIELD_TYPE_IMAGE);
                                        setKeyName(CConfig.SYSTEM_LOGO);
                                        setKeyValue("");
                                    }
                                },
                                new AppConfig() {
                                    {
                                        setName("API访问地址");
                                        setSort(30);
                                        setFieldType(BackendConstant.APP_CONFIG_FIELD_TYPE_INPUT);
                                        setKeyName(CConfig.SYSTEM_API_URL);
                                        setKeyValue("");
                                        setHelp("请输入API访问地址");
                                    }
                                },
                                new AppConfig() {
                                    {
                                        setName("PC端口访问地址");
                                        setSort(40);
                                        setFieldType(BackendConstant.APP_CONFIG_FIELD_TYPE_INPUT);
                                        setKeyName(CConfig.SYSTEM_PC_URL);
                                        setKeyValue("");
                                        setHelp("请输入PC端访问地址");
                                    }
                                },
                                new AppConfig() {
                                    {
                                        setName("H5端口访问地址");
                                        setSort(50);
                                        setFieldType(BackendConstant.APP_CONFIG_FIELD_TYPE_INPUT);
                                        setKeyName(CConfig.SYSTEM_H5_URL);
                                        setKeyValue("");
                                        setHelp("请输入H5端访问地址");
                                    }
                                },
                                new AppConfig() {
                                    {
                                        setName("网站页脚");
                                        setSort(60);
                                        setFieldType(BackendConstant.APP_CONFIG_FIELD_TYPE_INPUT);
                                        setKeyName("system.pc_index_footer_msg");
                                        setKeyValue("");
                                        setHelp("自定义一句话显示在前台页脚");
                                    }
                                },
                            });
                    // 播放配置
                    put(
                            "播放配置",
                            new AppConfig[] {
                                new AppConfig() {
                                    {
                                        setName("播放器封面");
                                        setSort(10);
                                        setFieldType(BackendConstant.APP_CONFIG_FIELD_TYPE_IMAGE);
                                        setKeyName("player.poster");
                                        setKeyValue("");
                                        setHelp("播放器封面在学员观看视频时默认显示");
                                    }
                                },
                                new AppConfig() {
                                    {
                                        setName("启用跑马灯");
                                        setSort(20);
                                        setFieldType(BackendConstant.APP_CONFIG_FIELD_TYPE_SWITCH);
                                        setKeyName("player.is_enabled_bullet_secret");
                                        setKeyValue("0");
                                        setHelp("开启之后视频播放器将会随机显示学员信息");
                                    }
                                },
                                new AppConfig() {
                                    {
                                        setName("跑马灯内容");
                                        setSort(30);
                                        setFieldType(BackendConstant.APP_CONFIG_FIELD_TYPE_TEXT);
                                        setKeyName("player.bullet_secret_text");
                                        setKeyValue("");
                                        setHelp("请配置跑马灯显示的内容模板");
                                    }
                                },
                                new AppConfig() {
                                    {
                                        setName("跑马灯颜色");
                                        setSort(40);
                                        setFieldType(BackendConstant.APP_CONFIG_FIELD_TYPE_TEXT);
                                        setKeyName("player.bullet_secret_color");
                                        setKeyValue("");
                                    }
                                },
                                new AppConfig() {
                                    {
                                        setName("跑马灯透明度");
                                        setSort(50);
                                        setFieldType(BackendConstant.APP_CONFIG_FIELD_TYPE_TEXT);
                                        setKeyName("player.bullet_secret_opacity");
                                        setKeyValue("1");
                                    }
                                },
                                new AppConfig() {
                                    {
                                        setName("禁止拖拽播放");
                                        setSort(60);
                                        setFieldType(BackendConstant.APP_CONFIG_FIELD_TYPE_SWITCH);
                                        setKeyName("player.disabled_drag");
                                        setKeyValue("0");
                                    }
                                },
                            });
                    put(
                            "学员配置",
                            new AppConfig[] {
                                new AppConfig() {
                                    {
                                        setName("默认头像");
                                        setSort(10);
                                        setFieldType(BackendConstant.APP_CONFIG_FIELD_TYPE_IMAGE);
                                        setKeyName(CConfig.MEMBER_DEFAULT_AVATAR);
                                        setKeyValue("");
                                    }
                                },
                            });
                }
            };

    @Autowired private AppConfigService configService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, Long> keys = configService.allKeys();
        List<AppConfig> list = new ArrayList<>();
        Date now = new Date();

        configs.forEach(
                (groupNameValue, items) -> {
                    for (int i = 0; i < items.length; i++) {
                        AppConfig configItem = items[i];

                        if (keys.get(configItem.getKeyName()) != null) {
                            continue;
                        }

                        configItem.setGroupName(groupNameValue);
                        configItem.setCreatedAt(now);
                        list.add(configItem);
                    }
                });

        if (list.size() > 0) {
            configService.saveBatch(list);
        }
    }
}
