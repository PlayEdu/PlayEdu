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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.naming.NamingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.playedu.common.domain.Department;
import xyz.playedu.common.domain.LdapDepartment;
import xyz.playedu.common.domain.LdapSyncDepartmentDetail;
import xyz.playedu.common.domain.LdapSyncRecord;
import xyz.playedu.common.domain.LdapSyncUserDetail;
import xyz.playedu.common.domain.LdapUser;
import xyz.playedu.common.domain.User;
import xyz.playedu.common.exception.NotFoundException;
import xyz.playedu.common.service.*;
import xyz.playedu.common.types.LdapConfig;
import xyz.playedu.common.types.config.S3Config;
import xyz.playedu.common.util.HelperUtil;
import xyz.playedu.common.util.S3Util;
import xyz.playedu.common.util.ldap.LdapTransformDepartment;
import xyz.playedu.common.util.ldap.LdapTransformUser;
import xyz.playedu.common.util.ldap.LdapUtil;

@Component
@Slf4j
public class LDAPBus {

    @Autowired private AppConfigService appConfigService;

    @Autowired private DepartmentService departmentService;

    @Autowired private LdapDepartmentService ldapDepartmentService;

    @Autowired private LdapUserService ldapUserService;

    @Autowired private UserService userService;

    @Autowired private LdapSyncRecordService ldapSyncRecordService;

    @Autowired private LdapSyncDepartmentDetailService ldapSyncDepartmentDetailService;

    @Autowired private LdapSyncUserDetailService ldapSyncUserDetailService;

    public boolean enabledLDAP() {
        return appConfigService.enabledLdapLogin();
    }

    /** 检查是否有进行中的同步任务 */
    public boolean hasSyncInProgress() {
        return ldapSyncRecordService.hasSyncInProgress();
    }

    /**
     * 执行LDAP同步并记录同步数据
     *
     * @param adminId 执行同步的管理员ID，0为系统自动执行
     * @return 同步记录ID
     */
    public Integer syncAndRecord(Integer adminId)
            throws NamingException, IOException, NotFoundException {
        // 检查是否有进行中的同步任务
        if (hasSyncInProgress()) {
            throw new RuntimeException("有正在进行的LDAP同步任务，请稍后再试");
        }

        // 创建同步记录
        LdapSyncRecord record = ldapSyncRecordService.create(adminId);

        try {
            // 获取LDAP配置
            LdapConfig ldapConfig = appConfigService.ldapConfig();

            // 查询LDAP数据（只查询一次）
            List<LdapTransformDepartment> departments =
                    LdapUtil.departments(ldapConfig, ldapConfig.getBaseDN());
            List<LdapTransformUser> users = LdapUtil.users(ldapConfig, ldapConfig.getBaseDN());

            // 使用查询的数据进行统计
            Map<String, Object> result = collectSyncStatistics(departments, users);

            // 将同步数据保存到S3
            String s3FilePath = saveDataToS3(result, record.getId());

            // 收集部门和用户的详细同步信息
            List<LdapSyncDepartmentDetail> departmentDetails =
                    collectDepartmentSyncDetails(record.getId(), departments);
            List<LdapSyncUserDetail> userDetails = collectUserSyncDetails(record.getId(), users);

            // 使用同样的数据执行实际同步
            departmentSync(departments);
            userSync(users);

            // 保存部门和用户的详细同步信息
            ldapSyncDepartmentDetailService.batchCreate(departmentDetails);
            ldapSyncUserDetailService.batchCreate(userDetails);

            // 更新同步记录
            ldapSyncRecordService.updateSyncResult(
                    record.getId(),
                    1, // 成功
                    s3FilePath,
                    (Integer) result.get("totalDepartmentCount"),
                    (Integer) result.get("createdDepartmentCount"),
                    (Integer) result.get("updatedDepartmentCount"),
                    (Integer) result.get("deletedDepartmentCount"),
                    (Integer) result.get("totalUserCount"),
                    (Integer) result.get("createdUserCount"),
                    (Integer) result.get("updatedUserCount"),
                    (Integer) result.get("deletedUserCount"),
                    (Integer) result.get("bannedUserCount"));

            return record.getId();
        } catch (Exception e) {
            // 记录同步失败
            ldapSyncRecordService.updateSyncFailed(record.getId(), e.getMessage());
            log.error("LDAP同步失败", e);
            throw e;
        }
    }

