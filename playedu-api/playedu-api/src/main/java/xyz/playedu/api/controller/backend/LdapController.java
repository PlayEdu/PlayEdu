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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.playedu.common.annotation.BackendPermission;
import xyz.playedu.common.annotation.Log;
import xyz.playedu.common.constant.BPermissionConstant;
import xyz.playedu.common.constant.BusinessTypeConstant;
import xyz.playedu.common.domain.LdapSyncRecord;
import xyz.playedu.common.service.AppConfigService;
import xyz.playedu.common.service.LdapSyncRecordService;
import xyz.playedu.common.types.JsonResponse;
import xyz.playedu.common.types.config.S3Config;
import xyz.playedu.common.types.paginate.PaginationResult;
import xyz.playedu.common.util.S3Util;

@RestController
@RequestMapping("/backend/v1/ldap")
public class LdapController {

    @Autowired private LdapSyncRecordService ldapSyncRecordService;
    @Autowired private AppConfigService appConfigService;

    @BackendPermission(slug = BPermissionConstant.DEPARTMENT_CUD)
    @GetMapping("/sync-records")
    @Log(title = "LDAP-同步记录列表", businessType = BusinessTypeConstant.GET)
    public JsonResponse syncRecords(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {

        PaginationResult<LdapSyncRecord> result = ldapSyncRecordService.paginate(page, size);

        HashMap<String, Object> data = new HashMap<>();
        data.put("data", result.getData());
        data.put("total", result.getTotal());

        return JsonResponse.data(data);
    }

    @BackendPermission(slug = BPermissionConstant.DEPARTMENT_CUD)
    @GetMapping("/sync-records/{id}")
    @Log(title = "LDAP-同步记录详情", businessType = BusinessTypeConstant.GET)
    public JsonResponse syncRecordDetail(@PathVariable Integer id) {
        LdapSyncRecord record = ldapSyncRecordService.getById(id);
        if (record == null) {
            return JsonResponse.error("记录不存在");
        }

        return JsonResponse.data(record);
    }

    @BackendPermission(slug = BPermissionConstant.DEPARTMENT_CUD)
    @GetMapping("/sync-records/{id}/download")
    @Log(title = "LDAP-同步记录下载", businessType = BusinessTypeConstant.GET)
    public JsonResponse syncRecordDownload(@PathVariable Integer id) {
        LdapSyncRecord record = ldapSyncRecordService.getById(id);
        if (record == null) {
            return JsonResponse.error("记录不存在");
        }

        if (record.getS3FilePath() == null || record.getS3FilePath().isEmpty()) {
            return JsonResponse.error("同步记录文件不存在");
        }

        try {
            // 生成下载URL
            S3Config s3Config = appConfigService.getS3Config();
            S3Util s3Util = new S3Util(s3Config);
            String url = s3Util.generateEndpointPreSignUrl(record.getS3FilePath());

            HashMap<String, Object> data = new HashMap<>();
            data.put("url", url);

            return JsonResponse.data(data);
        } catch (Exception e) {
            return JsonResponse.error("生成下载链接失败: " + e.getMessage());
        }
    }
}
