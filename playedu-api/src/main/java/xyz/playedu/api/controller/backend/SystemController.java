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

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import xyz.playedu.common.annotation.Log;
import xyz.playedu.common.constant.BusinessTypeConstant;
import xyz.playedu.common.constant.ConfigConstant;
import xyz.playedu.common.context.BCtx;
import xyz.playedu.common.types.JsonResponse;
import xyz.playedu.common.util.RequestUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/backend/v1/system")
@Slf4j
public class SystemController {

    @GetMapping("/config")
    @Log(title = "其它-系统配置", businessType = BusinessTypeConstant.GET)
    public JsonResponse config() {
        Map<String, String> configData = BCtx.getConfig();

        String apiUrl = configData.get(ConfigConstant.SYSTEM_API_URL);
        if (apiUrl == null || apiUrl.trim().isEmpty()) {
            apiUrl = RequestUtil.uriWithProtocol();
        } else {
            if (apiUrl.endsWith("/")) {
                apiUrl = apiUrl.substring(0, apiUrl.length() - 1);
            }
        }

        HashMap<String, Object> data = new HashMap<>();

        data.put(ConfigConstant.SYSTEM_NAME, configData.get(ConfigConstant.SYSTEM_NAME));
        data.put(ConfigConstant.SYSTEM_LOGO, configData.get(ConfigConstant.SYSTEM_LOGO));
        data.put(ConfigConstant.SYSTEM_API_URL, apiUrl);
        data.put(ConfigConstant.SYSTEM_PC_URL, configData.get(ConfigConstant.SYSTEM_PC_URL));
        data.put(ConfigConstant.SYSTEM_H5_URL, configData.get(ConfigConstant.SYSTEM_H5_URL));

        // 学员的默认头像
        String memberDefaultAvatar = configData.get(ConfigConstant.MEMBER_DEFAULT_AVATAR);
        if (memberDefaultAvatar == null || memberDefaultAvatar.trim().isEmpty()) {
            data.put(ConfigConstant.MEMBER_DEFAULT_AVATAR, apiUrl + "/images/default_avatar.png");
        } else {
            data.put(ConfigConstant.MEMBER_DEFAULT_AVATAR, memberDefaultAvatar);
        }

        // 内置的三个线上课封面
        List<String> defaultCourseThumbs = new ArrayList<>();
        defaultCourseThumbs.add(apiUrl + "/images/courses/thumb1.png");
        defaultCourseThumbs.add(apiUrl + "/images/courses/thumb2.png");
        defaultCourseThumbs.add(apiUrl + "/images/courses/thumb3.png");
        data.put("default.course_thumbs", defaultCourseThumbs);

        // LDAP登录
        data.put("ldap-enabled", "1".equals(configData.get(ConfigConstant.LDAP_ENABLED)));

        return JsonResponse.data(data);
    }
}
