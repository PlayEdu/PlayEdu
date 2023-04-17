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
package xyz.playedu.api.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import xyz.playedu.api.domain.AppConfig;
import xyz.playedu.api.request.backend.AppConfigRequest;
import xyz.playedu.api.service.AppConfigService;
import xyz.playedu.api.types.JsonResponse;

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

    @GetMapping("")
    public JsonResponse index() {
        List<AppConfig> configs = configService.allShow();
        return JsonResponse.data(configs);
    }

    @PutMapping("")
    public JsonResponse save(@RequestBody AppConfigRequest req) {
        configService.saveFromMap(req.getData());
        return JsonResponse.data(null);
    }
}
