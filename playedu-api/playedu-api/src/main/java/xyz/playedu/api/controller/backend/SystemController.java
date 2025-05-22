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

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.playedu.common.annotation.Log;
import xyz.playedu.common.constant.BusinessTypeConstant;
import xyz.playedu.common.constant.ConfigConstant;
import xyz.playedu.common.context.BCtx;
import xyz.playedu.common.service.AppConfigService;
import xyz.playedu.common.service.CategoryService;
import xyz.playedu.common.service.DepartmentService;
import xyz.playedu.common.types.JsonResponse;
import xyz.playedu.common.util.StringUtil;
import xyz.playedu.resource.service.ResourceService;

@RestController
@RequestMapping("/backend/v1/system")
@Slf4j
public class SystemController {

    @Autowired private DepartmentService departmentService;

    @Autowired private CategoryService categoryService;

    @Autowired private ResourceService resourceService;

    @Autowired private AppConfigService appConfigService;

    @GetMapping("/config")
    @Log(title = "其它-系统配置", businessType = BusinessTypeConstant.GET)
    public JsonResponse config() {
        Map<String, String> configData = BCtx.getConfig();

        HashMap<String, Object> data = new HashMap<>();

        data.put(ConfigConstant.SYSTEM_NAME, configData.get(ConfigConstant.SYSTEM_NAME));
        data.put(ConfigConstant.SYSTEM_LOGO, configData.get(ConfigConstant.SYSTEM_LOGO));
        data.put(ConfigConstant.SYSTEM_PC_URL, configData.get(ConfigConstant.SYSTEM_PC_URL));
        data.put(ConfigConstant.SYSTEM_H5_URL, configData.get(ConfigConstant.SYSTEM_H5_URL));

        Integer rid = -1;
        String avatar = configData.get(ConfigConstant.MEMBER_DEFAULT_AVATAR);
        if (StringUtil.isNotEmpty(avatar)) {
            rid = Integer.parseInt(avatar);
        }

        data.put(ConfigConstant.MEMBER_DEFAULT_AVATAR, rid);

        // 获取签名url
        data.put(
                "resource_url",
                resourceService.chunksPreSignUrlByIds(appConfigService.getAllImageValue()));

        // LDAP登录
        data.put("ldap-enabled", "1".equals(configData.get(ConfigConstant.LDAP_ENABLED)));

        // 全部部门
        data.put("departments", departmentService.groupByParent());

        // 全部资源分类
        data.put("resource_categories", categoryService.groupByParent());

        return JsonResponse.data(data);
    }
}