    /**
     * 收集部门同步详情
     *
     * @param recordId 同步记录ID
     * @param departments LDAP部门数据
     * @return 部门同步详情列表
     */
    private List<LdapSyncDepartmentDetail> collectDepartmentSyncDetails(
            Integer recordId, List<LdapTransformDepartment> departments) throws NotFoundException {
        List<LdapSyncDepartmentDetail> details = new ArrayList<>();
        Date now = new Date();

        // 读取已经同步的记录
        Map<String, LdapDepartment> ldapDepartments =
                ldapDepartmentService.all().stream()
                        .collect(Collectors.toMap(LdapDepartment::getUuid, e -> e));

        // 记录新增和更新的部门
        for (LdapTransformDepartment dept : departments) {
            LdapDepartment existingDept = ldapDepartments.get(dept.getUuid());
            LdapSyncDepartmentDetail detail = new LdapSyncDepartmentDetail();
            detail.setRecordId(recordId);
            detail.setUuid(dept.getUuid());
            detail.setDn(dept.getDn());

            // 从DN中提取部门名称
            String[] parts = dept.getDn().split(",");
            String name = parts[parts.length - 1].replace("ou=", "");
            detail.setName(name);
            detail.setCreatedAt(now);

            if (existingDept == null) {
                // 新增部门
                detail.setAction(1);
            } else if (!existingDept.getDn().equals(dept.getDn())) {
                // 更新部门
                detail.setDepartmentId(existingDept.getDepartmentId());
                detail.setAction(2);
            } else {
                // 无变化
                detail.setDepartmentId(existingDept.getDepartmentId());
                detail.setAction(4);
            }

            details.add(detail);
        }

        // 记录删除的部门
        List<String> uuidList = departments.stream().map(LdapTransformDepartment::getUuid).toList();
        List<LdapDepartment> deletedDepts = ldapDepartmentService.notChunkByUUIDList(uuidList);
        if (deletedDepts != null && !deletedDepts.isEmpty()) {
            for (LdapDepartment dept : deletedDepts) {
                LdapSyncDepartmentDetail detail = new LdapSyncDepartmentDetail();
                detail.setRecordId(recordId);
                detail.setDepartmentId(dept.getDepartmentId());
                detail.setUuid(dept.getUuid());
                detail.setDn(dept.getDn());

                // 获取部门名称
                Department department = departmentService.findOrFail(dept.getDepartmentId());
                detail.setName(department.getName());

                detail.setAction(3); // 删除
                detail.setCreatedAt(now);
                details.add(detail);
            }
        }

        return details;
    }

    /**
     * 收集用户同步详情
     *
     * @param recordId 同步记录ID
     * @param users LDAP用户数据
     * @return 用户同步详情列表
     */
    private List<LdapSyncUserDetail> collectUserSyncDetails(
            Integer recordId, List<LdapTransformUser> users) {
        List<LdapSyncUserDetail> details = new ArrayList<>();
        Date now = new Date();

        // 处理新增和更新的用户
        for (LdapTransformUser user : users) {
            LdapSyncUserDetail detail = new LdapSyncUserDetail();
            detail.setRecordId(recordId);
            detail.setUuid(user.getId());
            detail.setDn(user.getDn());
            detail.setCn(user.getCn());
            detail.setUid(user.getUid());
            detail.setEmail(user.getEmail());
            detail.setOu(String.join(",", user.getOu()));
            detail.setCreatedAt(now);

            // 查找现有用户
            LdapUser existingUser = ldapUserService.findByUUID(user.getId());

            // 检查用户是否被禁止
            if (user.isBan()) {
                // 标记为禁止的用户
                detail.setAction(5); // 5-禁止的用户
                if (existingUser != null) {
                    detail.setUserId(existingUser.getUserId());
                }
                details.add(detail);
                continue;
            }

            if (existingUser == null) {
                // 新增用户
                detail.setAction(1);
            } else {
                // 检查是否有变更
                boolean hasChanges = false;
                if (!user.getCn().equals(existingUser.getCn())) {
                    hasChanges = true;
                }

                String newOU = String.join(",", user.getOu());
                if (!newOU.equals(existingUser.getOu())) {
                    hasChanges = true;
                }

                // 设置用户ID
                detail.setUserId(existingUser.getUserId());

                if (hasChanges) {
                    // 更新用户
                    detail.setAction(2);
                } else {
                    // 无变化
                    detail.setAction(4);
                }
            }

            details.add(detail);
        }

        // 处理删除的用户
        List<String> uuidList =
                users.stream().filter(u -> !u.isBan()).map(LdapTransformUser::getId).toList();

        // 获取所有现有的LDAP用户记录
        List<LdapUser> allLdapUsers = ldapUserService.list();

        // 过滤出不在当前LDAP用户列表中的用户
        List<LdapUser> deletedUsers =
                allLdapUsers.stream().filter(lu -> !uuidList.contains(lu.getUuid())).toList();

        for (LdapUser deletedUser : deletedUsers) {
            LdapSyncUserDetail detail = new LdapSyncUserDetail();
            detail.setRecordId(recordId);
            detail.setUserId(deletedUser.getUserId());
            detail.setUuid(deletedUser.getUuid());
            detail.setDn(deletedUser.getDn());
            detail.setCn(deletedUser.getCn());
            detail.setUid(deletedUser.getUid());
            detail.setEmail(deletedUser.getEmail());
            detail.setOu(deletedUser.getOu());
            detail.setAction(3); // 删除
            detail.setCreatedAt(now);

            details.add(detail);
        }

        return details;
    }

