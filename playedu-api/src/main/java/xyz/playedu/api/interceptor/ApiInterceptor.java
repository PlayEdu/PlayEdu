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
package xyz.playedu.api.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import xyz.playedu.common.config.PlayEduConfig;
import xyz.playedu.common.constant.BackendConstant;
import xyz.playedu.common.service.RateLimiterService;
import xyz.playedu.common.types.JsonResponse;
import xyz.playedu.common.util.HelperUtil;
import xyz.playedu.common.util.IpUtil;

@Component
@Slf4j
@Order(10)
public class ApiInterceptor implements HandlerInterceptor {

    @Autowired private RateLimiterService rateLimiterService;

    @Autowired private PlayEduConfig playEduConfig;

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "86400");

        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(204);
            // 返回false意味着整个请求执行到这里结束，不会继续乡下执行了
            return false;
        }

        // 当前api的请求路径
        String path = request.getRequestURI();
        // 白名单过滤 || OPTIONS请求
        if (BackendConstant.API_LIMIT_WHITELIST.contains(path)) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }

        // 限流判断
        String reqCountKey = "api-limiter:" + IpUtil.getIpAddress();
        Long reqCount = rateLimiterService.current(reqCountKey, playEduConfig.getLimiterDuration());
        long limitCount = playEduConfig.getLimiterLimit();
        long limitRemaining = limitCount - reqCount;
        response.setHeader("X-RateLimit-Limit", String.valueOf(limitCount));
        response.setHeader("X-RateLimit-Remaining", String.valueOf(limitRemaining));
        if (limitRemaining <= 0) {
            response.setStatus(429);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(HelperUtil.toJsonStr(JsonResponse.error("太多请求")));
            return false;
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
