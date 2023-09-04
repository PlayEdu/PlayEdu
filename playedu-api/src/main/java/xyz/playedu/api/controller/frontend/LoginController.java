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
package xyz.playedu.api.controller.frontend;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import xyz.playedu.api.bus.LoginBus;
import xyz.playedu.api.cache.LoginLimitCache;
import xyz.playedu.api.cache.LoginLockCache;
import xyz.playedu.api.event.UserLogoutEvent;
import xyz.playedu.api.request.frontend.LoginLdapRequest;
import xyz.playedu.api.request.frontend.LoginPasswordRequest;
import xyz.playedu.common.constant.ConfigConstant;
import xyz.playedu.common.context.FCtx;
import xyz.playedu.common.domain.User;
import xyz.playedu.common.exception.LimitException;
import xyz.playedu.common.exception.ServiceException;
import xyz.playedu.common.service.*;
import xyz.playedu.common.types.JsonResponse;
import xyz.playedu.common.util.*;
import xyz.playedu.common.util.ldap.LdapTransformUser;
import xyz.playedu.common.util.ldap.LdapUtil;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth/login")
@Slf4j
public class LoginController {

    @Autowired private UserService userService;

    @Autowired private FrontendAuthService authService;

    @Autowired private ApplicationContext ctx;

    @Autowired private AppConfigService appConfigService;

    @Autowired private LoginBus loginBus;

    @Autowired private LoginLimitCache loginLimitCache;

    @Autowired private LoginLockCache loginLockCache;

    @PostMapping("/password")
    @SneakyThrows
    public JsonResponse password(
            @RequestBody @Validated LoginPasswordRequest req, LoginBus loginBus)
            throws LimitException {
        if (appConfigService.enabledLdapLogin()) {
            return JsonResponse.error("请使用LDAP登录");
        }

        String email = req.getEmail();

        User user = userService.find(email);
        if (user == null) {
            return JsonResponse.error("邮箱或密码错误");
        }

        loginLimitCache.check(email);

        if (!HelperUtil.MD5(req.getPassword() + user.getSalt()).equals(user.getPassword())) {
            return JsonResponse.error("邮箱或密码错误");
        }

        if (user.getIsLock() == 1) {
            return JsonResponse.error("当前学员已锁定无法登录");
        }

        loginLimitCache.destroy(email);

        return JsonResponse.data(loginBus.tokenByUser(user));
    }

    @PostMapping("/ldap")
    @SneakyThrows
    public JsonResponse ldap(@RequestBody @Validated LoginLdapRequest req) {
        String username = req.getUsername();

        // 系统配置
        Map<String, String> config = appConfigService.keyValues();
        String url = config.get(ConfigConstant.LDAP_URL);
        String adminUser = config.get(ConfigConstant.LDAP_ADMIN_USER);
        String adminPass = config.get(ConfigConstant.LDAP_ADMIN_PASS);
        String baseDN = config.get(ConfigConstant.LDAP_BASE_DN);
        if (url.isEmpty() || adminUser.isEmpty() || adminPass.isEmpty() || baseDN.isEmpty()) {
            return JsonResponse.error("LDAP服务未配置");
        }

        String mail = null;
        String uid = null;
        if (StringUtil.contains(username, "@")) {
            mail = username;
        } else {
            uid = username;
        }

        // 限流控制
        loginLimitCache.check(username);

        // 锁控制-防止并发登录重复写入数据
        if (!loginLockCache.apply(username)) {
            return JsonResponse.error("请稍候再试");
        }

        try {
            LdapTransformUser ldapTransformUser =
                    LdapUtil.loginByMailOrUid(
                            url, adminUser, adminPass, baseDN, mail, uid, req.getPassword());
            if (ldapTransformUser == null) {
                return JsonResponse.error("登录失败.请检查账号和密码");
            }

            HashMap<String, Object> data = loginBus.tokenByLdapTransformUser(ldapTransformUser);

            // 删除限流控制
            loginLimitCache.destroy(username);

            return JsonResponse.data(data);
        } catch (ServiceException e) {
            return JsonResponse.error(e.getMessage());
        } catch (Exception e) {
            log.error("LDAP登录失败", e);
            return JsonResponse.error("系统错误");
        } finally {
            loginLockCache.release(username);
        }
    }

    @PostMapping("/logout")
    public JsonResponse logout() {
        authService.logout();
        ctx.publishEvent(new UserLogoutEvent(this, FCtx.getId(), FCtx.getJwtJti()));
        return JsonResponse.success();
    }
}
