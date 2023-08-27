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
package xyz.playedu.api.controller.frontend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import xyz.playedu.common.constant.ConfigConstant;
import xyz.playedu.common.service.AppConfigService;
import xyz.playedu.common.types.JsonResponse;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/system")
public class SystemController {

    @Autowired private AppConfigService appConfigService;

    @GetMapping("/config")
    public JsonResponse config() {
        Map<String, String> configs = appConfigService.keyValues();

        HashMap<String, String> data = new HashMap<>();

        data.put("system-name", configs.get(ConfigConstant.SYSTEM_NAME));
        data.put("system-logo", configs.get(ConfigConstant.SYSTEM_LOGO));
        data.put("system-api-url", configs.get(ConfigConstant.SYSTEM_API_URL));
        data.put("system-pc-url", configs.get(ConfigConstant.SYSTEM_PC_URL));
        data.put("system-h5-url", configs.get(ConfigConstant.SYSTEM_H5_URL));
        data.put("system-pc-index-footer-msg", configs.get("system.pc_index_footer_msg"));

        data.put("player-poster", configs.get("player.poster"));
        data.put("player-is-enabled-bullet-secret", configs.get("player.is_enabled_bullet_secret"));
        data.put("player-bullet-secret-text", configs.get("player.bullet_secret_text"));
        data.put("player-bullet-secret-color", configs.get("player.bullet_secret_color"));
        data.put("player-bullet-secret-opacity", configs.get("player.bullet_secret_opacity"));
        data.put("player-disabled-drag", configs.get("player.disabled_drag"));

        return JsonResponse.data(data);
    }
}
