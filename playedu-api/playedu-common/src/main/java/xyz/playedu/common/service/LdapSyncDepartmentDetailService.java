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
package xyz.playedu.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import xyz.playedu.common.domain.LdapSyncDepartmentDetail;

/** LDAP部门同步详情服务接口 */
public interface LdapSyncDepartmentDetailService extends IService<LdapSyncDepartmentDetail> {

    /**
     * 批量创建部门同步详情记录
     *
     * @param details 部门同步详情记录列表
     */
    void batchCreate(List<LdapSyncDepartmentDetail> details);

    /**
     * 根据同步记录ID和操作类型获取部门同步详情
     *
     * @param recordId 同步记录ID
     * @param action 操作类型，1-新增，2-更新，3-删除，4-无变化
     * @return 部门同步详情列表
     */
    List<LdapSyncDepartmentDetail> getByRecordIdAndAction(Integer recordId, Integer action);
}
