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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.playedu.common.annotation.BackendPermission;
import xyz.playedu.common.constant.BPermissionConstant;
import xyz.playedu.common.domain.LdapSyncDepartmentDetail;
import xyz.playedu.common.domain.LdapSyncUserDetail;
import xyz.playedu.common.service.LdapSyncDepartmentDetailService;
import xyz.playedu.common.service.LdapSyncUserDetailService;
import xyz.playedu.common.types.JsonResponse;

/** LDAP同步详情控制器 */
@RestController
@RequestMapping("/backend/v1/ldap")
public class LdapSyncDetailController {

    @Autowired private LdapSyncDepartmentDetailService ldapSyncDepartmentDetailService;

    @Autowired private LdapSyncUserDetailService ldapSyncUserDetailService;

    /**
     * 获取同步详情
     *
     * @param id 同步记录ID
     * @param type 详情类型：department-部门，user-用户
     * @param action 操作类型： - 部门：1-新增，2-更新，3-删除，4-无变化 - 用户：1-新增，2-更新，3-删除，4-无变化，5-禁止
     * @param page 页码
     * @param size 每页数量
     * @return 分页结果
     */
    @BackendPermission(slug = BPermissionConstant.DEPARTMENT_CUD)
    @GetMapping("/sync-records/{id}/details")
    public JsonResponse getDetails(
            @PathVariable Integer id,
            @RequestParam String type,
            @RequestParam(defaultValue = "0") Integer action,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        if ("department".equals(type)) {
            // 部门同步详情
            QueryWrapper<LdapSyncDepartmentDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("record_id", id);
            if (action > 0) {
                queryWrapper.eq("action", action);
            }
            queryWrapper.orderByDesc("id");

            IPage<LdapSyncDepartmentDetail> pageResult =
                    ldapSyncDepartmentDetailService.page(new Page<>(page, size), queryWrapper);

            return JsonResponse.data(pageResult);
        } else if ("user".equals(type)) {
            // 用户同步详情
            QueryWrapper<LdapSyncUserDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("record_id", id);
            if (action > 0) {
                queryWrapper.eq("action", action);
            }
            queryWrapper.orderByDesc("id");

            IPage<LdapSyncUserDetail> pageResult =
                    ldapSyncUserDetailService.page(new Page<>(page, size), queryWrapper);

            return JsonResponse.data(pageResult);
        }

        return JsonResponse.error("不支持的详情类型");
    }
}
