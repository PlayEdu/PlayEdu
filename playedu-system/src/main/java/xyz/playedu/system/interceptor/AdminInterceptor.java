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
package xyz.playedu.system.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import xyz.playedu.common.bus.BackendBus;
import xyz.playedu.common.config.PlayEduConfig;
import xyz.playedu.common.context.BCtx;
import xyz.playedu.common.domain.AdminUser;
import xyz.playedu.common.service.AdminUserService;
import xyz.playedu.common.service.AppConfigService;
import xyz.playedu.common.service.BackendAuthService;
import xyz.playedu.common.service.RateLimiterService;
import xyz.playedu.common.types.JsonResponse;
import xyz.playedu.common.util.HelperUtil;
import xyz.playedu.common.util.IpUtil;

import java.io.IOException;
import java.util.Map;

@Component
@Slf4j
public class AdminInterceptor implements HandlerInterceptor {

    @Autowired private BackendAuthService authService;

    @Autowired private AdminUserService adminUserService;

    @Autowired private BackendBus backendBus;

    @Autowired private AppConfigService configService;

    @Autowired private RateLimiterService rateLimiterService;

    @Autowired private PlayEduConfig playEduConfig;

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if ("OPTIONS".equals(request.getMethod())) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }

        String reqCountKey = "api-limiter:" + IpUtil.getIpAddress();
        Long reqCount = rateLimiterService.current(reqCountKey, playEduConfig.getLimiterDuration());
        if (reqCount > playEduConfig.getLimiterLimit()) {
            return responseTransform(response, 429, "太多请求");
        }

        // 读取全局配置
        Map<String, String> systemConfig = configService.keyValues();
        BCtx.setConfig(systemConfig);

        if (BackendBus.inUnAuthWhitelist(request.getRequestURI())) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }

        if (!authService.check()) {
            return responseTransform(response, 401, "请登录");
        }

        AdminUser adminUser = adminUserService.findById(authService.userId());
        if (adminUser == null) {
            return responseTransform(response, 401, "管理员不存在");
        }
        if (adminUser.getIsBanLogin() == 1) {
            return responseTransform(response, 403, "当前管理员禁止登录");
        }

        BCtx.setId(authService.userId());
        BCtx.setAdminUser(adminUser);
        BCtx.setAdminPer(backendBus.adminUserPermissions(adminUser.getId()));

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private boolean responseTransform(HttpServletResponse response, int code, String msg)
            throws IOException {
        response.setStatus(code);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(HelperUtil.toJsonStr(JsonResponse.error(msg)));
        return false;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        BCtx.remove();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
