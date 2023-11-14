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
package xyz.playedu.common.bus;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import xyz.playedu.common.domain.Department;
import xyz.playedu.common.domain.LdapDepartment;
import xyz.playedu.common.exception.NotFoundException;
import xyz.playedu.common.service.AppConfigService;
import xyz.playedu.common.service.DepartmentService;
import xyz.playedu.common.service.LdapDepartmentService;
import xyz.playedu.common.types.LdapConfig;
import xyz.playedu.common.util.ldap.LdapTransformDepartment;
import xyz.playedu.common.util.ldap.LdapUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.naming.NamingException;

@Component
@Slf4j
public class LDAPBus {

    @Autowired private AppConfigService appConfigService;

    @Autowired private DepartmentService departmentService;

    @Autowired private LdapDepartmentService ldapDepartmentService;

    public boolean enabledLDAP() {
        return appConfigService.enabledLdapLogin();
    }

    public void departmentSync() throws NamingException, NotFoundException {
        LdapConfig ldapConfig = appConfigService.ldapConfig();

        List<LdapTransformDepartment> ouList =
                LdapUtil.departments(
                        ldapConfig.getUrl(),
                        ldapConfig.getAdminUser(),
                        ldapConfig.getAdminPass(),
                        ldapConfig.getBaseDN());

        if (ouList == null || ouList.isEmpty()) {
            return;
        }

        // 读取已经同步的记录
        Map<String, LdapDepartment> ldapDepartments =
                ldapDepartmentService.all().stream()
                        .collect(Collectors.toMap(LdapDepartment::getUuid, e -> e));

        // 本地缓存表
        HashMap<String, Integer> depIdKeyByName = new HashMap<>();
        // 全局排序计数
        Integer sort = 0;

        // 新建+编辑的处理
        for (LdapTransformDepartment ldapTransformDepartment : ouList) {
            String uuid = ldapTransformDepartment.getUuid();
            String dn = ldapTransformDepartment.getDn();
            String[] tmpChains = dn.replace("ou=", "").split(",");
            String prevName = "";

            // 同步记录
            LdapDepartment tmpLdapDepartment = ldapDepartments.get(uuid);
            if (tmpLdapDepartment != null && tmpLdapDepartment.getDn().equals(dn)) {
                // 当前部门已经同步 && 未发生改变
                continue;
            }

            // 执行到这里的有两种情况：
            // 1.部门未同步
            // 2.部门已同步，但是发生了变化
            // 2.1 部门名称修改
            // 2.2 部门上级修改

            int length = tmpChains.length;
            for (int i = 0; i < length; i++) {
                sort++;
                int parentId = 0;

                String tmpName = tmpChains[i];

                // 部门的链名=>父部门1,父部门2,子部门
                String fullName = tmpName;
                if (!prevName.isEmpty()) {
                    fullName = prevName + "," + tmpName;
                    // 取父级ID
                    parentId = depIdKeyByName.get(prevName);
                }

                // 最后一个记录 && 已存在部门-发生了变动
                if (i + 1 == length && tmpLdapDepartment != null) {
                    Department tmpDepartment =
                            departmentService.findOrFail(tmpLdapDepartment.getDepartmentId());
                    departmentService.update(tmpDepartment, tmpName, parentId, sort);
                } else {
                    // 检查本地是否有缓存
                    Integer depId = depIdKeyByName.get(fullName);
                    if (depId == null) {
                        Department tmpDep = departmentService.findByName(tmpName, parentId);
                        if (tmpDep != null) {
                            depId = tmpDep.getId();
                        } else {
                            depId = departmentService.create(tmpName, parentId, sort);
                            // 创建同步记录
                            ldapDepartmentService.create(depId, uuid, dn);
                        }
                        // 写入本地缓存
                        depIdKeyByName.put(fullName, depId);
                    }
                }

                // 父级叠加
                prevName = fullName;
            }
        }

        // 删除的处理
        List<String> uuidList = ouList.stream().map(LdapTransformDepartment::getUuid).toList();
        List<LdapDepartment> ldapDepartmentList =
                ldapDepartmentService.notChunkByUUIDList(uuidList);
        for (LdapDepartment ldapDepartment : ldapDepartmentList) {
            // 删除本地部门
            departmentService.destroy(ldapDepartment.getDepartmentId());
            // 删除关联记录
            ldapDepartmentService.destroy(ldapDepartment.getId());
        }
    }

    public void userSync() {}
}
