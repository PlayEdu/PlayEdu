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
package xyz.playedu.api.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import xyz.playedu.api.constant.BPermissionConstant;
import xyz.playedu.api.constant.SystemConstant;
import xyz.playedu.api.domain.AppConfig;
import xyz.playedu.api.middleware.BackendPermissionMiddleware;
import xyz.playedu.api.request.backend.AppConfigRequest;
import xyz.playedu.api.service.AppConfigService;
import xyz.playedu.api.types.JsonResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author 杭州白书科技有限公司
 *
 * @create 2023/3/9 11:14
 */
@RestController
@RequestMapping("/backend/v1/app-config")
public class AppConfigController {

    @Autowired private AppConfigService configService;

    @BackendPermissionMiddleware(slug = BPermissionConstant.SYSTEM_CONFIG)
    @GetMapping("")
    public JsonResponse index() {
        List<AppConfig> configs = configService.allShow();
        List<AppConfig> data = new ArrayList<>();
        for (AppConfig item : configs) {
            if (item.getIsPrivate() == 1) {
                item.setKeyValue(SystemConstant.CONFIG_MASK);
            }
            data.add(item);
        }
        return JsonResponse.data(data);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.SYSTEM_CONFIG)
    @PutMapping("")
    public JsonResponse save(@RequestBody AppConfigRequest req) {
        HashMap<String, String> data = new HashMap<>();
        req.getData()
                .forEach(
                        (key, value) -> {
                            if (SystemConstant.CONFIG_MASK.equals(value)) {
                                return;
                            }
                            data.put(key, value);
                        });
        configService.saveFromMap(data);
        return JsonResponse.data(null);
    }
}
