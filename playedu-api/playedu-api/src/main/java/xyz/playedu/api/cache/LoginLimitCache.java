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
package xyz.playedu.api.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.playedu.common.config.PlayEduConfig;
import xyz.playedu.common.exception.ServiceException;
import xyz.playedu.common.service.RateLimiterService;
import xyz.playedu.common.util.MemoryCacheUtil;

@Component
public class LoginLimitCache {

    @Autowired private RateLimiterService rateLimiterService;

    @Autowired private PlayEduConfig playEduConfig;

    public void check(String email) throws ServiceException {
        checkKey(cacheKey(email), 10L, "您的账号已被锁定");
    }

    // 按客户端IP计数,避免旋转username绕过账号维度的限流
    public void checkIp(String ip) throws ServiceException {
        checkKey(ipCacheKey(ip), 30L, "登录尝试过于频繁");
    }

    public void destroy(String email) {
        MemoryCacheUtil.del(cacheKey(email));
    }

    // 登录成功仅退还本次计数,不清零,防止攻击者用自有账号登录重置整个IP的失败计数
    public void releaseIp(String ip) {
        String limitKey = ipCacheKey(ip);
        if (MemoryCacheUtil.exists(limitKey)) {
            MemoryCacheUtil.increment(limitKey, -1L, 600L);
        }
    }

    private void checkKey(String limitKey, long limit, String reason) throws ServiceException {
        Long reqCount = rateLimiterService.current(limitKey, 600L);
        if (reqCount >= limit && !playEduConfig.getTesting()) {
            Long exp = MemoryCacheUtil.ttlWithoutPrefix(limitKey);
            String msg = String.format("%s，请%s后重试", reason, exp > 60 ? exp / 60 + "分钟" : exp + "秒");
            throw new ServiceException(msg);
        }
    }

    private String cacheKey(String email) {
        return "login-limit:" + email;
    }

    private String ipCacheKey(String ip) {
        return "login-limit-ip:" + ip;
    }
}
