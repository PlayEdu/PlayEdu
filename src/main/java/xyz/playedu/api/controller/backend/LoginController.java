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

import xyz.playedu.api.BCtx;
import xyz.playedu.api.bus.BackendBus;
import xyz.playedu.api.constant.BPermissionConstant;
import xyz.playedu.api.domain.AdminUser;
import xyz.playedu.api.event.AdminUserLoginEvent;
import xyz.playedu.api.middleware.BackendPermissionMiddleware;
import xyz.playedu.api.request.backend.LoginRequest;
import xyz.playedu.api.request.backend.PasswordChangeRequest;
import xyz.playedu.api.service.AdminUserService;
import xyz.playedu.api.service.BackendAuthService;
import xyz.playedu.api.service.RateLimiterService;
import xyz.playedu.api.types.JsonResponse;
import xyz.playedu.api.util.HelperUtil;
import xyz.playedu.api.util.IpUtil;
import xyz.playedu.api.util.RedisUtil;
import xyz.playedu.api.util.RequestUtil;

import java.util.HashMap;

@RestController
@RequestMapping("/backend/v1/auth")
public class LoginController {

    @Autowired private AdminUserService adminUserService;

    @Autowired private BackendBus backendBus;

    @Autowired private BackendAuthService authService;

    @Autowired private ApplicationContext ctx;

    @Autowired private RateLimiterService rateLimiterService;

    @PostMapping("/login")
    public JsonResponse login(@RequestBody @Validated LoginRequest loginRequest) {
        AdminUser adminUser = adminUserService.findByEmail(loginRequest.email);
        if (adminUser == null) {
            return JsonResponse.error("邮箱或密码错误");
        }

        String limitKey = "admin-login-limit:" + loginRequest.getEmail();
        Long reqCount = rateLimiterService.current(limitKey, 3600L);
        if (reqCount > 5) {
            return JsonResponse.error("多次账密错误，账号被锁1个小时");
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
    public JsonResponse logout() {
        authService.logout();
        return JsonResponse.success("success");
    }

    @GetMapping("/detail")
    public JsonResponse detail() {
        AdminUser user = BCtx.getAdminUser();
        HashMap<String, Boolean> permissions = backendBus.adminUserPermissions(user.getId());

        HashMap<String, Object> data = new HashMap<>();
        data.put("user", user);
        data.put("permissions", permissions);

        return JsonResponse.data(data);
    }

    @BackendPermissionMiddleware(slug = BPermissionConstant.PASSWORD_CHANGE)
    @PutMapping("/password")
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
