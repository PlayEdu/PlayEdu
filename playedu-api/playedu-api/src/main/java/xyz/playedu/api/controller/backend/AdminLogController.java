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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.playedu.common.annotation.BackendPermission;
import xyz.playedu.common.annotation.Log;
import xyz.playedu.common.bus.BackendBus;
import xyz.playedu.common.constant.BPermissionConstant;
import xyz.playedu.common.constant.BusinessTypeConstant;
import xyz.playedu.common.context.BCtx;
import xyz.playedu.common.domain.AdminLog;
import xyz.playedu.common.exception.ServiceException;
import xyz.playedu.common.service.AdminLogService;
import xyz.playedu.common.types.JsonResponse;
import xyz.playedu.common.types.paginate.AdminLogPaginateFiler;
import xyz.playedu.common.types.paginate.PaginationResult;

@RestController
@Slf4j
@RequestMapping("/backend/v1/admin/log")
public class AdminLogController {

    @Autowired private AdminLogService adminLogService;

    @Autowired private BackendBus backendBus;

    @BackendPermission(slug = BPermissionConstant.ADMIN_LOG)
    @GetMapping("/index")
    @Log(title = "管理员日志-列表", businessType = BusinessTypeConstant.GET)
    public JsonResponse index(@RequestParam HashMap<String, Object> params) {
        Integer page = MapUtils.getInteger(params, "page", 1);
        Integer size = MapUtils.getInteger(params, "size", 10);
        String sortField = MapUtils.getString(params, "sort_field");
        String sortAlgo = MapUtils.getString(params, "sort_algo");

        Integer adminId = MapUtils.getInteger(params, "admin_id");
        String adminName = MapUtils.getString(params, "admin_name");
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
        filter.setAdminName(adminName);
        filter.setModule(module);
        filter.setTitle(title);
        filter.setOpt(opt);
        filter.setStartTime(startTime);
        filter.setEndTime(endTime);
        filter.setSortField(sortField);
        filter.setSortAlgo(sortAlgo);

        PaginationResult<AdminLog> result = adminLogService.paginate(page, size, filter);

        HashMap<String, Object> data = new HashMap<>();
        data.put("data", result.getData());
        data.put("total", result.getTotal());

        return JsonResponse.data(data);
    }

    @BackendPermission(slug = BPermissionConstant.ADMIN_LOG)
    @GetMapping("/detail/{id}")
    public JsonResponse detail(@PathVariable(name = "id") Integer id) {
        Integer adminId = 0;
        if (!backendBus.isSuperAdmin()) {
            adminId = BCtx.getId();
        }

        AdminLog log = adminLogService.find(id, adminId);
        if (log == null) {
            throw new ServiceException("日志不存在");
        }
        return JsonResponse.data(log);
    }
}
