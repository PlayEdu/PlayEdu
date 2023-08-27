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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import xyz.playedu.api.event.AdminUserLoginEvent;
import xyz.playedu.api.request.backend.LoginRequest;
import xyz.playedu.api.request.backend.PasswordChangeRequest;
import xyz.playedu.common.annotation.BackendPermission;
import xyz.playedu.common.annotation.Log;
import xyz.playedu.common.bus.BackendBus;
import xyz.playedu.common.config.PlayEduConfig;
import xyz.playedu.common.constant.BPermissionConstant;
import xyz.playedu.common.constant.BusinessTypeConstant;
import xyz.playedu.common.context.BCtx;
import xyz.playedu.common.domain.AdminUser;
import xyz.playedu.common.service.AdminUserService;
import xyz.playedu.common.service.BackendAuthService;
import xyz.playedu.common.service.RateLimiterService;
import xyz.playedu.common.types.JsonResponse;
import xyz.playedu.common.util.HelperUtil;
import xyz.playedu.common.util.IpUtil;
import xyz.playedu.common.util.RedisUtil;
import xyz.playedu.common.util.RequestUtil;

import java.util.HashMap;

@RestController
@RequestMapping("/backend/v1/auth")
public class LoginController {

    @Autowired private AdminUserService adminUserService;

    @Autowired private BackendBus backendBus;

    @Autowired private BackendAuthService authService;

    @Autowired private ApplicationContext ctx;

    @Autowired private RateLimiterService rateLimiterService;

    @Autowired private PlayEduConfig playEduConfig;

    @PostMapping("/login")
    @Log(title = "管理员-登录", businessType = BusinessTypeConstant.LOGIN)
    public JsonResponse login(@RequestBody @Validated LoginRequest loginRequest) {
        AdminUser adminUser = adminUserService.findByEmail(loginRequest.email);
        if (adminUser == null) {
            return JsonResponse.error("邮箱或密码错误");
        }

        String limitKey = "admin-login-limit:" + loginRequest.getEmail();
        Long reqCount = rateLimiterService.current(limitKey, 3600L);
        if (reqCount > 5 && !playEduConfig.getTesting()) {
            Long exp = RedisUtil.ttlWithoutPrefix(limitKey);
            return JsonResponse.error(
                    String.format("您的账号已被锁定，请%s后重试", exp > 60 ? exp / 60 + "分钟" : exp + "秒"));
        }

        String password =
                HelperUtil.MD5(loginRequest.getPassword() + adminUser.getSalt()).toLowerCase();
        if (!adminUser.getPassword().equals(password)) {
            return JsonResponse.error("邮箱或密码错误");
        }

        RedisUtil.del(limitKey);

        if (adminUser.getIsBanLogin().equals(1)) {
            return JsonResponse.error("当前管理员已禁止登录");
        }

        String token = authService.loginUsingId(adminUser.getId(), RequestUtil.url());

        HashMap<String, Object> data = new HashMap<>();
        data.put("token", token);

        ctx.publishEvent(
                new AdminUserLoginEvent(
                        this,
                        adminUser.getId(),
                        token,
                        IpUtil.getIpAddress(),
                        adminUser.getLoginTimes()));

        return JsonResponse.data(data);
    }

    @PostMapping("/logout")
    @Log(title = "管理员-登出", businessType = BusinessTypeConstant.LOGOUT)
    public JsonResponse logout() {
        authService.logout();
        return JsonResponse.success("success");
    }

    @GetMapping("/detail")
    @Log(title = "管理员-详情", businessType = BusinessTypeConstant.GET)
    public JsonResponse detail() {
        AdminUser user = BCtx.getAdminUser();
        HashMap<String, Boolean> permissions = backendBus.adminUserPermissions(user.getId());

        HashMap<String, Object> data = new HashMap<>();
        data.put("user", user);
        data.put("permissions", permissions);

        return JsonResponse.data(data);
    }

    @BackendPermission(slug = BPermissionConstant.PASSWORD_CHANGE)
    @PutMapping("/password")
    @Log(title = "管理员-密码修改", businessType = BusinessTypeConstant.UPDATE)
    public JsonResponse changePassword(@RequestBody @Validated PasswordChangeRequest req) {
        AdminUser user = BCtx.getAdminUser();
        String password = HelperUtil.MD5(req.getOldPassword() + user.getSalt());
        if (!password.equals(user.getPassword())) {
            return JsonResponse.error("原密码不正确");
        }
        adminUserService.passwordChange(user, req.getNewPassword());
        return JsonResponse.success();
    }
}