    /** 收集同步统计数据 */
    private Map<String, Object> collectSyncStatistics(
            List<LdapTransformDepartment> departments, List<LdapTransformUser> users) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> syncData = new HashMap<>();

        // 部门同步统计
        int totalDepartmentCount = 0;
        int createdDepartmentCount = 0;
        int updatedDepartmentCount = 0;
        int deletedDepartmentCount = 0;

        // 用户同步统计
        int totalUserCount = 0;
        int createdUserCount = 0;
        int updatedUserCount = 0;
        int deletedUserCount = 0;
        int bannedUserCount = 0;

        // 处理部门数据
        if (departments != null && !departments.isEmpty()) {
            syncData.put("departments", departments);
            totalDepartmentCount = departments.size();

            // 读取已经同步的记录
            Map<String, LdapDepartment> ldapDepartments =
                    ldapDepartmentService.all().stream()
                            .collect(Collectors.toMap(LdapDepartment::getUuid, e -> e));

            // 计算新增和更新的部门
            for (LdapTransformDepartment dept : departments) {
                LdapDepartment existingDept = ldapDepartments.get(dept.getUuid());
                if (existingDept == null) {
                    createdDepartmentCount++;
                } else if (!existingDept.getDn().equals(dept.getDn())) {
                    updatedDepartmentCount++;
                }
            }

            // 计算删除的部门
            List<String> uuidList =
                    departments.stream().map(LdapTransformDepartment::getUuid).toList();
            List<LdapDepartment> ldapDepartmentList =
                    ldapDepartmentService.notChunkByUUIDList(uuidList);
            deletedDepartmentCount = ldapDepartmentList != null ? ldapDepartmentList.size() : 0;
        }

        // 处理用户数据
        if (users != null && !users.isEmpty()) {
            syncData.put("users", users);
            totalUserCount = users.size();

            // 计算被禁止的用户数量
            bannedUserCount = (int) users.stream().filter(LdapTransformUser::isBan).count();

            // 计算新增、更新的用户
            for (LdapTransformUser user : users) {
                if (user.isBan()) {
                    continue;
                }

                LdapUser existingUser = ldapUserService.findByUUID(user.getId());
                if (existingUser == null) {
                    createdUserCount++;
                } else {
                    // 检查用户信息是否有变化
                    boolean hasChanges = false;
                    if (!user.getCn().equals(existingUser.getCn())) {
                        hasChanges = true;
                    }

                    String newOU = String.join(",", user.getOu());
                    if (!newOU.equals(existingUser.getOu())) {
                        hasChanges = true;
                    }

                    if (hasChanges) {
                        updatedUserCount++;
                    }
                }
            }
        }

        // 将同步结果数据存储到结果对象中
        result.put("data", syncData);
        result.put("totalDepartmentCount", totalDepartmentCount);
        result.put("createdDepartmentCount", createdDepartmentCount);
        result.put("updatedDepartmentCount", updatedDepartmentCount);
        result.put("deletedDepartmentCount", deletedDepartmentCount);
        result.put("totalUserCount", totalUserCount);
        result.put("createdUserCount", createdUserCount);
        result.put("updatedUserCount", updatedUserCount);
        result.put("deletedUserCount", deletedUserCount);
        result.put("bannedUserCount", bannedUserCount);

