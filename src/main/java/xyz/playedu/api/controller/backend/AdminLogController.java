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

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import xyz.playedu.api.BCtx;
import xyz.playedu.api.annotation.Log;
import xyz.playedu.api.bus.BackendBus;
import xyz.playedu.api.constant.BPermissionConstant;
import xyz.playedu.api.constant.BusinessType;
import xyz.playedu.api.domain.*;
import xyz.playedu.api.middleware.BackendPermissionMiddleware;
import xyz.playedu.api.service.AdminLogService;
import xyz.playedu.api.service.AdminUserService;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.types.paginate.AdminLogPaginateFiler;
import xyz.playedu.api.types.paginate.PaginationResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/backend/v1/admin/log")
public class AdminLogController {

    @Autowired private AdminLogService adminLogService;

    @Autowired private AdminUserService adminUserService;

    @Autowired private BackendBus backendBus;

    @BackendPermissionMiddleware(slug = BPermissionConstant.ADMIN_LOG)
    @GetMapping("/index")
    @Log(title = "管理员日志-列表", businessType = BusinessType.GET)
    public JsonResponse index(@RequestParam HashMap<String, Object> params) {
        Integer page = MapUtils.getInteger(params, "page", 1);
        Integer size = MapUtils.getInteger(params, "size", 10);
        String sortField = MapUtils.getString(params, "sort_field");
        String sortAlgo = MapUtils.getString(params, "sort_algo");

        Integer adminId = MapUtils.getInteger(params, "admin_id");
        String module = MapUtils.getString(params, "module");
        String title = MapUtils.getString(params, "title");
        Integer opt = MapUtils.getInteger(params, "opt");
        String startTime = MapUtils.getString(params, "start_time");
        String endTime = MapUtils.getString(params, "end_time");

        AdminLogPaginateFiler filter = new AdminLogPaginateFiler();
        if (backendBus.isSuperAdmin()) {
            filter.setAdminId(adminId);
        } else {
            filter.setAdminId(BCtx.getId());
        }
        filter.setModule(module);
        filter.setTitle(title);
        filter.setOpt(opt);
        filter.setStartTime(startTime);
        filter.setEndTime(endTime);
        filter.setSortField(sortField);
        filter.setSortAlgo(sortAlgo);

        PaginationResult<AdminLog> result = adminLogService.paginate(page, size, filter);
        if(result.getTotal() > 0){
            List<AdminUser> adminUsers =  adminUserService.chunks(result.getData().stream().map(AdminLog::getAdminId).toList());
            if(null != adminUsers && adminUsers.size() > 0){
                Map<Integer, String> adminUserMap = adminUsers.stream().collect(Collectors.toMap(AdminUser::getId, AdminUser::getName));
                result.getData()
                        .forEach(
                                adminLog -> {
                                    adminLog.setAdminName(adminUserMap.get(adminLog.getAdminId()));
                                });
            }
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("data", result.getData());
        data.put("total", result.getTotal());

        return JsonResponse.data(data);
    }
}
