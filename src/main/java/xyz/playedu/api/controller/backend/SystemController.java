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

import xyz.playedu.api.BCtx;
import xyz.playedu.api.annotation.Log;
import xyz.playedu.api.constant.BusinessType;
import xyz.playedu.api.constant.CConfig;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.util.RequestUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/backend/v1/system")
@Slf4j
public class SystemController {

    @GetMapping("/config")
    @Log(title = "其它-系统配置", businessType = BusinessType.GET)
    public JsonResponse config() {
        Map<String, String> configData = BCtx.getConfig();

        String apiUrl = configData.get(CConfig.SYSTEM_API_URL);
        if (apiUrl == null || apiUrl.trim().length() == 0) {
            apiUrl = RequestUtil.uriWithProtocol();
        } else {
            if (apiUrl.endsWith("/")) {
                apiUrl = apiUrl.substring(0, apiUrl.length() - 1);
            }
        }

        HashMap<String, Object> data = new HashMap<>();

        data.put(CConfig.SYSTEM_NAME, configData.get(CConfig.SYSTEM_NAME));
        data.put(CConfig.SYSTEM_LOGO, configData.get(CConfig.SYSTEM_LOGO));
        data.put(CConfig.SYSTEM_API_URL, apiUrl);
        data.put(CConfig.SYSTEM_PC_URL, configData.get(CConfig.SYSTEM_PC_URL));
        data.put(CConfig.SYSTEM_H5_URL, configData.get(CConfig.SYSTEM_H5_URL));

        // 学员的默认头像
        String memberDefaultAvatar = configData.get(CConfig.MEMBER_DEFAULT_AVATAR);
        if (memberDefaultAvatar == null || memberDefaultAvatar.trim().length() == 0) {
            data.put(CConfig.MEMBER_DEFAULT_AVATAR, apiUrl + "/images/default_avatar.png");
        } else {
            data.put(CConfig.MEMBER_DEFAULT_AVATAR, memberDefaultAvatar);
        }

        // 内置的三个线上课封面
        List<String> defaultCourseThumbs = new ArrayList<>();
        defaultCourseThumbs.add(apiUrl + "/images/courses/thumb1.png");
        defaultCourseThumbs.add(apiUrl + "/images/courses/thumb2.png");
        defaultCourseThumbs.add(apiUrl + "/images/courses/thumb3.png");
        data.put("default.course_thumbs", defaultCourseThumbs);

        return JsonResponse.data(data);
    }
}