        return result;
    }

    /** 将同步数据保存到S3 */
    private String saveDataToS3(Map<String, Object> data, Integer recordId) throws IOException {
        // 将数据转换为JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonData = objectMapper.writeValueAsString(data);
        byte[] jsonBytes = jsonData.getBytes(StandardCharsets.UTF_8);

        // 生成保存路径
        String filename = "ldap_sync_" + recordId + "_" + new Date().getTime() + ".json";
        String savePath = "ldap/sync/" + filename;

        // 保存到S3
        S3Config s3Config = appConfigService.getS3Config();
        S3Util s3Util = new S3Util(s3Config);
        s3Util.saveBytes(jsonBytes, savePath, "application/json");

        return savePath;
    }

    /**
     * 执行部门同步 - 提供现有的LDAP部门数据
     *
     * @param ouList 已获取的LDAP部门数据
     */
    public void departmentSync(List<LdapTransformDepartment> ouList) throws NotFoundException {
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

            log.info("#####START#####[dn:{},uuid:{}]", dn, uuid);

            // 同步记录
            LdapDepartment tmpLdapDepartment = ldapDepartments.get(uuid);
            if (tmpLdapDepartment != null && tmpLdapDepartment.getDn().equals(dn)) {
                // 当前部门已经同步 && 未发生改变
                log.info("LDAP-部门同步处理-未发生改变|dn:{}", dn);
                continue;
            }

            // 执行到这里的有两种情况：
            // 1.部门未同步
            // 2.部门已同步，但是发生了变化
            // |-2.1 部门名称修改
            // |-2.2 部门上级名称修改
            // |-2.3 层级发生变动(增加层级|减少层级)

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

                log.info(
                        "LDAP-部门同步处理-链处理|ctx=[dn={},fullName:{},tmpName:{},parentId:{},sort:{}]",
                        dn,
                        fullName,
                        tmpName,
                        parentId,
                        sort);

                if (i + 1 == length && tmpLdapDepartment != null) {
                    // OU链发生了改变
                    // 1.部门名改变
                    // 2.上级部门名改变
                    // 3.层级改变

                    log.info("LDAP-部门同步处理-OU链发生改变|ctx=[新:{},旧:{}]", dn, tmpLdapDepartment.getDn());

                    Department tmpDepartment =
                            departmentService.findOrFail(tmpLdapDepartment.getDepartmentId());
                    if (!tmpDepartment.getName().equals(tmpName)
                            || !tmpLdapDepartment
                                    .getDn()
                                    .replace("ou=" + tmpName, "")
                                    .equals(dn.replaceAll("ou=" + tmpName, ""))) {
                        departmentService.update(tmpDepartment, tmpName, parentId, sort);
                    }

                    // 更新同步记录
                    tmpLdapDepartment.setDn(dn); // 最新的DN
                    ldapDepartmentService.updateDnById(tmpLdapDepartment.getId(), dn);
                    // 更新本地缓存
                    ldapDepartments.put(uuid, tmpLdapDepartment);
                    // 更新本地缓存
                    depIdKeyByName.put(fullName, tmpDepartment.getId());
                } else {
                    // 检查本地是否有缓存
                    Integer depId = depIdKeyByName.get(fullName);
                    log.info("LDAP-部门同步处理-从缓存查询depId|ctx=[fullName:{},depId:{}]", fullName, depId);
                    if (depId == null) {
                        Department tmpDep = departmentService.findByName(tmpName, parentId);
                        if (tmpDep != null) {
                            depId = tmpDep.getId();
                            log.info(
                                    "LDAP-部门同步处理-从数据库查询depId|ctx=[fullName:{},depId:{}]",
                                    fullName,
                                    depId);
                        } else {
                            depId = departmentService.create(tmpName, parentId, sort);
                            log.info(
                                    "LDAP-部门同步处理-新建部门|ctx=[fullName:{},depId:{}]", fullName, depId);
                        }

                        // 写入本地缓存
                        depIdKeyByName.put(fullName, depId);
                    }
                }

                if (i + 1 == length && tmpLdapDepartment == null) {
                    Integer tmpDepId = depIdKeyByName.get(fullName);
                    // 创建同步记录
                    ldapDepartmentService.create(tmpDepId, uuid, dn);

                    // 写入本地缓存
                    LdapDepartment storedLdapDepartment = new LdapDepartment();
                    storedLdapDepartment.setUuid(uuid);
                    storedLdapDepartment.setDn(dn);
                    storedLdapDepartment.setDepartmentId(tmpDepId);
                    ldapDepartments.put(uuid, storedLdapDepartment);
                }

                // 父级叠加
                prevName = fullName;
            }
        }

        // 删除的处理
        List<String> uuidList = ouList.stream().map(LdapTransformDepartment::getUuid).toList();
        List<LdapDepartment> ldapDepartmentList =
                ldapDepartmentService.notChunkByUUIDList(uuidList);
        if (ldapDepartmentList != null && !ldapDepartmentList.isEmpty()) {
            for (LdapDepartment ldapDepartment : ldapDepartmentList) {
                // 删除本地部门
                departmentService.destroy(ldapDepartment.getDepartmentId());
                // 删除同步记录
                ldapDepartmentService.destroy(ldapDepartment.getId());
            }
        }
    }

    /**
     * 执行用户同步 - 提供现有的LDAP用户数据
     *
     * @param userList 已获取的LDAP用户数据
     */
    public void userSync(List<LdapTransformUser> userList) {
        if (userList == null || userList.isEmpty()) {
            return;
        }

        Integer defaultAvatar = appConfigService.defaultAvatar();

        for (LdapTransformUser ldapTransformUser : userList) {
            if (ldapTransformUser.isBan()) {
                // 检查用户是否已在系统中存在
                LdapUser existingLdapUser = ldapUserService.findByUUID(ldapTransformUser.getId());
                if (existingLdapUser == null) {
                    // 对于新的被禁止用户，不同步到系统
                    log.info(
                            "LDAP-用户同步-新用户被禁止不同步|ctx=[dn:{},uuid={}]",
                            ldapTransformUser.getDn(),
                            ldapTransformUser.getId());
                    continue;
                }
            }

            singleUserSync(ldapTransformUser, defaultAvatar);
        }
    }

    public User singleUserSync(LdapTransformUser ldapTransformUser, Integer defaultAvatar) {
        log.info(
                "*****START*****LDAP-用户同步-开始|ctx=[dn:{},uuid:{}]",
                ldapTransformUser.getDn(),
                ldapTransformUser.getId());

        // LDAP用户的名字
        String ldapUserName = ldapTransformUser.getCn();

        // 将LDAP用户所属的部门同步到本地
        Integer depId = departmentService.createWithChainList(ldapTransformUser.getOu());
        Integer[] depIds = depId == 0 ? null : new Integer[] {depId};

        User user;

        // LDAP同步记录
        LdapUser ldapUser = ldapUserService.findByUUID(ldapTransformUser.getId());

        // 计算将LDAP用户关联到本地users表的email字段值
        String localUserEmail = ldapTransformUser.getUid();

        if (ldapUser == null) {
            // 检测localUserEmail是否存在
            if (userService.find(localUserEmail) != null) {
                log.info("LDAP-用户同步-email重复|ctx=[email:{}]", localUserEmail);
                return null;
            }

            // 创建同步记录
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

            log.info(
                    "LDAP-用户同步-录入数据|ctx=[userId:{},ldapUserId:{}]", user.getId(), ldapUser.getId());
        } else {
            log.info(
                    "LDAP-用户同步-检测变化值|ctx=[新dn:{},旧dn:{}]",
                    ldapTransformUser.getDn(),
                    ldapUser.getDn());

            user = userService.find(ldapUser.getUserId());

            if (user == null) {
                // 同步记录创建了，但是user却没创建
                log.info(
                        "LDAP-用户同步-同步记录存在但user不存在|ctx=[dn:{},ldapUserId:{}]",
                        ldapTransformUser.getDn(),
                        ldapUser.getId());
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

                if (ldapTransformUser.isBan()) {
                    log.info("LDAP-用户同步-被禁止用户部门已更新|ctx=[userId:{},新OU:{}]", user.getId(), newOU);
                }
            }

            // DN变化
            if (!ldapTransformUser.getDn().equals(ldapUser.getDn())) {
                ldapUserService.updateDN(ldapUser.getId(), ldapTransformUser.getDn());
            }
        }

        return user;
    }
}
