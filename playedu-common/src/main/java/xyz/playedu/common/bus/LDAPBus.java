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
import xyz.playedu.common.domain.LdapUser;
import xyz.playedu.common.domain.User;
import xyz.playedu.common.exception.NotFoundException;
import xyz.playedu.common.service.*;
import xyz.playedu.common.types.LdapConfig;
import xyz.playedu.common.util.HelperUtil;
import xyz.playedu.common.util.StringUtil;
import xyz.playedu.common.util.ldap.LdapTransformDepartment;
import xyz.playedu.common.util.ldap.LdapTransformUser;
import xyz.playedu.common.util.ldap.LdapUtil;

import java.io.IOException;
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

    @Autowired private LdapUserService ldapUserService;

    @Autowired private UserService userService;

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
                        // 写入本地缓存
                        LdapDepartment storedLdapDepartment = new LdapDepartment();
                        storedLdapDepartment.setUuid(uuid);
                        storedLdapDepartment.setDn(dn);
                        storedLdapDepartment.setDepartmentId(depId);
                        ldapDepartments.put(uuid, storedLdapDepartment);
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

    public void userSync() throws NamingException, IOException {
        LdapConfig ldapConfig = appConfigService.ldapConfig();

        List<LdapTransformUser> userList =
                LdapUtil.users(
                        ldapConfig.getUrl(),
                        ldapConfig.getAdminUser(),
                        ldapConfig.getAdminPass(),
                        ldapConfig.getBaseDN());

        if (userList == null || userList.isEmpty()) {
            return;
        }

        String defaultAvatar = appConfigService.defaultAvatar();

        for (LdapTransformUser ldapTransformUser : userList) {
            if (ldapTransformUser.isBan()) {
                continue;
            }
            singleUserSync(ldapTransformUser, defaultAvatar);
        }
    }

    public User singleUserSync(LdapTransformUser ldapTransformUser, String defaultAvatar) {
        // LDAP用户的名字
        String ldapUserName = ldapTransformUser.getCn();

        // 将LDAP用户所属的部门同步到本地
        Integer depId = departmentService.createWithChainList(ldapTransformUser.getOu());
        Integer[] depIds = depId == 0 ? null : new Integer[] {depId};

        // LDAP用户在本地的缓存记录
        LdapUser ldapUser = ldapUserService.findByUUID(ldapTransformUser.getId());
        User user;

        // 计算将LDAP用户关联到本地users表的email字段值
        String localUserEmail = ldapTransformUser.getUid();
        if (StringUtil.isNotEmpty(ldapTransformUser.getEmail())) {
            localUserEmail = ldapTransformUser.getEmail();
        }

        if (ldapUser == null) {
            // 检测localUserEmail是否存在
            if (userService.find(localUserEmail) != null) {
                localUserEmail = HelperUtil.randomString(5) + "_" + localUserEmail;
            }
            // LDAP用户数据缓存到本地
            ldapUser = ldapUserService.store(ldapTransformUser);
            // 创建本地user
            user =
                    userService.createWithDepIds(
                            localUserEmail,
                            ldapUserName,
                            defaultAvatar,
                            HelperUtil.randomString(10),
                            "",
                            depIds);
            // 将LDAP缓存数据与本地user关联
            ldapUserService.updateUserId(ldapUser.getId(), user.getId());
        } else {
            user = userService.find(ldapUser.getUserId());
            if (user == null) {
                user =
                        userService.createWithDepIds(
                                localUserEmail,
                                ldapUserName,
                                defaultAvatar,
                                HelperUtil.randomString(10),
                                "",
                                depIds);
            }
            // 账号修改[账号有可能是email也有可能是uid]
            if (!localUserEmail.equals(user.getEmail())) {
                // 检测localUserEmail是否存在
                if (userService.find(localUserEmail) != null) {
                    localUserEmail = HelperUtil.randomString(5) + "_" + localUserEmail;
                }
                userService.updateEmail(user.getId(), localUserEmail);
            }
            // ldap-email的变化
            if (!ldapUser.getEmail().equals(ldapTransformUser.getEmail())) {
                ldapUserService.updateEmail(ldapUser.getId(), ldapTransformUser.getEmail());
            }
            // ldap-uid的变化
            if (!ldapUser.getUid().equals(ldapTransformUser.getUid())) {
                ldapUserService.updateUid(ldapUser.getId(), ldapTransformUser.getUid());
            }
            // 名字同步修改
            if (!ldapUserName.equals(ldapUser.getCn())) {
                userService.updateName(user.getId(), ldapUserName);
                ldapUserService.updateCN(ldapUser.getId(), ldapUserName);
            }
            // 部门修改同步
            String newOU = String.join(",", ldapTransformUser.getOu());
            if (!newOU.equals(ldapUser.getOu())) {
                userService.updateDepId(user.getId(), depIds);
                ldapUserService.updateOU(ldapUser.getId(), newOU);
            }
        }

        return user;
    }
}
