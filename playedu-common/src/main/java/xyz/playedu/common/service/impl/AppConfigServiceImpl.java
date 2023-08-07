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
package xyz.playedu.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

import xyz.playedu.common.constant.ConfigConstant;
import xyz.playedu.common.domain.AppConfig;
import xyz.playedu.common.mapper.AppConfigMapper;
import xyz.playedu.common.service.AppConfigService;
import xyz.playedu.common.types.config.MinioConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tengteng
 * @description 针对表【app_config】的数据库操作Service实现
 * @createDate 2023-03-09 11:13:33
 */
@Service
public class AppConfigServiceImpl extends ServiceImpl<AppConfigMapper, AppConfig>
        implements AppConfigService {

    @Override
    public Map<String, Long> allKeys() {
        return list().stream().collect(Collectors.toMap(AppConfig::getKeyName, AppConfig::getId));
    }

    @Override
    public List<AppConfig> allShow() {
        return list(query().getWrapper().eq("is_hidden", 0));
    }

    @Override
    public void saveFromMap(HashMap<String, String> data) {
        Map<String, AppConfig> configs =
                list(query().getWrapper().in("key_name", data.keySet())).stream()
                        .collect(Collectors.toMap(AppConfig::getKeyName, e -> e));
        List<AppConfig> list = new ArrayList<>();

        data.forEach(
                (keyNameValue, keyValueValue) -> {
                    if (keyValueValue == null) {
                        return;
                    }
                    if ("******".equals(keyNameValue)) { // 私密信息默认place
                        return;
                    }
                    AppConfig configItem = configs.get(keyNameValue);
                    if (configItem == null) { // 不存在的配置
                        return;
                    }
                    if (keyValueValue.equals(configItem.getKeyValue())) { // 没有变化
                        return;
                    }
                    list.add(
                            new AppConfig() {
                                {
                                    setId(configItem.getId());
                                    setKeyValue(keyValueValue);
                                }
                            });
                });

        if (list.size() > 0) {
            updateBatchById(list);
        }
    }

    @Override
    public Map<String, String> keyValues() {
        return list(query().getWrapper().eq("is_hidden", 0)).stream()
                .collect(Collectors.toMap(AppConfig::getKeyName, AppConfig::getKeyValue));
    }

    @Override
    public MinioConfig getMinioConfig() {
        MinioConfig minioConfig = new MinioConfig();
        Map<String, String> config = keyValues();
        minioConfig.setAccessKey(config.get(ConfigConstant.MINIO_ACCESS_KEY));
        minioConfig.setSecretKey(config.get(ConfigConstant.MINIO_SECRET_KEY));
        minioConfig.setBucket(config.get(ConfigConstant.MINIO_BUCKET));
        minioConfig.setEndpoint(config.get(ConfigConstant.MINIO_ENDPOINT));
        minioConfig.setDomain(config.get(ConfigConstant.MINIO_DOMAIN));
        return minioConfig;
    }
}
