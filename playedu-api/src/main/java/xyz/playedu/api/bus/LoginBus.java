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
package xyz.playedu.api.bus;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import xyz.playedu.api.event.UserLoginEvent;
import xyz.playedu.common.domain.LdapUser;
import xyz.playedu.common.domain.User;
import xyz.playedu.common.exception.ServiceException;
import xyz.playedu.common.service.*;
import xyz.playedu.common.util.HelperUtil;
import xyz.playedu.common.util.IpUtil;
import xyz.playedu.common.util.RequestUtil;
import xyz.playedu.common.util.StringUtil;
import xyz.playedu.common.util.ldap.LdapTransformUser;

import java.util.HashMap;

@Component
@Slf4j
public class LoginBus {

    @Autowired private FrontendAuthService authService;

    @Autowired private DepartmentService departmentService;

    @Autowired private LdapUserService ldapUserService;

    @Autowired private UserService userService;

    @Autowired private AppConfigService appConfigService;

    @Autowired private ApplicationContext ctx;

    public HashMap<String, Object> tokenByUser(User user) {
        String token = authService.loginUsingId(user.getId(), RequestUtil.url());

        HashMap<String, Object> data = new HashMap<>();
        data.put("token", token);

        ctx.publishEvent(
                new UserLoginEvent(
                        this,
                        user.getId(),
                        user.getEmail(),
                        token,
                        IpUtil.getIpAddress(),
                        RequestUtil.ua()));

        return data;
    }

    @Transactional
    public HashMap<String, Object> tokenByLdapTransformUser(LdapTransformUser ldapTransformUser)
            throws ServiceException {
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
                throw new ServiceException(String.format("已有其它账号在使用：%s", localUserEmail));
            }
            // LDAP用户数据缓存到本地
            ldapUser = ldapUserService.store(ldapTransformUser);
            // 创建本地user
            user =
                    userService.createWithDepIds(
                            localUserEmail,
                            ldapUserName,
                            appConfigService.defaultAvatar(),
                            HelperUtil.randomString(20),
                            "",
                            depIds);
            // 将LDAP缓存数据与本地user关联
            ldapUserService.updateUserId(ldapUser.getId(), user.getId());
        } else {
            user = userService.find(ldapUser.getUserId());
            // 账号修改[账号有可能是email也有可能是uid]
            if (!localUserEmail.equals(user.getEmail())) {
                // 检测localUserEmail是否存在
                if (userService.find(localUserEmail) != null) {
                    throw new ServiceException(String.format("已有其它账号在使用：%s", localUserEmail));
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

        return tokenByUser(user);
    }
}
